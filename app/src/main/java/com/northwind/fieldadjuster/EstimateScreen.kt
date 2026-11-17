package com.northwind.fieldadjuster

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimateScreen(
    inspection: Inspection,
    canUndo: Boolean,
    onRemove: (String) -> Unit,
    onUndo: () -> Unit,
    onBack: () -> Unit,
) {
    val total = inspection.totalCents
    val average = EstimateCalculator.averageItemCents(total, inspection.lineItems.size)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(inspection.claimId) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onUndo, enabled = canUndo, modifier = Modifier.testTag("undo-remove")) {
                        Text("Undo")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        EstimateCalculator.format(total),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.testTag("estimate-total"),
                    )
                    Text(
                        "Average per item ${EstimateCalculator.format(average)}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("average-per-item"),
                    )
                }
            }
            item {
                Text(
                    "Damage line items",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            items(inspection.lineItems, key = { it.id }) { item ->
                ListItem(
                    headlineContent = { Text(item.description) },
                    supportingContent = { Text(EstimateCalculator.format(item.amountCents)) },
                    trailingContent = {
                        TextButton(onClick = { onRemove(item.id) }, modifier = Modifier.testTag("remove-${item.id}")) {
                            Text("Remove", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.testTag("line-item-${item.id}"),
                )
                HorizontalDivider()
            }
            if (inspection.lineItems.isEmpty()) {
                item {
                    Text(
                        "No line items",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp).testTag("empty-estimate"),
                    )
                }
            }
        }
    }
}
