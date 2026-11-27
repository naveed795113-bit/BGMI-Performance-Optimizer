package com.performance.bgmioptimizer

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

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

@OptIn(ExperimentalMaterial3Api::class)
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
                listOf("Home", "Boost", "Graphics", "Device").forEach { item ->
                    NavigationBarItem(
                        selected = page == item,
                        onClick = { page = item },
                        icon = {
                            Icon(
                                when (item) {
                                    "Graphics" -> Icons.Default.Tune
                                    "Boost" -> Icons.Default.Bolt
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
                    item { ModuleCard("Gaming session", "Prepare the phone and launch BGMI", Icons.Default.Bolt) { page = "Boost" } }
                    item { ModuleCard("Graphics & FPS", "Review supported in-game settings", Icons.Default.Tune) { page = "Graphics" } }
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
                "Boost" -> {
                    item { SectionTitle("Gaming session", "Device-side preparation for BGMI on OnePlus 9") }
                    item { GamingSessionPanel() }
                    item { InfoCard("What this can do", "Launch BGMI, optionally silence interruptions with Android Do Not Disturb, and open the phone's own display and battery settings. These are device-side actions, not game modifications.") }
                    item { InfoCard("What it cannot do", "A normal app cannot force BGMI to render at 120 FPS, unlock restricted graphics, control the game's frame pacing, or guarantee lag-free hot drops. Actual performance depends on BGMI support, temperature, network and server load.") }
                    item { InfoCard("Controls stay untouched", "Camera sensitivity, ADS, gyroscope, button layout and control size are never read or changed.") }
                }
                "Device" -> {
                    item { SectionTitle("Live device status", "Read-only Android telemetry; no BGMI settings are changed") }
                    item { LiveDeviceMetrics() }
                    item { InfoCard("Model", Build.MODEL ?: "Not reported") }
                    item { InfoCard("Manufacturer", Build.MANUFACTURER ?: "Not reported") }
                    item { InfoCard("Android version", "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})") }
                    item { InfoCard("Supported CPU ABIs", Build.SUPPORTED_ABIS.joinToString()) }
                    item { InfoCard("Display refresh rate", "The live reading above is the Android display refresh rate, not BGMI's rendered FPS. Your OnePlus 9 panel supports up to 120 Hz.") }
                    item { InfoCard("Thermal care", "If Android reports a severe thermal state, pause the session and let the phone cool. Avoid charging during long competitive sessions when possible.") }
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
private fun GamingSessionPanel() {
    val context = LocalContext.current
    val nm = remember { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    var dndGranted by remember { mutableStateOf(nm.isNotificationPolicyAccessGranted) }
    var dndActive by remember { mutableStateOf(nm.currentInterruptionFilter == NotificationManager.INTERRUPTION_FILTER_NONE) }
    var status by remember { mutableStateOf("Ready to prepare your session.") }

    LaunchedEffect(Unit) {
        while (true) {
            dndGranted = nm.isNotificationPolicyAccessGranted
            dndActive = nm.currentInterruptionFilter == NotificationManager.INTERRUPTION_FILTER_NONE
            delay(1000)
        }
    }

    fun launchBgmi() {
        val intent = context.packageManager.getLaunchIntentForPackage("com.pubg.imobile")
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            status = "BGMI launch requested."
        } else {
            status = "BGMI was not found. Install the official BGMI app first."
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CardBlock {
            Text("BGMI launch", color = Cyan, fontWeight = FontWeight.Bold)
            Text("Open the installed official BGMI app. No game data is modified.", color = Muted, fontSize = 12.sp)
            Button(onClick = { launchBgmi() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.SportsEsports, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Launch BGMI")
            }
        }
        CardBlock {
            Text("Gaming Do Not Disturb", color = Cyan, fontWeight = FontWeight.Bold)
            Text("Optional. Silences interruptions only after you grant Android notification-policy access. It does not increase FPS.", color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
            Text(if (dndActive) "Status: Do Not Disturb is active" else "Status: Do Not Disturb is off", color = Color.White, fontWeight = FontWeight.SemiBold)
            Button(onClick = {
                dndGranted = nm.isNotificationPolicyAccessGranted
                if (!dndGranted) {
                    context.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    status = "Grant access in Android settings, then return to this page."
                } else if (!dndActive) {
                    val prefs = context.getSharedPreferences("gaming_session", Context.MODE_PRIVATE)
                    prefs.edit().putInt("previous_filter", nm.currentInterruptionFilter).putBoolean("saved_filter", true).apply()
                    nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
                    dndActive = true
                    status = "Do Not Disturb enabled. Turn it off here after gaming."
                } else {
                    val prefs = context.getSharedPreferences("gaming_session", Context.MODE_PRIVATE)
                    val previous = if (prefs.getBoolean("saved_filter", false)) prefs.getInt("previous_filter", NotificationManager.INTERRUPTION_FILTER_ALL) else NotificationManager.INTERRUPTION_FILTER_ALL
                    nm.setInterruptionFilter(previous)
                    prefs.edit().putBoolean("saved_filter", false).apply()
                    dndActive = false
                    status = "Previous interruption mode restored."
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(if (!dndGranted) "Grant DND access" else if (dndActive) "Turn DND off" else "Turn DND on")
            }
            Text("DND may silence calls and notifications. Check your exceptions before enabling it.", color = Muted, fontSize = 11.sp)
        }
        CardBlock {
            Text("Phone settings", color = Cyan, fontWeight = FontWeight.Bold)
            Text("Use the manufacturer's supported settings; this app does not force hidden performance modes.", color = Muted, fontSize = 12.sp)
            OutlinedButton(onClick = { context.startActivity(Intent(Settings.ACTION_DISPLAY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }, modifier = Modifier.fillMaxWidth()) {
                Text("Open display settings")
            }
            OutlinedButton(onClick = { context.startActivity(Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }, modifier = Modifier.fillMaxWidth()) {
                Text("Open battery settings")
            }
        }
        Text(status, color = Cyan, fontSize = 12.sp)
    }
}

private data class DeviceMetrics(val availableRamMb: Long = 0, val totalRamMb: Long = 0, val batteryTempC: Float? = null, val thermalState: String = "Checking…")

@Composable
private fun LiveDeviceMetrics() {
    val context = LocalContext.current
    val view = LocalView.current
    val refreshRate = view.display?.refreshRate
    val metrics by produceState(initialValue = DeviceMetrics(), context) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        while (true) {
            val memory = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memory)
            val battery = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val tempRaw = battery?.getIntExtra("temperature", -1) ?: -1
            val thermal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                when (powerManager.currentThermalStatus) {
                    PowerManager.THERMAL_STATUS_NONE -> "Normal"
                    PowerManager.THERMAL_STATUS_LIGHT -> "Light"
                    PowerManager.THERMAL_STATUS_MODERATE -> "Moderate"
                    PowerManager.THERMAL_STATUS_SEVERE -> "Severe"
                    PowerManager.THERMAL_STATUS_CRITICAL -> "Critical"
                    PowerManager.THERMAL_STATUS_EMERGENCY -> "Emergency"
                    PowerManager.THERMAL_STATUS_SHUTDOWN -> "Shutdown risk"
                    else -> "Unknown"
                }
            } else "Not exposed by this Android version"
            value = DeviceMetrics(memory.availMem / (1024L * 1024L), memory.totalMem / (1024L * 1024L), if (tempRaw >= 0) tempRaw / 10f else null, thermal)
            delay(3000)
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("Display", refreshRate?.let { "%.0f Hz".format(it) } ?: "Unavailable", Modifier.weight(1f))
            MetricCard("Thermal state", metrics.thermalState, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("Available RAM", if (metrics.totalRamMb > 0) "${metrics.availableRamMb} MB" else "Reading…", Modifier.weight(1f))
            MetricCard("Battery temperature", metrics.batteryTempC?.let { "%.1f °C".format(it) } ?: "Unavailable", Modifier.weight(1f))
        }
        Text("Readings refresh automatically. Display Hz is not the same as game FPS; Android may limit or withhold some thermal readings.", color = Muted, fontSize = 11.sp, lineHeight = 16.sp)
    }
}

@Composable
private fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Panel)) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, color = Muted, fontSize = 11.sp)
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
