package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import java.sql.Date
import java.text.SimpleDateFormat
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.enablers.Sortable
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization.TransformerSerializationOps
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import com.harana.sdk.backend.models.flow.actions.exceptions.ColumnDoesNotExistError
import com.harana.sdk.shared.models.flow.actionobjects.SortColumnParameter

class SortTransformationIntegSpec extends IntegratedTestSupport with TransformerSerialization {

  trait SampleData {

    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

    implicit def stringToSqlDate(s: String): Date = new Date(sdf.parse(s).getTime)

    implicit def sortableDataFrame(implicit rowOrdering: Ordering[Row]): Sortable[DataFrame] =
      (sequence: DataFrame) => implicitly[Sortable[Array[Row]]].isSorted(sequence.sparkDataFrame.collect())

    case class OrderingSpec[T](columnIndex: Int, descendingFlag: Boolean, scalaOrderingForColumnValues: Ordering[T])

    /** <p> Creates lexicographic <code>Ordering</code> for <code>Row</code>s taking into account column indices,
      * ascending/descending flag and the importance of columns as given in the <code>Seq</code> (more important columns
      * come first). </p> <p> Also returns a <code>Seq</code> of <code>SortColumnParameter</code>s to be given to the
      * <code>SortTransformer</code> which should match the <code>Ordering</code> in order to pass the test. </p> <p> In
      * other words - after running the transformer parameterized with the <code>SortColumnParameter</code>s the resulting
      * <code>DataFrame</code> should be sorted according to the lexicographical <code>Ordering</code>. </p>
      *
      * @param columnIndices
      *   sequence of <code>OrderingSpecs</code>
      * @return
      *   <code>Row Ordering</code> and <code>SortColumnParameter</code>s to be used in test
      */
    def generateOrderingAndSortColumnParameters(columnIndices: Seq[OrderingSpec[_]]) = {
      (
        new Ordering[Row] {
          val tieredOrdering: Ordering[Row] =
            columnIndices.map { case OrderingSpec(i, desc, o) =>
              new Ordering[Row] {
                override def compare(x: Row, y: Row): Int = {
                  val ordering = if (desc) o.reverse else o
                  ordering.asInstanceOf[Ordering[Any]].compare(x.get(i), y.get(i))
                }
              }
            }.reduceRight((o1: Ordering[Row], o2: Ordering[Row]) => {
              new Ordering[Row] {
                override def compare(x: Row, y: Row): Int = {
                  val cmp = o1.compare(x, y)
                  if (cmp == 0) o2.compare(x, y) else cmp
                }
              }
            })

          def compare(x: Row, y: Row): Int = tieredOrdering.compare(x, y)
        },
        columnIndices.map { case OrderingSpec(i, desc, _) => SortColumnParameter(i, desc) }
      )
    }

    val data = Seq[Row](
      Row("2016-01-01": Date, 2, "A"),
      Row("2016-01-03": Date, 1, "B"),
      Row("2016-01-02": Date, 1, "B"),
      Row("2016-01-01": Date, 1, "A"),
      Row("2016-01-03": Date, 2, "B"),
      Row("2016-01-02": Date, 1, "A"),
      Row("2016-01-03": Date, 1, "A")
    )

    val col1StructField = StructField("col1", DateType)

    val col2StructField = StructField("col2", IntegerType)

    val col3StructField = StructField("col3", StringType)

    val schema = StructType(
      Seq(
        col1StructField,
        col2StructField,
        col3StructField
      )
    )
    val transformer = new SortTransformer()
    val df = createDataFrame(data, schema)
  }

  "SortTransformer" should {
    "not produce error" when {
      "given empty set" in {
        new SampleData {
          transformer.setColumns(Seq())
          val resDf = transformer.applyTransformationAndSerialization(tempDir, createDataFrame(Seq(), schema))
          resDf.sparkDataFrame.collect() shouldBe empty
        }
      }
    }

    "throw exception" when {
      "given non-existing column name" in {
        new SampleData {
          transformer.setColumns(Seq(SortColumnParameter("non-existing-column-name", descending = false)))
          a[ColumnDoesNotExistError] shouldBe thrownBy {
            transformer.applyTransformationAndSerialization(tempDir, df)
          }
        }
      }
      "transforming a DataFrame using non-existing column name" in {
        new SampleData {
          transformer.setColumns(Seq(SortColumnParameter("non-existing-column-name", descending = false)))
          intercept[ColumnDoesNotExistError] {
            transformer._transform(executionContext, df)
          }
        }
      }
      "transforming a schema using non-existing column name" in {
        new SampleData {
          transformer.setColumns(Seq(SortColumnParameter("non-existing-column-name", descending = false)))
          intercept[ColumnDoesNotExistError] {
            transformer._transformSchema(schema)
          }
        }
      }
    }

    "sort a single column in ascending order" in {
      new SampleData {

        val (ordering, sortColumns) = generateOrderingAndSortColumnParameters(
          Seq(OrderingSpec(0, descendingFlag = false, implicitly[Ordering[java.util.Date]]))
        )

        implicit val o = ordering
        transformer.setColumns(sortColumns)

        val resDf = transformer.applyTransformationAndSerialization(tempDir, df)
        resDf.sparkDataFrame.collect() should contain
        theSameElementsAs(df.sparkDataFrame.collect())

        resDf shouldBe sorted
      }
    }

    "sort multiple columns" in {
      new SampleData {

        val (ordering, sortColumns) = generateOrderingAndSortColumnParameters(
          Seq(
            OrderingSpec(1, descendingFlag = true, implicitly[Ordering[Integer]]),
            OrderingSpec(0, descendingFlag = false, implicitly[Ordering[java.util.Date]])
          )
        )

        implicit val o = ordering

        transformer.setColumns(sortColumns)

        val resDf = transformer.applyTransformationAndSerialization(tempDir, df)
        resDf.sparkDataFrame.collect() should contain
        theSameElementsAs(df.sparkDataFrame.collect())

        resDf shouldBe sorted
      }
    }
  }
}
