package nz.jamiesanson.resktx.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import resktx.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(K.layout.activity_main)

        findViewById<TextView>(K.id.textView).text = getString(K.string.hello_resktx)
    }
}
