package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone // <-- IMPORTAR
import androidx.compose.material.icons.filled.Pin // <-- IMPORTAR
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
fun SignInScreen(
    onSignIn: () -> Unit,

    onCreateAccount: (nome: String, email: String, senha: String, cpf: String, telefone: String) -> Any
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var attemptSubmit by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Criar Conta", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(48.dp))

        AuthInputField(
            value = nome,
            onValueChange = { nome = it },
            label = "Nome",
            leadingIcon = Icons.Default.Person,
            isError = attemptSubmit && nome.isBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthInputField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            leadingIcon = Icons.Default.Email,
            isError = attemptSubmit && email.isBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthInputField(
            value = cpf,
            onValueChange = { cpf = it },
            label = "CPF",
            leadingIcon = Icons.Default.Pin,
            isError = attemptSubmit && cpf.isBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthInputField(
            value = telefone,
            onValueChange = { telefone = it },
            label = "Telefone",
            leadingIcon = Icons.Default.Phone,
            isError = attemptSubmit && telefone.isBlank()
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
                //Vatualizada
                if (nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && cpf.isNotBlank() && telefone.isNotBlank()) {
                    //Chamada atualizada
                    onCreateAccount(nome, email, senha, cpf, telefone)
                    onSignIn()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
        ) {
            Text("Cadastrar", fontSize = 18.sp)
        }
    }
}