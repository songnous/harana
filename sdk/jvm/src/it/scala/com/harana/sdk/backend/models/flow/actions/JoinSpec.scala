package com.harana.sdk.backend.models.flow.actions

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.{ColumnDoesNotExistError, ColumnsDoNotExistError, DuplicatedColumnsError, WrongColumnTypeError}
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.designer.flow
import com.harana.sdk.shared.models.flow.actions.JoinTypeChoice
import com.harana.sdk.shared.models.flow.parameters.selections.{IndexSingleColumnSelection, NameSingleColumnSelection}

class JoinSpec extends IntegratedTestSupport {

  import com.harana.sdk.backend.models.flow.IntegratedTestSupport._

  val defaultJoinType = JoinTypeChoice.LeftOuter()

  val leftTablePrefix = Some("leftTable_")

  val rightTablePrefix = Some("rightTable_")

  "Join action" should {
    // Smoke test. Each row from both tables is matched so the output is the same for all types.
    "execute for all types" in {
      val (ldf, rdf, expected, joinColumns) = oneColumnFixture()
      val joinTypes = joinWithMultipleColumnSelection(joinColumns, Set.empty, joinType = defaultJoinType).joinTypeParameter.choiceInstances

      for (joinType <- joinTypes) {
        val join   = joinWithMultipleColumnSelection(joinColumns, Set.empty, joinType = joinType)
        val joinDF = executeAction(join, ldf, rdf)
        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }
    }
    "LEFT JOIN two DataFrames" when {
      "based upon a single column selection by name" in {
        val (ldf, rdf, expected, joinColumns) = oneColumnFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = executeAction(join, ldf, rdf)

        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }
      "based upon a single column selection by index" in {
        val (ldf, rdf, expected, joinColumnIds) = oneColumnByIndicesFixture()
        val join = joinWithMultipleColumnSelection(Set.empty, joinColumnIds)
        val joinDF = executeAction(join, ldf, rdf)

        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }
      "based upon a single column selection with colliding join column" in {
        val (ldf, rdf, expected, joinColumns) = oneColumnFixture(None, None)
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty, leftPrefix = None, rightPrefix = None)
        val joinDF = executeAction(join, ldf, rdf)

        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }
      "based upon two columns" in {
        val (ldf, rdf, expected, joinColumns) = twoColumnsFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = executeAction(join, ldf, rdf)

        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }
      "based upon two columns with different column names" in {
        val (ldf, rdf, expected, leftJoinColumns, rightJoinColumns) = twoColumnsDifferentNamesFixture()

        val join = joinWithMultipleColumnSelection(
          leftJoinColumns,
          List.empty,
          rightJoinColumns,
          List.empty,
          leftPrefix = None,
          rightPrefix = None,
          joinType = defaultJoinType
        )

        val joinDF = executeAction(join, ldf, rdf)
        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }
      "based upon two columns with different column indices" in {
        val (ldf, rdf, expected, leftJoinIndices, rightJoinIndices) = twoColumnsDifferentIndicesFixture()

        val join = joinWithMultipleColumnSelection(
          List.empty,
          leftJoinIndices,
          List.empty,
          rightJoinIndices,
          leftPrefix = None,
          rightPrefix = None,
          joinType = defaultJoinType
        )

        val joinDF = executeAction(join, ldf, rdf)
        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }
      "some rows from left dataframe have no corresponding values in the right one" in {
        val (ldf, rdf, expected, joinColumns) = noSomeRightValuesFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = executeAction(join, ldf, rdf)

        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }

      "dataframes have no matching values" in {
        val (ldf, rdf, expected, joinColumns) = noMatchingValuesFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = executeAction(join, ldf, rdf)

        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }

      "some column values are null" in {
        val (ldf, rdf, expected, joinColumns) = nullFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = executeAction(join, ldf, rdf)

        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }

      "with null values only in left DataFrame" in {
        val (ldf, rdf, expected, joinColumns) = nullValuesInLeftDataFrameFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = executeAction(join, ldf, rdf)

        assertDataFramesEqual(joinDF, expected, checkRowOrder = false)
      }

      "with null values only in both DataFrames".is(pending)
      "with empty join column selection".is(pending)
    }

    "throw an exception" when {
      "with columns of the same name in both and no join on them" in {
        a[DuplicatedColumnsError] should be thrownBy {
          val (ldf, rdf, joinColumns) = sameColumnNamesFixture()

          val join = joinWithMultipleColumnSelection(joinColumns, Set.empty, None, None)
          executeAction(join, ldf, rdf)
        }
      }

      "the columns selected by name does not exist" in {
        a[ColumnDoesNotExistError] should be thrownBy {
          val nonExistingColumnName = "thisColumnDoesNotExist"
          val join = joinWithMultipleColumnSelection(Set(nonExistingColumnName), Set.empty)
          val (ldf, rdf, _, _) = oneColumnFixture()
          executeAction(join, ldf, rdf)
        }
      }
      "the columns selected by index does not exist" in {
        a[ColumnDoesNotExistError] should be thrownBy {
          val nonExistingColumnIndex = 1000
          val join = joinWithMultipleColumnSelection(Set.empty, Set(nonExistingColumnIndex))
          val (ldf, rdf, _, _) = oneColumnFixture()
          executeAction(join, ldf, rdf)
        }
      }
      "the columns selected by name are of different types" in {
        a[WrongColumnTypeError] should be thrownBy {
          val (ldf, rdf, _, wrongTypeColumnNames) = differentTypesFixture()
          val join = joinWithMultipleColumnSelection(wrongTypeColumnNames, Set.empty)
          executeAction(join, ldf, rdf)
        }
      }
      "the joinColumns MultipleColumnSelector is empty" in {
        a[ColumnsDoNotExistError] should be thrownBy {
          val (ldf, rdf, _, wrongTypeColumnNames) = joinColumnsIsEmptyFixture()
          val join = joinWithMultipleColumnSelection(wrongTypeColumnNames, Set.empty)
          executeAction(join, ldf, rdf)
        }
      }
    }
  }

  it should {
    "infer DataFrame schema" when {
      "based upon a single column selection by name" in {
        val (ldf, rdf, expected, joinColumns) = oneColumnFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = inferSchema(join, ldf, rdf)

        joinDF shouldBe expected.sparkDataFrame.schema
      }
      "based upon a single column selection by index" in {
        val (ldf, rdf, expected, joinColumnIds) = oneColumnByIndicesFixture()
        val join = joinWithMultipleColumnSelection(Set.empty, joinColumnIds)
        val joinDF = inferSchema(join, ldf, rdf)

        joinDF shouldBe expected.sparkDataFrame.schema
      }
      "based upon a single column selection with colliding join column" in {
        val (ldf, rdf, expected, joinColumns) = oneColumnFixture(None, None)
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty, leftPrefix = None, rightPrefix = None)
        val joinDF = inferSchema(join, ldf, rdf)

        joinDF shouldBe expected.sparkDataFrame.schema
      }
      "based upon two columns" in {
        val (ldf, rdf, expected, joinColumns) = twoColumnsFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = inferSchema(join, ldf, rdf)

        joinDF shouldBe expected.sparkDataFrame.schema
      }

      "based upon two columns with different column names" in {
        val (ldf, rdf, expected, leftJoinColumns, rightJoinColumns) = twoColumnsDifferentNamesFixture()

        val join = joinWithMultipleColumnSelection(
          leftJoinColumns,
          List.empty,
          rightJoinColumns,
          List.empty,
          leftPrefix = None,
          rightPrefix = None,
          joinType = defaultJoinType
        )

        val joinDF = inferSchema(join, ldf, rdf)
        joinDF shouldBe expected.sparkDataFrame.schema
      }

      "based upon two columns with different column indices" in {
        val (ldf, rdf, expected, leftJoinIndices, rightJoinIndices) = twoColumnsDifferentIndicesFixture()

        val join = joinWithMultipleColumnSelection(
          List.empty,
          leftJoinIndices,
          List.empty,
          rightJoinIndices,
          leftPrefix = None,
          rightPrefix = None,
          joinType = defaultJoinType
        )

        val joinDF = inferSchema(join, ldf, rdf)
        joinDF shouldBe expected.sparkDataFrame.schema
      }

      "some rows from left dataframe have no corresponding values in the right one" in {
        val (ldf, rdf, expected, joinColumns) = noSomeRightValuesFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = inferSchema(join, ldf, rdf)
        joinDF shouldBe expected.sparkDataFrame.schema
      }

      "dataframes have no matching values" in {
        val (ldf, rdf, expected, joinColumns) = noMatchingValuesFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = inferSchema(join, ldf, rdf)
        joinDF shouldBe expected.sparkDataFrame.schema
      }

      "some column values are null" in {
        val (ldf, rdf, expected, joinColumns) = nullFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = inferSchema(join, ldf, rdf)
        joinDF shouldBe expected.sparkDataFrame.schema
      }

      "with null values only in left DataFrame" in {
        val (ldf, rdf, expected, joinColumns) = nullValuesInLeftDataFrameFixture()
        val join = joinWithMultipleColumnSelection(joinColumns, Set.empty)
        val joinDF = inferSchema(join, ldf, rdf)
        joinDF shouldBe expected.sparkDataFrame.schema
      }

      "with null values only in both DataFrames".is(pending)
      "with empty join column selection".is(pending)
    }

    "not report error" when {
      "column prefixes are empty" in {
        val join = joinWithMultipleColumnSelection(
          names = Set(),
          ids = Set(),
          leftPrefix = None,
          rightPrefix = None
        )

        join.validateParameters shouldBe empty
      }
    }
    "throw an exception during inference" when {
      "with columns of the same name in both and no join on them" in {
        a[DuplicatedColumnsError] should be thrownBy {
          val (ldf, rdf, joinColumns) = sameColumnNamesFixture()
          val join = joinWithMultipleColumnSelection(joinColumns, Set.empty, None, None)
          inferSchema(join, ldf, rdf)
        }
      }

      "the columns selected by name does not exist" in {
        a[ColumnDoesNotExistError] should be thrownBy {
          val nonExistingColumnName = "thisColumnDoesNotExist"
          val join = joinWithMultipleColumnSelection(Set(nonExistingColumnName), Set.empty)
          val (ldf, rdf, _, _) = oneColumnFixture()
          inferSchema(join, ldf, rdf)
        }
      }

      "the columns selected by index does not exist" in {
        a[ColumnDoesNotExistError] should be thrownBy {
          val nonExistingColumnIndex = 1000
          val join = joinWithMultipleColumnSelection(Set.empty, Set(nonExistingColumnIndex))
          val (ldf, rdf, _, _) = oneColumnFixture()
          inferSchema(join, ldf, rdf)
        }
      }
      "the columns selected by name are of different types" in {
        a[WrongColumnTypeError] should be thrownBy {
          val (ldf, rdf, _, wrongTypeColumnNames) = differentTypesFixture()
          val join = joinWithMultipleColumnSelection(wrongTypeColumnNames, Set.empty)
          inferSchema(join, ldf, rdf)
        }
      }
      "the joinColumns MultipleColumnSelector is empty" in {
        a[ColumnsDoNotExistError] should be thrownBy {
          val (ldf, rdf, _, wrongTypeColumnNames) = joinColumnsIsEmptyFixture()
          val join = joinWithMultipleColumnSelection(wrongTypeColumnNames, Set.empty)
          inferSchema(join, ldf, rdf)
        }
      }
    }
  }

  def oneColumnFixture(leftPrefix: Option[String] = leftTablePrefix, rightPrefix: Option[String] = rightTablePrefix) = {
    val column1 = "column1"
    val joinColumns = Set(column1)

    // Left dataframe
    val colsL = List("column2", "column3", column1, "column4")
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), DoubleType),
        StructField(colsL(1), StringType),
        StructField(colsL(2), DoubleType),
        StructField(colsL(3), DoubleType)
      )
    )
    val rowsL = Seq(
      (3.5, "a", 1.5, 5.0),
      (3.6, "b", 1.6, 6.0),
      (3.7, "c", 1.7, 10.0),
      (4.6, "d", 1.6, 9.0),
      (4.5, "e", 1.5, 11.0)
    ).map(Row.fromTuple)
    val ldf = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR = List(column1, "column22", "column5")
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), DoubleType),
        StructField(colsR(1), DoubleType),
        StructField(colsR(2), StringType)
      )
    )
    val rowsR   = Seq(
      (1.6, 2.6, "two"),
      (1.7, 2.7, "three"),
      (1.5, 2.5, "one"),
      (1.5, 3.5, "four")
    ).map(Row.fromTuple)
    val rdf     = createDataFrame(rowsR, schemaR)

    // join dataframe
    val joinRows   = Seq(
      (3.5, "a", 1.5, 5.0, 2.5, "one"),
      (3.5, "a", 1.5, 5.0, 3.5, "four"),
      (3.6, "b", 1.6, 6.0, 2.6, "two"),
      (3.7, "c", 1.7, 10.0, 2.7, "three"),
      (4.6, "d", 1.6, 9.0, 2.6, "two"),
      (4.5, "e", 1.5, 11.0, 2.5, "one"),
      (4.5, "e", 1.5, 11.0, 3.5, "four")
    ).map(Row.fromTuple)
    val joinSchema = StructType(
      appendPrefix(schemaL.fields, leftPrefix) ++
        appendPrefix(Seq(StructField(colsR(1), DoubleType), StructField(colsR(2), StringType)), rightPrefix)
    )
    val edf        = createDataFrame(joinRows, joinSchema)

    (ldf, rdf, edf, joinColumns)
  }

  def oneColumnByIndicesFixture(
      leftPrefix: Option[String] = leftTablePrefix,
      rightPrefix: Option[String] = rightTablePrefix
  ): (DataFrame, DataFrame, DataFrame, Set[Int]) = {
    val column1 = "column1"

    // Left dataframe
    val colsL   = List(column1, "column2", "column3", "column4")
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), DoubleType),
        StructField(colsL(1), DoubleType),
        StructField(colsL(2), StringType),
        StructField(colsL(3), DoubleType)
      )
    )
    val rowsL   = Seq(
      (1.5, 3.5, "a", 5.0),
      (1.6, 3.6, "b", 6.0),
      (1.7, 3.7, "c", 10.0),
      (1.6, 4.6, "d", 9.0),
      (1.5, 4.5, "e", 11.0)
    ).map(Row.fromTuple)
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List(column1, "column22", "column5")
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), DoubleType),
        StructField(colsR(1), DoubleType),
        StructField(colsR(2), StringType)
      )
    )
    val rowsR   = Seq(
      (1.6, 2.6, "two"),
      (1.7, 2.7, "three"),
      (1.5, 2.5, "one"),
      (1.5, 3.5, "four")
    ).map(Row.fromTuple)
    val rdf     = createDataFrame(rowsR, schemaR)

    // join dataframe
    val joinRows   = Seq(
      (1.5, 3.5, "a", 5.0, 2.5, "one"),
      (1.5, 3.5, "a", 5.0, 3.5, "four"),
      (1.6, 3.6, "b", 6.0, 2.6, "two"),
      (1.7, 3.7, "c", 10.0, 2.7, "three"),
      (1.6, 4.6, "d", 9.0, 2.6, "two"),
      (1.5, 4.5, "e", 11.0, 2.5, "one"),
      (1.5, 4.5, "e", 11.0, 3.5, "four")
    ).map(Row.fromTuple)
    val joinSchema = StructType(
      appendPrefix(schemaL.fields, leftPrefix) ++
        appendPrefix(Seq(StructField(colsR(1), DoubleType), StructField(colsR(2), StringType)), rightPrefix)
    )
    val edf        = createDataFrame(joinRows, joinSchema)

    (ldf, rdf, edf, Set(0))
  }

  def twoColumnsFixture(): (DataFrame, DataFrame, DataFrame, Set[String]) = {
    val column1     = "column1"
    val column2     = "column2"
    val joinColumns = Set(column1, column2)

    // Left dataframe
    val colsL   = List(column2, "column3", column1, "column4")
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), DoubleType),
        StructField(colsL(1), StringType),
        StructField(colsL(2), DoubleType),
        StructField(colsL(3), DoubleType)
      )
    )
    val rowsL   = Seq(
      (2.5, "a", 1.5, 5.0),
      (3.6, "b", 1.6, 6.0),
      (3.7, "c", 1.7, 10.0)
    ).map(Row.fromTuple)
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List(column1, column2, "column5")
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), DoubleType),
        StructField(colsR(1), DoubleType),
        StructField(colsR(2), StringType)
      )
    )
    val rowsR   = Seq(
      (1.5, 2.5, "one"),
      (1.5, 3.6, "two"),
      (1.7, 3.6, "c")
    ).map(Row.fromTuple)
    val rdf     = createDataFrame(rowsR, schemaR)

    // join dataframe
    val joinRows   = Seq(
      (2.5, "a", 1.5, 5.0, "one"),
      (3.6, "b", 1.6, 6.0, null),
      (3.7, "c", 1.7, 10.0, null)
    ).map(Row.fromTuple)
    val joinSchema = StructType(
      appendPrefix(schemaL.fields, leftTablePrefix) ++
        appendPrefix(Seq(StructField(colsR(2), StringType)), rightTablePrefix)
    )
    val edf        = createDataFrame(joinRows, joinSchema)

    (ldf, rdf, edf, joinColumns)
  }

  def twoColumnsDifferentNamesFixture(): (DataFrame, DataFrame, DataFrame, List[String], List[String]) = {

    // Left dataframe
    val colsL   = List("a", "b", "c", "d")
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), DoubleType),
        StructField(colsL(1), StringType),
        StructField(colsL(2), DoubleType),
        StructField(colsL(3), DoubleType)
      )
    )
    val rowsL   = Seq(
      (2.5, "a", 1.5, 5.0),
      (3.6, "b", 1.6, 6.0),
      (3.7, "c", 1.7, 10.0)
    ).map(Row.fromTuple)
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List("e", "f", "g")
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), DoubleType),
        StructField(colsR(1), DoubleType),
        StructField(colsR(2), StringType)
      )
    )
    val rowsR   = Seq(
      (1.5, 2.5, "one"),
      (1.5, 3.6, "two"),
      (1.7, 3.6, "c")
    ).map(Row.fromTuple)
    val rdf     = createDataFrame(rowsR, schemaR)

    // join dataframe
    val joinRows   = Seq(
      (2.5, "a", 1.5, 5.0, "one"),
      (3.6, "b", 1.6, 6.0, null),
      (3.7, "c", 1.7, 10.0, null)
    ).map(Row.fromTuple)
    val joinSchema = StructType(
      schemaL.fields ++ Seq(StructField(colsR(2), StringType))
    )
    val edf        = createDataFrame(joinRows, joinSchema)

    (ldf, rdf, edf, List("c", "a"), List("e", "f"))
  }

  def twoColumnsDifferentIndicesFixture(): (DataFrame, DataFrame, DataFrame, List[Int], List[Int]) = {

    // Left dataframe
    val colsL   = List("a", "b", "c", "d")
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), DoubleType),
        StructField(colsL(1), StringType),
        StructField(colsL(2), DoubleType),
        StructField(colsL(3), DoubleType)
      )
    )
    val rowsL   = Seq(
      (2.5, "a", 1.5, 5.0),
      (3.6, "b", 1.6, 6.0),
      (3.7, "c", 1.7, 10.0)
    ).map(Row.fromTuple)
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List("e", "f", "g")
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), DoubleType),
        StructField(colsR(1), DoubleType),
        StructField(colsR(2), StringType)
      )
    )
    val rowsR   = Seq(
      (1.5, 2.5, "one"),
      (1.5, 3.6, "two"),
      (1.7, 3.6, "c")
    ).map(Row.fromTuple)
    val rdf     = createDataFrame(rowsR, schemaR)

    // join dataframe
    val joinRows   = Seq(
      (2.5, "a", 1.5, 5.0, "one"),
      (3.6, "b", 1.6, 6.0, null),
      (3.7, "c", 1.7, 10.0, null)
    ).map(Row.fromTuple)
    val joinSchema = StructType(
      schemaL.fields ++ Seq(StructField(colsR(2), StringType))
    )
    val edf        = createDataFrame(joinRows, joinSchema)

    (ldf, rdf, edf, List(2, 0), List(0, 1))
  }

  def differentTypesFixture(): (DataFrame, DataFrame, DataFrame, Set[String]) = {
    val column1     = "column1"
    val joinColumns = Set(column1)

    // Left dataframe
    val colsL   = List(column1, "column2", "column3", "column4")
    val rowsL   = Seq(
      (1.5, 2.5, "a", 3.5),
      (1.6, 2.6, "b", 3.6),
      (1.7, 2.7, "c", 3.7)
    ).map(Row.fromTuple)
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), DoubleType),
        StructField(colsL(1), DoubleType),
        StructField(colsL(2), StringType),
        StructField(colsL(3), DoubleType)
      )
    )
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List(column1, "column22")
    val rowsR   = Seq(
      ("1.5", 0.5),
      ("1.6", 0.6),
      ("1.7", 0.7)
    ).map(Row.fromTuple)
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), StringType),
        StructField(colsR(1), DoubleType)
      )
    )
    val rdf     = createDataFrame(rowsR, schemaR)

    val ignored = mock[DataFrame]

    (ldf, rdf, ignored, joinColumns)
  }

  def joinColumnsIsEmptyFixture(): (DataFrame, DataFrame, DataFrame, Set[String]) = {
    val joinColumns = Set.empty[String]

    // Left dataframe
    val colsL   = List("column1", "column2", "column3", "column4")
    val rowsL   = Seq(
      (1.5, 2.5, "a", 3.5),
      (1.6, 2.6, "b", 3.6),
      (1.7, 2.7, "c", 3.7)
    ).map(Row.fromTuple)
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), DoubleType),
        StructField(colsL(1), DoubleType),
        StructField(colsL(2), StringType),
        StructField(colsL(3), DoubleType)
      )
    )
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List("column1", "column22")
    val rowsR   = Seq(
      ("1.5", 0.5),
      ("1.6", 0.6),
      ("1.7", 0.7)
    ).map(Row.fromTuple)
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), StringType),
        StructField(colsR(1), DoubleType)
      )
    )
    val rdf     = createDataFrame(rowsR, schemaR)

    val ignored = mock[DataFrame]

    (ldf, rdf, ignored, joinColumns)
  }

  def noSomeRightValuesFixture(): (DataFrame, DataFrame, DataFrame, Set[String]) = {
    val column1     = "column1"
    val joinColumns = Set(column1)

    // Left dataframe
    val colsL   = List(column1)
    val rowsL   = Seq(
      1.5,
      1.6,
      1.7
    ).map(Seq(_)).map(Row.fromSeq)
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), DoubleType)
      )
    )
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List(column1, "column2")
    val rowsR   = Seq(
      (1.4, 0.5),
      (1.7, 2.7)
    ).map(Row.fromTuple)
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), DoubleType),
        StructField(colsR(1), DoubleType)
      )
    )
    val rdf     = createDataFrame(rowsR, schemaR)

    // join dataframe
    val joinRows   = Seq(
      (1.5, null),
      (1.6, null),
      (1.7, 2.7)
    ).map(Row.fromTuple)
    val joinSchema = StructType(
      appendPrefix(schemaL.fields, leftTablePrefix) ++
        appendPrefix(schemaR.fields.filterNot(_.name == column1), rightTablePrefix)
    )
    val edf        = createDataFrame(joinRows, joinSchema)

    (ldf, rdf, edf, joinColumns)
  }

  def noMatchingValuesFixture(): (DataFrame, DataFrame, DataFrame, Set[String]) = {
    val column1     = "column1"
    val joinColumns = Set(column1)

    // Left dataframe
    val colsL   = List(column1)
    val schemaL = StructType(
      Seq(
        StructField(column1, DoubleType)
      )
    )
    val rowsL   = Seq(
      1.5,
      1.6,
      1.7
    ).map(Seq(_)).map(Row.fromSeq)
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List(column1, "column2")
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), DoubleType),
        StructField(colsR(1), DoubleType)
      )
    )
    val rowsR   = Seq(
      (1.4, 0.5),
      (1.8, 1.7)
    ).map(Row.fromTuple)
    val rdf     = createDataFrame(rowsR, schemaR)

    // join dataframe
    val joinRows   = Seq(
      (1.5, null),
      (1.6, null),
      (1.7, null)
    ).map(Row.fromTuple)
    val joinSchema = StructType(
      appendPrefix(schemaL.fields, leftTablePrefix) ++
        appendPrefix(schemaR.fields.tail, rightTablePrefix)
    )
    val edf        = createDataFrame(joinRows, joinSchema)

    (ldf, rdf, edf, joinColumns)
  }

  def nullFixture(): (DataFrame, DataFrame, DataFrame, Set[String]) = {
    val column1     = "column1"
    val joinColumns = Set(column1)

    // Left dataframe
    val colsL   = List(column1)
    val rowsL   = Seq(
      1.5,
      1.6,
      null
    ).map(Seq(_)).map(Row.fromSeq)
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), DoubleType)
      )
    )
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List(column1, "column2")
    val rowsR   = Seq(
      (null, 0.5),
      (null, 0.7),
      (1.6, null)
    ).map(Row.fromTuple)
    val schemaR = StructType(
      Seq(
        StructField(colsR(0), DoubleType),
        StructField(colsR(1), DoubleType)
      )
    )
    val rdf     = createDataFrame(rowsR, schemaR)

    // join dataframe
    val joinRows   = Seq(
      (1.5, null),
      (1.6, null),
      (null, null)
    ).map(Row.fromTuple)
    val joinSchema = StructType(
      appendPrefix(schemaL.fields, leftTablePrefix) ++
        appendPrefix(schemaR.fields.filterNot(_.name == column1), rightTablePrefix)
    )
    val edf        = createDataFrame(joinRows, joinSchema)

    (ldf, rdf, edf, joinColumns)
  }

  def nullValuesInLeftDataFrameFixture(): (DataFrame, DataFrame, DataFrame, Set[String]) = {
    val column1     = "nulls"
    val joinColumns = Set(column1)

    // Left dataframe
    val colsL   = List(column1)
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), StringType)
      )
    )
    val rowsL   = Seq(
      null
    ).map(Seq(_)).map(Row.fromSeq)
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List(column1, "owner")
    val schemaR = StructType(
      Seq(
        StructField(colsL(0), StringType),
        StructField(colsR(1), StringType)
      )
    )
    val rowsR   = Seq(
      ("kot", "Wojtek"),
      ("wiewiorka", "Jacek"),
      ("pies", "Rafal")
    ).map(Row.fromTuple)
    val rdf     = createDataFrame(rowsR, schemaR)

    // join dataframe
    val joinRows   = Seq(
      (null, null)
    ).map(Row.fromTuple)
    val joinSchema = StructType(
      appendPrefix(Seq(StructField(colsL(0), StringType)) ++ schemaL.fields.tail, leftTablePrefix) ++
        appendPrefix(schemaR.fields.filterNot(_.name == column1), rightTablePrefix)
    )
    val edf        = createDataFrame(joinRows, joinSchema)

    (ldf, rdf, edf, joinColumns)
  }

  private def sameColumnNamesFixture(): (DataFrame, DataFrame, Set[String]) = {
    val column1     = "nulls"
    val joinColumns = Set(column1)

    val sameNameColumns = Seq(
      ("A", StringType),
      ("B", DoubleType)
    )

    object Gen {
      def generate(dt: DataType) = dt match {
        case StringType => "s"
        case DoubleType => 1.0
      }
    }
    import Gen._

    // Left dataframe
    val colsL   = List(column1) ++ sameNameColumns.map { case (name, _) => name }
    val schemaL = StructType(
      Seq(
        StructField(colsL(0), StringType)
      ) ++ sameNameColumns.map { case (name, tpe) => StructField(name, tpe) }
    )
    val rowsL   = Seq(
      null +: sameNameColumns.map { case (_, t) => generate(t) }
    ).map(Row.fromSeq)
    val ldf     = createDataFrame(rowsL, schemaL)

    // Right dataframe
    val colsR   = List(column1, "owner") ++ sameNameColumns.map { case (name, _) => name }
    val schemaR = StructType(
      Seq(
        StructField(colsL(0), StringType),
        StructField(colsR(1), StringType)
      ) ++ sameNameColumns.map { case (name, tpe) => StructField(name, tpe) }
    )
    val rowsR   = Seq(
      Seq("kot", "Wojtek") ++ sameNameColumns.map { case (_, t) => generate(t) },
      Seq("wiewiorka", "Jacek") ++ sameNameColumns.map { case (_, t) => generate(t) },
      Seq("pies", "Rafal") ++ sameNameColumns.map { case (_, t) => generate(t) }
    ).map(Row.fromSeq)
    val rdf     = createDataFrame(rowsR, schemaR)

    (ldf, rdf, joinColumns)
  }

  private def joinWithMultipleColumnSelection(
      namesLeft: List[String],
      idsLeft: List[Int],
      namesRight: List[String],
      idsRight: List[Int],
      leftPrefix: Option[String],
      rightPrefix: Option[String],
      joinType: JoinTypeChoice.Option
  ): Join = {
    val action = new Join

    val parametersByName: Seq[flow.actions.Join.ColumnPair] =
      namesLeft.zip(namesRight).map { case (leftColName, rightColName) =>
        flow.actions.Join
          .ColumnPair()
          .setLeftColumn(NameSingleColumnSelection(leftColName))
          .setRightColumn(NameSingleColumnSelection(rightColName))
      }

    val parametersById = idsLeft.zip(idsRight).map { case (leftColId, rightColId) =>
      flow.actions.Join
        .ColumnPair()
        .setLeftColumn(IndexSingleColumnSelection(leftColId))
        .setRightColumn(IndexSingleColumnSelection(rightColId))
    }

    action.setJoinType(joinType)
    action.setJoinColumns(parametersByName ++ parametersById)
    leftPrefix.foreach(p => action.setLeftPrefix(p))
    rightPrefix.foreach(p => action.setRightPrefix(p))

    action
  }

  private def joinWithMultipleColumnSelection(
      names: Set[String],
      ids: Set[Int],
      leftPrefix: Option[String] = leftTablePrefix,
      rightPrefix: Option[String] = rightTablePrefix,
      joinType: JoinTypeChoice.Option = defaultJoinType
  ): Join = {
    val namesVector = names.toList
    val idsVector   = ids.toList
    joinWithMultipleColumnSelection(namesVector, idsVector, namesVector, idsVector, leftPrefix, rightPrefix, joinType)
  }

  private def appendPrefix(schema: Seq[StructField], prefix: Option[String]) =
    schema.map(field => field.copy(name = prefix.getOrElse("") + field.name))

  private def inferSchema(action: Join, leftDataFrame: DataFrame, rightDataFrame: DataFrame): StructType = {
    val (knowledge, _) = action.inferKnowledgeUntyped(
      List(
        Knowledge(DataFrame.forInference(leftDataFrame.sparkDataFrame.schema)),
        Knowledge(DataFrame.forInference(rightDataFrame.sparkDataFrame.schema))
      )
    )(mock[InferContext])

    val dataFrameKnowledge = knowledge.head.single.asInstanceOf[DataFrame]
    dataFrameKnowledge.schema.get
  }
}
