package com.rk.calculatorvault

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

/**
 * Small optional home launcher. App filtering is applied only here; Android's
 * stock launcher cannot be controlled by a regular third-party application.
 */
class LauncherActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("rk_vault_secure", MODE_PRIVATE) }
    private val bgColor = Color.rgb(243, 246, 252)
    private val inkColor = Color.rgb(31, 40, 57)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bgColor
        window.navigationBarColor = bgColor
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }

    override fun onResume() {
        super.onResume()
        renderHome()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun rounded(color: Int, radius: Int): GradientDrawable = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radius).toFloat()
    }

    private fun makeText(text: String, size: Float, bold: Boolean = false): TextView = TextView(this).apply {
        this.text = text
        textSize = size
        setTextColor(inkColor)
        if (bold) setTypeface(typeface, Typeface.BOLD)
        gravity = Gravity.CENTER_VERTICAL
        maxLines = 2
        ellipsize = android.text.TextUtils.TruncateAt.END
    }

    private fun renderHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bgColor)
            setPadding(dp(12), dp(12), dp(12), dp(8))
        }
        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val titles = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        titles.addView(makeText("RK Home", 22f, true))
        titles.addView(makeText("Beranda ringan", 12f).apply { setTextColor(Color.rgb(112, 124, 145)) })
        top.addView(titles, LinearLayout.LayoutParams(0, -2, 1f))
        val settings = Button(this).apply {
            text = "Ganti Beranda"
            textSize = 12f
            isAllCaps = false
            setTextColor(Color.WHITE)
            background = rounded(inkColor, 12)
            stateListAnimator = null
            setOnClickListener {
                try { startActivity(Intent(android.provider.Settings.ACTION_HOME_SETTINGS)) }
                catch (_: Exception) {
                    try { startActivity(Intent(android.provider.Settings.ACTION_SETTINGS)) }
                    catch (_: Exception) { Toast.makeText(this@LauncherActivity, "Setelan tidak bisa dibuka.", Toast.LENGTH_SHORT).show() }
                }
            }
        }
        top.addView(settings, LinearLayout.LayoutParams(-2, dp(40)))
        root.addView(top)
        root.addView(makeText("Aplikasi yang disembunyikan tidak ditampilkan di sini.", 12f).apply {
            setTextColor(Color.rgb(112, 124, 145))
            setPadding(0, dp(6), 0, dp(10))
        })

        val scroll = ScrollView(this)
        val grid = GridLayout(this).apply {
            columnCount = 3
            setPadding(dp(1), dp(1), dp(1), dp(8))
        }
        val hidden = prefs.getStringSet("hidden_launcher_packages", emptySet()) ?: emptySet()
        val query = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolved = packageManager.queryIntentActivities(query, 0)
            .distinctBy { it.activityInfo?.packageName }
            .filter { info -> info.activityInfo?.packageName?.let { it !in hidden } == true }
            .sortedBy { it.loadLabel(packageManager).toString().lowercase(Locale.getDefault()) }

        resolved.forEach { info -> grid.addView(makeAppTile(info)) }
        if (resolved.isEmpty()) {
            grid.addView(makeText("Belum ada aplikasi yang bisa ditampilkan.", 14f).apply {
                setPadding(dp(12), dp(18), dp(12), dp(18))
            })
        }
        scroll.addView(grid)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val bottom = makeText("Untuk kembali ke launcher lama: Setelan Android → Aplikasi default → Aplikasi Beranda.", 10f).apply {
            setTextColor(Color.rgb(112, 124, 145))
            setPadding(dp(2), dp(8), dp(2), dp(2))
        }
        root.addView(bottom)
        setContentView(root)
    }

    private fun makeAppTile(info: ResolveInfo): View {
        val activityInfo = info.activityInfo
        val tile = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(5), dp(8), dp(5), dp(6))
            background = rounded(Color.WHITE, 16)
            contentDescription = info.loadLabel(packageManager)?.toString()
            setOnClickListener { launchResolvedApp(info) }
        }
        val icon = ImageView(this).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
            try { setImageDrawable(info.loadIcon(packageManager)) } catch (_: Exception) { }
        }
        tile.addView(icon, LinearLayout.LayoutParams(dp(38), dp(38)).apply { bottomMargin = dp(5) })
        tile.addView(makeText(info.loadLabel(packageManager)?.toString() ?: activityInfo.packageName, 11f).apply {
            gravity = Gravity.CENTER
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            maxLines = 2
        }, LinearLayout.LayoutParams(-1, dp(32)))
        val params = GridLayout.LayoutParams().apply {
            width = 0
            height = dp(92)
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            setMargins(dp(3), dp(3), dp(3), dp(3))
        }
        tile.layoutParams = params
        return tile
    }

    private fun launchResolvedApp(info: ResolveInfo) {
        val target = info.activityInfo ?: return
        val intent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .setClassName(target.packageName, target.name)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(this, "Aplikasi tidak bisa dibuka.", Toast.LENGTH_SHORT).show()
        }
    }
}
