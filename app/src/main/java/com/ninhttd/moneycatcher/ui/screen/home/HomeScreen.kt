package com.ninhttd.moneycatcher.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ninhttd.moneycatcher.common.TimeFilter
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.domain.model.CategorySummary
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.add.component.formatMoney
import com.ninhttd.moneycatcher.ui.screen.home.component.BalanceHeader
import com.ninhttd.moneycatcher.ui.screen.home.component.Chart
import com.ninhttd.moneycatcher.ui.screen.home.component.TopSpendingSection
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary

@Composable
fun HomeScreen(
    onNavigateDetails: (String) -> Unit,
    onNavigateSettings: () -> Unit,
    viewModel: HomViewModel = hiltViewModel()
) {
    val mainViewModal: MainSharedViewModel = hiltActivityViewModel()
    val walletList by mainViewModal.walletList.collectAsState()
    val currentWalletId by mainViewModal.currentWalletId.collectAsState(initial = null)
    val currentWallet by mainViewModal.currentWallet.collectAsState(initial = null)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val topCategories by viewModel.topSpendingCategories.collectAsState()
    val timeFilter by viewModel.timeFilter.collectAsState()


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
        topCategories = topCategories,
        timeFilter = timeFilter,
        currentWallet = currentWallet,
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
    topCategories: List<CategorySummary>,
    timeFilter: TimeFilter,
    currentWallet: Wallet?,
    onNavigateDetails: (String) -> Unit,
    onRefresh: () -> Unit,
    onDismissError: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomViewModel = hiltViewModel()
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
            Column {
                BalanceHeader(
                    "${formatMoney(currentWallet?.balance ?: 0)}₫",
                    isBalanceVisible,
                    onToggleVisibility = { isBalanceVisible = !isBalanceVisible },
                    onSearchClick = {
                        onNavigateDetails(Screen.Search.route)
                    })

                Chart(onViewAllClick = {})

                TopSpendingSection(
                    topCategories,
                    timeFilter,
                    onFilterClick = { it ->
                        viewModel.setTimeFilter(it)
                    },
                    onViewAllClick = {}
                )
            }
        }
    }
}