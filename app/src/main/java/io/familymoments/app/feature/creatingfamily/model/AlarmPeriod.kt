package io.familymoments.app.feature.creatingfamily.model

enum class UploadCycle(val value:String, val number:Int?) {
    ONE_DAY("1일", 1),
    THREE_DAY("3일", 3),
    FIVE_DAY("5일", 5),
    ONE_WEEK("일주일", 7),
    TWO_WEEK("2주일", 14),
    ONE_MONTH("한달", 31),
    NONE("주기 설정 하지 않음", null)
}
