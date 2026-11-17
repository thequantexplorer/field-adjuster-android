package com.northwind.fieldadjuster

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InspectionFlowTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private fun openClm2004() {
        rule.onNodeWithText("Inspections").assertIsDisplayed()
        rule.onNodeWithTag("inspection-CLM-2004").performClick()
        rule.onNodeWithText("CLM-2004").assertIsDisplayed()
    }

    @Test
    fun listShowsInspections() {
        rule.onNodeWithTag("inspection-CLM-1007").assertIsDisplayed()
        rule.onNodeWithTag("inspection-CLM-2004").assertIsDisplayed()
        rule.onNodeWithText("88 Harbor Rd, Seattle").assertIsDisplayed()
    }

    @Test
    fun openClm2004ShowsTotalAndAverage() {
        openClm2004()
        rule.onNodeWithTag("estimate-total").assertTextEquals("$9,700.00")
        rule.onNodeWithTag("average-per-item").assertTextEquals("Average per item $3,233.33")
        rule.onNodeWithTag("undo-remove").assertIsNotEnabled()
    }

    @Test
    fun removeLineItemUpdatesTotal() {
        openClm2004()
        rule.onNodeWithTag("remove-drywall").performClick()
        rule.onNodeWithTag("line-item-drywall").assertDoesNotExist()
        rule.onNodeWithTag("estimate-total").assertTextEquals("$6,600.00")
        rule.onNodeWithTag("average-per-item").assertTextEquals("Average per item $3,300.00")
    }

    @Test
    fun undoRestoresRemovedItem() {
        openClm2004()
        rule.onNodeWithTag("remove-drywall").performClick()
        rule.onNodeWithTag("undo-remove").assertIsEnabled().performClick()
        rule.onNodeWithTag("line-item-drywall").assertIsDisplayed()
        rule.onNodeWithTag("estimate-total").assertTextEquals("$9,700.00")
    }

    @Test
    fun removeEveryLineItemShowsZeroTotal() {
        openClm2004()
        listOf("drywall", "flooring", "dryout").forEach { rule.onNodeWithTag("remove-$it").performClick() }
        rule.onNodeWithTag("estimate-total").assertTextEquals("$0.00")
        rule.onNodeWithTag("average-per-item").assertTextEquals("Average per item $0.00")
        rule.onNodeWithTag("empty-estimate").assertIsDisplayed()
    }

    @Test
    fun backReturnsToListAndKeepsEdits() {
        openClm2004()
        rule.onNodeWithTag("remove-flooring").performClick()
        rule.onNodeWithTag("back").performClick()
        rule.onNodeWithTag("inspection-CLM-2004").performClick()
        rule.onNodeWithTag("estimate-total").assertTextEquals("$4,300.00")
    }

    @Test
    fun undoDoesNotCrossInspections() {
        openClm2004()
        rule.onNodeWithTag("remove-drywall").performClick()
        rule.onNodeWithTag("back").performClick()
        rule.onNodeWithTag("inspection-CLM-1007").performClick()
        rule.onNodeWithTag("undo-remove").assertIsNotEnabled()
        rule.onNodeWithTag("estimate-total").assertTextEquals("$4,850.00")
    }

    @Test
    fun editsSurviveActivityRecreation() {
        openClm2004()
        rule.onNodeWithTag("remove-drywall").performClick()
        rule.activityRule.scenario.recreate()
        rule.onNodeWithTag("estimate-total").assertTextEquals("$6,600.00")
        rule.onNodeWithTag("undo-remove").assertIsEnabled()
    }
}
