package com.example.freshkeeper.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.freshkeeper.model.GroceryRepository
import com.example.freshkeeper.model.db.Grocery
import com.example.freshkeeper.ui.theme.BlueRoyal
import com.example.freshkeeper.ui.theme.GreenEmerald
import com.example.freshkeeper.ui.theme.RedCoral
import com.example.freshkeeper.viewmodel.GroceryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: GroceryViewModel,
    onAddClick: (Int?) -> Unit,
) {
    val groceries by viewModel.groceries.collectAsState()


    Scaffold(
        topBar = { TopAppBar(title = { Text("FreshKeeper") }) },
        floatingActionButton = {
            Button(
                modifier = Modifier.height(50.dp),
                onClick = { onAddClick(null) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Text("Add Grocery")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(groceries) { item ->
                FoodItemCard(item = item, onClick = {
                    onAddClick(item.id)
                })
            }
        }
    }
}



