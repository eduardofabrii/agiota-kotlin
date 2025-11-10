package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // <-- IMPORTAR
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit // <-- IMPORTAR
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.*
import androidx.compose.runtime.* // <-- IMPORTAR
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agiotabank.data.Conta
import com.agiotabank.ui.theme.CardBackground
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.Red
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary


@Composable
fun PerfilScreen(
    goBack: () -> Unit = {},
    onSair: () -> Unit = {},
    conta: Conta?,
    onUpdateNome: (String) -> Unit = {}
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { goBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
                Text(
                    text = "Meu Perfil",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = LightBlue
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            conta?.nome?.firstOrNull()?.toString() ?: "C",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 48.sp
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = conta?.nome ?: "Erro ao carregar nome",
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar Nome",
                            tint = TextSecondary,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { showEditDialog = true }
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = conta?.email ?: "Erro ao carregar email",
                        color = TextSecondary,
                        fontSize = 16.sp
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground) //
                ) {
                    Column(Modifier.padding(16.dp)) {
                        InfoRow(
                            icon = Icons.Default.Phone,
                            label = "Telefone",
                            value = conta?.telefone ?: "Não informado"
                        )
                        Divider(
                            color = TextSecondary.copy(alpha = 0.2f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        InfoRow(
                            icon = Icons.Default.Info,
                            label = "CPF",
                            value = conta?.cpf ?: "Não informado"
                        )
                        Divider(
                            color = TextSecondary.copy(alpha = 0.2f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        InfoRow(
                            icon = Icons.Default.Pin,
                            label = "Agência",
                            value = conta?.agencia ?: "Erro ao carregar agência"
                        )
                        Divider(
                            color = TextSecondary.copy(alpha = 0.2f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        InfoRow(
                            icon = Icons.Default.Info,
                            label = "Conta",
                            value = conta?.numero ?: "Erro ao carregar conta"
                        )
                    }
                }
            }

            Button(
                onClick = onSair,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardBackground,
                    contentColor = Red
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = Red
                )
                Spacer(Modifier.width(8.dp))
                Text("Sair da Conta", fontSize = 16.sp)
            }
        }
    }

    if (showEditDialog) {
        EditNameDialog(
            currentName = conta?.nome ?: "",
            onDismiss = { showEditDialog = false },
            onConfirm = { novoNome ->
                onUpdateNome(novoNome)
                showEditDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Nome", color = TextPrimary) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Novo nome") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = LightBlue,
                    focusedIndicatorColor = LightBlue,
                    unfocusedIndicatorColor = TextSecondary,
                    focusedLabelColor = LightBlue,
                    unfocusedLabelColor = TextSecondary,
                )
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) {
                Text("Salvar", color = LightBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        },
        containerColor = CardBackground
    )
}


@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, null, tint = TextSecondary)
            Text(label, color = TextSecondary, fontSize = 14.sp)
        }
        Text(value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}