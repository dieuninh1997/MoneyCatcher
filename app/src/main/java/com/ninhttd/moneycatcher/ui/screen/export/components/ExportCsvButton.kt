package com.ninhttd.moneycatcher.ui.screen.export.components

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.ninhttd.moneycatcher.domain.model.SpendingRecord
import com.ninhttd.moneycatcher.ui.screen.export.ExportButton
import com.ninhttd.moneycatcher.ui.screen.export.exportAndSaveToDownloads

@Composable
fun ExportCsvButton(spendingRecords: List<SpendingRecord>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as Activity

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            exportAndSaveToDownloads(context, spendingRecords)
            Toast.makeText(context, "Đã lưu CSV vào Downloads", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Không có quyền ghi tệp", Toast.LENGTH_SHORT).show()
        }
    }

    ExportButton("Xuất CSV", onClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ không cần xin quyền
            exportAndSaveToDownloads(context, spendingRecords)
            Toast.makeText(context, "Đã lưu CSV vào Downloads", Toast.LENGTH_SHORT).show()
        } else {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            val granted = ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            if (granted) {
                exportAndSaveToDownloads(context, spendingRecords)
                Toast.makeText(context, "Đã lưu CSV vào Downloads", Toast.LENGTH_SHORT).show()
            } else {
                permissionLauncher.launch(permission)
            }
        }
    })
}
