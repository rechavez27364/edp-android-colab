package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- ULTRA MODERN NEON PALETTE ---
val NeonPurple = Color(0xFFBC00FF)
val NeonCyan = Color(0xFF00F0FF)
val DarkNight = Color(0xFF050505)
val SurfaceCard = Color(0xFF121212)
val HotPink = Color(0xFFFF007F)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeonGroceryTheme {
                NeonGroceryApp()
            }
        }
    }
}

@Composable
fun NeonGroceryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = NeonPurple,
            secondary = NeonCyan,
            background = DarkNight,
            surface = SurfaceCard
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeonGroceryApp() {
    var newItem by remember { mutableStateOf("") }
    val groceries = remember { mutableStateListOf<String>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(DarkNight, Color(0xFF1A0033))
                )
            )
    ) {
        // Decorative Neon Glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-100).dp)
                .background(Brush.radialGradient(listOf(NeonPurple.copy(alpha = 0.15f), Color.Transparent)))
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = NeonCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "NEON GROCERY",
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                color = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                // --- NEON INPUT FIELD ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(20.dp), ambientColor = NeonPurple, spotColor = NeonPurple)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceCard)
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newItem,
                        onValueChange = { newItem = it },
                        placeholder = { Text("Search or add items...", color = Color.Gray) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = NeonCyan,
                            focusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                    
                    IconButton(
                        onClick = {
                            if (newItem.isNotBlank()) {
                                groceries.add(0, newItem.trim())
                                newItem = ""
                            }
                        },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(48.dp)
                            .background(
                                Brush.linearGradient(listOf(NeonPurple, HotPink)),
                                CircleShape
                            )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // --- STATS BAR ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "IN CART: ${groceries.size}",
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    
                    if (groceries.isNotEmpty()) {
                        TextButton(onClick = { groceries.clear() }) {
                            Text("WIPE ALL", color = HotPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // --- THE LIST ---
                if (groceries.isEmpty()) {
                    NeonEmptyState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        contentPadding = PaddingValues(bottom = 50.dp)
                    ) {
                        items(groceries, key = { it + it.length + Math.random() }) { item ->
                            NeonItemCard(
                                name = item,
                                onDelete = { groceries.remove(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NeonItemCard(name: String, onDelete: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(NeonCyan, CircleShape)
                    .shadow(5.dp, CircleShape, ambientColor = NeonCyan, spotColor = NeonCyan)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = name.uppercase(),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun NeonEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.ShoppingBag,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = NeonPurple.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "THE SYSTEM IS CLEAR",
            color = Color.Gray,
            fontWeight = FontWeight.Light,
            letterSpacing = 2.sp
        )
    }
}
