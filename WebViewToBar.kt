package com.example.webviewapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.webviewapp.ui.theme.WebViewAppTheme
import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebViewAppTheme {
                WebBrowser(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebBrowser(modifier: Modifier) {
    var url by remember { mutableStateOf("https://www.youtube.com/watch?v=9tZ6j1wgOrg") }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var textState by remember { mutableStateOf(TextFieldValue(url)) }
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(title = { Text(text = "Web Browser") }, actions = {

                IconButton(onClick = { webView?.goBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Page")
                }
                IconButton(onClick = { webView?.goForward() }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Page")
                }
            })
        },
        content = { paddingValues ->
            Column(Modifier.padding(paddingValues)) {

                AndroidView(modifier = Modifier
                    .fillMaxSize()
                    , factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl(url)
                        webView = this
                    }
                }, update = {
                    webView?.loadUrl(url)
                })
            }
        })
}
