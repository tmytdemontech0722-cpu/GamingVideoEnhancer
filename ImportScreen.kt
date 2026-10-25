package com.example.viralgamingenhancer.ui.importvideo

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.viralgamingenhancer.media.VideoInfo
import com.example.viralgamingenhancer.media.VideoMetadataReader
import com.example.viralgamingenhancer.media.VideoPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImportScreen(uri: Uri, onBack: () -> Unit) {
    val context = LocalContext.current
    var info by remember(uri) { mutableStateOf<VideoInfo?>(null) }
    var error by remember(uri) { mutableStateOf<String?>(null) }

    LaunchedEffect(uri) {
        val result = withContext(Dispatchers.IO) { VideoMetadataReader.read(context, uri) }
        result
            .onSuccess { info = it }
            .onFailure { error = it.message ?: "Could not read this video." }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextButton(onClick = onBack) { Text("← Back") }
        Text("Imported video", style = MaterialTheme.typography.headlineSmall)

        if (error == null) {
            VideoPreview(
                uri = uri,
                modifier = Modifier.fillMaxWidth().height(380.dp),
                onError = { error = it },
            )
        }

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Text("Try an MP4 (H.264 or HEVC) recorded on this phone.")
        }

        info?.let { v ->
            Text("Name: ${v.name}")
            Text("Duration: ${formatDuration(v.durationMs)}")
            Text("Resolution: ${v.width} × ${v.height}")
            Text("Frame rate: ${v.frameRate?.let { "%.0f fps".format(it) } ?: "not reported by file"}")
            Text("Size: ${v.sizeBytes?.let { formatSize(it) } ?: "unknown"}")
            Text("Codec/MIME: ${v.mimeType}")
        }
    }
}

private fun formatDuration(ms: Long): String {
    val total = ms / 1000
    return "%d:%02d".format(total / 60, total % 60)
}

private fun formatSize(bytes: Long): String =
    if (bytes >= 1_000_000) "%.1f MB".format(bytes / 1_000_000.0) else "%d KB".format(bytes / 1000)
