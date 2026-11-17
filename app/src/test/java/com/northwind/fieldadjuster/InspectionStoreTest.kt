package com.northwind.fieldadjuster

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InspectionStoreTest {
    private val store = InspectionStore()
    private fun clm2004() = store.inspection("INS-302")!!

    @Test
    fun sampleTotals() {
        assertEquals(485_000L, store.inspection("INS-301")!!.totalCents)
        assertEquals(970_000L, clm2004().totalCents)
    }

    @Test
    fun removeLineItemUpdatesTotal() {
        store.removeLineItem("drywall", "INS-302")
        assertEquals(listOf("flooring", "dryout"), clm2004().lineItems.map { it.id })
        assertEquals(660_000L, clm2004().totalCents)
        assertEquals(485_000L, store.inspection("INS-301")!!.totalCents)
    }

    @Test
    fun undoRestoresItemAtOriginalPosition() {
        store.removeLineItem("flooring", "INS-302")
        store.removeLineItem("drywall", "INS-302")
        store.undoRemove("INS-302")
        assertEquals(listOf("drywall", "dryout"), clm2004().lineItems.map { it.id })
        store.undoRemove("INS-302")
        assertEquals(listOf("drywall", "flooring", "dryout"), clm2004().lineItems.map { it.id })
        assertFalse(store.canUndo("INS-302"))
    }

    @Test
    fun undoWithNothingRemovedIsNoOp() {
        assertFalse(store.canUndo("INS-302"))
        store.undoRemove("INS-302")
        assertEquals(970_000L, clm2004().totalCents)
    }

    @Test
    fun removingEveryItemLeavesZeroTotalAndAverage() {
        listOf("drywall", "flooring", "dryout").forEach { store.removeLineItem(it, "INS-302") }
        val inspection = clm2004()
        assertTrue(inspection.lineItems.isEmpty())
        assertEquals(0L, inspection.totalCents)
        assertEquals(0L, EstimateCalculator.averageItemCents(inspection.totalCents, inspection.lineItems.size))
    }

    @Test
    fun removingUnknownItemIsNoOp() {
        store.removeLineItem("nope", "INS-302")
        store.removeLineItem("drywall", "INS-999")
        assertEquals(3, clm2004().lineItems.size)
        assertFalse(store.canUndo("INS-302"))
    }

    @Test
    fun undoIsScopedToInspection() {
        store.removeLineItem("drywall", "INS-302")
        store.removeLineItem("roof", "INS-301")
        assertTrue(store.canUndo("INS-302"))
        store.undoRemove("INS-302")
        assertEquals(970_000L, clm2004().totalCents)
        assertEquals(listOf("gutter"), store.inspection("INS-301")!!.lineItems.map { it.id })
        assertFalse(store.canUndo("INS-302"))
        assertTrue(store.canUndo("INS-301"))
    }
}
