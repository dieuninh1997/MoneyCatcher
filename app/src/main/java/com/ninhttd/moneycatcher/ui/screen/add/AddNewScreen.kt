package com.ninhttd.moneycatcher.ui.screen.add.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.Transaction
import com.ninhttd.moneycatcher.domain.model.TransactionType
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.add.AddNewiewModel
import kotlinx.coroutines.launch

@Composable
fun AddNewScreen(
    onNavigateNote: (String) -> Unit,
    onNavigateEditCategory: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddNewiewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val categoriesList = viewModel.categoriesList.collectAsState(initial = listOf()).value

    val walletList by viewModel.walletList.collectAsState()
    val currentWallet by viewModel.currentWallet.collectAsState(initial = null)
    val currentWalletId by viewModel.currentWalletId.collectAsState(initial = null)
    val currentUser by viewModel.currentUser.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        viewModel.getWalletList()
    }

    LaunchedEffect(walletList) {
        if (walletList.isNotEmpty()) {
            viewModel.setCurrentWalletId(walletList.first().id)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {

        MainContent(
            selectedIndex = pagerState.currentPage,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            categoriesList = categoriesList,
            currentWalletId = currentWalletId,
            currentWallet = currentWallet,
            wallets = walletList,
            pagerState = pagerState,
            onNavigateEditCategory = onNavigateEditCategory,
            onNavigateDetails = onNavigateDetails,
            onChangeCurrentWalletId = { id ->
                viewModel.setCurrentWalletId(id)
            },
            onSubmit = { money, note, transactionType, date, category ->
                viewModel.createTransaction(
                    Transaction(
                        userId = currentUser?.id.toString(),
                        walletId = currentWalletId.toString(),
                        categoryId = category?.id.toString(),
                        transactionType = transactionType,
                        amount = money,
                        note = note,
                        transactionDate = date,
                    )
                ) { success ->
                    if (success) {
                        Toast.makeText(context, "Add success!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Error!!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )

        FABGroup(onNavigateNote = onNavigateNote, modifier = Modifier.align(Alignment.BottomEnd))
    }
}


@Composable
fun FABGroup(modifier: Modifier = Modifier, onNavigateNote: (String) -> Unit) {
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.End
    ) {
        SmallFloatingActionButton(
            onClick = {
                onNavigateNote(Screen.VoiceNote.route)
            },
            containerColor = Color.Blue
        ) {
            Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White)
        }

        FloatingActionButton(
            onClick = {
                onNavigateNote(Screen.ManualNote.route)
            },
            contentColor = Color.Blue
        ) {
            Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null, tint = Color.White)
        }
    }
}


@Composable
fun MainContent(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    pagerState: PagerState,
    categoriesList: List<Category>?,
    currentWalletId: String?,
    currentWallet: Wallet?,
    onNavigateEditCategory: () -> Unit,
    wallets: List<Wallet>,
    onNavigateDetails: (String) -> Unit,
    onChangeCurrentWalletId: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSubmit: (String, String, TransactionType, java.time.LocalDate, Category?) -> Unit
) {
    Column {
        //tabs
        ExpenseIncomeTab(
            selectedIndex = selectedIndex,
            onTabSelected = onTabSelected
        )
        HorizontalPager(
            pageSize = PageSize.Fill,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> ExpenseContent(
                    onChangeCurrentWalletId = onChangeCurrentWalletId,
                    categoriesList = categoriesList,
                    currentWalletId = currentWalletId,
                    currentWallet = currentWallet,
                    wallets = wallets,
                    onNavigateEditCategory = onNavigateEditCategory,
                    onNavigateDetails = onNavigateDetails,
                    onSubmit = onSubmit
                )

                1 -> IncomeContent(
                    onChangeCurrentWalletId = onChangeCurrentWalletId,
                    categoriesList = categoriesList,
                    currentWalletId = currentWalletId,
                    currentWallet = currentWallet,
                    wallets = wallets,
                    onNavigateEditCategory = onNavigateEditCategory,
                    onNavigateDetails = onNavigateDetails,
                    onSubmit = onSubmit
                )
            }
        }
    }
}


@Composable
fun IncomeContent(
    categoriesList: List<Category>?,
    onChangeCurrentWalletId: (String) -> Unit,
    currentWalletId: String?,
    currentWallet: Wallet?,
    wallets: List<Wallet>,
    onNavigateEditCategory: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSubmit: (String, String, TransactionType, java.time.LocalDate, Category?) -> Unit
) {
    TransactionContent(
        onChangeCurrentWalletId = onChangeCurrentWalletId,
        currentWalletId = currentWalletId,
        currentWallet = currentWallet,
        wallets = wallets,
        date = "02-07-2025",
        categoriesList = categoriesList,
        onSubmit = onSubmit,
        onNavigateEditCategory = onNavigateEditCategory,
        onNavigateDetails = onNavigateDetails,
        transactionType = TransactionType.INCOME
    )
}

@Composable
fun ExpenseContent(
    categoriesList: List<Category>?,
    onChangeCurrentWalletId: (String) -> Unit,
    currentWalletId: String?,
    currentWallet: Wallet?,
    wallets: List<Wallet>,
    onNavigateEditCategory: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSubmit: (String, String, TransactionType, java.time.LocalDate, Category?) -> Unit
) {
    TransactionContent(
        onChangeCurrentWalletId = onChangeCurrentWalletId,
        currentWalletId = currentWalletId,
        wallets = wallets,
        currentWallet = currentWallet,
        date = "02-07-2025",
        categoriesList = categoriesList,
        onSubmit = onSubmit,
        onNavigateEditCategory = onNavigateEditCategory,
        onNavigateDetails = onNavigateDetails,
        transactionType = TransactionType.EXPENSE
    )
}

@Preview(showBackground = true)
@Composable
private fun AddNewScreenPreview() {
    AddNewScreen(onNavigateNote = {}, onNavigateEditCategory = {}, onNavigateDetails = { route ->

    })
}