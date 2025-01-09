package com.amitraj.animationsdemo

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JetpackComposeAnimationsDemo(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimateColorChange()
        AnimateSizeChange()
        AnimateVisibility()
        AnimateAlpha()
        ChoreographedAnimations()
        MultipleAnimationsExample()
        InfiniteAnimationExample()
        KeyframeAnimationExample()
        CustomAnimationExample()
    }
}

@Composable
fun AnimateColorChange() {
    var isRed by remember { mutableStateOf(true) }
    val color by animateColorAsState(
        targetValue = if (isRed) Color.Red else Color.Blue,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    Button(
        onClick = { isRed = !isRed },
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text("Change Color")
    }
}

@Composable
fun AnimateSizeChange() {
    var expanded by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (expanded) 150.dp else 50.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
    )

    Box(
        modifier = Modifier
            .size(size)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { expanded = !expanded }
    )
}

@Composable
fun AnimateVisibility() {
    var isVisible by remember { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { isVisible = !isVisible }) {
            Text(if (isVisible) "Hide Box" else "Show Box")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}

@Composable
fun AnimateAlpha() {
    var isFaded by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isFaded) 0.2f else 1f,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha))
            .clickable { isFaded = !isFaded },
        contentAlignment = Alignment.Center
    ) {
        Text("Tap me", color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun ChoreographedAnimations() {
    var isExpanded by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = isExpanded, label = "ChoreographedAnimation")
    val boxSize by transition.animateDp(label = "BoxSize") { expanded ->
        if (expanded) 150.dp else 50.dp
    }
    val boxColor by transition.animateColor(label = "BoxColor") { expanded ->
        if (expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    }
    val boxCornerRadius by transition.animateDp(label = "BoxCornerRadius") { expanded ->
        if (expanded) 20.dp else 0.dp
    }

    Box(
        modifier = Modifier
            .size(boxSize)
            .background(
                color = boxColor,
                shape = RoundedCornerShape(boxCornerRadius)
            )
            .clickable { isExpanded = !isExpanded }
    )
}


@Composable
fun MultipleAnimationsExample() {
    var isExpanded by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isExpanded, label = "expandTransition")

    val boxSize by transition.animateDp(
        transitionSpec = { tween(durationMillis = 500) },
        label = "boxSizeAnimation"
    ) { expanded ->
        if (expanded) 200.dp else 100.dp
    }

    val boxColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 500) },
        label = "boxColorAnimation"
    ) { expanded ->
        if (expanded) Color.Green else Color.Yellow
    }

    Box(
        modifier = Modifier
            .size(boxSize)
            .background(boxColor)
            .clickable { isExpanded = !isExpanded }
    )
}

@Composable
fun InfiniteAnimationExample() {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(color)
    )
}

@Composable
fun KeyframeAnimationExample() {
    var isAnimating by remember { mutableStateOf(false) }

    val animationProgress by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 0f,
        animationSpec = keyframes {
            durationMillis = 1000
            0.0f at 0
            0.8f at 300
            0.6f at 600
            1.0f at 1000
        }, label = ""
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer(
                scaleX = 1 + animationProgress,
                scaleY = 1 + animationProgress
            )
            .background(Color.Magenta)
            .clickable { isAnimating = !isAnimating }
    )
}

@Composable
fun CustomAnimationExample() {
    val animatable = remember { Animatable(0f) }

    // Launch the animation when the Composable is first created
    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    }

    val scale = animatable.value * 2

    Box(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .background(Color.Cyan)
    )
}
