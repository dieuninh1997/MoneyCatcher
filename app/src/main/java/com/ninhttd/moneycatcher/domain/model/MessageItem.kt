package com.ninhttd.moneycatcher.domain.model

import android.net.Uri

sealed class MessageItem {
    data class TextMessage(val text: String) : MessageItem()
    data class ImageMessage(val uri: Uri) : MessageItem()
}