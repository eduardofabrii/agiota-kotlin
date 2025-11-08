package com.agiotabank.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agiotabank.data.Card
import com.agiotabank.ui.theme.AgiotaBankTheme
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary
import com.agiotabank.viewmodel.CardViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(
    goBack: () -> Unit = {},
    holderName: String,
    viewModel: CardViewModel = hiltViewModel()
) {
    val cards by viewModel.cards.collectAsState()
    var showAddCardDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cartão",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    IconButton(onClick = { /* RF-020: Alertas */ }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Alertas")
                    }
                    IconButton(onClick = { /* RF-019: Histórico de acessos */ }) {
                        Icon(Icons.Filled.History, contentDescription = "Histórico")
                    }
                    Box(
                        Modifier
                            .padding(bottom = 20.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(LightBlue)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("C", fontWeight = FontWeight.Bold)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddCardDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Cartão")
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(cards) { card ->
                    CreditCardItem(card = card, onDelete = { viewModel.deleteCard(card) })
                }
            }
        }

        if (showAddCardDialog) {
            AddCardDialog(
                holderName = holderName,
                onDismiss = { showAddCardDialog = false },
                onCardAdded = {
                    viewModel.addCard(it)
                    showAddCardDialog = false
                }
            )
        }
    }
}

@Composable
fun CreditCardItem(card: Card, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.6f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {
            val glow = Brush.linearGradient(
                listOf(
                    LightBlue.copy(alpha = 0.25f),
                    Color.Transparent
                )
            )
            Box(
                Modifier
                    .matchParentSize()
                    .background(glow)
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    Modifier.size(36.dp),
                    shape = CircleShape,
                    color = LightBlue.copy(alpha = 0.15f)
                ) {}
                Icon(Icons.Filled.CreditCard, null, tint = LightBlue)
            }

            Column(
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 8.dp)
            ) {
                CardNumber(masked = card.number)
                Spacer(Modifier.height(10.dp))
                Text(
                    text = card.holderName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                Modifier.align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Válido até", color = TextSecondary, fontSize = 12.sp)
                Spacer(Modifier.width(8.dp))
                Text(card.expiryDate, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(
                text = "crédito • débito",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.BottomEnd)
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar Cartão", tint = LightBlue)
            }
        }
    }
}

@Composable
fun AddCardDialog(holderName: String, onDismiss: () -> Unit, onCardAdded: (Card) -> Unit) {
    val cardNumber by remember { mutableStateOf(generateRandomCardNumber()) }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Cartão") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = holderName,
                    onValueChange = {},
                    label = { Text("Nome do Titular") },
                    readOnly = true
                )
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { },
                    label = { Text("Número do Cartão") },
                    readOnly = true
                )
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = {
                        if (it.length <= 4) expiryDate = it.filter { char -> char.isDigit() }
                    },
                    label = { Text("Data de Validade (MM/AA)") },
                    visualTransformation = ExpiryDateVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = {
                        if (it.length <= 3) cvv = it.filter { char -> char.isDigit() }
                    },
                    label = { Text("CVV") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newCard = Card(
                        holderName = holderName,
                        number = cardNumber,
                        expiryDate = expiryDate.let { if (it.length == 4) it.substring(0, 2) + "/" + it.substring(2, 4) else it },
                        cvv = cvv
                    )
                    onCardAdded(newCard)
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

private fun generateRandomCardNumber(): String {
    return (1..4).joinToString(" ") {
        Random.nextInt(1000, 10000).toString()
    }
}

class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1) out += "/"
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }

        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}


@Composable
private fun CardNumber(masked: String) {
    val text = buildAnnotatedString {
        val chunks = masked.split(" ")
        chunks.forEachIndexed { idx, chunk ->
            withStyle(
                SpanStyle(
                    letterSpacing = 2.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(chunk)
            }
            if (idx != chunks.lastIndex) append("  ")
        }
    }
    Text(text = text, fontSize = 18.sp)
}

@Preview(
    name = "Card Screen — Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewCardDark() {
    AgiotaBankTheme(darkTheme = true, dynamicColor = false) {
        // O preview pode não funcionar corretamente com Hilt, mas o AddCardDialog pode ser testado isoladamente
        AddCardDialog(holderName = "Nome do Titular", onDismiss = {}, onCardAdded = {})
    }
}
