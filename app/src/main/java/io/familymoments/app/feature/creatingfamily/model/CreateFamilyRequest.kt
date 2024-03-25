package io.familymoments.app.feature.creatingfamily.model

data class CreateFamilyRequest(
    val familyName: String = "",
    val uploadCycle: Int? = 0
)
