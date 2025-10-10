package com.example.freshkeeper.grocery.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.freshkeeper.grocery.view.bottomnav.BottomNavBar
import com.example.freshkeeper.grocery.viewmodel.GroceryViewModel

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
                    firstScroll = false
                } else {
                    fabVisible = when {
                        index > previousIndex -> false // scrolling down
                        index < previousIndex -> true  // scrolling up
                        else -> offset < previousOffset
                    }
                }
                previousIndex = index
                previousOffset = offset
            }
    }

    // ✅ Remove Scaffold — use simple layout
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            // Top bar
            TopAppBar(
                title = { Text("FreshKeeper") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )

            // List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp), // leave space for FAB
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(groceries) { item ->
                    FoodItemCard(item = item) {
                        onAddClick(item.id)
                    }
                }
            }
        }

        AnimatedVisibility(
        visible = fabVisible,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(
                end = 16.dp,
                bottom = 100.dp + WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            )
    ) {
        Button(
            onClick = { onAddClick(null) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            modifier = Modifier.height(50.dp)
        ) {
            Text("Add Grocery")
        }
    }

    }
}
