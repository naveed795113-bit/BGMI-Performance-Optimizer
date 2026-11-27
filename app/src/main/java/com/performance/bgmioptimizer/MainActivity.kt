package com.performance.bgmioptimizer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF101522)
private val Panel = Color(0xFF1B2638)
private val Cyan = Color(0xFF54D6C8)
private val Muted = Color(0xFFAAB6C8)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Cyan, background = Ink, surface = Panel,
                onPrimary = Ink, onBackground = Color.White, onSurface = Color.White
            )) {
                OptimizerApp()
            }
        }
    }
}

@Composable
private fun OptimizerApp() {
    var page by remember { mutableStateOf("Home") }

    Scaffold(
        containerColor = Ink,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("PERFORMANCE LAB", fontSize = 11.sp, color = Cyan, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        Text("BGMI Optimizer", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Ink),
                actions = {
                    IconButton(onClick = { page = "Device" }) {
                        Icon(Icons.Default.Memory, contentDescription = "Device status", tint = Cyan)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Panel, contentColor = Cyan) {
                listOf("Home", "Graphics", "Scopes", "Device").forEach { item ->
                    NavigationBarItem(
                        selected = page == item,
                        onClick = { page = item },
                        icon = {
                            Icon(
                                when (item) {
                                    "Graphics" -> Icons.Default.Tune
                                    "Scopes" -> Icons.Default.CenterFocusStrong
                                    "Device" -> Icons.Default.Memory
                                    else -> Icons.Default.Dashboard
                                }, contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Cyan, selectedTextColor = Cyan, indicatorColor = Cyan.copy(alpha = .14f), unselectedTextColor = Muted)
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (page) {
                "Home" -> {
                    item {
                        Box(
                            Modifier.fillMaxWidth().background(
                                Brush.linearGradient(listOf(Color(0xFF214B55), Color(0xFF202B42), Panel)),
                                RoundedCornerShape(24.dp)
                            ).padding(20.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("DEVICE PROFILE", color = Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                                Text("OnePlus 9", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
                                Text("Snapdragon 888 • Adreno 660 • 120 Hz display", color = Muted, fontSize = 13.sp)
                                Text("Profile selected for your device", color = Color(0xFFB7F3E8), fontSize = 12.sp)
                            }
                        }
                    }
                    item { SectionTitle("Optimisation modules", "Practical settings guidance — no game-file changes") }
                    item { ModuleCard("Graphics & FPS", "Choose stable in-game graphics targets", Icons.Default.Tune) { page = "Graphics" } }
                    item { ModuleCard("Controls protection", "This app never edits BGMI sensitivity or controls", Icons.Default.Lock) { page = "Scopes" } }
                    item { ModuleCard("Device performance", "View hardware and thermal guidance", Icons.Default.Memory) { page = "Device" } }
                    item {
                        Text("Safety note: this companion app does not control BGMI, remove recoil, add aim assist, unlock unsupported FPS modes, or bypass anti-cheat.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
                    }
                }
                "Graphics" -> {
                    item { SectionTitle("Graphics & frame-rate guide", "Use only options shown inside BGMI settings") }
                    item { InfoCard("Starting point", "Begin with Smooth graphics and the highest frame-rate option BGMI officially offers on this device. Test in Training Grounds for 10–15 minutes.") }
                    item { InfoCard("120 Hz display", "Your OnePlus 9 screen supports up to 120 Hz. This does not guarantee BGMI will render at 120 FPS; the game controls its supported frame-rate profiles.") }
                    item { InfoCard("Stability first", "If frame pacing drops or the phone heats up, lower graphics one step, close background apps, remove the case during long sessions, and allow the phone to cool.") }
                    item { InfoCard("Avoid risky tools", "Do not use GFX config editors, modified game files, injectors, or third-party unlockers. They can cause instability or account penalties.") }
                }
                "Scopes" -> {
                    item { SectionTitle("Controls protection", "Your BGMI settings remain exactly as you set them") }
                    item { InfoCard("No sensitivity changes", "This app will not change camera sensitivity, ADS sensitivity, gyroscope sensitivity, button layout, control size, or any other BGMI control setting.") }
                    item { InfoCard("No game modification", "The app does not access BGMI private data, edit game files, inject code, or automate in-game actions. You remain in full control of every setting.") }
                }
                "Device" -> {
                    item { SectionTitle("Device status", "Detected locally from Android system information") }
                    item { InfoCard("Model", Build.MODEL ?: "Not reported") }
                    item { InfoCard("Manufacturer", Build.MANUFACTURER ?: "Not reported") }
                    item { InfoCard("Android version", "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})") }
                    item { InfoCard("Supported CPU ABIs", Build.SUPPORTED_ABIS.joinToString()) }
                    item { InfoCard("Display target", "OnePlus 9 panel supports up to 120 Hz. Actual refresh rate and BGMI FPS may vary with system mode, game version, temperature, and battery state.") }
                    item { InfoCard("Thermal care", "Avoid charging during long competitive sessions when possible. Reduce brightness if hot, keep vents unobstructed, and stop if the device becomes uncomfortably hot.") }
                }
                else -> {
                    item { SectionTitle("Optimisation", "Performance guidance only") }
                    item { InfoCard("Controls untouched", "Your BGMI sensitivity, gyroscope and button layout are never changed by this app.") }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun ModuleCard(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(onClick = onClick, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Panel), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Cyan, modifier = Modifier.size(28.dp))
            Column(Modifier.weight(1f).padding(start = 14.dp)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(description, color = Muted, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Muted)
        }
    }
}

@Composable
private fun InfoCard(title: String, body: String) {
    CardBlock {
        Text(title, color = Cyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(body, color = Color.White, fontSize = 13.sp, lineHeight = 19.sp)
    }
}

@Composable
private fun CardBlock(content: @Composable ColumnScope.() -> Unit) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Panel), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp), content = content)
    }
}
