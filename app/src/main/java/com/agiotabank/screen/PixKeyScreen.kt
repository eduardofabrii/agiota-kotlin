package com.agiotabank.screen

import android.util.Patterns
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agiotabank.data.PixKey
import com.agiotabank.data.PixKeyType
import com.agiotabank.ui.PixKeyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixKeyScreen(goBack: () -> Unit = {}, viewModel: PixKeyViewModel = hiltViewModel()) {
    val pixKeys by viewModel.pixKeys.collectAsState()
    var showAddKeyDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Minhas Chaves Pix") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddKeyDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Chave Pix")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pixKeys) { pixKey ->
                PixKeyItem(pixKey)
            }
        }

        if (showAddKeyDialog) {
            AddPixKeyDialog(
                onDismiss = { showAddKeyDialog = false },
                onKeyAdded = {
                    viewModel.createPixKey(it.type, it.key)
                    showAddKeyDialog = false
                }
            )
        }
    }
}

private fun formatPixKey(key: String, type: PixKeyType): String {
    return when (type) {
        PixKeyType.CPF -> {
            if (key.length == 11) {
                key.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})".toRegex(), "$1.$2.$3-$4")
            } else {
                key
            }
        }

        PixKeyType.PHONE -> {
            if (key.length == 11) {
                key.replaceFirst("(\\d{2})(\\d{5})(\\d{4})".toRegex(), "($1) $2-$3")
            } else {
                key
            }
        }

        else -> key
    }
}

private fun getPixKeyValidationError(key: String, type: PixKeyType): String? {
    return when (type) {
        PixKeyType.CPF -> {
            if (key.all { it.isDigit() } && key.length == 11) null
            else "CPF inválido. Deve conter exatamente 11 dígitos."
        }
        PixKeyType.PHONE -> {
            if (key.all { it.isDigit() } && key.length == 11) null
            else "Telefone inválido. Deve conter 11 dígitos (DDD + número)."
        }
        PixKeyType.EMAIL -> {
            if (Patterns.EMAIL_ADDRESS.matcher(key).matches()) null
            else "Formato de e-mail inválido."
        }
        PixKeyType.RANDOM -> {
            if (key.isNotBlank()) null
            else "A chave aleatória não pode estar em branco."
        }
    }
}

@Composable
fun PixKeyItem(pixKey: PixKey) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val icon: ImageVector = when (pixKey.type) {
                PixKeyType.CPF -> Icons.Default.Badge
                PixKeyType.EMAIL -> Icons.Default.AlternateEmail
                PixKeyType.PHONE -> Icons.Default.Phone
                PixKeyType.RANDOM -> Icons.Default.VpnKey
            }

            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(icon, contentDescription = pixKey.type.name, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(pixKey.type.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = formatPixKey(pixKey.key, pixKey.type),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPixKeyDialog(onDismiss: () -> Unit, onKeyAdded: (PixKey) -> Unit) {
    var key by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(PixKeyType.CPF) }
    var keyError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Chave Pix") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedType.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de Chave") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        PixKeyType.entries.forEach {
                            DropdownMenuItem(text = { Text(it.name) }, onClick = {
                                selectedType = it
                                key = ""
                                keyError = null
                                expanded = false
                            })
                        }
                    }
                }
                OutlinedTextField(
                    value = key,
                    onValueChange = {
                        key = it
                        if (keyError != null) {
                            keyError = null
                        }
                    },
                    label = { Text("Chave") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = keyError != null,
                    supportingText = {
                        if (keyError != null) {
                            Text(keyError!!)
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val error = getPixKeyValidationError(key, selectedType)
                    if (error == null) {
                        val newKey = PixKey(type = selectedType, key = key, contaId = 0)
                        onKeyAdded(newKey)
                    } else {
                        keyError = error
                    }
                }
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
