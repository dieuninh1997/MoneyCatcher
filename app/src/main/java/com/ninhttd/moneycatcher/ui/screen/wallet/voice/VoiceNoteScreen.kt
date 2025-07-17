package com.ninhttd.moneycatcher.ui.screen.wallet.voice

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ninhttd.moneycatcher.R
import com.ninhttd.moneycatcher.common.TransactionType
import com.ninhttd.moneycatcher.common.formatDate
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import com.ninhttd.moneycatcher.di.VoiceRecognizerHelper
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.Transaction
import com.ninhttd.moneycatcher.ui.screen.add.component.WalletPickerRow
import com.ninhttd.moneycatcher.ui.screen.add.component.formatMoney
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorMutedPinkGray
import com.ninhttd.moneycatcher.ui.theme.ColorPastelOrange
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceNoteScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VoiceNoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState

    val mainViewModal: MainSharedViewModel = hiltActivityViewModel()
    val walletList by mainViewModal.walletList.collectAsState()
    val currentUser by mainViewModal.currentUser.collectAsState()
    val currentWalletId by mainViewModal.currentWalletId.collectAsState(initial = null)
    val currentWallet by mainViewModal.currentWallet.collectAsState(initial = null)
    val categoriesList = mainViewModal.categoriesList.collectAsState(initial = listOf()).value

    var showBtnDelete by remember { mutableStateOf(false) }

    var isListening by remember { mutableStateOf(false) }
    var spokenText by remember { mutableStateOf("") }
    val recognizer = remember {
        VoiceRecognizerHelper(
            context = context,
            onResult = {
                spokenText = it
                isListening = false
            },
            onError = {
                Toast.makeText(context, "Lỗi: $it", Toast.LENGTH_SHORT).show()
                spokenText = ""
                isListening = false
            }
        )
    }

    val intentResult by viewModel.intentResult.collectAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                recognizer.startListening()
            } else {
                Toast.makeText(context, "Bạn cần cấp quyền micro", Toast.LENGTH_SHORT).show()
            }
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            recognizer.destroy()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.voice_notes)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }

            )
        },
        bottomBar = {
            BottomBarAction(
                originalText = spokenText,
                isListening = isListening,
                isShowBtnDelete = showBtnDelete,
                context = context,
                onDelete = {

                    if (showBtnDelete) {
                        if (isListening) {
                            isListening = false
                            recognizer.destroy()
                        }
                    } else {
                        if (isListening) {
                            isListening = false
                            recognizer.destroy()
                        }
                        recognizer.startListening()
                        isListening = true
                    }

                    spokenText = ""
                    viewModel.resetIntentResult()
                    showBtnDelete = false

                },
                onMicTap = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        if (isListening) {
                            recognizer.stopListening()
                            isListening = false
                        } else {
                            recognizer.startListening()
                            isListening = true
                        }
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                onConfirm = {
                    showBtnDelete = !showBtnDelete
                    if (spokenText.isNotBlank()) {
                        val categoryList = categoriesList?.map { it -> it.name }
                        viewModel.parseSpokenText(
                            spokenText,
                            categoryList = categoryList ?: emptyList()
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (uiState) {
                is VoiceNoteUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is VoiceNoteUiState.Success -> {
                }

                is VoiceNoteUiState.Error -> {
                }

                VoiceNoteUiState.Idle -> {
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WalletPickerRow(
                    wallet = currentWallet,
                    onClick = { value -> },
                    onNavigateDetails = { value -> })
                Spacer(modifier = Modifier.height(48.dp))
                Crossfade(targetState = intentResult) { result ->
                    if (result == null) {
                        Column {
                            Text(
                                text = context.getString(R.string.say_somthing_vv),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = context.getString(R.string.example),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }

                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                        TransactionsResultCard(
                            result,
                            categoriesList,
                            walletName = currentWallet?.name ?: "",
                            walletId = currentWallet?.id.toString(),
                            userId = currentUser?.id.toString(),
                            onDelete = { item ->
                                viewModel.removeIntentResult(item)
                            },
                            onConfirm = { tx, txIndex ->
                                mainViewModal.createTransaction(
                                    tx
                                ) { success ->
                                    if (success) {
                                        viewModel.markItemAsAdded(txIndex)
                                        Toast.makeText(
                                            context,
                                            "Nhập thành công!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(context, "Error!!", Toast.LENGTH_LONG).show()
                                    }
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionsResultCard(
    data: List<ParseIntentResponse>,
    categoriesList: List<Category>?,
    userId: String,
    walletId: String,
    walletName: String,
    modifier: Modifier = Modifier,
    walletIcon: ImageVector = Icons.Default.AccountBalanceWallet,
    onDelete: (ParseIntentResponse) -> Unit = {},
    onConfirm: (Transaction, Int) -> Unit,
) {

    LazyColumn {
        items(data.size) { index ->
            val transaction = data[index]
            TransactionItem(
                transaction,
                userId = userId,
                walletId = walletId,
                onDelete = onDelete,
                onConfirm = onConfirm,
                walletName = walletName,
                categoriesList = categoriesList,
                index = index,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TransactionItem(
    transaction: ParseIntentResponse,
    userId: String,
    walletId: String,
    onDelete: (ParseIntentResponse) -> Unit,
    onConfirm: (Transaction, Int) -> Unit,
    walletName: String,
    categoriesList: List<Category>?,
    modifier: Modifier = Modifier,
    index: Int = 0,
    walletIcon: ImageVector = Icons.Default.AccountBalanceWallet
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorMutedPinkGray) // dark mode
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val categoryData = categoriesList?.getOrNull(transaction.category_index)
            if (categoryData != null) {
                Text(text = categoryData.icon, fontSize = 24.sp)
                Spacer(Modifier.width(8.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.category, // "Foods"
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatDate(
                        transaction.transaction_date ?: ""
                    ), // "Thứ Sáu, 11 tháng 7 2025"
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorColdPurplePink
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = transaction.note,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ColorColdPurplePink
                )
            }


            // Dòng 3: Ví + số tiền + hành động
            Column(modifier = Modifier.weight(1f)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = walletIcon,
                        contentDescription = null,
                        tint = Color(0xFFFF9800)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = walletName, color = ColorColdPurplePink)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${formatMoney(transaction.amount)}đ",
                    color = Color.Red,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (!transaction.isAdded) {
                Column {
                    IconButton(onClick = {
                        onDelete(transaction)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Xóa",
                            tint = Color.Gray
                        )
                    }

                    IconButton(onClick = {
                        val tx = Transaction(
                            userId = userId,
                            walletId = walletId,
                            categoryId = categoryData?.id.toString(),
                            transactionType = TransactionType.fromId(
                                transaction.transaction_type_id ?: 0
                            )
                                ?: TransactionType.EXPENSE,
                            amount = transaction.amount,
                            note = transaction.note,
                            transactionDate = LocalDate.parse(
                                transaction.transaction_date,
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                            ),
                        )
                        onConfirm(tx, index)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Xác nhận",
                            tint = Color.Cyan
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarAction(
    originalText: String,
    context: Context,
    isShowBtnDelete: Boolean,
    isListening: Boolean,
    onDelete: () -> Unit,
    onMicTap: () -> Unit,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = originalText,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onDelete, modifier = Modifier.size(56.dp)) {
                Icon(
                    if (isShowBtnDelete) Icons.Default.DeleteForever else Icons.Default.Close,
                    contentDescription = context.getString(R.string.cancel),
                    tint = Color.White
                )
            }
            MicButtonWithPulse(
                onClick = onMicTap,
                isListening = isListening
            )

            IconButton(
                onClick = onConfirm,
                modifier = Modifier.size(56.dp),
                enabled = !isShowBtnDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = context.getString(R.string.confirm),
                    tint = if (!isShowBtnDelete) Color.White else Color.Gray
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

    }

}

@Composable
fun MicButtonWithPulse(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.mic_pulse)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isListening,
        iterations = LottieConstants.IterateForever,
        speed = 1.2f
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(120.dp)
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(72.dp)
                .background(ColorPinkPrimary, CircleShape)
        ) {
            Icon(
                Icons.Default.Mic,
                contentDescription = "Mic",
                tint = ColorPastelOrange,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
