package nz.jamiesanson.resktx.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import resktx.extensions.android.content.getString
import resktx.extensions.androidx.appcompat.app.findViewById
import resktx.extensions.androidx.appcompat.app.setContentView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(K.layout.activity_main)

        findViewById<TextView>(K.id.textView).text = getString(K.string.hello_resktx)
    }
}
