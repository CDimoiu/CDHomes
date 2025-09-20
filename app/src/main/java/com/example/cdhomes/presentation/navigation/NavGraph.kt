package com.example.cdhomes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cdhomes.presentation.listingdetail.ListingDetailScreen
import com.example.cdhomes.presentation.listings.ListingsScreen
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

    composable(Destinations.LISTINGS) {
      ListingsScreen(
        onItemClick = { id ->
          navController.navigate(Destinations.listingDetailRoute(id))
        }
      )
    }

    composable(
      route = Destinations.LISTING_DETAIL,
      arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { backStackEntry ->
      val id = backStackEntry.arguments?.getInt("id") ?: return@composable
      ListingDetailScreen(id = id)
    }
  }
}
