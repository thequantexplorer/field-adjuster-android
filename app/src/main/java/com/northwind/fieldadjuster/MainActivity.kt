package com.northwind.fieldadjuster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel

class InspectionStoreViewModel : ViewModel() {
    val store = InspectionStore()
}

class MainActivity : ComponentActivity() {
    private val viewModel: InspectionStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { FieldAdjusterApp(viewModel.store) }
    }
}
