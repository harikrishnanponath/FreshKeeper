package com.example.freshkeeper.grocery.view.helper

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

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