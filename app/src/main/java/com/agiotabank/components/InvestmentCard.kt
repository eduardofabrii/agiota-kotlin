package com.agiotabank.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agiotabank.data.Investimento
import com.agiotabank.ui.theme.CardBackground
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun InvestmentCard(
    investment: Investimento,
    onClick: () -> Unit
) {
    val formatadorMoeda = remember { NumberFormat.getCurrencyInstance(Locale("pt", "BR")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Mapeando os dados do 'Investimento' para o Card
                Text(
                    investment.nomeAtivo,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    investment.tipoAtivo,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
            Text(
                formatadorMoeda.format(investment.valorCompra),
                color = TextPrimary,
                fontSize = 16.sp
            )
        }
    }
}