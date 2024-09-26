package org.larv9n.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.cookie.Cookie
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.coroutines.flow.filter


@Composable
@Preview
fun App() {
  val initialUrl = "http://www.aftonbladet.se"
  val state = rememberWebViewState(url = initialUrl)
  DisposableEffect(Unit) {
    state.webSettings.apply {
      logSeverity = KLogSeverity.Debug
      customUserAgentString =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1) AppleWebKit/625.20 (KHTML, like Gecko) Version/14.3.43 Safari/625.20"
    }

    onDispose { }
  }
  val navigator = rememberWebViewNavigator()
  var textFieldValue by remember(state.lastLoadedUrl) {
    mutableStateOf(state.lastLoadedUrl)
  }
  MaterialTheme {
    Column {
      TopAppBar(
        title = { Text(text = "WebView Sample") },
        navigationIcon = {
          IconButton(onClick = {
            if (navigator.canGoBack) {
              navigator.navigateBack()
            }
          }) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
            )
          }
        },
      )

      Row {
        Box(modifier = Modifier.weight(1f)) {
          if (state.errorsForCurrentRequest.isNotEmpty()) {
            Image(
              imageVector = Icons.Default.Close,
              contentDescription = "Error",
              colorFilter = ColorFilter.tint(Color.Red),
              modifier =
                Modifier
                  .align(Alignment.CenterEnd)
                  .padding(8.dp),
            )
          }

          OutlinedTextField(
            value = textFieldValue ?: "",
            onValueChange = { textFieldValue = it },
            modifier = Modifier.fillMaxWidth(),
          )
        }

        Button(
          onClick = {
            textFieldValue?.let {
              navigator.loadUrl(it)
            }
          },
          modifier = Modifier.align(Alignment.CenterVertically),
        ) {
          Text("Go")
        }
      }

      val loadingState = state.loadingState
      if (loadingState is LoadingState.Loading) {
        LinearProgressIndicator(
          progress = loadingState.progress,
          modifier = Modifier.fillMaxWidth(),
        )
      }

      WebView(
        state = state,
        modifier =
          Modifier
            .fillMaxSize(),
        navigator = navigator,
      )
    }
  }


}

@Composable
fun WebViewSample() {
  MaterialTheme {
    val webViewState =
      rememberWebViewState("http://www.aftonbladet.se")
    Column(Modifier.fillMaxSize()) {
      val text = webViewState.let {
        "${it.pageTitle ?: ""} ${it.loadingState} ${it.lastLoadedUrl ?: ""}"
      }
      Text(text)
      WebView(
        state = webViewState,
        modifier = Modifier.fillMaxSize(),
      )
    }

  }
}