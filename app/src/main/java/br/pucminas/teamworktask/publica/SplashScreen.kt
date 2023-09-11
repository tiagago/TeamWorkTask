package br.pucminas.teamworktask.publica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.publica.login.PublicActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this, PublicActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 3000)
    }
}