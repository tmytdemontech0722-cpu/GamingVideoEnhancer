
package com.example.viralgamingenhancer.media

import android.content.Context
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns

data class VideoInfo(
    val name: String,
    val durationMs: Long,
    val width: Int,
    val height: Int,
    val frameRate: Float?,
    val sizeBytes: Long?,
    val mimeType: String,
)

object VideoMetadataReader {

    fun read(context: Context, uri: Uri): Result<VideoInfo> = runCatching {
        var name = "video"
        var size: Long? = null

        context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
            null,
            null,
            null,
        )?.use { c ->
            if (c.moveToFirst()) {
                val n = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (n >= 0 && !c.isNull(n)) name = c.getString(n)

                val s = c.getColumnIndex(OpenableColumns.SIZE)
                if (s >= 0 && !c.isNull(s)) size = c.getLong(s)
            }
        }

        var width = 0
        var height = 0
        var fps: Float? = null
        var mime: String? = null
        var durationUs = 0L

        val extractor = MediaExtractor()

        try {
            extractor.setDataSource(context, uri, null)

            for (i in 0 until extractor.trackCount) {
                val f = extractor.getTrackFormat(i)
                val m = f.getString(MediaFormat.KEY_MIME) ?: continue

                if (m.startsWith("video/")) {
                    mime = m

                    if (f.containsKey(MediaFormat.KEY_WIDTH)) {
                        width = f.getInteger(MediaFormat.KEY_WIDTH)
                    }

                    if (f.containsKey(MediaFormat.KEY_HEIGHT)) {
                        height = f.getInteger(MediaFormat.KEY_HEIGHT)
                    }

                    if (f.containsKey(MediaFormat.KEY_DURATION)) {
                        durationUs = f.getLong(MediaFormat.KEY_DURATION)
                    }

                    if (f.containsKey(MediaFormat.KEY_FRAME_RATE)) {
                        fps = runCatching {
                            f.getInteger(MediaFormat.KEY_FRAME_RATE).toFloat()
                        }.recoverCatching {
                            f.getFloat(MediaFormat.KEY_FRAME_RATE)
                        }.getOrNull()
                    }

                    break
                }
            }
        } finally {
            extractor.release()
        }

        val videoMime = mime
            ?: error("No video track found. This file is not a video this phone can read.")

        var rotation = 0
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(context, uri)

            rotation = retriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION
            )?.toIntOrNull() ?: 0

            if (durationUs <= 0L) {
                durationUs = (
                    retriever.extractMetadata(
                        MediaMetadataRetriever.METADATA_KEY_DURATION
                    )?.toLongOrNull() ?: 0L
                ) * 1000L
            }
        } finally {
            retriever.release()
        }

        val swap = rotation == 90 || rotation == 270

        VideoInfo(
            name = name,
            durationMs = durationUs / 1000L,
            width = if (swap) height else width,
            height = if (swap) width else height,
            frameRate = fps,
            sizeBytes = size,
            mimeType = videoMime,
        )
    }
}
