package org.larv9n.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "larv9n-webview-test",
    ) {
        App()
    }
}