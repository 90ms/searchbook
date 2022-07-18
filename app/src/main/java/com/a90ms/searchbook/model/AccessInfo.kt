package com.a90ms.searchbook.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccessInfo(
    val country: String?,
    val viewability: String?,
    val embeddable: Boolean?,
    val publicDomain: Boolean?,
    val textToSpeechPermission: String?,
    val epub: Epub?,
    val pdf: Pdf?,
    val webReaderLink: String?,
    val accessViewStatus: String?,
    val quoteSharingAllowed: Boolean?
): Parcelable

@Parcelize
data class Epub(
    val isAvailable: Boolean?
): Parcelable

@Parcelize
data class Pdf(
    val isAvailable: Boolean?,
    val acsTokenLink: String?
): Parcelable

