package com.drawling.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.drawling.app.auth.presentation.OtpScreen
import com.drawling.app.auth.presentation.PhoneScreen
import com.drawling.app.auth.presentation.SplashAuthScreen
import com.drawling.app.canvas.presentation.CanvasScreen
import com.drawling.app.gallery.presentation.GalleryScreen
import com.drawling.app.rooms.presentation.HomeScreen
import com.drawling.app.rooms.presentation.JoinRoomScreen
import com.drawling.app.surprises.presentation.SurpriseRevealScreen

sealed class Screen(val route: String) {
    object SplashAuth : Screen("splash_auth")
    object Phone : Screen("phone")
    object Otp : Screen("otp/{phoneNumber}") {
        fun createRoute(phone: String) = "otp/$phone"
    }
    object Home : Screen("home")
    object JoinRoom : Screen("join_room?code={code}") {
        fun createRoute(code: String = "") = "join_room?code=$code"
    }
    object Canvas : Screen("canvas/{roomId}/{canvasId}") {
        fun createRoute(roomId: String, canvasId: String) = "canvas/$roomId/$canvasId"
    }
    object Gallery : Screen("gallery/{roomId}") {
        fun createRoute(roomId: String) = "gallery/$roomId"
    }
    object SurpriseReveal : Screen("surprise_reveal/{surpriseId}") {
        fun createRoute(surpriseId: String) = "surprise_reveal/$surpriseId"
    }
}

@Composable
fun DrawlingNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.SplashAuth.route) {
        composable(Screen.SplashAuth.route) {
            SplashAuthScreen(
                onAuthenticated = { navController.navigate(Screen.Home.route) { popUpTo(0) } },
                onUnauthenticated = { navController.navigate(Screen.Phone.route) { popUpTo(0) } }
            )
        }
        composable(Screen.Phone.route) {
            PhoneScreen(onOtpSent = { phone -> navController.navigate(Screen.Otp.createRoute(phone)) })
        }
        composable(Screen.Otp.route, arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })) { backStack ->
            OtpScreen(
                phoneNumber = backStack.arguments?.getString("phoneNumber") ?: "",
                onVerified = { navController.navigate(Screen.Home.route) { popUpTo(0) } }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCanvas = { rId, cId -> navController.navigate(Screen.Canvas.createRoute(rId, cId)) },
                onNavigateToGallery = { rId -> navController.navigate(Screen.Gallery.createRoute(rId)) },
                onNavigateToJoin = { navController.navigate(Screen.JoinRoom.createRoute()) }
            )
        }
        composable(Screen.JoinRoom.route,
            arguments = listOf(navArgument("code") { type = NavType.StringType; defaultValue = "" }),
            deepLinks = listOf(navDeepLink { uriPattern = "https://drawling.app/join?code={code}" })
        ) { backStack ->
            JoinRoomScreen(
                initialCode = backStack.arguments?.getString("code") ?: "",
                onJoined = { rId -> navController.navigate(Screen.Canvas.createRoute(rId, "current")) { popUpTo(Screen.Home.route) } }
            )
        }
        composable(Screen.Canvas.route, arguments = listOf(
            navArgument("roomId") { type = NavType.StringType },
            navArgument("canvasId") { type = NavType.StringType }
        )) { backStack ->
            CanvasScreen(
                roomId = backStack.arguments?.getString("roomId") ?: "",
                canvasId = backStack.arguments?.getString("canvasId") ?: "",
                onNavigateBack = { navController.popBackStack() },
                onNavigateToGallery = { navController.navigate(Screen.Gallery.createRoute(backStack.arguments?.getString("roomId") ?: "")) }
            )
        }
        composable(Screen.Gallery.route, arguments = listOf(navArgument("roomId") { type = NavType.StringType })) { backStack ->
            val roomId = backStack.arguments?.getString("roomId") ?: ""
            GalleryScreen(
                roomId = roomId,
                onNavigateBack = { navController.popBackStack() },
                onOpenCanvas = { cId -> navController.navigate(Screen.Canvas.createRoute(roomId, cId)) }
            )
        }
        composable(Screen.SurpriseReveal.route, arguments = listOf(navArgument("surpriseId") { type = NavType.StringType })) { backStack ->
            SurpriseRevealScreen(
                surpriseId = backStack.arguments?.getString("surpriseId") ?: "",
                onDone = { navController.popBackStack() }
            )
        }
    }
}
