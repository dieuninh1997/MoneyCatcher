package com.ninhttd.moneycatcher.ui.screen.add.component

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninhttd.moneycatcher.navigation.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun AddNewScreen(modifier: Modifier = Modifier, onNavigateNote: (String) -> Unit) {
    val tabs = listOf(
        TabItem("Chi tiêu", Icons.Default.ArrowUpward),
        TabItem("Thu nhập", Icons.Default.ArrowDownward)
    )
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
            }, tabs=tabs,
            pagerState = pagerState
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
            Icon(Icons.Default.Message, contentDescription = null, tint = Color.White)
        }
    }
}

data class TabItem(val title: String, val icon: ImageVector)

@Composable
fun MainContent(
    selectedIndex: Int,
    tabs: List<TabItem>,
    onTabSelected: (Int) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    Column {
        //tabs
        TabRow(
            selectedTabIndex = selectedIndex,
            contentColor = Color.White,
            backgroundColor = Color.DarkGray
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = {
                        onTabSelected(index)
                    },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (selectedIndex == index) Color.Red else Color.White
                            )
                            Text(
                                text = tab.title,
                                fontSize = 12.sp,
                                color = if (selectedIndex == index) Color.Red else Color.White
                            )
                        }
                    }
                )
            }
        }

        HorizontalPager(
            pageSize = PageSize.Fill,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> ExpenseContent()
                1 -> IncomeContent()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncomeContent(modifier: Modifier = Modifier) {
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseContent(modifier: Modifier = Modifier) {
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

@Preview(showBackground = true)
@Composable
private fun AddNewScreenPreview() {
    AddNewScreen(onNavigateNote = {})
}