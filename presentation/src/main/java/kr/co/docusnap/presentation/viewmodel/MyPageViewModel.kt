package kr.co.docusnap.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.docusnap.domain.model.AccountInfo
import kr.co.docusnap.domain.usecase.MyPageUseCase
import kr.co.docusnap.presentation.R
import kr.co.docusnap.presentation.utils.ToastUtils
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val useCase: MyPageUseCase
): ViewModel() {

    val accountInfo = useCase.getAccountInfo()
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    /**
     * 구글 로그인
     */
    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    // 구글 - 로그인 및 프로필 조회 처리
    fun handleGoogleSignInResult(context: Context, intent: Intent?) {
        if (intent != null) {
            useCase.handleGoogleSignInResult(
                context = context,
                intent = intent,
                firebaseAuth = firebaseAuth,
                onSignInSuccess = { account ->
                    // 유저 정보 조회 성공
                    signInAccount(
                        AccountInfo(
                            nickname = account.displayName.orEmpty(),
                            type = AccountInfo.Type.GOOGLE,
                            profileImageUrl = account.photoUrl.toString(),
                            tokenId = account.idToken.orEmpty()
                        )
                    )
                },
                onSignInFailure = {
                    // 유저 정보 조회 실패
                    ToastUtils.showShortToast(it)
                    firebaseAuth.signOut()
                }
            )
        } else {
            ToastUtils.showShortToast(
                context.getString(R.string.google_sign_in_failed_msg)
            )
        }
    }

    /**
     * 카카오 로그인
     */

    // 카카오 로그인 콜백
    private val kakaoSignInCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        when {
            error != null -> {
                ToastUtils.showShortToast(
                    context.getString(R.string.kakao_sign_in_failed_msg)
                )
            }
            token != null -> {
                getProfileByKakaoApi(token)
            }
        }
    }

    // 카카오 - 로그인
    fun signInKakao() {
        val kakaoClient = UserApiClient.instance

        if (kakaoClient.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡 설치된 상태일 때 처리

            kakaoClient.loginWithKakaoTalk(context) { _, error ->
                if (error != null) {
                    // 카카오톡 로그인 실패
                    Log.e("Kakao", "kakaotalk sign in failed", error)
                }
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    ToastUtils.showShortToast(
                        context.getString(R.string.kakao_sign_in_cancel_msg)
                    )
                    return@loginWithKakaoTalk
                }

                kakaoClient.loginWithKakaoAccount(context, callback = kakaoSignInCallback)
            }
        } else {
            // 카카오톡 미설치된 상태일 때 처리

            kakaoClient.loginWithKakaoAccount(context, callback = kakaoSignInCallback)
        }
    }

    // 카카오 - 프로필 조회
    private fun getProfileByKakaoApi(token: OAuthToken) {
        UserApiClient.instance.me { user, error ->
            when {
                error != null -> {
                    ToastUtils.showShortToast(
                        context.getString(R.string.kakao_request_user_info_failed_msg)
                    )
                }
                user != null -> {
                    val profile = user.kakaoAccount?.profile
                    signInAccount(
                        AccountInfo(
                            nickname = profile?.nickname.orEmpty(),
                            type = AccountInfo.Type.KAKAO,
                            profileImageUrl = profile?.profileImageUrl.orEmpty(),
                            tokenId = token.idToken.orEmpty()
                        )
                    )
                }
            }
        }
    }

    /**
     * 네이버 로그인
     */

    // 네이버 - 로그인
    private val naverLoginCallback = object : OAuthLoginCallback {
        override fun onError(errorCode: Int, message: String) {
            ToastUtils.showShortToast(
                context.getString(R.string.naver_sign_in_failed_msg)
            )
        }

        override fun onFailure(httpStatus: Int, message: String) {
            ToastUtils.showShortToast(
                context.getString(R.string.naver_sign_in_failed_msg)
            )
        }

        override fun onSuccess() {
            naverTokenId = NaverIdLoginSDK.getAccessToken()
            NidOAuthLogin().callProfileApi(naverProfileApiCallback)
        }
    }

    private var naverTokenId: String? = null

    // 네이버 - 프로필 조회
    private val naverProfileApiCallback = object : NidProfileCallback<NidProfileResponse> {
        override fun onError(errorCode: Int, message: String) {
            ToastUtils.showShortToast(
                context.getString(R.string.naver_request_user_info_failed_msg)
            )
        }

        override fun onFailure(httpStatus: Int, message: String) {
            ToastUtils.showShortToast(
                context.getString(R.string.naver_request_user_info_failed_msg)
            )
        }

        override fun onSuccess(result: NidProfileResponse) {
            val profile = result.profile

            signInAccount(
                AccountInfo(
                    nickname = profile?.nickname.orEmpty(),
                    type = AccountInfo.Type.NAVER,
                    profileImageUrl = profile?.profileImage.orEmpty(),
                    tokenId = naverTokenId.orEmpty()
                )
            )
        }

    }

    fun signInNaver(context: Context) {
        NaverIdLoginSDK.authenticate(context, naverLoginCallback)
    }

    private fun signInAccount(accountInfo: AccountInfo) {
        viewModelScope.launch {
            useCase.signInAccount(accountInfo)
        }
    }

    fun signOutAccount() {
        firebaseAuth.signOut()

        viewModelScope.launch {
            useCase.signOutAccount()
        }
    }

    fun removeAllData() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                useCase.removeAllData(context)
            }
            when (result) {
                true -> ToastUtils.showShortToast(context.getString(R.string.data_clear_completed_msg))
                false -> ToastUtils.showShortToast(context.getString(R.string.data_clear_failed_msg))
            }
        }
    }
}