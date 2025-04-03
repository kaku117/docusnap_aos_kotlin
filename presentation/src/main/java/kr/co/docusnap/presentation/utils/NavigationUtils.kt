package kr.co.docusnap.presentation.utils

import androidx.navigation.NavHostController
import kr.co.docusnap.presentation.ui.Destination
import kr.co.docusnap.presentation.ui.MainNav
import kr.co.docusnap.presentation.ui.NavigationRouteName

object NavigationUtils {

    fun navigate(
        navController: NavHostController,
        routeName: String,
        backStackRouteName: String? = null,
        isLaunchSingleTop: Boolean = true,
        needToRestoreState: Boolean = true
    ) {
        navController.navigate(routeName) {
            if (backStackRouteName != null) {
                popUpTo(backStackRouteName) {
                    saveState = true
                }
                launchSingleTop = isLaunchSingleTop
                restoreState = needToRestoreState
            }
        }
    }

    fun findDestination(route: String?): Destination {
        return when(route) {
            NavigationRouteName.MAIN_HOME -> MainNav.Home
            NavigationRouteName.MAIN_FAVORITE -> MainNav.Favorite
            NavigationRouteName.MAIN_MY_PAGE -> MainNav.MyPage
            else -> MainNav.Home
        }
    }
}