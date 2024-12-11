package view
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.TransformOrigin




// Hàm hiệu ứng phóng to khi thành phần xuất hiện
fun scaleInEffect(
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
    initialScale: Float = 0.0f,
    transformOrigin: TransformOrigin = TransformOrigin.Center
): EnterTransition {
    return scaleIn(
        animationSpec = animationSpec,
        initialScale = initialScale,
        transformOrigin = transformOrigin
    )
}

// Hàm hiệu ứng mờ dần khi thành phần xuất hiện
fun fadeInEffect(
    animationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 500),
    initialAlpha: Float = 0.0f
): EnterTransition {
    return fadeIn(
        animationSpec = animationSpec,
        initialAlpha = initialAlpha
    )
}

// Hàm hiệu ứng phóng to và mờ dần khi thành phần xuất hiện
fun scaleAndFadeInEffect(
    scaleAnimationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
    fadeAnimationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 500)
): EnterTransition {
    return scaleIn(scaleAnimationSpec) + fadeIn(fadeAnimationSpec)
}

// Hàm hiệu ứng thu nhỏ và mờ dần khi thành phần biến mất
fun scaleAndFadeOutEffect(
    scaleAnimationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
    fadeAnimationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 500)
): ExitTransition {
    return scaleOut(scaleAnimationSpec) + fadeOut(fadeAnimationSpec)
}