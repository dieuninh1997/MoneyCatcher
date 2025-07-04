package com.ninhttd.moneycatcher.ui.screen.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ninhttd.moneycatcher.R


@Composable
fun LoginScreen(
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user = viewModel.uiState
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isShowPassword by remember { mutableStateOf(false) }


    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onNavigate()
            viewModel.reset() // đừng quên reset lại uiState sau navigate
        }
    }
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())

        ) {
            Spacer(modifier = Modifier.height(48.dp))
            //logo + app name
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Money Catcher",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Đăng nhập vào tài khoản của bạn",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            //email input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                placeholder = { Text("Nhập email") },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    focusedBorderColor = Color.White,
                    placeholderColor = Color.Gray,
                    leadingIconColor = Color.Gray,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            //pass input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                placeholder = { Text("Nhập mật khẩu") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isShowPassword = !isShowPassword
                        },
                    ) {
                        Icon(
                            imageVector = if (isShowPassword) Icons.Rounded.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    focusedBorderColor = Color.White,
                    placeholderColor = Color.Gray,
                    leadingIconColor = Color.Gray,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            //forgot pwd
            Text(
                text = "Quên mật khẩu?",
                color = Color(0xFF2196F3),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        //TODO
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.login(email, password)
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF2186F3))
            ) {
                Text("Login", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
                Text(text = " Hoặc đăng nhập bằng ", color = Color.Gray, fontSize = 14.sp)
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))
            //gg button

            OutlinedButton(
                onClick = {
                    //TODO
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(Color.White)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Tiếp tục với Google",
                    color = Color(0xFF2196F3),
                    modifier = Modifier.clickable {
//                loginWithGoogle(context)
                    })
            }

            Spacer(modifier = Modifier.height(24.dp))


        }
        when (uiState) {
            is LoginUiState.Error -> {
                Spacer(Modifier.height(8.dp))
                Text(text = (uiState as LoginUiState.Error).message, color = Color.Red)
            }

            is LoginUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .pointerInput(Unit) {}
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            else -> Unit
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(onNavigate = {})
}