package com.ninhttd.moneycatcher.domain.model

import android.net.Uri
import com.ninhttd.moneycatcher.data.model.ParseIntentResponse

sealed class MessageItem {
    data class TextMessage(val text: String, val isSent: Boolean = true) : MessageItem()
    data class ImageMessage(val uri: Uri) : MessageItem()
    data class ResultMessage(val data: ParseIntentResponse) : MessageItem()
}