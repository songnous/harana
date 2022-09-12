package com.harana.sdk.backend.models.flow

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

abstract class UnitSpec extends AnyWordSpec
  with Matchers
  with OptionValues
  with Inside
  with Inspectors
  with MockitoSugar
