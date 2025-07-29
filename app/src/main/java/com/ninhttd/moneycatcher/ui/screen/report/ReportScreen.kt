package com.ninhttd.moneycatcher.ui.screen.add

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ninhttd.moneycatcher.domain.model.CategorySummary
import com.ninhttd.moneycatcher.domain.model.TransactionUiModel
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.add.component.WalletPickerBottomSheet
import com.ninhttd.moneycatcher.ui.screen.add.component.WalletPickerRow
import com.ninhttd.moneycatcher.ui.screen.home.HomeUiState
import com.ninhttd.moneycatcher.ui.screen.home.HomeViewModel
import com.ninhttd.moneycatcher.ui.screen.home.LoadResult
import com.ninhttd.moneycatcher.ui.screen.home.component.TopSpendingItem
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.screen.report.components.EmptyReport
import com.ninhttd.moneycatcher.ui.screen.report.components.LegendItem
import com.ninhttd.moneycatcher.ui.screen.report.components.PeriodFilterState
import com.ninhttd.moneycatcher.ui.screen.report.components.PeriodPickerSheet
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun ReportScreen(
    onNavigateDetails: (String) -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateToAdd: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val homeViewModel: HomeViewModel = hiltActivityViewModel()
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    val groupedTransactions by homeViewModel.groupTransactionsUiFlow.collectAsState()
    val topCategoriesState by homeViewModel.topSpendingCategories.collectAsState()

    val mainViewModal: MainSharedViewModel = hiltActivityViewModel()
    val walletList by mainViewModal.walletList.collectAsState()
    val currentWallet by mainViewModal.currentWallet.collectAsState(initial = null)

    ReportScreen(
        uiState = uiState,
        homeUiState = homeUiState,
        topCategoriesState = topCategoriesState,
        groupTransactions = groupedTransactions,
        wallets = walletList,
        currentWallet = currentWallet,
        onNavigateToAdd = onNavigateToAdd,
        onUpdateTimeFilterFromPeriod = {
            homeViewModel.updatePeriodFilter(it)
        },
        onChangeCurrentWalletId = { id ->
            mainViewModal.setCurrentWalletId(id)
            homeViewModel.fetchTransactionsWithCategory(id)
        },
        onNavigateSettings = onNavigateSettings,
        onNavigateDetails = onNavigateDetails,
        onRefresh = {
        },
        onDismissError = { errorMessageId ->
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ReportScreen(
    uiState: ReportUiState,
    homeUiState: HomeUiState,
    topCategoriesState: LoadResult<List<CategorySummary>>,
    groupTransactions: List<Pair<LocalDate, List<TransactionUiModel>>>,
    wallets: List<Wallet>,
    currentWallet: Wallet?,
    onNavigateToAdd: () -> Unit,
    onUpdateTimeFilterFromPeriod: (PeriodFilterState) -> Unit,
    onChangeCurrentWalletId: (String) -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    onRefresh: () -> Unit,
    onDismissError: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    )

    var periodState by remember { mutableStateOf(PeriodFilterState.default()) }


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

            //content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                HeaderWithSearchAndExport(context, onNavigateDetails)
                WalletPickerRow(
                    currentWallet,
                    onClick = {
                        showBottomSheet = true
                    },
                    onNavigateDetails = onNavigateDetails
                )


                PeriodPicker(
                    periodState = periodState,
                    onPeriodSelected = {
                        periodState = it
                        onUpdateTimeFilterFromPeriod(it)

                    }
                )

                when (topCategoriesState) {
                    is LoadResult.Error<*> -> {

                    }

                    is LoadResult.Loading<*> -> {

                    }

                    is LoadResult.Success<*> -> {
                        val topCategories =
                            (topCategoriesState as LoadResult.Success<*>).data as List<CategorySummary>
                        if (topCategories.isNotEmpty()) {
                            SpendingPieChart(topCategories)
                            topCategories.forEach {
                                TopSpendingItem(
                                    icon = it.category.icon,
                                    title = it.category.name,
                                    amount = it.amount,
                                    percentage = it.percent
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        } else {
                            Spacer(Modifier.height(36.dp))
                            EmptyReport(onAddTransactionClick = onNavigateToAdd)
                        }
                    }
                }
            }

            //bottom sheet
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
fun SpendingPieChart(topCategories: List<CategorySummary>, modifier: Modifier = Modifier) {
    var selectedLabel by remember { mutableStateOf<String?>(null) }
    val colors = listOf(
        Color(0xFFF48FB1),
        Color(0xFFCDDC39),
        Color(0xFF17C683),
        Color(0xFFFF0056),
        Color(0xFF1AD0D5),
    )

    var data by remember { mutableStateOf(emptyList<Pie>()) }

    LaunchedEffect(topCategories) {
        data = topCategories.mapIndexed { index, item ->
            Pie(
                data = item.amount.toDouble(),
                color = colors[index],
                label = item.category.name
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Biểu đồ hình Doughnut
        PieChart(
            modifier = Modifier.size(200.dp),
            onPieClick = {
                println("${it.label} Clicked")
                selectedLabel = "${it.label}: %,d".format(it.data.toLong())
                val pieIndex = data.indexOf(it)
                data =
                    data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
            },
            data = data,
            style = Pie.Style.Fill,
        )

        Spacer(modifier = Modifier.height(36.dp))
        selectedLabel?.let { label ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Chú thích

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(topCategories.size) { index ->
                val item = topCategories[index]
                LegendItem(color = colors[index], text = item.category.name)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodPicker(
    periodState: PeriodFilterState,
    onPeriodSelected: (PeriodFilterState) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
        ) {
            PeriodPickerSheet(
                currentState = periodState,
                onDismiss = { showSheet = false },
                onConfirm = {
                    onPeriodSelected(it)
                    showSheet = false
                }
            )
        }
    }

    // Nút bấm mở picker
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val label = "${periodState.value} (${periodState.startDate.format(formatter)} - ${
        periodState.endDate.format(formatter)
    })"

    OutlinedButton(
        onClick = { showSheet = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label)
    }
}


@Composable
fun HeaderWithSearchAndExport(
    context: Context,
    onNavigateDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = context.getString(R.string.report_screen),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Row {
            IconButton(onClick = { /* TODO: open search */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = Color.White
                )
            }

            IconButton(onClick = {
                onNavigateDetails(Screen.Export.route)
            }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Export/Share",
                    tint = Color.White
                )
            }
        }

    }
}