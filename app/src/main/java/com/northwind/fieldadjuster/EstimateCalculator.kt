package com.northwind.fieldadjuster

import java.text.NumberFormat
import java.util.Locale

object EstimateCalculator {
    fun totalCents(items: List<LineItem>): Long = items.sumOf { it.amountCents }

    /** Average cost per line item, truncated to whole cents. An empty estimate averages to 0. */
    fun averageItemCents(totalCents: Long, itemCount: Int): Long =
        if (itemCount <= 0) 0 else totalCents / itemCount

    fun format(cents: Long): String =
        NumberFormat.getCurrencyInstance(Locale.US).format(cents / 100.0)
}
