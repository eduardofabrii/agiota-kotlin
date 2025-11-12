package com.agiotabank.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import kotlin.math.pow

/**
 * Transforma a entrada de dígitos (ex: 12345) em texto de moeda formatado (ex: 1.234,56).
 * Usado em TransacaoScreen e ComprarInvestimentoScreen.
 */
class DinheiroVisualTransformation(
    private val fixedDecimals: Int = 2
) : VisualTransformation {

    private val format = DecimalFormat("#,##0.00").apply {
        isDecimalSeparatorAlwaysShown = true
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val cleanText = text.text.filter { it.isDigit() }

        if (cleanText.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val valueLong = cleanText.toLongOrNull() ?: 0L
        val valueInReais = valueLong.toDouble() / 10.0.pow(fixedDecimals)

        val formattedValue = format.format(valueInReais)
        val transformedText = AnnotatedString(formattedValue)

        val symbolsAdded = formattedValue.length - cleanText.length

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return minOf(transformedText.length, offset + symbolsAdded)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val result = offset - symbolsAdded
                return result.coerceIn(0, cleanText.length)
            }
        }
        return TransformedText(transformedText, offsetTranslator)
    }
}

/**
 * Transforma a entrada de dígitos (ex: 123456789) em texto de conta bancária (ex: 12345678-9).
 * Usado em TransacaoScreen.
 */
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