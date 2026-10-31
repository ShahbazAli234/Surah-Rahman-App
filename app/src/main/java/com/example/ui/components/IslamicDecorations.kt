package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.quran.SurahRahmanProvider
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight

@Composable
fun IslamicDivider(
    modifier: Modifier = Modifier,
    color: Color = GoldAccent.copy(alpha = 0.5f)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, color)
                    )
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(color, Color.Transparent)
                    )
                )
        )
    }
}

@Composable
fun BismillahHeader(
    modifier: Modifier = Modifier,
    showTranslations: Boolean = true
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(GoldAccent, GoldLight.copy(alpha = 0.3f))),
                shape = RoundedCornerShape(16.dp)
            ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = SurahRahmanProvider.BISMILLAH_ARABIC,
                    fontFamily = AmiriFontFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                if (showTranslations) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = SurahRahmanProvider.BISMILLAH_ENGLISH,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = SurahRahmanProvider.BISMILLAH_URDU,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun AyahNumberBadge(
    number: Int,
    modifier: Modifier = Modifier,
    isRefrain: Boolean = false
) {
    val bgColor = if (isRefrain) GoldAccent.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer
    val borderColor = if (isRefrain) GoldAccent else MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(1.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$number",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isRefrain) GoldAccent else MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
