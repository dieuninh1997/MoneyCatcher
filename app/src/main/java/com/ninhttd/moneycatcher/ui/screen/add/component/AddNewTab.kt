package com.ninhttd.moneycatcher.ui.screen.add.component

import androidx.annotation.StringRes
import com.ninhttd.moneycatcher.R


enum class AddNewTab(@StringRes val nameId: Int) {
    Outcome(R.string.outcome),
    Income(R.string.income),
}