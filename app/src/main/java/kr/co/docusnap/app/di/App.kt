package kr.co.docusnap.app.di

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp
import kr.co.docusnap.R

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        /*val keyHash = Utility.getKeyHash(this)
        Log.d("aaa", "keyHash: $keyHash")*/

        // Kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))

        // Naver SDK 초기화
        NaverIdLoginSDK.initialize(
            context = this,
            clientId = getString(R.string.naver_client_id),
            clientSecret = getString(R.string.naver_client_secret),
            clientName = getString(R.string.naver_client_name)
        )
    }
}