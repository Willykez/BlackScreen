package com.willkez.darker

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {

    private var canDrawOverlays by mutableStateOf(false)
    private var notifGranted by mutableStateOf(false)
    private var serviceRunning by mutableStateOf(false)

    private val overlayPermLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { refreshState() }

    private val notifPermLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { refreshState() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refreshState()
        setContent {
            BlackScreenTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF0A0A0A)) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Black Screen", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.height(8.dp))
                        Text("AMOLED power saver overlay", fontSize = 14.sp, color = Color(0xFF888888))
                        Spacer(Modifier.height(48.dp))

                        PermissionRow(
                            label = "Draw over other apps",
                            granted = canDrawOverlays,
                            onRequest = {
                                overlayPermLauncher.launch(
                                    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:$packageName"))
                                )
                            }
                        )

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Spacer(Modifier.height(16.dp))
                            PermissionRow(
                                label = "Post notifications",
                                granted = notifGranted,
                                onRequest = {
                                    notifPermLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                }
                            )
                        }

                        Spacer(Modifier.height(40.dp))

                        Button(
                            onClick = {
                                if (serviceRunning) {
                                    stopService(Intent(this@MainActivity, OverlayService::class.java))
                                } else {
                                    startForegroundService(Intent(this@MainActivity, OverlayService::class.java))
                                }
                                serviceRunning = !serviceRunning
                            },
                            enabled = canDrawOverlays,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (serviceRunning) Color(0xFF2A2A2A) else Color.White,
                                contentColor = if (serviceRunning) Color.White else Color.Black,
                                disabledContainerColor = Color(0xFF1A1A1A),
                                disabledContentColor = Color(0xFF444444)
                            ),
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Text(
                                text = if (serviceRunning) "Stop Overlay Service" else "Start Overlay Service",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }

                        if (!canDrawOverlays) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "Grant overlay permission to enable service",
                                fontSize = 12.sp,
                                color = Color(0xFF555555)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshState()
    }

    private fun refreshState() {
        canDrawOverlays = Settings.canDrawOverlays(this)
        serviceRunning = OverlayService.isRunning
        notifGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
        } else true
    }
}

@Composable
private fun PermissionRow(label: String, granted: Boolean, onRequest: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(label, color = Color.White, fontSize = 15.sp)
            Text(
                if (granted) "Granted" else "Not granted",
                color = if (granted) Color(0xFF4CAF50) else Color(0xFFFF5252),
                fontSize = 12.sp
            )
        }
        if (!granted) {
            TextButton(onClick = onRequest) {
                Text("Grant", color = Color.White)
            }
        }
    }
}

@Composable
fun BlackScreenTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color(0xFF0A0A0A),
            surface = Color(0xFF111111),
            primary = Color.White,
            onPrimary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        ),
        content = content
    )
}
