package com.soa.products.ejb.exception


sealed class FilterException(
    override val message: String,
    open val status: Int,
): RuntimeException(message) {

    class UnsupportedOperatorForValueTypeException(override val message: String, override val status: Int)
        : FilterException(message, status)
    class UnsupportedFieldException(override val message: String, override val status: Int)
        : FilterException(message, status)
    class UnsupportedOperatorException(override val message: String, override val status: Int)
        : FilterException(message, status)
    class UnsupportedSortTypeException(override val message: String, override val status: Int)
        : FilterException(message, status)
    class ValueParseException(override val message: String, override val status: Int)
        : FilterException(message, status)
}

sealed class BandOperationException(
    override val message: String,
    open val status: Int,
): RuntimeException(message) {
    class NotFoundBandException(override val message: String, override val status: Int)
        : BandOperationException(message, status)
    class BandOperationParamsException(override val message: String, override val status: Int)
        : BandOperationException(message, status)
}

sealed class BestGroupOperationException(
    override val message: String,
    open val status: Int,
): RuntimeException(message) {
    class TwiceRewardException(override val message: String, override val status: Int)
        : BestGroupOperationException(message, status)
}

class UnknownError(
    override val message: String,
    val status: Int,
): RuntimeException(message)