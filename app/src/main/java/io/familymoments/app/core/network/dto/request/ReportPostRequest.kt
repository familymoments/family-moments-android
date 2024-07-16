package io.familymoments.app.core.network.dto.request

data class ReportPostRequest(
    val reportReason: String,
    val details: String
)
