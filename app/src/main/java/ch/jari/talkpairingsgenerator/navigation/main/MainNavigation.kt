package ch.jari.talkpairingsgenerator.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ch.jari.talkpairingsgenerator.navigation.NavigationManager
import ch.jari.talkpairingsgenerator.navigation.Screen
import ch.jari.talkpairingsgenerator.ui.pairing.PairingScreen
import ch.jari.talkpairingsgenerator.ui.person.PersonWithFriendsScreen
import ch.jari.talkpairingsgenerator.ui.personlist.PersonListScreen

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController(),
) {
    val mainRoutes = MainRoutes()
    NavHost(
        navController = navController,
        startDestination = mainRoutes.getFirstScreen().route
    ) {
        mainRoutes.buildNavGraph(this, navController)
    }
}

class MainRoutes : NavigationManager() {
    object PersonList : Screen(
        route = "person_list",
        content = { navController, _ ->
            PersonListScreen(navController = navController)
        })

    object PersonWithFriends : Screen(route = "person",
        content = { navController, _ ->
            PersonWithFriendsScreen(navController = navController)
        })

    object Pairing : Screen(route = "pairings",
        title = "Pairings",
        showBackButton = true,
        content = { navController, _ ->
            PairingScreen(navController = navController)
        })

    override val screenList = listOf(PersonList, PersonWithFriends, Pairing)
}