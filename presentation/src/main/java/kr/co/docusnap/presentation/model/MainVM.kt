package kr.co.docusnap.presentation.model

import kr.co.docusnap.domain.model.ScanFile
import kr.co.docusnap.presentation.delegate.ScanFileDelegate

class MainVM(
    scanFile: ScanFile,
    private val scanFileDelegate: ScanFileDelegate
): PresentationVM<ScanFile>(scanFile), ScanFileDelegate by scanFileDelegate {

    fun onFavoriteButtonClick() {
        scanFileDelegate.onFavoriteButtonClick(model)
    }
}