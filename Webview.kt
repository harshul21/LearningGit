import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.view.ViewGroup.LayoutParams
import androidx.activity.compose.BackHandler

@Composable
fun InAppUrlViewer(url: String, onClose: () -> Unit) {
    var isLoading by remember { mutableStateOf(true) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // WebView
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT
                    )
                    
                    // Configure WebView settings
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        mediaPlaybackRequiresUserGesture = false
                        builtInZoomControls = true
                        displayZoomControls = false
                        // Handle video content
                        settings.mediaPlaybackRequiresUserGesture = false
                    }
                    
                    // Prevent opening in external apps
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            url: String
                        ): Boolean {
                            view.loadUrl(url)
                            return true
                        }
                        
                        override fun onPageFinished(view: WebView, url: String) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                    }
                    
                    // Handle fullscreen video
                    webChromeClient = WebChromeClient()
                    
                    // Load the URL
                    loadUrl(url)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Close button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        
        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
    }
}

@Composable
fun MainScreen() {
    var showWebView by remember { mutableStateOf(false) }
    
    // Handle back button
    BackHandler(enabled = showWebView) {
        showWebView = false
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Your main content
        Button(
            onClick = { showWebView = true },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Text("Open URL")
        }
        
        // Overlay WebView when active
        if (showWebView) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                InAppUrlViewer(
                    url = "https://example.com",  // Replace with your URL
                    onClose = { showWebView = false }
                )
            }
        }
    }
}
