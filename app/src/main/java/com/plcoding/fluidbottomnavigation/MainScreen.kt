package com.plcoding.fluidbottomnavigation

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plcoding.fluidbottomnavigation.ui.theme.FluidBottomNavigationTheme
import kotlin.math.PI
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.S) // function should only be called on devices running Android S (API level 31) or higher.
private fun getRenderEffect(): RenderEffect {
    val blurEffect = RenderEffect //This function creates a render effect that applies a blur effect to the UI component.
        .createBlurEffect(80f, 80f, Shader.TileMode.MIRROR)
    //80f and 80f represent the horizontal and vertical blur radii, respectively, in pixels.
    // Shader.TileMode.MIRROR: specifies the tiling mode of the blur effect. In this case, it uses a mirror tiling mode.

    val alphaMatrix = RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
       //           R   G   B   A   *
/*RED = */          1f, 0f, 0f, 0f, 0f,
/*GREEN = */        0f, 1f, 0f, 0f, 0f,
/*GREEN = */        0f, 0f, 1f, 0f, 0f,
/*ALPHA = */        0f, 0f, 0f, 50f, -8000f
                )
            )
        )
    )

    return RenderEffect
        .createChainEffect(alphaMatrix, blurEffect)
}

@Composable
fun MainScreen() {
    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnimationProgress by animateFloatAsState(   // In this we are setting the ADD button animation
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(  //Here is the animation spec
            durationMillis = 1000,  //Duration to Open the inside icon after pressing ADD button
            easing = LinearEasing //type of opening and closing effect
        )
    )

    val clickAnimationProgress by animateFloatAsState(  // In this we are setting the ADD button outro animation
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,  //Duration to Open and Close the Outro after pressing ADD button
            easing = LinearEasing
        )
    )
//First, it checks the Android version using the Build.VERSION.SDK_INT property.
// If the version is equal to or greater than the Build.VERSION_CODES.S constant (indicating that the device is running Android 12 or a newer version),
// it calls the getRenderEffect() function and converts the result to a Compose render effect using the asComposeRenderEffect() function.
// This rendered effect is assigned to the variable renderEffect.
    val renderEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getRenderEffect().asComposeRenderEffect()
    } else {
        null
    }


    MainScreen(
        renderEffect = renderEffect, //This property is assigned the value of renderEffect. If the Android version is lower than Android 12, this value will be null.
        fabAnimationProgress = fabAnimationProgress, //This property seems to represent the progress of an animation related to a Floating Action Button (FAB).
        clickAnimationProgress = clickAnimationProgress //This property appears to represent the progress of a click animation.
    ) {
        isMenuExtended.value = isMenuExtended.value.not()
    }
}

@Composable
fun MainScreen(
    renderEffect: androidx.compose.ui.graphics.RenderEffect?, //It represents a visual effect that can be applied to the UI component, like a blur or color filter. It's an optional parameter that can be null
    fabAnimationProgress: Float = 0f, //It represents the progress of an animation related to a Floating Action Button (FAB). It has a default value of 0f.
    clickAnimationProgress: Float = 0f, //It represents the progress of a click animation. It also has a default value of 0f.
    toggleAnimation: () -> Unit = { } //It's a callback function that can be provided to handle animation toggling. It doesn't take any arguments and returns no value.
) {
    Box(
        Modifier //This represent the Bottom navigation Bar
            .fillMaxSize()
            .padding(bottom = 4.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        CustomBottomNavigation()
        Circle(
            color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            animationProgress = 0.5f
        )

        FabGroup(renderEffect = renderEffect, animationProgress = fabAnimationProgress)
        FabGroup(
            renderEffect = null,
            animationProgress = fabAnimationProgress,
            toggleAnimation = toggleAnimation
        )
        Circle(
            color = Color.White,   // This gives the white circular effect
            animationProgress = clickAnimationProgress
        )
    }
}

@Composable
fun Circle(color: Color, animationProgress: Float) {  //animationProgress of type Float: It represents the progress of an animation for the circle.
    val animationValue = sin(PI * animationProgress).toFloat()
    // This line calculates an animationValue based on the animationProgress parameter.
    // The sin function from the Kotlin standard library is used to calculate the sine of the angle (in radians) formed by multiplying PI (π) with animationProgress.
    // The result is then converted to a Float.


    Box(
        modifier = Modifier
            .padding(43.dp)
            .size(56.dp)
            .scale(2 - animationValue)
            // Scales the size of the circle based on the animationValue.
            // As the animationValue increases from 0 to 1, the circle scales down from 2 times its original size to its original size.
            // This creates an animation effect of the circle shrinking.
            .border(
                width = 2.dp,
                color = color.copy(alpha = color.alpha * animationValue),
                shape = CircleShape  //Shape of the animation. Specifies that the shape of the border is a circle.
            )
    )
}

@Composable
fun CustomBottomNavigation() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        //Aligns the child icons horizontally with equal spacing between them.
        // Example: Groups and calendar icon
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .paint(
                painter = painterResource(R.drawable.bottom_navigation),
                contentScale = ContentScale.FillHeight  //This one arranges the length of the bottom navigation bar
            )
            .padding(horizontal = 40.dp)
    ) {
        listOf(Icons.Filled.CalendarToday, Icons.Filled.Group).map { image ->
            IconButton(onClick = { }) { // This creates an IconButton with an empty onClick callback function.
                // It means that clicking on the button doesn't trigger any action.
                Icon(imageVector = image, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@Composable
fun FabGroup(
    animationProgress: Float = 0f,
    renderEffect: androidx.compose.ui.graphics.RenderEffect? = null,
    toggleAnimation: () -> Unit = { }
) {
    Box(
        Modifier
            .fillMaxSize()
            .graphicsLayer { this.renderEffect = renderEffect }
            .padding(bottom = 44.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        AnimatedFab(
            icon = Icons.Default.PhotoCamera,
            modifier = Modifier
                .padding(
                    PaddingValues(
                        bottom = 72.dp,
                        end = 210.dp
                    ) * FastOutSlowInEasing.transform(0f, 0.8f, animationProgress)
                ),
            opacity = LinearEasing.transform(0.2f, 0.7f, animationProgress)
        )

        AnimatedFab(
            icon = Icons.Default.Settings,
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 88.dp,
                ) * FastOutSlowInEasing.transform(0.1f, 0.9f, animationProgress)
            ),
            opacity = LinearEasing.transform(0.3f, 0.8f, animationProgress)
        )

        AnimatedFab(
            icon = Icons.Default.ShoppingCart,
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 72.dp,
                    start = 210.dp
                ) * FastOutSlowInEasing.transform(0.2f, 1.0f, animationProgress)
            ),
            opacity = LinearEasing.transform(0.4f, 0.9f, animationProgress)
        )

        AnimatedFab(
            modifier = Modifier
                .scale(1f - LinearEasing.transform(0.5f, 0.85f, animationProgress)),
        )

        AnimatedFab(
            icon = Icons.Default.Add,
            modifier = Modifier
                .rotate(
                    225 * FastOutSlowInEasing
                        .transform(0.35f, 0.65f, animationProgress)
                ),
            onClick = toggleAnimation,
            backgroundColor = Color.Transparent
        )
    }
}

@Composable
fun AnimatedFab(
    modifier: Modifier,
    icon: ImageVector? = null,
    opacity: Float = 1f,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        onClick = onClick,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        backgroundColor = backgroundColor,
        modifier = modifier.scale(1.25f)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White.copy(alpha = opacity) //Color of Icons
            )
        }
    }
}
