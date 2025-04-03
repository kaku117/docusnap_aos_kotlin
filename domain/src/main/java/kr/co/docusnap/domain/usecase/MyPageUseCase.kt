package kr.co.docusnap.domain.usecase

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kr.co.docusnap.domain.model.AccountInfo
import kr.co.docusnap.domain.repository.HomeRepository
import kr.co.docusnap.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageUseCase @Inject constructor(
    private val repository: MyPageRepository,
    private val homeRepository: HomeRepository
) {

    fun getAccountInfo(): Flow<AccountInfo?> {
        return repository.getAccountInfo()
    }

    suspend fun signInAccount(accountInfo: AccountInfo) {
        repository.signIn(accountInfo)
    }

    suspend fun signOutAccount() {
        repository.signOut()
    }

    fun handleGoogleSignInResult(
        context: Context,
        intent: Intent,
        firebaseAuth: FirebaseAuth,
        onSignInSuccess: (GoogleSignInAccount) -> Unit,
        onSignInFailure: (String) -> Unit
    ) {
        try {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

            val account = task.result ?: return
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(context as Activity) { task ->
                    if (task.isSuccessful) {
                        // 로그인 성공
                        onSignInSuccess(account)

                    } else {
                        // 로그인 실패
                        onSignInFailure("구글 로그인에 실패했습니다.")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeAllData(context: Context): Boolean {
        homeRepository.removeAllData()

        val filesDir = context.filesDir
        return filesDir.deleteRecursively()
    }
}