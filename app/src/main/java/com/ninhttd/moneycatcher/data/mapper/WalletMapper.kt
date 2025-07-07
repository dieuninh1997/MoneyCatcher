package com.ninhttd.moneycatcher.data.mapper

import com.ninhttd.moneycatcher.data.model.WalletDto
import com.ninhttd.moneycatcher.domain.model.Wallet

data class WalletWrapper(
    val wallet: Wallet,
    val isSelected: Boolean = false
)

fun List<WalletDto>.asWrappedList(currentId: String?): List<WalletWrapper> {
    return this.map { dto ->
        val wallet = dto.asDomainModel()
        WalletWrapper(
            wallet = wallet,
            isSelected = wallet.id == currentId
        )
    }
}


private fun WalletDto.asDomainModel(): Wallet {
    return Wallet(
        id = this.id,
        name = this.name,
        balance = this.balance,
        isDefault = this.is_default,
        userId = this.user_id,
        initBalance = this.init_balance
    )
}
