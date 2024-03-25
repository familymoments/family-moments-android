package io.familymoments.app.feature.creatingfamily.model

import android.graphics.Bitmap

data class FamilyProfile(
    val name: String = "",
    val img: Bitmap? = null,
    val uploadCycle: Int? = null
)
