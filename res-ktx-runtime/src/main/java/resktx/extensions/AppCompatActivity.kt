@file:JvmName("ResKtx_AppCompatActivity")
@file:Suppress("PackageDirectoryMismatch")

package resktx

import android.view.View
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.setContentView(layoutRes: LayoutRes) = setContentView(layoutRes.value)

fun AppCompatActivity.setTheme(styleRes: StyleRes) = setTheme(styleRes.value)

fun <T: View> AppCompatActivity.findViewById(idRes: IdRes): T = findViewById(idRes.value)
