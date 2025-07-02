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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate


data class Wallet(val name: String, val isDefault: Boolean, val money: Long)
data class Category(val name: String, val icon: ImageVector)

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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionContent(
    wallet: Wallet,
    date: String,
    categories: List<Category>,
    onSubmit: (note: String, amount: String, category: Category?) -> Unit,
    modifier: Modifier = Modifier
) {
    var note by remember { mutableStateOf("") }
    var money by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val incomeCategories = listOf(
        Category("Lương", Icons.Default.AttachMoney),
        Category("Thưởng", Icons.Default.Star),
        Category("Đầu tư", Icons.Default.TrendingUp),
        Category("Bán hàng", Icons.Default.ShoppingCart),
        Category("Khác", Icons.Default.MoreHoriz)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 72.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
                .verticalScroll(rememberScrollState()),
        ) {

            //vi
            WalletPickerRow(wallet, {})
            Spacer(modifier = Modifier.height(1.dp))
            DatePickerRow(selectedDate, onDateSelected = {
                selectedDate = it
            })
            Spacer(modifier = Modifier.height(5.dp))
            NoteInput(
                note = note,
                onNoteChange = {
                    note = it
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
            MoneyInput(money, onAmountChange = {
                money = it
            })
            Spacer(modifier = Modifier.height(5.dp))
            CategoryPickerRow(
                incomeCategories,
                selectedCategory = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                }
            )
        }

        Button(
            onClick = { /* handle submit */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Gửi", fontWeight = FontWeight.Bold)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun TransactionContentPreview() {
    TransactionContent(
        Wallet("Vi 1", false, 27_000_000L),
        "02-07-2025",
        listOf(
            Category("An uong", Icons.Default.Category)
        ),
        onSubmit = { note, amount, category ->
            //TODO
        },
    )
}