package com.example.cdhomes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cdhomes.presentation.listingdetail.ListingDetailScreen
import com.example.cdhomes.presentation.listings.ListingsScreen
import com.example.cdhomes.presentation.navigation.Destinations.LISTING_DETAIL
import com.example.cdhomes.presentation.onboarding.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
  NavHost(
    navController = navController,
    startDestination = Destinations.SPLASH
  ) {
    composable(Destinations.SPLASH) {
      SplashScreen {
        navController.navigate(Destinations.LISTINGS) {
          popUpTo(Destinations.SPLASH) { inclusive = true }
        }
      }
    }

    composable(Destinations.LISTINGS) {
      ListingsScreen(onItemClick = { id ->
        navController.navigate("listing_detail/$id")
      })
    }

    composable(
      route = LISTING_DETAIL,
      arguments = listOf(navArgument("id") { type = NavType.StringType })
    ) { backStackEntry ->
      ListingDetailScreen(viewModel = hiltViewModel())
    }
  }
}
