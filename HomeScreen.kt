package com.example.viralgamingenhancer.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val presetNames = listOf(
    "Viral 120 FPS",
    "Free Fire MAX Pro",
    "Cinematic Teal & Orange",
    "Red Glow",
    "Clean Competitive",
    "Instagram Reels Viral",
)

@Composable
fun HomeScreen(onImport: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            "VIRAL GAMING ENHANCER",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text("Local, offline gameplay enhancement. Your videos never leave this phone.")

        Button(onClick = onImport, modifier = Modifier.fillMaxWidth()) {
            Text("Import Gameplay")
        }

        Text("Recent projects", style = MaterialTheme.typography.titleMedium)
        Text("No projects yet. Saved projects arrive in Milestone 2.")

        Text("Presets", style = MaterialTheme.typography.titleMedium)
        presetNames.forEach {
            Text("• $it  (applies in Milestone 3)", color = MaterialTheme.colorScheme.secondary)
        }

        Text("Status", style = MaterialTheme.typography.titleMedium)
        Text("Idle. Export is not available yet (Milestone 2).")
    }
}
