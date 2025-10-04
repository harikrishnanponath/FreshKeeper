package com.example.freshkeeper.grocery.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.freshkeeper.grocery.view.bottomnav.BottomNavBar
import com.example.freshkeeper.grocery.viewmodel.GroceryViewModel

@SuppressLint("FrequentlyChangingValue")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: GroceryViewModel,
    onAddClick: (Int?) -> Unit,
    navController: NavController
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
        bottomBar = { BottomNavBar(navController = navController) },
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



