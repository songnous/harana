package com.harana.sdk.backend.models.flow.utils

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import org.apache.spark.sql.types.StructType
import org.scalatest.matchers.should.Matchers
import org.scalatest.Assertion

trait DataFrameMatchers extends Matchers {

  def assertDataFramesEqual(actualDf: DataFrame, expectedDf: DataFrame, checkRowOrder: Boolean = true, checkNullability: Boolean = true) = {
    // Checks only semantic identity, not objects location in memory
    assertSchemaEqual(actualDf.sparkDataFrame.schema, expectedDf.sparkDataFrame.schema, checkNullability)
    val collectedRows1 = actualDf.sparkDataFrame.collect()
    val collectedRows2 = expectedDf.sparkDataFrame.collect()
    if (checkRowOrder)
      collectedRows1 shouldBe collectedRows2
    else
      collectedRows1 should contain theSameElementsAs collectedRows2
  }

  def assertSchemaEqual(actualSchema: StructType, expectedSchema: StructType, checkNullability: Boolean): Assertion = {
    val (actual, expected) =
      if (checkNullability)
        (actualSchema, expectedSchema)
      else {
        val actualNonNull = StructType(actualSchema.map(_.copy(nullable = false)))
        val expectedNonNull = StructType(expectedSchema.map(_.copy(nullable = false)))
        (actualNonNull, expectedNonNull)
      }
    assertSchemaEqual(actual, expected)
  }

  def assertSchemaEqual(actualSchema: StructType, expectedSchema: StructType): Assertion =
    actualSchema.treeString shouldBe expectedSchema.treeString

}

object DataFrameMatchers extends DataFrameMatchers
