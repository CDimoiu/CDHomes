package com.example.cdhomes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cdhomes.presentation.onboarding.SplashScreen

@Composable
fun NavGraph(
  navController: NavHostController = rememberNavController(),
) {
  NavHost(
    navController = navController,
    startDestination = Destinations.SPLASH
  ) {
    composable(Destinations.SPLASH) { SplashScreen { navController.navigate(Destinations.LISTINGS) } }
  }
}
