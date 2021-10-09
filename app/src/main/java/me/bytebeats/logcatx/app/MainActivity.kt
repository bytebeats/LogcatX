package me.bytebeats.logcatx.app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import me.bytebeats.logcatx.lazyFind

class MainActivity : AppCompatActivity() {
    private val mWebView by lazyFind<WebView>(R.id.web_view)
    private val mProgressBar by lazyFind<ProgressBar>(R.id.progress_horizontal)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                supportActionBar?.title = title
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                mProgressBar.progress = newProgress
                mProgressBar.visibility = if (newProgress != 100) View.VISIBLE else View.GONE
            }
        }
        mWebView.webViewClient = WebViewClient()
        mWebView.loadUrl("https://github.com/bytebeats/LogcatX")
        findViewById<View>(resources.getIdentifier("action_bar_title", "id", "android"))?.setOnClickListener {
            val url = mWebView.url
            if (!url.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        mWebView.onResume()
                        mWebView.resumeTimers()
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        mWebView.onPause()
                        mWebView.pauseTimers()
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        mWebView.clearHistory()
                        mWebView.stopLoading()
                        mWebView.loadUrl("about:blank")
                        mWebView.webChromeClient = null
                        mWebView.removeAllViews()
                        mWebView.destroy()
                        (mWebView.parent as ViewGroup).removeView(mWebView)
                    }
                }
            }
        })
    }
}