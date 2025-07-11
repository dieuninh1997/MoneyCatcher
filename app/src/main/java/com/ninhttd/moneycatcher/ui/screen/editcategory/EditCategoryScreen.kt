package com.ninhttd.moneycatcher.ui.screen.editcategory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.ui.screen.add.component.ExpenseIncomeTab
import com.ninhttd.moneycatcher.ui.screen.add.component.WalletPickerRow
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import kotlinx.coroutines.launch

@Composable
fun EditCategoryScreen(
    onNavigateUp: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditCategoryViewModel = hiltViewModel()
) {
    val categoriesList = viewModel.categoriesList.collectAsState(initial = listOf()).value
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 2 }
    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        TopBar(title = "Danh mục", onBackPress = { onNavigateUp() })

        Spacer(Modifier.height(16.dp))
        ExpenseIncomeTab(
            selectedIndex = pagerState.currentPage,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
        )

        Spacer(Modifier.height(16.dp))
        WalletPickerRow(
            Wallet(
                name = "Vi 1",
                balance = 27_000_000L,
                initBalance = 27_000_000L
            ),
            {},
            onNavigateDetails
        )

        Spacer(Modifier.height(12.dp))
        AddCategoryButton()

        Spacer(Modifier.height(12.dp))
        CategoryList(categoriesList)
    }
}

@Composable
fun CategoryList(categories: List<Category>?, modifier: Modifier = Modifier) {
    LazyColumn {
        categories?.let {
            items(categories.size) { index ->
                val category = categories[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color(0xFF1C1C1E), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = category.icon, fontSize = 24.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(category.name, color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun AddCategoryButton(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1E), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null, tint = Color.Blue)
        Spacer(Modifier.width(12.dp))
        Text("Thêm danh mục", color = Color.White)
    }
}

@Composable
fun TopBar(
    title: String,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = ColorColdPurplePink,
            modifier = Modifier.clickable {
                onBackPress()
            }
        )
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = ColorColdPurplePink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Preview
@Composable
private fun EditCategoryScreenPreview() {
    EditCategoryScreen({}, { route -> })
}