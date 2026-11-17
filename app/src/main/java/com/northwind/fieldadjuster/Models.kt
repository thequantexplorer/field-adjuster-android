package com.northwind.fieldadjuster

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class LineItem(
    val id: String,
    val description: String,
    val amountCents: Long,
)

data class Inspection(
    val id: String,
    val claimId: String,
    val address: String,
    val lineItems: List<LineItem>,
) {
    val totalCents: Long get() = EstimateCalculator.totalCents(lineItems)
}

class InspectionStore(initial: List<Inspection> = sampleInspections) {
    var inspections by mutableStateOf(initial)
        private set

    private data class Removal(val inspectionId: String, val item: LineItem, val position: Int)

    /** Removed line items, most recent last, so an adjuster can undo a removal on the same inspection. */
    private var removed by mutableStateOf(emptyList<Removal>())

    fun canUndo(inspectionId: String): Boolean = removed.any { it.inspectionId == inspectionId }

    fun inspection(id: String): Inspection? = inspections.firstOrNull { it.id == id }

    fun removeLineItem(itemId: String, inspectionId: String) {
        val inspection = inspection(inspectionId) ?: return
        val position = inspection.lineItems.indexOfFirst { it.id == itemId }
        if (position < 0) return
        removed = removed + Removal(inspectionId, inspection.lineItems[position], position)
        replace(inspection.copy(lineItems = inspection.lineItems.filterIndexed { i, _ -> i != position }))
    }

    fun undoRemove(inspectionId: String) {
        val index = removed.indexOfLast { it.inspectionId == inspectionId }
        if (index < 0) return
        val last = removed[index]
        removed = removed.filterIndexed { i, _ -> i != index }
        val inspection = inspection(inspectionId) ?: return
        val items = inspection.lineItems.toMutableList()
        items.add(last.position.coerceAtMost(items.size), last.item)
        replace(inspection.copy(lineItems = items))
    }

    private fun replace(updated: Inspection) {
        inspections = inspections.map { if (it.id == updated.id) updated else it }
    }

    companion object {
        val sampleInspections = listOf(
            Inspection(
                id = "INS-301", claimId = "CLM-1007", address = "412 Alder St, Portland",
                lineItems = listOf(
                    LineItem("roof", "Roof shingles", 420_000),
                    LineItem("gutter", "Gutter repair", 65_000),
                ),
            ),
            Inspection(
                id = "INS-302", claimId = "CLM-2004", address = "88 Harbor Rd, Seattle",
                lineItems = listOf(
                    LineItem("drywall", "Drywall", 310_000),
                    LineItem("flooring", "Flooring", 540_000),
                    LineItem("dryout", "Water extraction", 120_000),
                ),
            ),
        )
    }
}
