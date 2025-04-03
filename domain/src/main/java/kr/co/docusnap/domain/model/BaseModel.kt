package kr.co.docusnap.domain.model

sealed class BaseModel {
    abstract val type: ModelType
}

enum class ModelType {
    HOME
}