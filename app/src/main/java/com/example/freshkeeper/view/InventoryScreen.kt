package com.example.freshkeeper.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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

@SuppressLint("FrequentlyChangingValue")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: GroceryViewModel,
    onAddClick: (Int?) -> Unit,
) {
    val groceries by viewModel.groceries.collectAsState()

    val listState = rememberLazyListState()
    var fabVisible by remember { mutableStateOf(true) }
    var previousIndex by remember { mutableStateOf(0) }
    var previousOffset by remember { mutableStateOf(0) }
    var firstScroll by remember { mutableStateOf(true) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                if (firstScroll) {
                    // Skip comparison for the first emission
                    firstScroll = false
                } else {
                    fabVisible = when {
                        index > previousIndex -> false // scrolling down
                        index < previousIndex -> true  // scrolling up
                        else -> offset < previousOffset // same item, check offset
                    }
                }
                previousIndex = index
                previousOffset = offset
            }
    }


    Scaffold(
        topBar = { TopAppBar(title = { Text("FreshKeeper") }) },
        floatingActionButton = {AnimatedVisibility(visible = fabVisible) {
            Button(
                modifier = Modifier.height(50.dp),
                onClick = { onAddClick(null) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Text("Add Grocery")
            }
        }}
    ) { padding ->
        LazyColumn(contentPadding = padding, state = listState) {
            items(groceries) { item ->
                FoodItemCard(item = item, onClick = {
                    onAddClick(item.id)
                })
            }
        }
    }
}



