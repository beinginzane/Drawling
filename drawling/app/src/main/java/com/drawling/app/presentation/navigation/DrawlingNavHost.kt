package com.drawling.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.drawling.app.presentation.auth.AuthScreen
import com.drawling.app.presentation.auth.OtpScreen
import com.drawling.app.presentation.canvas.CanvasScreen
import com.drawling.app.presentation.gallery.GalleryScreen
import com.drawling.app.presentation.home.HomeScreen
import com.drawling.app.presentation.room.CreateRoomScreen
import com.drawling.app.presentation.room.JoinRoomScreen
import com.drawling.app.presentation.surprise.SurpriseRevealScreen

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Otp : Screen("otp/{phoneNumber}") {
        fun createRoute(phone: String) = "otp/$phone"
    }
    object Home : Screen("home")
    object Canvas : Screen("canvas/{roomId}/{canvasId}") {
        fun createRoute(roomId: String, canvasId: String) = "canvas/$roomId/$canvasId"
    }
    object Gallery : Screen("gallery/{roomId}") {
        fun createRoute(roomId: String) = "gallery/$roomId"
    }
    object CreateRoom : Screen("create_room")
    object JoinRoom : Screen("join_room?inviteCode={inviteCode}") {
        fun createRoute(inviteCode: String? = null) =
            if (inviteCode != null) "join_room?inviteCode=$inviteCode" else "join_room"
    }
    object SurpriseReveal : Screen("surprise/{surpriseId}") {
        fun createRoute(surpriseId: String) = "surprise/$surpriseId"
    }
}

@Composable
fun DrawlingNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onNavigateToOtp = { phone ->
                    navController.navigate(Screen.Otp.createRoute(phone))
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Otp.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStack ->
            val phone = backStack.arguments?.getString("phoneNumber") ?: ""
            OtpScreen(
                phoneNumber = phone,
                onVerified = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCanvas = { roomId, canvasId ->
                    navController.navigate(Screen.Canvas.createRoute(roomId, canvasId))
                },
                onNavigateToGallery = { roomId ->
                    navController.navigate(Screen.Gallery.createRoute(roomId))
                },
                onNavigateToCreateRoom = {
                    navController.navigate(Screen.CreateRoom.route)
                },
                onNavigateToJoinRoom = {
                    navController.navigate(Screen.JoinRoom.createRoute())
                },
                onNavigateToSurprise = { surpriseId ->
                    navController.navigate(Screen.SurpriseReveal.createRoute(surpriseId))
                }
            )
        }

        composable(
            route = Screen.Canvas.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType },
                navArgument("canvasId") { type = NavType.StringType }
            )
        ) {
            CanvasScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.Gallery.route,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) {
            GalleryScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenCanvas = { roomId, canvasId ->
                    navController.navigate(Screen.Canvas.createRoute(roomId, canvasId))
                }
            )
        }

        composable(Screen.CreateRoom.route) {
            CreateRoomScreen(
                onRoomCreated = { roomId, canvasId ->
                    navController.navigate(Screen.Canvas.createRoute(roomId, canvasId)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.JoinRoom.route,
            arguments = listOf(
                navArgument("inviteCode") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://drawling.app/join?inviteCode={inviteCode}"
            })
        ) {
            JoinRoomScreen(
                onRoomJoined = { roomId, canvasId ->
                    navController.navigate(Screen.Canvas.createRoute(roomId, canvasId)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SurpriseReveal.route,
            arguments = listOf(navArgument("surpriseId") { type = NavType.StringType })
        ) {
            SurpriseRevealScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
