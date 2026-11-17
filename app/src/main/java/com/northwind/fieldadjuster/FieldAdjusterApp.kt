package com.northwind.fieldadjuster

import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun FieldAdjusterApp(store: InspectionStore = remember { InspectionStore() }) {
    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }

    MaterialTheme {
        val selected = selectedId?.let(store::inspection)
        if (selected == null) {
            InspectionListScreen(store.inspections, onOpen = { selectedId = it })
        } else {
            BackHandler { selectedId = null }
            EstimateScreen(
                inspection = selected,
                canUndo = store.canUndo(selected.id),
                onRemove = { store.removeLineItem(it, selected.id) },
                onUndo = { store.undoRemove(selected.id) },
                onBack = { selectedId = null },
            )
        }
    }
}
