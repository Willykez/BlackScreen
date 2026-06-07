package com.willkez.darker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val overlayPermLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    private val notifPermLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlackScreenTheme {
                var canDrawOverlays by remember { mutableStateOf(Settings.canDrawOverlays(this)) }
                var serviceRunning by remember { mutableStateOf(OverlayService.isRunning) }

                LaunchedEffect(Unit) {
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.RESUMED) {
                            canDrawOverlays = Settings.canDrawOverlays(this@MainActivity)
                            serviceRunning = OverlayService.isRunning
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0A0A0A)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Black Screen",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "AMOLED power saver overlay",
                            fontSize = 14.sp,
                            color = Color(0xFF888888)
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        PermissionRow(
                            label = "Draw over other apps",
                            granted = canDrawOverlays,
                            onRequest = {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:$packageName")
                                )
                                overlayPermLauncher.launch(intent)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val notifGranted = remember {
                                ActivityCompat.checkSelfPermission(
                                    this@MainActivity,
                                    android.Manifest.permission.POST_NOTIFICATIONS
                                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                            }
                            PermissionRow(
                                label = "Post notifications",
                                granted = notifGranted,
                                onRequest = {
                                    notifPermLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                if (!canDrawOverlays) return@Button
                                if (serviceRunning) {
                                    stopService(Intent(this@MainActivity, OverlayService::class.java))
                                    serviceRunning = false
                                } else {
                                    startForegroundService(Intent(this@MainActivity, OverlayService::class.java))
                                    serviceRunning = true
                                }
                            },
                            enabled = canDrawOverlays,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (serviceRunning) Color(0xFF333333) else Color.White,
                                contentColor = if (serviceRunning) Color.White else Color.Black,
                                disabledContainerColor = Color(0xFF222222),
                                disabledContentColor = Color(0xFF555555)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                text = if (serviceRunning) "Stop Overlay Service" else "Start Overlay Service",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }

                        if (!canDrawOverlays) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Grant overlay permission to enable service",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setContent {
            BlackScreenTheme {
                var canDrawOverlays by remember { mutableStateOf(Settings.canDrawOverlays(this)) }
                var serviceRunning by remember { mutableStateOf(OverlayService.isRunning) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0A0A0A)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Black Screen",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "AMOLED power saver overlay",
                            fontSize = 14.sp,
                            color = Color(0xFF888888)
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        PermissionRow(
                            label = "Draw over other apps",
                            granted = canDrawOverlays,
                            onRequest = {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:$packageName")
                                )
                                overlayPermLauncher.launch(intent)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val notifGranted = ActivityCompat.checkSelfPermission(
                                this@MainActivity,
                                android.Manifest.permission.POST_NOTIFICATIONS
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                            PermissionRow(
                                label = "Post notifications",
                                granted = notifGranted,
                                onRequest = {
                                    notifPermLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                if (!canDrawOverlays) return@Button
                                if (serviceRunning) {
                                    stopService(Intent(this@MainActivity, OverlayService::class.java))
                                    serviceRunning = false
                                } else {
                                    startForegroundService(Intent(this@MainActivity, OverlayService::class.java))
                                    serviceRunning = true
                                }
                            },
                            enabled = canDrawOverlays,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (serviceRunning) Color(0xFF333333) else Color.White,
                                contentColor = if (serviceRunning) Color.White else Color.Black,
                                disabledContainerColor = Color(0xFF222222),
                                disabledContentColor = Color(0xFF555555)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                text = if (serviceRunning) "Stop Overlay Service" else "Start Overlay Service",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
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
            Text(text = label, color = Color.White, fontSize = 15.sp)
            Text(
                text = if (granted) "Granted" else "Not granted",
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
