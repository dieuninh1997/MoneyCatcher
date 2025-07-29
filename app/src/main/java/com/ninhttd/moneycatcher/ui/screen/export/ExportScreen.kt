package com.ninhttd.moneycatcher.ui.screen.export

import android.R.attr.textSize
import android.R.attr.typeface
import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.domain.model.SpendingRecord
import com.ninhttd.moneycatcher.ui.screen.export.components.ExportCsvButton
import com.ninhttd.moneycatcher.ui.screen.home.HomeViewModel
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary
import com.ninhttd.moneycatcher.ui.theme.ColorSurfaceDark
import java.io.FileOutputStream

@Composable
fun ExportScreen(
    navController: NavController,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val homeViewModel: HomeViewModel = hiltActivityViewModel()
    val groupedTransactions by homeViewModel.groupTransactionsUiFlow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Xuất dữ liệu") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = ColorColdPurplePink,
                contentColor = Color.White
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(ColorColdPurplePink)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                backgroundColor = ColorPinkPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Xuất dữ liệu", color = ColorSurfaceDark, fontWeight = FontWeight.Bold)
                    Text(
                        "Dữ liệu đã ghi trong quá khứ có thể được xuất ra dưới dạng tệp Excel.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Mã ký tự", color = ColorSurfaceDark)
                    Text("UTF-8", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                backgroundColor = ColorPinkPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    val spendingRecords: List<SpendingRecord> =
                        groupedTransactions.map { (date, transactions) ->
                            SpendingRecord(date = date, transactions = transactions)
                        }
                    ExportCsvButton(spendingRecords)
                    Divider(color = ColorColdPurplePink)
                    ExportButton("In", onClick = {
                        printPdf(context, spendingRecords)
                    })
                    Divider(color = ColorColdPurplePink)
                    ExportButton("Xuất PDF", onClick = {
                        exportToPdf(context, spendingRecords)
                    })
                }
            }
        }
    }
}

@Composable
fun ExportButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = ColorSurfaceDark, fontSize = 16.sp)
    }
}

fun exportAndSaveToDownloads(context: Context, data: List<SpendingRecord>): Uri? {
    val filename = "spending_records_${System.currentTimeMillis()}.csv"
    val mimeType = "text/csv"

    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, filename)
        put(MediaStore.Downloads.MIME_TYPE, mimeType)
        put(MediaStore.Downloads.IS_PENDING, 1)
    }

    val resolver = context.contentResolver
    val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    val fileUri = resolver.insert(collection, contentValues)

    fileUri?.let { uri ->
        resolver.openOutputStream(uri)?.use { outputStream ->
            val writer = outputStream.bufferedWriter()
            // Write header
            writer.write("Ngày, Ví giao dịch, Số tiền, Danh mục, Ghi chú\n")

            data.forEach { record ->
                val dateStr = record.date.toString()
                record.transactions.forEach { transaction ->
                    val row = listOf(
                        dateStr,
                        transaction.wallet.name,
                        transaction.amount.toString(),
                        transaction.category,
                        transaction.note
                    ).joinToString(",")
                    writer.write(row)
                    writer.newLine()
                }
            }
            writer.flush()
        }

        contentValues.clear()
        contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)
    }

    return fileUri
}

fun exportToPdf(context: Context, data: List<SpendingRecord>) {
    val pdfDocument = PdfDocument()
    val paint = Paint().apply {
        textSize = 12f
    }

    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    var x = 40f
    var y = 50f
    val lineSpacing = 25f

    // Tiêu đề
    paint.apply {
        textSize = 16f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    canvas.drawText("Báo cáo chi tiêu", x, y, paint)

    y += 40f
    paint.textSize = 12f
    paint.typeface = Typeface.DEFAULT

    // Header
    canvas.drawText("Ngày", x, y, paint)
    canvas.drawText("Tên ví", x + 100, y, paint)
    canvas.drawText("Số tiền", x + 250, y, paint)
    canvas.drawText("Danh mục", x + 350, y, paint)
    canvas.drawText("Ghi chú", x + 470, y, paint)

    y += lineSpacing

    // Nội dung
    data.forEach { record ->
        record.transactions.forEach { tx ->
            if (y > 800) { // Gần hết trang thì bỏ qua hoặc tạo trang mới
                return@forEach
            }
            canvas.drawText(record.date.toString(), x, y, paint)
            canvas.drawText(tx.wallet.name, x + 100, y, paint)
            canvas.drawText(tx.amount.toString(), x + 250, y, paint)
            canvas.drawText(tx.category.name, x + 350, y, paint)
            canvas.drawText(tx.note, x + 470, y, paint)
            y += lineSpacing
        }
    }

    pdfDocument.finishPage(page)

    // Ghi vào Downloads
    val fileName = "spending_${System.currentTimeMillis()}.pdf"
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }
    }

    pdfDocument.close()
    Toast.makeText(context, "Đã lưu PDF vào Downloads", Toast.LENGTH_SHORT).show()
}

fun printPdf(context: Context, spendingRecords: List<SpendingRecord>) {
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

    val jobName = "Spending_Report"

    printManager.print(
        jobName,
        object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: android.os.CancellationSignal?,
                layoutResultCallback: LayoutResultCallback?,
                extras: Bundle?
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    layoutResultCallback?.onLayoutCancelled()
                    return
                }

                val info = PrintDocumentInfo.Builder("spending_report.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build()
                layoutResultCallback?.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<PageRange>,
                destination: ParcelFileDescriptor,
                cancellationSignal: android.os.CancellationSignal?,
                writeResultCallback: WriteResultCallback?
            ) {
                val pdfDocument = PdfDocument()
                val paint = Paint().apply {
//                    color = Color.BLACK
                    textSize = 12f
                }

                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas

                var x = 40f
                var y = 50f
                val lineSpacing = 25f

                paint.textSize = 16f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("Báo cáo chi tiêu", x, y, paint)

                y += 40f
                paint.textSize = 12f
                paint.typeface = Typeface.DEFAULT

                // Header
                canvas.drawText("Ngày", x, y, paint)
                canvas.drawText("Tên ví", x + 100, y, paint)
                canvas.drawText("Số tiền", x + 250, y, paint)
                canvas.drawText("Danh mục", x + 350, y, paint)
                canvas.drawText("Ghi chú", x + 470, y, paint)
                y += lineSpacing

                spendingRecords.forEach { record ->
                    record.transactions.forEach { tx ->
                        if (y > 800) return@forEach
                        canvas.drawText(record.date.toString(), x, y, paint)
                        canvas.drawText(tx.wallet.name, x + 100, y, paint)
                        canvas.drawText(tx.amount.toString(), x + 250, y, paint)
                        canvas.drawText(tx.category.name, x + 350, y, paint)
                        canvas.drawText(tx.note, x + 470, y, paint)
                        y += lineSpacing
                    }
                }

                pdfDocument.finishPage(page)

                pdfDocument.writeTo(FileOutputStream(destination.fileDescriptor))
                pdfDocument.close()
                writeResultCallback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }
        },
        null
    )
}

