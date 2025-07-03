package com.ninhttd.moneycatcher.ui.screen.add.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import timber.log.Timber

@Composable
fun CategoryPickerRow(
    categories: List<Category>?,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onNavigateEditCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowGrid by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(
            onClick = {
                isShowGrid = !isShowGrid
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.DarkGray,
            )
        ) {
            Text(
                "Danh mục: ${selectedCategory?.name ?: ""}" ?: "Chọn danh mục",
                color = Color.White
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
                            containerColor = Color.LightGray
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
                                tint = Color.DarkGray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                categories?.let {
                    items(categories.size) { index ->
                        val category = categories[index]
                        val isSelected = category.name == selectedCategory?.name
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 80.dp)
                                .padding(6.dp)
                                .clickable {
                                    onCategorySelected(category)
                                    isShowGrid = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color.Blue else Color.LightGray
                            )
                        ) {
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
                                    color = if (isSelected) Color.White else Color.DarkGray,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = category.name,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White else Color.DarkGray,
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