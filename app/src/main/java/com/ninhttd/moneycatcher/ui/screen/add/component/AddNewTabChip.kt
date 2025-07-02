package com.ninhttd.moneycatcher.ui.screen.add.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun AddNewTabChip(
    addNewTab: AddNewTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        leadingIcon = {
            val imageVector = when (addNewTab) {
                AddNewTab.Income -> Icons.Rounded.KeyboardArrowDown
                AddNewTab.Outcome -> Icons.Rounded.KeyboardArrowDown
            }

            Icon(
                imageVector = imageVector,
                tint = if (selected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                contentDescription = null
            )
        },
        label = { Text(text = stringResource(addNewTab.nameId)) },
        onClick = onClick,
        selected = selected,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        border = null,

        )
}