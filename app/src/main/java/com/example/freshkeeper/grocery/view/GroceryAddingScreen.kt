package com.example.freshkeeper.grocery.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.freshkeeper.ui.theme.GreenEmerald
import com.example.freshkeeper.grocery.view.helper.CategoryDropdown
import com.example.freshkeeper.grocery.view.helper.DatePicker
import com.example.freshkeeper.grocery.view.helper.DeleteConfirmationDialog
import com.example.freshkeeper.grocery.view.helper.formatExpiryDate
import com.example.freshkeeper.grocery.viewmodel.GroceryViewModel
import kotlinx.coroutines.launch


@SuppressLint("FrequentlyChangingValue")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryAddingScreen(
    viewModel: GroceryViewModel,
    onSaveClicked: (Int?) -> Unit,
    onBackClicked: () -> Unit,
    groceryId: Int?,
    onDeleteClicked: (Int?) -> Unit,
    navController: NavController
) {
    val groceryName by viewModel.groceryName.collectAsState()
    val groceryCategory by viewModel.groceryCategory.collectAsState()
    val groceryQuantity by viewModel.groceryQuantity.collectAsState()
    val groceryUnit by viewModel.groceryUnit.collectAsState()
    val groceryExpiryDate by viewModel.groceryExpiryDate.collectAsState()
    val noExpiry by viewModel.noExpiry.collectAsState()

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var isSaving by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (groceryId == -1) "Add Grocery" else "Edit Grocery"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            Button(
                onClick = {
                    if (viewModel.validateFields()) {
                        onSaveClicked(groceryId)

                        // show snackbar after save
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                if (groceryId == -1) "Grocery added!" else "Grocery updated!"
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenEmerald,
                    contentColor = Color.Black
                )
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                Text(text = if (groceryId == -1) "Save" else "Update")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Grocery Name
            OutlinedTextField(
                value = groceryName,
                onValueChange = { viewModel.setGroceryName(it) },
                label = { Text("Name") },
                leadingIcon = { Icon(Icons.Default.ShoppingCart, null) },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.nameError.collectAsState().value
            )
            if (viewModel.nameError.collectAsState().value) {
                Text(
                    text = "Name cannot be empty",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            CategoryDropdown(
                selectedCategory = groceryCategory,
                onCategorySelected = { viewModel.setGroceryCategory(it) },
                categories = listOf("Fruits", "Vegetables", "Dairy", "Bakery", "Meat", "Other"),
                icon = { Icon(Icons.Default.Category, null) },
                labelText = { Text("Category") },
            )

            if (viewModel.categoryError.collectAsState().value) {
                Text(
                    text = "Please select a category",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            // Quantity + Unit Row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = groceryQuantity,
                    onValueChange = { value ->
                        value.let { viewModel.setGroceryQuantity(it) }
                    },
                    label = { Text("Quantity") },
                    leadingIcon = { Icon(Icons.Default.FormatListNumbered, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                Column(modifier = Modifier.weight(1f)) {
                    CategoryDropdown(
                        selectedCategory = groceryUnit,
                        onCategorySelected = { viewModel.setGroceryUnit(it) },
                        categories = listOf("kg", "g", "L", "ml", "pcs", "other"),
                        icon = { Icon(Icons.Default.Scale, null) },
                        labelText = { Text("Unit") }
                    )
                }

            }
            if (viewModel.quantityError.collectAsState().value) {
                Text(
                    text = "Enter a valid quantity",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (viewModel.unitError.collectAsState().value) {
                Text(
                    text = "Please select a unit",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Expiry Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Expiry Date",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = noExpiry,
                        onCheckedChange = { viewModel.setNoExpiry(!noExpiry) }
                    )
                    Text("No Expiry")
                }
            }


            // Date selector field (only enabled if not "no expiry")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(enabled = !noExpiry) { showDatePickerDialog = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (noExpiry) "No expiry" else formatExpiryDate(groceryExpiryDate),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (noExpiry) Color.Gray else MaterialTheme.colorScheme.onSurface
                    )
                    Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                }
            }
            if (viewModel.expiryError.collectAsState().value) {
                Text(
                    text = "Please select a expiry date or set 'No Expiry'",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            // Show delete button only in edit mode
            if (groceryId != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Grocery", color = Color.White)
                }
            }

            DeleteConfirmationDialog(
                showDialog = showDeleteDialog,
                onDismiss = { showDeleteDialog = false },
                onConfirm = { onDeleteClicked(groceryId) }
            )

            // Show DatePicker
            if (showDatePickerDialog && !noExpiry) {
                DatePicker(
                    onDateSelected = { millis ->
                        if (millis != null) {
                            // ✅ store exactly what user picked
                            Log.d("DatePicker", "Selected date: $millis")
                            viewModel.setGroceryExpiryDate(millis)
                        }
                        showDatePickerDialog = false
                    },
                    onDismiss = { showDatePickerDialog = false }
                )

            }


        }
    }
}






