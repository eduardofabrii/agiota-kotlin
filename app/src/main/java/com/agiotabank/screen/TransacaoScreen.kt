package com.agiotabank.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agiotabank.ui.theme.AgiotaBankTheme
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.TextSecondary
import java.text.DecimalFormat
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransacaoScreen(goBack: () -> Unit = {}) {
    var etapa by remember { mutableStateOf(1) }
    fun voltar() {
        if (etapa > 1) {
            etapa--
        } else {
            goBack()
        }
    }

    fun avancar() {
        if (etapa < 3) {
            etapa++
        }
    }
    BackHandler {
        voltar()
    }
    var isButtonEnabled by remember { mutableStateOf(false) }
    fun toggleButton(boolean: Boolean) {
        isButtonEnabled = boolean
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = { Text("Transação") },
                navigationIcon = {
                    IconButton(onClick = { voltar() }) {
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
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                    Button(
                        onClick = { avancar() },
                        shape = RoundedCornerShape(12.dp),
                        enabled = isButtonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                    ) {
                        Text("Continuar", fontSize = 18.sp)
                    }
            }
        }) { paddingValues ->
        val options = mapOf(
            "Agibank S.A." to "019",
            "Banco do Brasil" to "001",
            "Banco Inter S.A." to "077",
        )
        var selected by remember { mutableStateOf(options.keys.first()) }
        val tipos = listOf("Conta Corrente", "Conta Poupança")
        var tipo by remember { mutableStateOf("") }
        var agencia by remember { mutableStateOf("") }
        var conta by remember { mutableStateOf("") }
        var valor by remember { mutableStateOf("") }
        when (etapa) {
            1 -> Etapa1(
                paddingValues = paddingValues,
                options = options,
                selected = selected,
                onBancoSelected = { selected = it },
                tipos = tipos,
                tipo = tipo,
                onTipoSelected = { tipo = it },
                agencia = agencia,
                onAgenciaChange = { agencia = it },
                conta = conta,
                onContaChange = { conta = it },
                toggleButton = { toggleButton(it) }
            )

            2 -> Etapa2(paddingValues = paddingValues, valor = valor, onValorChange = { valor = it }, toggleButton = { toggleButton(it) })
            3 -> Etapa3(paddingValues = paddingValues)
            else -> {}

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Etapa1(
    paddingValues: PaddingValues,
    options: Map<String, String>,
    selected: String,
    onBancoSelected: (String) -> Unit,
    onTipoSelected: (String) -> Unit,
    tipos: List<String>,
    tipo: String,
    agencia: String,
    onAgenciaChange: (String) -> Unit,
    conta: String,
    onContaChange: (String) -> Unit,
    toggleButton: (Boolean) -> Unit
) {
    DisposableEffect(selected, tipo, agencia, conta) {
        if (selected.isNotEmpty() && tipo.isNotEmpty() && agencia.isNotEmpty() && conta.isNotEmpty()) {
            toggleButton(true)
        } else {
            toggleButton(false)
        }
        onDispose {
            toggleButton(false)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Dados de destino")

        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                var expandido by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expandido,
                        onExpandedChange = { expandido = !expandido },
                    ) {
                        OutlinedTextField(
                            value = "$selected - ${options[selected]}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Banco") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
                            modifier = Modifier
                                .menuAnchor(
                                    ExposedDropdownMenuAnchorType.PrimaryNotEditable, true
                                )
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandido,
                            onDismissRequest = { expandido = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            options.keys.forEach { banco ->
                                DropdownMenuItem(text = {
                                    Text(
                                        text = "$banco - ${options[banco]}",
                                    )
                                }, onClick = {
                                    onBancoSelected(banco)
                                    expandido = false
                                })
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))

                    var tipoExpandido by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = tipoExpandido,
                        onExpandedChange = { tipoExpandido = !tipoExpandido },
                    ) {
                        OutlinedTextField(
                            value = tipo,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de conta") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    tipoExpandido
                                )
                            },
                            modifier = Modifier
                                .menuAnchor(
                                    ExposedDropdownMenuAnchorType.PrimaryNotEditable, true
                                )
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = tipoExpandido,
                            onDismissRequest = { tipoExpandido = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            tipos.forEach { tipoConta ->
                                DropdownMenuItem(text = {
                                    Text(
                                        text = tipoConta,
                                    )
                                }, onClick = {
                                    onTipoSelected(tipoConta)
                                    tipoExpandido = false
                                })
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = agencia,
                        onValueChange = {
                            onAgenciaChange(it.filter { ch -> ch.isDigit() }.take(4))
                        },
                        label = { Text("Agência") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = conta,
                        onValueChange = {
                            onContaChange(it.filter { ch -> ch.isDigit() }.take(12))
                        },
                        label = { Text("Conta") },
                        visualTransformation = ContaBancariaVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }
    }
}

@Composable
fun Etapa2(paddingValues: PaddingValues, valor: String, onValorChange: (String) -> Unit, toggleButton: (Boolean) -> Unit = {}) {
    DisposableEffect(valor) {
        if (valor.isNotEmpty() && valor.toDouble() > 0) {
            toggleButton(true)
        } else {
            toggleButton(false)
        }
        onDispose {
            toggleButton(false)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column {
            Text("Saldo disponível", fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                var isHidden by remember { mutableStateOf(false) }
                if (isHidden) {
                    Text(
                        "R$ ----,--", fontSize = 28.sp, fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        "R$ 3.363,32", fontSize = 28.sp, fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.width(12.dp))
                IconButton(
                    onClick = { isHidden = !isHidden }, modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Filled.Visibility,
                        null,
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            OutlinedTextField(
                value = valor,
                onValueChange = { onValorChange(it.filter {ch -> ch.isDigit()}.take(12)) },
                label = { Text("Valor") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                singleLine = true,
                prefix = { Text("R$") },
                visualTransformation = DinheiroVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}

@Composable
fun Etapa3(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Confirmação", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Banco: Agibank S.A. - 019")
                Text("Tipo de conta: Conta Corrente")
                Text("Agência: 1234")
                Text("Conta: 123456-7")
                Text("Valor: R$ 1.234,56")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransacaoScreenPreview() {
    AgiotaBankTheme(darkTheme = true, dynamicColor = false) {
        TransacaoScreen()
    }
}

class ContaBancariaVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        val limpa = original.filter { it.isDigit() }

        // Se o comprimento for <= 2, não há hífen
        if (limpa.length <= 2) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // 2. Aplica a formatação: tudo exceto o último dígito + hífen + último dígito
        val formatada = limpa.dropLast(1) + "-" + limpa.last()

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Mapeia a posição do cursor no texto original para o texto formatado
                return when {
                    offset <= limpa.length - 1 -> offset
                    else -> offset + 1 // +1 para o hífen
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Mapeia a posição do cursor no texto formatado para o texto original
                return when {
                    offset <= limpa.length - 1 -> offset
                    else -> offset - 1 // -1 para o hífen
                }
            }
        }

        return TransformedText(
            AnnotatedString(formatada),
            offsetTranslator
        )
    }
}

class DinheiroVisualTransformation(
    private val fixedDecimals: Int = 2
) : VisualTransformation {

    private val format = DecimalFormat("#,##0.00").apply {
        isDecimalSeparatorAlwaysShown = true
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val cleanText = text.text.filter { it.isDigit() }

        if (cleanText.isEmpty()) {
            // Não formata se vazio
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val valueLong = cleanText.toLongOrNull() ?: 0L
        val valueInReais = valueLong.toDouble() / 10.0.pow(fixedDecimals)

        // Aplica a formatação APENAS ao número
        val formattedValue = format.format(valueInReais)

        // O texto transformado é SÓ a parte formatada (ex: "1.234,56")
        val transformedText = AnnotatedString(formattedValue)

        // 1. Número de caracteres de formatação (pontos e vírgula)
        val symbolsAdded = formattedValue.length - cleanText.length

        val offsetTranslator = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                // Mapeia do texto limpo (apenas dígitos) para o texto formatado (com pontos/vírgula)
                return minOf(transformedText.length, offset + symbolsAdded)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Mapeia do texto formatado (com pontos/vírgula) de volta para o texto limpo
                val result = offset - symbolsAdded

                return result.coerceIn(0, cleanText.length)
            }
        }

        return TransformedText(
            transformedText,
            offsetTranslator
        )
    }
}