package br.pucminas.teamworktask.privada

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.databinding.ActivityPrivateBinding

class PrivateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        changeFragment(DashboardFragment.newInstance())
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.privateFl.id , fragment)
            .addToBackStack(null).commit()
    }
}