package com.ninhttd.moneycatcher.ui.screen.add.component

import android.icu.text.DecimalFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ninhttd.moneycatcher.common.TransactionType
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorOnSurfaceDark
import java.time.LocalDate


fun formatMoney(amount: Long): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(amount)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatVietnameseDate(date: LocalDate): String {
    val dayOfWeek = when (date.dayOfWeek) {
        java.time.DayOfWeek.MONDAY -> "Thứ Hai"
        java.time.DayOfWeek.TUESDAY -> "Thứ Ba"
        java.time.DayOfWeek.WEDNESDAY -> "Thứ Tư"
        java.time.DayOfWeek.THURSDAY -> "Thứ Năm"
        java.time.DayOfWeek.FRIDAY -> "Thứ Sáu"
        java.time.DayOfWeek.SATURDAY -> "Thứ Bảy"
        java.time.DayOfWeek.SUNDAY -> "Chủ Nhật"
    }

    return "$dayOfWeek, ${
        date.dayOfMonth.toString().padStart(2, '0')
    } tháng ${date.monthValue} năm ${date.year}"
}


@Composable
fun TransactionContent(
    currentWalletId: String?,
    currentWallet: Wallet?,
    onChangeCurrentWalletId: (String) -> Unit,
    wallets: List<Wallet>,
    date: String,
    categoriesList: List<Category>?,
    onSubmit: (
        note: String,
        amount: String,
        type: TransactionType,
        date: LocalDate,
        category: Category?, onResetInputing: () -> Unit
    ) -> Unit,
    onNavigateEditCategory: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    transactionType: TransactionType,
    modifier: Modifier = Modifier
) {
    var note by remember { mutableStateOf("") }
    var money by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 50.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {

            //vi
            WalletPickerRow(
                currentWallet, onClick = {
                    showBottomSheet = true
                }, onNavigateDetails = onNavigateDetails
            )
            Spacer(modifier = Modifier.height(1.dp))

            DatePickerRow(selectedDate, onDateSelected = {
                selectedDate = it
            })
            Spacer(modifier = Modifier.height(5.dp))

            NoteInput(
                note = note, onNoteChange = {
                    note = it
                })
            Spacer(modifier = Modifier.height(5.dp))

            MoneyInput(money, onAmountChange = {
                money = it
            })
            Spacer(modifier = Modifier.height(5.dp))

            CategoryPickerRow(
                categoriesList, selectedCategory = selectedCategory, onCategorySelected = {
                    selectedCategory = it
                }, onNavigateEditCategory = onNavigateEditCategory
            )
        }

        Button(
            onClick = {
                onSubmit(
                    money, note, transactionType, selectedDate, selectedCategory, {
                        note = ""
                        money = ""
                        selectedDate = LocalDate.now()
                        selectedCategory = null
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(ColorColdPurplePink)
        ) {
            Text("Gửi", fontWeight = FontWeight.Bold, color = ColorOnSurfaceDark)
        }

        if (showBottomSheet) {
            WalletPickerBottomSheet(
                currentWalletId = currentWalletId.toString(),
                wallets = wallets,
                onSelect = {
                    onChangeCurrentWalletId(it.id)
                    showBottomSheet = false
                },
                onDismiss = { showBottomSheet = false })
        }
    }
}
