package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.StopWordsRemover
import com.harana.sdk.backend.models.flow.actiontypes.TransformerAsActionType
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.StopWordsRemover
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers.RemoveStopWordsInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class RemoveStopWords extends TransformerAsActionType[StopWordsRemover]
  with RemoveStopWordsInfo
  with SparkActionDocumentation