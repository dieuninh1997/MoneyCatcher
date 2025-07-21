package com.ninhttd.moneycatcher.ui.screen.calendar

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ninhttd.moneycatcher.R
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.domain.model.MonthlySummary
import com.ninhttd.moneycatcher.domain.model.TransactionUiModel
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.ui.screen.add.component.WalletPickerBottomSheet
import com.ninhttd.moneycatcher.ui.screen.add.component.WalletPickerRow
import com.ninhttd.moneycatcher.ui.screen.add.component.formatMoney
import com.ninhttd.moneycatcher.ui.screen.calendar.component.BarChartView
import com.ninhttd.moneycatcher.ui.screen.calendar.component.formatCompactToDouble
import com.ninhttd.moneycatcher.ui.screen.home.HomeUiState
import com.ninhttd.moneycatcher.ui.screen.home.HomeViewModel
import com.ninhttd.moneycatcher.ui.screen.home.component.RecentTransactionItem
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import ir.ehsannarmani.compose_charts.models.Bars
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CalendarScreen(
    onNavigateDetails: (String) -> Unit,
    onNavigateSettings: () -> Unit,
    viewModel: CalendariewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val homeViewModel: HomeViewModel = hiltActivityViewModel()
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val transactionsUiFlow by homeViewModel.transactionsUiFlow.collectAsState()
    val groupedTransactions by homeViewModel.groupTransactionsUiFlow.collectAsState()
    val monthlySummaries by homeViewModel.monthlySummaries.collectAsState()

    val mainViewModal: MainSharedViewModel = hiltActivityViewModel()
    val walletList by mainViewModal.walletList.collectAsState()
    val currentWalletId by mainViewModal.currentWalletId.collectAsState(initial = null)
    val currentWallet by mainViewModal.currentWallet.collectAsState(initial = null)

    CalendarScreen(
        uiState = uiState,
        homeUiState = homeUiState,
        wallets = walletList,
        currentWallet = currentWallet,
        groupTransactions = groupedTransactions,
        monthlySummaries,
        onNavigateSettings = onNavigateSettings,
        onNavigateDetails = onNavigateDetails,
        onChangeCurrentWalletId = { id ->
            mainViewModal.setCurrentWalletId(id)
            homeViewModel.fetchTransactionsWithCategory(id)
        },
        onRefresh = {
        },
        onDismissError = { errorMessageId ->
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    homeUiState: HomeUiState,
    wallets: List<Wallet>,
    currentWallet: Wallet?,
    groupTransactions: List<Pair<LocalDate, List<TransactionUiModel>>>,
    monthlySummaries: List<MonthlySummary>,
    onNavigateSettings: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    onChangeCurrentWalletId: (String) -> Unit,
    onRefresh: () -> Unit,
    onDismissError: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    )
    val totalIncome = monthlySummaries.sumOf { it.income }
    val totalExpense = monthlySummaries.sumOf { it.expense }
    val data = monthlySummaries.map { item ->
        Bars(
            label = "${context.getString(R.string.month)} ${item.month.month.value}",
            values = listOf(
                Bars.Data(
                    value = formatCompactToDouble(item.income.toDouble()),
                    color = Brush.verticalGradient(
                        listOf(Color.Green, Color.Green.copy(alpha = 0.5f))
                    )
                ),
                Bars.Data(
                    value = formatCompactToDouble(-1.0 * item.expense.toDouble()),
                    color = Brush.verticalGradient(
                        listOf(Color.Red, Color.Red.copy(alpha = 0.5f))
                    )
                )
            )
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .padding(scaffoldPadding)
        ) {
            if (homeUiState.isLoading == true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .zIndex(1f),
                    contentAlignment = Alignment.Center
                )
                {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                HeaderWithSearch(context)
                WalletPickerRow(
                    currentWallet, onClick = {
                        showBottomSheet = true
                    }, onNavigateDetails = onNavigateDetails
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                ) {
                    item {
                        if (data.isNotEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(Color.Green, shape = CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Thu nhập",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(Color.Red, shape = CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Chi tiêu",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            BarChartView(data)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        HeaderSection(
                            expense = formatMoney(totalExpense),
                            income = formatMoney(totalIncome),
                            difference = "${formatMoney(totalIncome - totalExpense)}đ",
                            initBalance = "${formatMoney(currentWallet?.initBalance ?: 0)}đ",
                            currentBalance = "${formatMoney(currentWallet?.balance ?: 0)}đ"
                        )
                    }
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

            if (showBottomSheet) {
                WalletPickerBottomSheet(
                    currentWalletId = currentWallet?.id.toString(),
                    wallets = wallets,
                    onSelect = {
                        onChangeCurrentWalletId(it.id)
                        showBottomSheet = false
                    },
                    onDismiss = { showBottomSheet = false })
            }
        }
    }
}

@Composable
fun HeaderItem(title: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 14.sp, color = Color.Gray)
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

@Composable
fun HeaderSection(
    expense: String,
    income: String,
    difference: String,
    initBalance: String,
    currentBalance: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2E), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        // Row 1: Chi tiêu - Thu nhập - Chênh lệch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min) // Bắt buộc để Divider dọc hoạt động đúng
        ) {
            HeaderItem("Chi tiêu", expense, Color.Red, Modifier.weight(1f))
            VerticalDivider()
            HeaderItem("Thu nhập", income, Color.Green, Modifier.weight(1f))
            VerticalDivider()
            HeaderItem("Chênh lệch", difference, Color.Red, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = Color.Gray, thickness = 1.dp)

        // Row 2: Số dư đầu kỳ - Số dư cuối kỳ
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(top = 8.dp)
        ) {
            HeaderItem("Số dư đầu kỳ", initBalance, Color.White, Modifier.weight(1f))
            VerticalDivider()
            HeaderItem("Số dư cuối kỳ", currentBalance, Color.White, Modifier.weight(1f))
        }
    }
}

@Composable
fun HeaderWithSearch(context: Context, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = context.getString(R.string.calendar),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        IconButton(onClick = { /* TODO: open search */ }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Tìm kiếm",
                tint = Color.White
            )
        }
    }
}