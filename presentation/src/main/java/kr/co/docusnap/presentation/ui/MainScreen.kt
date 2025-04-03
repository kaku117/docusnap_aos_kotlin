package kr.co.docusnap.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kr.co.docusnap.presentation.model.MainVM
import kr.co.docusnap.presentation.ui.favorite.FavoriteScreen
import kr.co.docusnap.presentation.ui.main.HomeScreen
import kr.co.docusnap.presentation.ui.my_page.MyPageScreen
import kr.co.docusnap.presentation.ui.viewer.DocumentViewerScreen
import kr.co.docusnap.presentation.ui.viewer.ImageViewerScreen
import kr.co.docusnap.presentation.utils.NavigationUtils
import kr.co.docusnap.presentation.viewmodel.MainViewModel

// FilterChip, ChoiceChip 파일 타입 선택 버튼 -> 리스트 뷰 보여줄 때

@Composable
fun MainScreen() {
    val viewModel = hiltViewModel<MainViewModel>()

    val navController = rememberNavController()

    val navBackStackEntity by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntity?.destination?.route

    Scaffold(
        bottomBar = {
            currentRoute?.let { route ->
                if (MainNav.isMainScreen(route)) {
                    MainBottomNavigationBar(navController, route)
                }
            }

        }
    ) { innerPadding ->
        MainNavigateScreen(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@Composable
fun MainBottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?
) {

    val bottomNavigationItems = listOf(
        MainNav.Home,
        MainNav.Favorite,
        MainNav.MyPage
    )

    NavigationBar {
        bottomNavigationItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, item.route) },
                selected = currentRoute == item.route,
                onClick = {
                    NavigationUtils.navigate(navController, item.route, navController.graph.startDestinationRoute)
                }
            )
        }
    }
}

@Composable
fun MainNavigateScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = MainNav.Home.route
    ) {
        composable(MainNav.Home.route) {
            HomeScreen(modifier, navController, viewModel)
        }
        composable(MainNav.Favorite.route) {
            FavoriteScreen(modifier, navController, viewModel)
        }
        composable(MainNav.MyPage.route) {
            MyPageScreen()
        }
        composable(
            route = DocumentViewerNav.routeWithArgName(),
            arguments = DocumentViewerNav.arguments,
            deepLinks = DocumentViewerNav.deepLinks
        ) {
            val model = DocumentViewerNav.findArgument(it)
            model?.let {
                val convertModel = viewModel.convertModelToModelVM(model) as MainVM
                DocumentViewerScreen(viewModel, convertModel)
            }
        }
        composable(
            route = ImageViewerNav.routeWithArgName(),
            arguments = ImageViewerNav.arguments,
            deepLinks = ImageViewerNav.deepLinks
        ) {
            val model = ImageViewerNav.findArgument(it)
            model?.let {
                val convertModel = viewModel.convertModelToModelVM(model) as MainVM
                ImageViewerScreen(viewModel, convertModel)
            }
        }
    }
}