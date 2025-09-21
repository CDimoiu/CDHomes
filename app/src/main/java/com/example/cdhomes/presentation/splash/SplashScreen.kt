package com.example.cdhomes.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.example.cdhomes.R
import com.example.cdhomes.presentation.theme.Dimens.ImageHeightMedium
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onTimeout: () -> Unit,
) {

  val scale = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    scale.animateTo(
      targetValue = 1f,
      animationSpec = tween(
        durationMillis = 1000,
        easing = EaseOutBack
      )
    )

    delay(1000)
    onTimeout()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(id = R.drawable.icon_home),
      contentDescription = null,
      modifier = Modifier
        .size(ImageHeightMedium)
        .scale(scale.value)
    )
  }
}
