package io.familymoments.app.feature.postdetail.model.uistate

data class PopupUiState(
    val showDeleteCompletePopup: Boolean = false,
    val executePopupUiState: ExecutePopupUiState = ExecutePopupUiState(),
    val reportPopupUiState: ReportPopupUiState = ReportPopupUiState(),
    val popupStatusLogics: PopupStatusLogics = PopupStatusLogics()
)

data class PopupStatusLogics(
    val showDeleteCompletePopup: (Boolean) -> Unit = {},
    val showExecutePopup: (Boolean, String, () -> Unit) -> Unit = { _, _, _ -> },
    val showReportPopup: (Boolean, () -> Unit) -> Unit = { _, _ -> },
)

data class ExecutePopupUiState(
    val show: Boolean = false,
    val content: String = "",
    val execute: () -> Unit = {}
)

data class ReportPopupUiState(
    val show: Boolean = false,
    val execute: () -> Unit = {}
)
