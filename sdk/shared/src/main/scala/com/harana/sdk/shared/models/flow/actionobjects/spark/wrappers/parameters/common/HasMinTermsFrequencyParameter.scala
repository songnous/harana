package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.DoubleParameter
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMinTermsFrequencyParameter extends HasInputColumnParameter with HasOutputColumnParameter {

  val minTFParameter = DoubleParameter("min term frequency", Some("""A filter to ignore rare words in a document. For each document, terms with
                         |a frequency/count less than the given threshold are ignored. If this is an integer >= 1,
                         |then this specifies a count (of times the term must appear in the document); if this is
                         |a double in [0,1), then it specifies a fraction (out of the document's token count).
                         |Note that the parameter is only used in transform of CountVectorizer model and does not
                         |affect fitting.""".stripMargin),
    RangeValidator(0.0, Double.MaxValue)
  )

  setDefault(minTFParameter, 1.0)
  def setMinTF(value: Double): this.type = set(minTFParameter, value)
}
