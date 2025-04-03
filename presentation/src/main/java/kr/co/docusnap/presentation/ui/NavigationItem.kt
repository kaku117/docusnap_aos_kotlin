package kr.co.docusnap.presentation.ui

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import kr.co.docusnap.domain.model.ScanFile
import kr.co.docusnap.presentation.ui.DocumentViewerNav.argName
import kr.co.docusnap.presentation.ui.NavigationRouteName.DEEP_LINK_SCHEME
import kr.co.docusnap.presentation.ui.NavigationRouteName.MAIN_FAVORITE
import kr.co.docusnap.presentation.ui.NavigationRouteName.MAIN_HOME
import kr.co.docusnap.presentation.ui.NavigationRouteName.MAIN_MY_PAGE
import kr.co.docusnap.presentation.utils.GsonUtils

sealed class MainNav(
    override val route: String,
    val icon: ImageVector
): Destination {

    object Home: MainNav(MAIN_HOME, Icons.Filled.Home)
    object Favorite: MainNav(MAIN_FAVORITE, Icons.Filled.Favorite)
    object MyPage: MainNav(MAIN_MY_PAGE, Icons.Filled.AccountCircle)

    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$argName" }
    )

    companion object {
        fun isMainScreen(route: String): Boolean {
            return when(route) {
                MAIN_HOME, MAIN_FAVORITE, MAIN_MY_PAGE -> true
                else -> false
            }
        }
    }
}

object LicenseReportScreenNav: Destination {
    override val route: String = NavigationRouteName.LIBRARY_LICENSE
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route" }
    )
}

object DocumentViewerNav: DestinationWithArgs<ScanFile> {
    override val route: String = NavigationRouteName.DOCUMENT_VIEWER
    override val argName: String = "scan_file"
    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route/{$argName}" }
    )

    override fun navigationWithArg(item: ScanFile): String {
        val arg = GsonUtils.toJson(item)
        // 파일 경로에 /가 들어가서 인코딩해서 전달함
        val encodedArg = Uri.encode(arg)
        return "$route/$encodedArg"
    }

    override fun findArgument(navBackStackEntry: NavBackStackEntry): ScanFile? {
        val scanFileString = navBackStackEntry.arguments?.getString(argName)
        // 전달받은 문자열을 디코딩하여 다시 객체화 함
        val decodedArg = Uri.decode(scanFileString)
        return GsonUtils.fromJson<ScanFile>(decodedArg)
    }
}

object ImageViewerNav: DestinationWithArgs<ScanFile> {
    override val route: String = NavigationRouteName.IMAGE_VIEWER
    override val argName: String = "scan_file"
    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route/{$argName}" }
    )

    override fun navigationWithArg(item: ScanFile): String {
        val arg = GsonUtils.toJson(item)
        // 파일 경로에 /가 들어가서 인코딩해서 전달함
        val encodedArg = Uri.encode(arg)
        return "$route/$encodedArg"
    }

    override fun findArgument(navBackStackEntry: NavBackStackEntry): ScanFile? {
        val scanFileString = navBackStackEntry.arguments?.getString(argName)
        // 전달받은 문자열을 디코딩하여 다시 객체화 함
        val decodedArg = Uri.decode(scanFileString)
        return GsonUtils.fromJson<ScanFile>(decodedArg)
    }
}

interface Destination {
    val route: String
    val deepLinks: List<NavDeepLink>
}

interface DestinationWithArgs<T>: Destination {
    val argName: String
    val arguments: List<NamedNavArgument>

    fun routeWithArgName() = "$route/{$argName}"
    fun navigationWithArg(item: T): String
    fun findArgument(navBackStackEntry: NavBackStackEntry): T?
}

object NavigationRouteName {
    const val DEEP_LINK_SCHEME = "docusnap://"
    const val MAIN_HOME = "main_home"
    const val MAIN_FAVORITE = "main_favorite"
    const val MAIN_MY_PAGE = "main_my_page"
    const val SETTING = "setting"
    const val QR_CODE_SCAN = "qr_code_scan"
    const val DOCUMENT_VIEWER = "document_viewer"
    const val IMAGE_VIEWER = "image_viewer"
    const val LIBRARY_LICENSE = "library_license"
}