package org.larv9n.desktop

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.max

fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    title = "larv9n-webview-test",
  ) {
    var restartRequired by remember { mutableStateOf(false) }
    var downloading by remember { mutableStateOf(0F) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
      withContext(Dispatchers.IO) {
        KCEF.init(builder = {
          installDir(File("kcef-bundle"))
          progress {
            onDownloading {
              downloading = max(it, 0F)
            }
            onInitialized {
              initialized = true
            }
          }
          settings {
            cachePath = File("cache").absolutePath
          }
        }, onError = {
          if (it != null) {
            it.printStackTrace()
          }
        }, onRestartRequired = {
          restartRequired = true
        })
      }
    }

    if (restartRequired) {
      Text(text = "Restart required.")
    } else {
      if (initialized) {
        WebViewSample()
      } else {
        Text(text = "Downloading $downloading%")
      }
    }

    DisposableEffect(Unit) {
      onDispose {
        KCEF.disposeBlocking()
      }
    }
  }
}