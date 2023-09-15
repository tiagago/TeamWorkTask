package br.pucminas.teamworktask.ui.publica

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.databinding.ActivityPublicBinding
import br.pucminas.teamworktask.ui.privada.PrivateActivity


class PublicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPublicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublicBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        changeFragment(PreLoginFragment())
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.publicFl.id , fragment)
            .addToBackStack(null).commit()
    }

    fun doLogin(){
        val intent = Intent(this, PrivateActivity::class.java)
        startActivity(intent)
    }
}