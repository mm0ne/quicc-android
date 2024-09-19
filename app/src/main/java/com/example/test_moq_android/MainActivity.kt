package com.example.test_moq_android

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.test_moq_android.ui.theme.ExoPlayerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExoPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExoPlayerView()
                }
            }
        }
    }
}

/**
 * Composable function that displays an ExoPlayer to play a video using Jetpack Compose.
 *
 * @OptIn annotation to UnstableApi is used to indicate that the API is still experimental and may
 * undergo changes in the future.
 *
 * @see EXAMPLE_VIDEO_URI Replace with the actual URI of the video to be played.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerView() {
    // Get the current context
    val logTag = "ExoplayerView"
    val context = LocalContext.current
    val quicServerUrl = "https://moq.rorre.me:8443"
    // Initialize ExoPlayer

    val exoPlayer = ExoPlayer.Builder(context).build()




    val lifecycleScope = (LocalContext.current as ComponentActivity).lifecycleScope
    lifecycleScope.launch(Dispatchers.IO) {
        try {
            val quicClient = QuicClient(quicServerUrl)
            Log.d(logTag, "Reading Input Stream")
            handleIncomingStream(quicClient)
        } catch (e: Exception) {
            Log.e(logTag, "Error in QUIC connection: ${e.message}")
        }
    }



    // Create a MediaSource
    val mediaSource = remember(EXAMPLE_VIDEO_URI) {
        MediaItem.fromUri(EXAMPLE_VIDEO_URI)
    }

    // Set MediaSource to ExoPlayer
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Set your desired height
    )
}


const val EXAMPLE_VIDEO_URI = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExoPlayerTheme {
        ExoPlayerView()
    }
}

suspend fun handleIncomingStream(quic : QuicClient) {

    val input = quic.getInputStream()
    while (true) {

        val bufferSize = input.available()
        if (bufferSize > 0) {

            Log.d("[Input Stream]", "Received " + bufferSize + " bytes")
            val buffer = input.readNBytes(bufferSize)

            Log.d("[Input Stream]", buffer.toString())
        }

    }
}