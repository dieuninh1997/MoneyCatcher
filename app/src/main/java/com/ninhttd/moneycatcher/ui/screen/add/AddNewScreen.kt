package com.ninhttd.moneycatcher.ui.screen.add.component

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.add.AddNewiewModel
import kotlinx.coroutines.launch

@Composable
fun AddNewScreen(
    onNavigateNote: (String) -> Unit,
    onNavigateEditCategory: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddNewiewModel = hiltViewModel()
) {

    val categoriesList = viewModel.categoriesList.collectAsState(initial = listOf()).value
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()

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
            pagerState = pagerState,
            onNavigateEditCategory = onNavigateEditCategory

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
    modifier: Modifier = Modifier,
    onNavigateEditCategory: () -> Unit
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
                0 -> ExpenseContent(categoriesList, onNavigateEditCategory = onNavigateEditCategory)
                1 -> IncomeContent(categoriesList, onNavigateEditCategory = onNavigateEditCategory)
            }
        }
    }
}


@Composable
fun IncomeContent(
    categoriesList: List<Category>?,
    modifier: Modifier = Modifier,
    onNavigateEditCategory: () -> Unit
) {
    TransactionContent(
        Wallet("Vi 1", false, 27_000_000L),
        "02-07-2025",
        categoriesList,
        onSubmit = { note, amount, category ->
            //TODO
        },
        onNavigateEditCategory = onNavigateEditCategory
    )
}

@Composable
fun ExpenseContent(
    categoriesList: List<Category>?,
    modifier: Modifier = Modifier,
    onNavigateEditCategory: () -> Unit
) {
    TransactionContent(
        Wallet("Vi 1", false, 27_000_000L),
        "02-07-2025",
        categoriesList,
        onSubmit = { note, amount, category ->
            //TODO
        },
        onNavigateEditCategory = onNavigateEditCategory
    )
}

@Preview(showBackground = true)
@Composable
private fun AddNewScreenPreview() {
    AddNewScreen(onNavigateNote = {}, onNavigateEditCategory = {})
}