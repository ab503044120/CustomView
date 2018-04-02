package org.huihui.scrollerdrag

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_drawerframlayout.*

class DrawerFramLayoutActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawerframlayout)
        handler = Handler()

        handler.postDelayed(object :Runnable {
            override fun run() {
                tv.text = (Math.random() * 1000).toString() + ""
                handler.postDelayed(this, 1000)
            }

        }, 1000)
    }
}