package com.ninhttd.moneycatcher.ui.screen.wallet.ocr

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ninhttd.moneycatcher.R
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.MessageItem
import com.ninhttd.moneycatcher.domain.model.Transaction
import com.ninhttd.moneycatcher.domain.model.UserInfo
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.ui.screen.add.component.WalletPickerBottomSheet
import com.ninhttd.moneycatcher.ui.screen.add.component.WalletPickerRow
import com.ninhttd.moneycatcher.ui.screen.editcategory.TopBar
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.screen.wallet.component.LoadingBubble
import com.ninhttd.moneycatcher.ui.screen.wallet.voice.TransactionItem
import com.ninhttd.moneycatcher.ui.screen.wallet.voice.VoiceNoteUiState
import com.ninhttd.moneycatcher.ui.screen.wallet.voice.VoiceNoteViewModel
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorMutedPinkGray
import com.ninhttd.moneycatcher.ui.theme.ColorPastelOrange
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimaryContainer
import com.ninhttd.moneycatcher.ui.theme.ColorZeroWhite
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OcrInvoiceScreen(
    onNavigateUp: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OcrInvoiceViewModel = hiltViewModel()
) {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val context = LocalContext.current
    val uiState = viewModel.uiState

    val mainViewModel: MainSharedViewModel = hiltActivityViewModel()
    val walletList by mainViewModel.walletList.collectAsState()
    val currentUser by mainViewModel.currentUser.collectAsState()
    val currentWalletId by mainViewModel.currentWalletId.collectAsState(initial = null)
    val currentWallet by mainViewModel.currentWallet.collectAsState(initial = null)
    val categoriesList = mainViewModel.categoriesList.collectAsState(initial = listOf()).value

    val voiceViewModel: VoiceNoteViewModel = hiltActivityViewModel()
    val voiceUiState = voiceViewModel.uiState

    var showBottomSheet by remember { mutableStateOf(false) }
    var recognizedText by remember { mutableStateOf("") }
    var isFileSelectorVisible by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var requestCamera by remember { mutableStateOf(false) }

    val noteList = listOf(
        "Đã chi 200.000đ cho việc ăn ngoài từ ví", "Đã nhận lương tháng 4 là 15.000.000 VND"
    )

    val messageList = remember { mutableStateListOf<MessageItem>() }

    fun analysisOcrInvoice(uri: Uri) {
        val categoryList = categoriesList?.map { it -> it.name }
        val resizedFile = resizeImage(context, uri)
        if (resizedFile != null) {
            val imageRequestBody =
                resizedFile.asRequestBody("image/*".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData(
                "file", resizedFile.name, imageRequestBody
            )

            val categoryListPart = categoryList?.map { category ->
                MultipartBody.Part.createFormData("category_list", category)
            }

            val safeCategoryList = categoryListPart ?: emptyList()
            viewModel.ocrInvoiceFilePart(
                file = filePart, categoryList = safeCategoryList
            )
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            messageList.add(MessageItem.ImageMessage(it))
            analysisOcrInvoice(it)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri?.let {
                messageList.add(MessageItem.ImageMessage(it))
                cameraImageUri?.let { uri ->
                    analysisOcrInvoice(uri)
                }
            }
        }
    }

    LaunchedEffect(requestCamera, permissionState.status) {
        if (requestCamera && permissionState.status.isGranted) {
            val newUri = createNewImageUri(context)
            cameraImageUri = newUri
            cameraLauncher.launch(newUri)
            requestCamera = false
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = ColorPinkPrimary, topBar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TopBar(
                title = context.getString(R.string.ocr_title), onBackPress = {
                    onNavigateUp()
                })
        }

    }, bottomBar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.Bottom
            ) {
                // NOTE list
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    noteList.take(2).forEach { suggestion ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    Color.LightGray, shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { recognizedText = suggestion }
                                .padding(horizontal = 12.dp, vertical = 6.dp)) {
                            Text(text = suggestion, color = Color.Black, maxLines = 2)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp, color = Color.White, shape = RoundedCornerShape(26.dp)
                        )
                        .padding(4.dp)
                ) {
                    OutlinedTextField(
                        value = recognizedText,
                        onValueChange = { recognizedText = it },
                        placeholder = { androidx.compose.material.Text("Nhập ghi chú nhanh...") },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = ColorColdPurplePink,
                            cursorColor = ColorColdPurplePink,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            IconButton(onClick = {
                                isFileSelectorVisible = !isFileSelectorVisible
                            }) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Thêm ảnh",
                                    tint = ColorPinkPrimaryContainer
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (recognizedText.isNotBlank()) {
                                    messageList.add(MessageItem.TextMessage(recognizedText))
                                    val categoryList = categoriesList?.map { it -> it.name }
                                    if (categoryList != null) {
                                        voiceViewModel.parseSpokenText(recognizedText, categoryList)
                                    }
                                    recognizedText = ""
                                }
                            }),
                    )
                }
            }

            DropdownMenu(
                expanded = isFileSelectorVisible,
                onDismissRequest = { isFileSelectorVisible = false },
                offset = DpOffset(x = 0.dp, y = (-220).dp) // đẩy lên trên ô input
            ) {
                DropdownMenuItem(text = { Text("Máy ảnh") }, onClick = {
                    isFileSelectorVisible = false
                    if (permissionState.status.isGranted) {
                        val newUri = createNewImageUri(context)
                        cameraImageUri = newUri
                        cameraLauncher.launch(newUri)
                    } else {
                        requestCamera = true
                        permissionState.launchPermissionRequest()
                    }
                }, leadingIcon = {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = ColorPinkPrimaryContainer
                    )
                })
                DropdownMenuItem(text = { Text("Ảnh") }, onClick = {
                    isFileSelectorVisible = false
                    pickImageLauncher.launch("image/*")
                }, leadingIcon = {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                        tint = ColorPinkPrimaryContainer
                    )
                })
            }

        }
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Column {
                WalletPickerRow(
                    currentWallet, onClick = {
                        showBottomSheet = true
                    }, onNavigateDetails = onNavigateDetails
                )

                Spacer(Modifier.height(16.dp))

                ChatBubble(
                    messageList,
                    categoriesList = categoriesList,
                    currentWallet = currentWallet,
                    currentUser = currentUser,
                    onDetele = { tx ->

                    },
                    onConfirm = { tx, txIndex ->
                        mainViewModel.createTransaction(
                            tx
                        ) { success ->
                            if (success) {
                                viewModel.markItemAsAdded(txIndex)
                                Toast.makeText(
                                    context, "Nhập thành công!", Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(context, "Error!!", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                )

                when (uiState) {
                    is OcrInvoiceUiState.Loading -> {
                        LoadingBubble()
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    is OcrInvoiceUiState.Error -> {
                        messageList.add(MessageItem.TextMessage(context.getString(R.string.warning_message), false))
                    }

                    is OcrInvoiceUiState.Success -> {
                        messageList.add(MessageItem.ResultMessage(uiState.result))
                    }

                    OcrInvoiceUiState.Idle -> {
                    }
                }

                when (voiceUiState) {
                    is VoiceNoteUiState.Error -> {
                        messageList.add(MessageItem.TextMessage(context.getString(R.string.warning_message), false))
                    }
                    VoiceNoteUiState.Idle -> {

                    }
                    VoiceNoteUiState.Loading -> {
                        LoadingBubble()
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                    is VoiceNoteUiState.Success -> {
                        voiceUiState.result.forEach { result ->
                            messageList.add(MessageItem.ResultMessage(result))
                        }
                    }
                }
            }

            if (showBottomSheet) {
                WalletPickerBottomSheet(
                    currentWalletId = currentWalletId.toString(),
                    wallets = walletList,
                    onSelect = {
                        mainViewModel.setCurrentWalletId(it.id)
                        showBottomSheet = false
                    },
                    onDismiss = { showBottomSheet = false })
            }
        }
    }
}

@Composable
fun ChatBubble(
    messageList: List<MessageItem>,
    categoriesList: List<Category>?,
    currentWallet: Wallet?,
    currentUser: UserInfo?,
    onDetele: (ParseIntentResponse) -> Unit,
    onConfirm: (Transaction, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        reverseLayout = true,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(messageList.size) { index ->
            val item = messageList[messageList.lastIndex - index]
            when (item) {
                is MessageItem.TextMessage -> {
                    val arrangement = if (item.isSent) Arrangement.End else Arrangement.Start
                    val bgColor = if (item.isSent) ColorMutedPinkGray else ColorPastelOrange

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = arrangement
                    ) {

                        val windowInfo = LocalWindowInfo.current
                        val density = LocalDensity.current

                        val maxWidth = with(density) {
                            windowInfo.containerSize.width.toDp()
                        } * 0.8f

                        Box(
                            modifier = Modifier
                                .widthIn(max = maxWidth)
                                .background(bgColor, shape = RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Text(item.text, color = ColorZeroWhite)
                        }
                    }
                }

                is MessageItem.ImageMessage -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.uri),
                            contentDescription = "Captured image",
                            modifier = Modifier
                                .widthIn(max = 220.dp)
                                .heightIn(max = 300.dp)
                                .clip(
                                    RoundedCornerShape(16.dp)
                                ),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                is MessageItem.ResultMessage -> {
                    val data = listOf<ParseIntentResponse>(item.data)
                    TransactionItem(
                        transaction = item.data,
                        walletName = currentWallet?.name ?: "",
                        walletId = currentWallet?.id.toString(),
                        userId = currentUser?.id.toString(),
                        onDelete = onDetele,
                        onConfirm = onConfirm,
                        categoriesList = categoriesList
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}


fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open input stream from URI")

    val tempFile = File.createTempFile("captured_", ".jpg", context.cacheDir)
    tempFile.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }

    return tempFile
}


fun createNewImageUri(context: Context): Uri {
    val file =
        File.createTempFile("camera_image_${System.currentTimeMillis()}", ".jpg", context.cacheDir)
            .apply {
                createNewFile()
                deleteOnExit()
            }
    return FileProvider.getUriForFile(
        context, "${context.packageName}.provider", file
    )
}

fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun resizeImage(context: Context, uri: Uri, maxSize: Int = 1024): File? {
    val inputStream = context.contentResolver.openInputStream(uri)
    val original = BitmapFactory.decodeStream(inputStream) ?: return null

    val aspectRatio = original.width.toFloat() / original.height.toFloat()
    val width: Int
    val height: Int

    if (original.width > original.height) {
        width = maxSize
        height = (width / aspectRatio).toInt()
    } else {
        height = maxSize
        width = (height * aspectRatio).toInt()
    }

    val resizedBitmap = Bitmap.createScaledBitmap(original, width, height, true)
    val file = File.createTempFile("resized_", ".jpg", context.cacheDir)
    val outStream = FileOutputStream(file)
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream) // 80% chất lượng
    outStream.close()

    return file
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // 80% chất lượng
    val bytes = outputStream.toByteArray()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}