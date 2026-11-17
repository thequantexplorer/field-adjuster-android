package com.northwind.fieldadjuster

import org.junit.Assert.assertEquals
import org.junit.Test

class EstimateCalculatorTest {
    @Test
    fun averagePerItem() {
        assertEquals(242_500L, EstimateCalculator.averageItemCents(totalCents = 485_000, itemCount = 2))
    }

    @Test
    fun averageTruncatesToWholeCents() {
        assertEquals(323_333L, EstimateCalculator.averageItemCents(totalCents = 970_000, itemCount = 3))
        assertEquals(33L, EstimateCalculator.averageItemCents(totalCents = 100, itemCount = 3))
    }

    @Test
    fun averageOfEmptyEstimateIsZero() {
        assertEquals(0L, EstimateCalculator.averageItemCents(totalCents = 0, itemCount = 0))
    }

    @Test
    fun totalSumsLineItems() {
        val items = listOf(LineItem("a", "A", 310_000), LineItem("b", "B", 540_000), LineItem("c", "C", 120_000))
        assertEquals(970_000L, EstimateCalculator.totalCents(items))
        assertEquals(0L, EstimateCalculator.totalCents(emptyList()))
    }

    @Test
    fun totalDoesNotOverflowInt() {
        val items = listOf(LineItem("a", "A", Int.MAX_VALUE.toLong()), LineItem("b", "B", 1))
        assertEquals(Int.MAX_VALUE.toLong() + 1, EstimateCalculator.totalCents(items))
    }

    @Test
    fun formatsCentsAsUsd() {
        assertEquals("$9,700.00", EstimateCalculator.format(970_000))
        assertEquals("$0.05", EstimateCalculator.format(5))
        assertEquals("$0.00", EstimateCalculator.format(0))
        assertEquals("$3,233.33", EstimateCalculator.format(323_333))
    }
}
