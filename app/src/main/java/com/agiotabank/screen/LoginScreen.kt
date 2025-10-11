package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agiotabank.components.AuthInputField
import com.agiotabank.components.PasswordInputField
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.TextPrimary

@Composable
fun LoginScreen(onLogin: () -> Unit, onNavigateToSignIn: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var attemptSubmit by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(48.dp))

        AuthInputField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            leadingIcon = Icons.Default.Email,
            isError = attemptSubmit && email.isBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))

        PasswordInputField(
            value = senha,
            onValueChange = { senha = it },
            label = "Senha",
            leadingIcon = Icons.Default.Lock,
            isError = attemptSubmit && senha.isBlank()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                attemptSubmit = true
                if (email.isNotBlank() && senha.isNotBlank()) {
                    onLogin()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
        ) {
            Text("Entrar", fontSize = 18.sp)
        }

        TextButton(onClick = onNavigateToSignIn) {
            Text("Não tem uma conta? Cadastre-se", color = LightBlue)
        }
    }
}