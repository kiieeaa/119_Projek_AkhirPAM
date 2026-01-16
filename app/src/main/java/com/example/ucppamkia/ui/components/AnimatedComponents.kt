package com.example.ucppamkia.ui.components

import AzureBlue
import BlueAccent
import BlueLight
import BluePrimary
import ElectricBlue
import GlassWhite
import GradientMintEnd
import GradientMintStart
import NeonBlue
import TurquoiseBlue
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ucppamkia.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.sin



//Membuat card transparan bergaya glassmorphism
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    containerColor: Color = GlassWhite,
    elevation: Dp = 8.dp,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = shape
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            content = content
        )
    }
}

//Membuat tombol dengan warna gradasi dan animasi
@Composable
fun GradientButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = listOf(BluePrimary, BlueAccent),
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "button")
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )
    
    // Press animation
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "buttonScale"
    )
    
    Box(
        modifier = modifier
            .height(56.dp)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors,
                    startX = shimmer * 500f,
                    endX = shimmer * 500f + 500f
                )
            )
            .clickable(
                enabled = enabled,
                onClick = {
                    isPressed = true
                    onClick()
                    // Reset after a short delay
                    kotlinx.coroutines.GlobalScope.launch {
                        kotlinx.coroutines.delay(100)
                        isPressed = false
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
            }
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

//Membuat tombol floating dengan efek denyut
@Composable
fun PulsingFAB(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = BluePrimary,
    contentDescription: String? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.scale(scale),
        containerColor = containerColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

//Membuat teks dengan efek berkilau
@Composable
fun ShimmerText(
    text: String,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = Color.White.copy(alpha = alpha)
    )
}

//Membuat card dengan animasi muncul geser
@Composable
fun AnimatedCard(
    modifier: Modifier = Modifier,
    delay: Int = 0,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        visible = true
    }
    
    val offsetX by animateFloatAsState(
        targetValue = if (visible) 0f else 300f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "offsetX"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )
    
    Box(
        modifier = modifier
            .offset(x = offsetX.dp)
            .graphicsLayer { this.alpha = alpha }
    ) {
        content()
    }
}



//Membuat animasi lingkaran sebagai dekorasi background
@Composable
fun FloatingCircles(
    modifier: Modifier = Modifier,
    circleCount: Int = 8,
    colors: List<Color> = listOf(ElectricBlue, NeonBlue, AzureBlue, TurquoiseBlue)
) {
    Box(modifier = modifier) {
        repeat(circleCount) { index ->
            val infiniteTransition = rememberInfiniteTransition(label = "circle$index")

            val offsetY by infiniteTransition.animateFloat(
                initialValue = -100f,
                targetValue = 100f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 4000 + (index * 500),
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "offsetY$index"
            )

            val scale by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 3000 + (index * 300),
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale$index"
            )

            Box(
                modifier = Modifier
                    .offset(
                        x = ((index * 80) % 350).dp,
                        y = offsetY.dp
                    )
                    .size((60 + index * 20).dp)
                    .scale(scale)
                    .alpha(0.15f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colors[index % colors.size],
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }
    }
}
