package br.pucminas.teamworktask.publica.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R


class PublicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public)
    }

    override fun onResume() {
        super.onResume()
        changeFragment(PreLoginFragment.newInstance())
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.public_fl, fragment, fragment.javaClass.simpleName)
            .addToBackStack(null).commit()
    }

}