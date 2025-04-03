package kr.co.docusnap.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.co.docusnap.presentation.R
import kr.co.docusnap.presentation.ui.theme.DocuSnapTheme
import kr.co.docusnap.presentation.utils.DisplayUtils
import kr.co.docusnap.presentation.utils.ToastUtils
import kr.co.docusnap.presentation.viewmodel.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var gmsScanOptions: GmsDocumentScannerOptions

    private val scannerLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            viewModel.handleScanResult(
                GmsDocumentScanningResult.fromActivityResultIntent(result.data)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            DocuSnapTheme {
                MainScreen()
            }
        }

        scopeViewModelEvent()

        viewModel.updateColumnCount(DisplayUtils.getColumnCount(this))
    }

    private fun scopeViewModelEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scanRequestEvent.collect {
                    val scanner = GmsDocumentScanning.getClient(gmsScanOptions)
                    scanner.getStartScanIntent(this@MainActivity)
                        .addOnSuccessListener { intentSender ->
                            scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                        }
                        .addOnFailureListener {
                            ToastUtils.showShortToast(getString(R.string.gms_document_scanning_failed_msg))
                        }
                }
            }
        }
    }
}