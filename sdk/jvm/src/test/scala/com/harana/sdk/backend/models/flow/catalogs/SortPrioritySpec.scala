package com.harana.sdk.backend.models.flow.catalogs

import com.harana.sdk.backend.models.flow.UnitSpec
import com.harana.sdk.shared.models.designer.flow.utils.catalog.SortPriority

class SortPrioritySpec extends UnitSpec {

  "SortPriority" should {
    "should be ordered" in {
      SortPriority(3) should be < SortPriority(4)
      SortPriority(4) should be > SortPriority(3)
      SortPriority(4) shouldEqual SortPriority(4)
      (SortPriority(4) should not).equal(SortPriority(3))
    }
    "generate a sequence of priorities" in {
      val cases = Seq(
        (13, 2, 100),
        (-100, 1, 31),
        (0, 99, 101),
        (Int.MinValue, 3, 10),
        (10, -5, 200)
      )
      forEvery(cases) { case (start, skip, count) =>
        val nums = SortPriority(start).inSequence(skip).take(count).map(_.value).toSeq
        assert(nums.length === count)
        assert(nums.head === start)
        assert(nums.last === start + (count - 1) * skip)
      }
    }
  }
}
