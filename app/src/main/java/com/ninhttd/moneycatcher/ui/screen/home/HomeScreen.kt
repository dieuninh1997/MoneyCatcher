package com.ninhttd.moneycatcher.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ninhttd.moneycatcher.common.TimeFilter
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.domain.model.CategorySummary
import com.ninhttd.moneycatcher.domain.model.TransactionUiModel
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.add.component.formatMoney
import com.ninhttd.moneycatcher.ui.screen.home.component.BalanceHeader
import com.ninhttd.moneycatcher.ui.screen.home.component.RecentTransactionItem
import com.ninhttd.moneycatcher.ui.screen.home.component.TopSpendingSection
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    onNavigateToCalendar: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    viewModel: HomeViewModel
) {
    val mainViewModal: MainSharedViewModel = hiltActivityViewModel()
    val walletList by mainViewModal.walletList.collectAsState()
    val currentWalletId by mainViewModal.currentWalletId.collectAsState(initial = null)
    val currentWallet by mainViewModal.currentWallet.collectAsState(initial = null)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val topCategoriesState by viewModel.topSpendingCategories.collectAsState()
    val timeFilter by viewModel.timeFilter.collectAsState()


    val groupedTransactions by viewModel.groupTransactionsUiFlow.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModal.getWalletList()
    }

    LaunchedEffect(walletList) {
        if (mainViewModal.currentWalletId.value == null && walletList.isNotEmpty()) {
            mainViewModal.setCurrentWalletId(walletList.first().id)
        }
    }


    HomeScreen(
        uiState = uiState,
        topCategoriesState = topCategoriesState,
        groupTransactions = groupedTransactions,
        timeFilter = timeFilter,
        currentWallet = currentWallet,
        onNavigateToCalendar = onNavigateToCalendar,
        onNavigateToReport = onNavigateToReport,
        onNavigateDetails = onNavigateDetails,
        onRefresh = {
        },
        onDismissError = { errorMessageId ->
        }
    )


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    topCategoriesState: LoadResult<List<CategorySummary>>,
    groupTransactions: List<Pair<LocalDate, List<TransactionUiModel>>>,
    timeFilter: TimeFilter,
    currentWallet: Wallet?,
    onNavigateToCalendar: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    onRefresh: () -> Unit,
    onDismissError: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    )
    var isBalanceVisible by remember { mutableStateOf(true) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .background(ColorPinkPrimary)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(scaffoldPadding)

        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                item {
                    BalanceHeader(
                        "${formatMoney(currentWallet?.balance ?: 0)}₫",
                        isBalanceVisible,
                        onToggleVisibility = { isBalanceVisible = !isBalanceVisible },
                        onSearchClick = {
                            onNavigateDetails(Screen.Search.route)
                        })
                }
//                item {
//                    Chart(onViewAllClick = {})
//                }

                item {
                    when (topCategoriesState) {
                        is LoadResult.Error<*> -> {

                        }

                        is LoadResult.Loading<*> -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        is LoadResult.Success<*> -> {
                            val data =
                                (topCategoriesState as LoadResult.Success<*>).data as List<CategorySummary>
                            TopSpendingSection(
                                data,
                                timeFilter,
                                onFilterClick = { it ->
                                    viewModel.setTimeFilter(it)
                                },
                                onViewAllClick = onNavigateToReport
                            )
                        }
                    }

                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Giao dịch gần đây",
                            fontWeight = FontWeight.Bold,
                            color = ColorColdPurplePink
                        )
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = onNavigateToCalendar) {
                            Text("Xem tất cả", color = ColorColdPurplePink)
                        }
                    }
                }
                //-------------------------------
                groupTransactions.take(3).forEach { (date, txList) ->
                    item {
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("dd MMM yyy")),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            ),
                            color = ColorColdPurplePink
                        )
                    }

                    items(txList.size) { index ->
                        val data = txList[index]
                        RecentTransactionItem(data)
                    }
                }
            }
        }
    }
}
