package com.ninhttd.moneycatcher.ui.screen.add

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ninhttd.moneycatcher.ui.screen.add.component.AddNewTab
import com.ninhttd.moneycatcher.ui.screen.add.component.AddNewTabChip


@Composable
fun AddNewScreen(
    onNavigateDetails: (String) -> Unit,
    onNavigateSettings: () -> Unit,
    viewModel: AddNewiewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddNewScreen(
        uiState = uiState,
        onNavigateSettings = onNavigateSettings,
        onRefresh = {
        },
        onDismissError = { errorMessageId ->
        },
        onUpdateAddNewTab = { addTab ->
            viewModel.updateAddNewTab(addTab)

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AddNewScreen(
    uiState: AddNewUiState,
    onUpdateAddNewTab: (AddNewTab) -> Unit,
    onNavigateSettings: () -> Unit,
    onRefresh: () -> Unit,
    onDismissError: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    )
    val listState = rememberLazyListState()
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

           AddNewContent(
               lazyListState = listState,
               addNewTab = uiState.addNewTab,
               onUpdateAddNewTab = onUpdateAddNewTab,
           )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewContent(
    addNewTab: AddNewTab,
    onUpdateAddNewTab: (AddNewTab) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                AddNewTabChip(
                    addNewTab = AddNewTab.Outcome,
                    selected = addNewTab == AddNewTab.Outcome,
                    onClick = {onUpdateAddNewTab(AddNewTab.Outcome) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))

                AddNewTabChip(
                    addNewTab = AddNewTab.Income,
                    selected = addNewTab == AddNewTab.Income,
                    onClick = {onUpdateAddNewTab(AddNewTab.Income) },
                    modifier = Modifier.weight(1f)
                )
            }
        }



    }
}
