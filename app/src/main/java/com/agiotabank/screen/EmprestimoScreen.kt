@file:OptIn(ExperimentalMaterial3Api::class)
package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agiotabank.ui.theme.AgiotaBankTheme
import com.agiotabank.ui.theme.CardBackground
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun EmprestimoScreen(onVoltar: () -> Unit = {}) {
    val scrollState = rememberScrollState()
    val limiteMaximo = 5000f
    val opcoesParcelas = listOf(2, 3, 6, 9, 12)

    var posicaoSliderValor by remember { mutableStateOf(0.2f) }
    val valorSelecionado = posicaoSliderValor * limiteMaximo
    var indiceSliderParcelas by remember { mutableStateOf(0) }
    val parcelasSelecionadas = opcoesParcelas[indiceSliderParcelas]

    val taxaJurosMensal = 0.05
    val valorSelecionadoDouble = valorSelecionado.toDouble()
    val parcelasSelecionadasDouble = parcelasSelecionadas.toDouble()
    val valorParcela = if (taxaJurosMensal > 0) {
        valorSelecionadoDouble * (taxaJurosMensal * (1 + taxaJurosMensal).pow(parcelasSelecionadasDouble)) / ((1 + taxaJurosMensal).pow(parcelasSelecionadasDouble) - 1)
    } else {
        valorSelecionadoDouble / parcelasSelecionadasDouble
    }
    val totalPagar = valorParcela * parcelasSelecionadasDouble

    val formatadorMoeda = remember {
        NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(WindowInsets.statusBars.asPaddingValues()),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Empréstimo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                        )
                    }
                },
                actions = {
                    Spacer(Modifier.size(40.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = DarkBackground,
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(0.5f)) {
                        Text("Total a pagar", color = TextSecondary, fontSize = 14.sp)
                        Text(
                            formatadorMoeda.format(totalPagar),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = { /* TODO: Lógica de solicitar empréstimo (US009) */ },
                        modifier = Modifier
                            .weight(0.5f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
                    ) {
                        Text("Solicitar", fontSize = 18.sp)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(20.dp)
            ,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column {
                    Text("Valor pré-aprovado", color = TextSecondary, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        formatadorMoeda.format(limiteMaximo),
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Simule seu empréstimo",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Valor desejado:", color = TextSecondary, fontSize = 14.sp)
                                Text(
                                    formatadorMoeda.format(valorSelecionado),
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            SliderBolinha(
                                value = posicaoSliderValor,
                                onValueChange = { posicaoSliderValor = it },
                                valueRange = 0f..1f
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Número de parcelas:", color = TextSecondary, fontSize = 14.sp)
                                Text(
                                    "$parcelasSelecionadas parcelas",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            SliderBolinha(
                                value = indiceSliderParcelas.toFloat(),
                                onValueChange = {
                                    indiceSliderParcelas = it.roundToInt()
                                },
                                valueRange = 0f..(opcoesParcelas.size - 1).toFloat(),
                                steps = opcoesParcelas.size - 2
                            )
                        }

                        Divider(color = TextSecondary.copy(alpha = 0.2f), thickness = 1.dp)

                        LinhaSimulacao(
                            rotulo = "Valor da parcela:",
                            valor = formatadorMoeda.format(valorParcela)
                        )
                        LinhaSimulacao(
                            rotulo = "Taxa de juros:",
                            valor = "${String.format("%.1f", taxaJurosMensal * 100)}% a.m."
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Aviso",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        "Simulação baseada na taxa de ${String.format("%.1f", taxaJurosMensal * 100)}% a.m. A aprovação final está sujeita a análise de crédito.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LinhaSimulacao(rotulo: String, valor: String) {
    Row(
        Modifier.fillMaxWidth(),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Text(rotulo, color = TextSecondary, fontSize = 14.sp)
        Text(
            valor,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SliderBolinha(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0
) {
    val sliderColors = SliderDefaults.colors(
        activeTrackColor = LightBlue,
        inactiveTrackColor = TextSecondary.copy(alpha = 0.3f),
        thumbColor = LightBlue
    )

    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        valueRange = valueRange,
        steps = steps,
        colors = sliderColors,
        thumb = {
            Card(
                modifier = Modifier.size(20.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = sliderColors.thumbColor)
            ) {}
        },
        track = { sliderState ->
            val fraction = ((sliderState.value - sliderState.valueRange.start) /
                    (sliderState.valueRange.endInclusive - sliderState.valueRange.start))
                .coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(sliderColors.inactiveTrackColor)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .background(sliderColors.activeTrackColor)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EmprestimoScreenPreview() {
    AgiotaBankTheme(darkTheme = true, dynamicColor = false) {
        EmprestimoScreen()
    }
}
