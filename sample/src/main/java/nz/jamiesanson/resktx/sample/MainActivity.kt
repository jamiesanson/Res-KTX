package nz.jamiesanson.resktx.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import resktx.getString
import resktx.setContentView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(K.layout.activity_main)

        getString(K.string.abc_action_bar_home_description)
    }
}
