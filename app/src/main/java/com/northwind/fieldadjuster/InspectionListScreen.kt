package com.northwind.fieldadjuster

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionListScreen(inspections: List<Inspection>, onOpen: (String) -> Unit) {
    Scaffold(topBar = { LargeTopAppBar(title = { Text("Inspections") }) }) { padding ->
        LazyColumn(contentPadding = padding) {
            items(inspections, key = { it.id }) { inspection ->
                ListItem(
                    headlineContent = { Text(inspection.claimId, style = MaterialTheme.typography.titleMedium) },
                    supportingContent = { Text(inspection.address) },
                    modifier = Modifier
                        .testTag("inspection-${inspection.claimId}")
                        .clickable { onOpen(inspection.id) },
                )
                HorizontalDivider()
            }
        }
    }
}
