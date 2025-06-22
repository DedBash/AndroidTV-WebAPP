package dev.deads.webapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var editUrl: EditText
    private lateinit var saveUrlButton: Button
    private lateinit var infoButton: Button
    private lateinit var languageButton: Button
    private lateinit var closeMenuButton: Button
    private lateinit var menuContainer: LinearLayout
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var sslCheckBox: CheckBox

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "TV_WEB_BROWSER_PREFS"
    private val URL_KEY = "SAVED_URL"
    private val LANGUAGE_KEY = "SAVED_LANG"
    private val SSL_IGNORE_KEY = "SSL_IGNORE"
    private val IS_FIRST_RUN_KEY = "IS_FIRST_RUN"
    private val DEFAULT_URL = "https://www.google.com"

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lang = prefs.getString(LANGUAGE_KEY, Locale.getDefault().language) ?: Locale.getDefault().language
        super.attachBaseContext(LocaleHelper.onAttach(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        rootLayout = findViewById(R.id.constraint_layout_root)
        webView = findViewById(R.id.webView)
        menuContainer = findViewById(R.id.menu_container)
        editUrl = findViewById(R.id.edit_url)
        saveUrlButton = findViewById(R.id.button_save_url)
        infoButton = findViewById(R.id.button_info)
        languageButton = findViewById(R.id.button_language)
        sslCheckBox = findViewById(R.id.checkbox_ignore_ssl)
        closeMenuButton = findViewById(R.id.button_close_menu)

        if (sharedPreferences.getBoolean(IS_FIRST_RUN_KEY, true)) {
            showWelcomeDialog()
            sharedPreferences.edit().putBoolean(IS_FIRST_RUN_KEY, false).apply()
        }

        sslCheckBox.isChecked = sharedPreferences.getBoolean(SSL_IGNORE_KEY, false)

        val savedUrl = loadUrl()
        editUrl.setText(savedUrl)
        setupWebView(savedUrl)

        setupFocusChangeListener()
        setupOnBackPressed()

        webView.requestFocus()

        closeMenuButton.setOnClickListener {
            closeMenu()
            webView.requestFocus()
        }

        sslCheckBox.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(SSL_IGNORE_KEY, isChecked).apply()
            webView.reload()
        }

        saveUrlButton.setOnClickListener {
            val newUrl = editUrl.text.toString().trim()
            if (newUrl.isNotEmpty()) {
                saveUrl(newUrl)
                webView.loadUrl(newUrl)
            }
        }

        infoButton.setOnClickListener { showInfoDialog() }
        languageButton.setOnClickListener { showLanguageSelectionDialog() }
    }

    // HIER IST DIE WICHTIGSTE ÄNDERUNG
    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (menuContainer.visibility == View.VISIBLE) {
                    closeMenu()
                    webView.requestFocus()
                }
                else if (webView.canGoBack()) {
                    webView.goBack()
                }
                else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun showWelcomeDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.welcome_title))
            .setMessage(getString(R.string.welcome_message))
            .setPositiveButton(getString(R.string.dialog_ok), null)
            .show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(url: String) {
        webView.webViewClient = object : WebViewClient() {
            @SuppressLint("WebViewClientOnReceivedSslError")
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                if (sslCheckBox.isChecked) {
                    handler?.proceed()
                } else {
                    super.onReceivedSslError(view, handler, error)
                }
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("Deutsch", "English")
        val languageCodes = arrayOf("de", "en")
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_language_title))
            .setItems(languages) { _, which ->
                saveLanguage(languageCodes[which])
                recreate()
            }.create().show()
    }

    private fun showInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.info_dialog_title))
            .setMessage(getString(R.string.info_dialog_message))
            .setPositiveButton(getString(R.string.dialog_ok), null).show()
    }

    private fun setupFocusChangeListener() {
        webView.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) closeMenu() }
        editUrl.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) openMenu() }
    }

    private fun openMenu() {
        if (menuContainer.visibility == View.VISIBLE) return
        menuContainer.visibility = View.VISIBLE
        val cs = ConstraintSet()
        cs.clone(rootLayout)
        cs.connect(R.id.webView, ConstraintSet.END, R.id.menu_container, ConstraintSet.START)
        cs.applyTo(rootLayout)
    }

    private fun closeMenu() {
        if (menuContainer.visibility == View.GONE) return
        menuContainer.visibility = View.GONE
        val cs = ConstraintSet()
        cs.clone(rootLayout)
        cs.connect(R.id.webView, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        cs.applyTo(rootLayout)
    }

    private fun saveUrl(url: String) {
        sharedPreferences.edit().putString(URL_KEY, url).apply()
    }

    private fun saveLanguage(langCode: String) {
        sharedPreferences.edit().putString(LANGUAGE_KEY, langCode).apply()
    }

    private fun loadUrl(): String {
        return sharedPreferences.getString(URL_KEY, DEFAULT_URL) ?: DEFAULT_URL
    }
}
