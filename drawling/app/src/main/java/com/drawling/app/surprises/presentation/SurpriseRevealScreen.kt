package com.drawling.app.surprises.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurpriseRevealScreen(
    surpriseId: String,
    onDone: () -> Unit,
    viewModel: SurpriseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(surpriseId) { viewModel.loadSurprise(surpriseId) }

    // Animation states
    val envelopeScale by animateFloatAsState(
        targetValue = if (state.isRevealed) 0f else 1f,
        animationSpec = tween(600, easing = EaseInOut),
        label = "envelope_scale"
    )
    val imageAlpha by animateFloatAsState(
        targetValue = if (state.isRevealed) 1f else 0f,
        animationSpec = tween(800, delayMillis = 400, easing = EaseOut),
        label = "image_alpha"
    )
    val imageScale by animateFloatAsState(
        targetValue = if (state.isRevealed) 1f else 0.7f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "image_scale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("A surprise for you 🎁") },
                navigationIcon = {
                    IconButton(onClick = onDone) { Icon(Icons.Default.ArrowBack, "Back") }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding).background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(state.error!!, color = MaterialTheme.colorScheme.error)
                else -> {
                    // Envelope (hidden after reveal)
                    if (!state.isRevealed) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.scale(envelopeScale)
                        ) {
                            Text("💌", fontSize = 120.sp)
                            Spacer(Modifier.height(24.dp))
                            Text(
                                "Your partner sent you something special",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                            Spacer(Modifier.height(32.dp))
                            Button(
                                onClick = { state.surprise?.let { viewModel.openSurprise(it.id) } },
                                modifier = Modifier.height(52.dp).fillMaxWidth(0.6f),
                                shape = RoundedCornerShape(26.dp)
                            ) {
                                Text("Open 💕", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Revealed drawing
                    if (state.isRevealed && state.surprise != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .alpha(imageAlpha)
                                .scale(imageScale)
                                .padding(24.dp)
                        ) {
                            Text("💕", fontSize = 40.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "A drawing, just for you",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(Modifier.height(16.dp))
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AsyncImage(
                                    model = state.surprise!!.imageUrl,
                                    contentDescription = "Surprise drawing",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                                )
                            }
                            Spacer(Modifier.height(24.dp))
                            Button(onClick = onDone, modifier = Modifier.fillMaxWidth(0.6f)) {
                                Text("Keep it forever 🖼️")
                            }
                        }
                    }
                }
            }
        }
    }
}
