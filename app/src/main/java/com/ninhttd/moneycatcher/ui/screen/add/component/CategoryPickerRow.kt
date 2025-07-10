package com.ninhttd.moneycatcher.ui.screen.add.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorOnSurfaceDark
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimaryContainer
import com.ninhttd.moneycatcher.ui.theme.ColorZeroWhite
import timber.log.Timber

@Composable
fun CategoryPickerRow(
    categories: List<Category>?,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onNavigateEditCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowGrid by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(
            onClick = {
                isShowGrid = !isShowGrid
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(ColorColdPurplePink)
        ) {
            Text(
                "Danh mục: ${selectedCategory?.name ?: ""}" ?: "Chọn danh mục",
                color = ColorOnSurfaceDark
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isShowGrid) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Color.White
            )
        }
        Timber.tag("CategoryGrid").d("Size: ${categories?.size}")

        if (isShowGrid) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(max = 350.dp)
                    .background(Color.White),
                contentPadding = PaddingValues(4.dp)
            ) {
                item() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 80.dp)
                            .padding(6.dp)
                            .clickable {
                                onNavigateEditCategory.invoke()
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = ColorColdPurplePink
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = ColorZeroWhite,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                categories?.let {
                    items(categories.size) { index ->
                        val category = categories[index]
                        val isSelected = category.name == selectedCategory?.name
                        val borderColor =
                            if (isSelected) ColorColdPurplePink else ColorPinkPrimaryContainer
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 80.dp)
                                .padding(6.dp)
                                .border(2.dp, borderColor, RoundedCornerShape(12.dp))
                                .clickable {
                                    onCategorySelected(category)
                                    isShowGrid = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorPinkPrimaryContainer)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(20.dp)
                                            .background(
                                                color = ColorColdPurplePink,
                                                shape = RoundedCornerShape(4.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = category.icon,
                                        fontSize = 24.sp,
                                        color = if (isSelected) ColorColdPurplePink else ColorZeroWhite,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = category.name,
                                        fontSize = 12.sp,
                                        color = if (isSelected) ColorColdPurplePink else ColorZeroWhite,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}