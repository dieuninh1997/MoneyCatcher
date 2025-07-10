package com.ninhttd.moneycatcher.ui.screen.add.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimaryContainer

@Composable
fun NoteInput(note: String, onNoteChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = note,
        onValueChange = onNoteChange,
        placeholder = {
            Text("Ghi chú...", color = Color.LightGray)
        },
        label = {
            Text("Ghi chú", color = ColorPinkPrimaryContainer)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = ColorPinkPrimaryContainer
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
            textColor = ColorColdPurplePink,
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = ColorColdPurplePink
        ),
    )
}