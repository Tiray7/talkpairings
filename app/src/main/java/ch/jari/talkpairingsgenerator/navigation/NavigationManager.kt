package ch.jari.talkpairingsgenerator.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ch.jari.talkpairingsgenerator.ui.components.TPAppBar

open class Screen(
    val route: String,
    val content: @Composable (NavHostController, NavBackStackEntry) -> Unit,
    val title: String? = null,
    val showBackButton: Boolean = false,
)

abstract class NavigationManager(
    private val firstScreen: Screen? = null,
) {
    abstract val screenList: List<Screen>
    open val navigationRoute: String? = null

    fun getFirstScreen() = firstScreen ?: screenList.first()

    private fun addComposables(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
    ) {
        navGraphBuilder.apply {
            screenList.forEach { screen ->
                composable(screen.route) { stackEntry ->
                    Scaffold(topBar = {
                        if (screen.showBackButton || screen.title != null) {
                            TPAppBar(
                                title = screen.title,
                                popBack = if (screen.showBackButton) {
                                    { navController.popBackStack() }
                                } else null
                            )
                        }
                    }) {
                        Box(Modifier.padding(it)) {
                            screen.content(navController, stackEntry)
                        }
                    }
                }
            }
        }
    }

    fun buildNavGraph(navGraphBuilder: NavGraphBuilder, navHostController: NavHostController) {
        navGraphBuilder.apply {
            navigationRoute?.let {
                navigation(
                    startDestination = screenList.first().route,
                    it
                ) {
                    addComposables(this, navHostController)
                }
            } ?: addComposables(this, navHostController)
        }
    }

    @Composable
    fun BuildNavHost(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = getFirstScreen().route,
            route = navigationRoute
        ) {
            buildNavGraph(this, navController)
        }
    }
}

fun NavHostController.screenNavigate(screen: Screen) {
    navigate(route = screen.route)
}