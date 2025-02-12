import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabColorSchemeParams
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.toArgb
import androidx.compose.material3.MaterialTheme
import android.app.Activity
import androidx.compose.ui.platform.LocalView
import android.util.DisplayMetrics

class CustomTabsManager(private val context: Context) {
    fun openCustomTab(
        url: String,
        customWidth: Int? = null,
        customHeight: Int? = null,
        toolbarColor: Int? = null
    ) {
        var processedUrl = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            processedUrl = "https://$url"
        }

        val colorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(toolbarColor ?: MaterialTheme.colorScheme.primary.toArgb())
            .build()

        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(colorSchemeParams)
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .apply {
                customHeight?.let { setInitialActivityHeightPx(it) }
            }
            .build()

        try {
            customTabsIntent.launchUrl(context, Uri.parse(processedUrl))
            
            // If we have an Activity context, we can set the window size
            if (context is Activity && customWidth != null && customHeight != null) {
                context.window?.let { window ->
                    window.setLayout(customWidth, customHeight)
                }
            }
        } catch (e: ActivityNotFoundException) {
            // Fallback to regular browser
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(processedUrl))
            context.startActivity(browserIntent)
        }
    }
}

@Composable
fun CustomTabButton(
    url: String,
    text: String = "Open Link",
    useCustomSize: Boolean = true
) {
    val context = LocalContext.current
    val customTabsManager = remember { CustomTabsManager(context) }
    
    // Get screen dimensions
    val displayMetrics = remember { DisplayMetrics() }
    val activity = context as? Activity
    activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    
    // Calculate dimensions (80% of screen size)
    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels
    val customWidth = (screenWidth * 0.8).toInt()
    val customHeight = (screenHeight * 0.8).toInt()

    Button(
        onClick = {
            if (useCustomSize) {
                customTabsManager.openCustomTab(
                    url = url,
                    customWidth = customWidth,
                    customHeight = customHeight,
                    toolbarColor = MaterialTheme.colorScheme.primary.toArgb()
                )
            } else {
                customTabsManager.openCustomTab(url = url)
            }
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text)
    }
}

// Example usage in a Composable screen
@Composable
fun MyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Default usage with 80% screen size
        CustomTabButton(
            url = "www.google.com",
            text = "Open Google"
        )
        
        // Custom dimensions example
        Button(
            onClick = {
                val context = LocalContext.current
                val customTabsManager = CustomTabsManager(context)
                customTabsManager.openCustomTab(
                    url = "www.google.com",
                    customWidth = 800,
                    customHeight = 1000
                )
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Open with Custom Size")
        }
    }
}
