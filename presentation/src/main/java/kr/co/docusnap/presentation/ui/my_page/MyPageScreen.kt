package kr.co.docusnap.presentation.ui.my_page

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kr.co.docusnap.presentation.R
import kr.co.docusnap.presentation.ui.common.MessageDialog
import kr.co.docusnap.presentation.viewmodel.MyPageViewModel

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val accountInfo by viewModel.accountInfo.collectAsState(null)

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            viewModel.handleGoogleSignInResult(context, result.data)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "Settings",
            tint = Color.DarkGray,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.TopEnd)
        )

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DocuSnap",
                fontSize = 54.sp,
                fontFamily = FontFamily(Font(resId = R.font.ownglyph_corncorn)),
                color = Color(0xFF5084D5),
                modifier = Modifier.padding(top = 150.dp)
            )
            Spacer(modifier = Modifier.padding(vertical = 30.dp))

            if (accountInfo == null) {
                GoogleSignInButton {
                    // On Click
                    googleLauncher.launch(viewModel.googleSignInClient.signInIntent)
                }
                Spacer(modifier = Modifier.padding(top = 10.dp))
                KakaoSignInButton {
                    // On Click
                    viewModel.signInKakao()
                }
                Spacer(modifier = Modifier.padding(top = 10.dp))
                NaverSignInButton {
                    // On Click
                    viewModel.signInNaver(context)
                }
            } else {
                var isShowDialog by remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        viewModel.signOutAccount()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.social_sign_out_button_text)
                    )
                }

                Button(
                    onClick = {
                        isShowDialog = true
                    },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.data_clear_button_text)
                    )
                }

                if (isShowDialog) {
                    MessageDialog(
                        isShowDialog = true,
                        title = stringResource(id = R.string.data_clear_dialog_title),
                        message = stringResource(id = R.string.data_clear_dialog_message),
                        onConfirmClick = {
                            viewModel.removeAllData()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .shadow(8.dp)
    ) {
        // Google 아이콘(예: vector asset) 배치
        Icon(
            painter = painterResource(id = R.drawable.ic_google_logo),
            contentDescription = "Google Logo",
            tint = Color.Unspecified // 원본 컬러 사용
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(id = R.string.google_sign_in_button_text),
            color = Color.DarkGray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun KakaoSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFEE500)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .shadow(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_kakao_logo),
            contentDescription = "Kakao Logo",
            modifier = Modifier.size(16.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = stringResource(id = R.string.kakao_sign_in_button_text),
            color = Color.Black
                .copy(alpha = 0.85f),
            fontSize = 14.sp
        )
    }
}

@Composable
fun NaverSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF03C75A)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .shadow(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_naver_logo),
            contentDescription = "Naver Logo",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = stringResource(id = R.string.naver_sign_in_button_text),
            fontSize = 14.sp,
            color = Color.White
        )
    }
}