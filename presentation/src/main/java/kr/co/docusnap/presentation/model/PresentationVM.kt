package kr.co.docusnap.presentation.model

import kr.co.docusnap.domain.model.BaseModel

sealed class PresentationVM<T: BaseModel>(val model: T) {

}