package resktx.extensions.androidx.appcompat.app

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import resktx.IdRes
import resktx.LayoutRes
import resktx.StyleRes

fun AppCompatActivity.setContentView(layoutRes: LayoutRes) = setContentView(layoutRes.value)

fun AppCompatActivity.setTheme(styleRes: StyleRes) = setTheme(styleRes.value)

fun <T: View> AppCompatActivity.findViewById(idRes: IdRes): T = findViewById(idRes.value)
