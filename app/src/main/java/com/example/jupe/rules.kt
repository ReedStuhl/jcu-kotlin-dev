package com.example.jupe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class rules : AppCompatActivity() {

    private var play: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        play = findViewById<View>(R.id.play) as Button
        play!!.setOnClickListener { openGame() }
    }

    /**
     * enters playing field
     */
    private fun openGame() {
        val intent = Intent(this, game::class.java)
            startActivity(intent)
    }
}