package com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.transformers.StopWordsRemover
import com.harana.sdk.backend.models.designer.flow.actions.TransformerAsAction
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.StopWordsRemover
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers.RemoveStopWordsInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class RemoveStopWords extends TransformerAsAction[StopWordsRemover]
  with RemoveStopWordsInfo
  with SparkActionDocumentation