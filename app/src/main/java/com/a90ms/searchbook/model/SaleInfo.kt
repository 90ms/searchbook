package com.a90ms.searchbook.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SaleInfo(
    val country: String?,
    val saleability: String?,
    val isEbook: Boolean?,
    val listPrice: PriceData?,
    val retailPrice: PriceData?,
    val buyLink: String?,
    val offers: List<Offers?>?
): Parcelable

@Parcelize
data class PriceData(
    val amount: Long?,
    val currencyCode: String?
): Parcelable

@Parcelize
data class Offers(
    val finskyOfferType: Int?,
    val listPrice: MicrosPriceData?,
    val retailPrice: MicrosPriceData?
): Parcelable

@Parcelize
data class MicrosPriceData(
    val amountInMicros: Long?,
    val currencyCode: String?
): Parcelable