package com.example.freshkeeper.view.helper

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.freshkeeper.viewmodel.GroceryViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimateSaveFab(
    isSaving: Boolean,
    isSaved: Boolean,
    onSaveClicked: () -> Unit
) {
    FloatingActionButton(onClick = { if (!isSaved) onSaveClicked() }) {
        when {
            isSaving -> {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
            isSaved -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Saved",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .animateScale()
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save"
                )
            }
        }
    }
}

// Simple scale animation extension
@Composable
fun Modifier.animateScale(): Modifier {
    val scale by animateFloatAsState(
        targetValue = 1.3f,
        animationSpec = tween(300, easing = LinearOutSlowInEasing),
        label = "scale"
    )
    return this.graphicsLayer(scaleX = scale, scaleY = scale)
}