package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                BusinessCard()
            }
        }
    }
}

@Composable
fun BusinessCard() {
    // Elegant Dark Theme Palette
    val darkBackground = Color(0xFF121814)
    val midGradient = Color(0xFF1B2E24)
    val accentGreen = Color(0xFF4CAF50)
    val lightText = Color(0xFFECEFF1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(darkBackground, midGradient)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp)
        ) {

            // --- Profile Photo ---
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(140.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(width = 2.dp, color = accentGreen.copy(alpha = 0.3f), shape = CircleShape)
                )

                Image(
                    painter = painterResource(id = R.drawable.profile),// Replace with R.drawable.profile when ready
                    contentDescription = "Profile Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E2922))
                        .border(width = 3.dp, color = accentGreen, shape = CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Name & Role ---
            Text(
                text = "Ritchie Ray Echavez Jr",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = lightText,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "BSIT STUDENT",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = accentGreen,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // --- Contact Info ---
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E2A22).copy(alpha = 0.7f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFF37474F).copy(alpha = 0.4f),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    ContactRow(
                        icon = Icons.Default.Phone,
                        label = "+63 965 883 2593",
                        iconColor = accentGreen,
                        textColor = lightText
                    )
                    ContactRow(
                        icon = Icons.Default.Email,
                        label = "ritchieray@email.com",
                        iconColor = accentGreen,
                        textColor = lightText
                    )
                }
            }
        }
    }
}

@Composable
fun ContactRow(
    icon: ImageVector,
    label: String,
    iconColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}