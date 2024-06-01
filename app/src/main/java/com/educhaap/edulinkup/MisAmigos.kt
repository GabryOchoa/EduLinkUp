package com.educhaap.edulinkup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MisAmigos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_amigos)
    }

    fun startNuevoAmigosActivity(view : View)
    {
        val intent = Intent(this, NuevoAmigo::class.java)
        startActivity(intent)
        finish()
    }
}