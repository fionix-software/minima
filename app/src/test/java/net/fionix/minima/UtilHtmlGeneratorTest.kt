package net.fionix.minima

import net.fionix.minima.test.TestData
import net.fionix.minima.util.UtilHtmlGenerator
import org.junit.Test

class UtilHtmlGeneratorTest {

    @Test
    fun nonOverlapTimetableCase() {
        assert(TestData.nonOverlapCase.size == 4)
        assert(!UtilHtmlGenerator.checkOverlapTimetableExist(TestData.nonOverlapCase))
    }

    @Test
    fun overlapTimetableCase() {
        assert(TestData.overlapCase.size == 2)
        assert(UtilHtmlGenerator.checkOverlapTimetableExist(TestData.overlapCase))
    }

}