package br.pucminas.teamworktask.privada.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.ActivityPrivateBinding
import br.pucminas.teamworktask.privada.ui.equipe.EquipeFragment
import br.pucminas.teamworktask.privada.ui.notificacao.NotificacaoFragment
import br.pucminas.teamworktask.privada.ui.projeto.DashboardFragment
import br.pucminas.teamworktask.privada.ui.tags.TagsFragment
import br.pucminas.teamworktask.privada.ui.tarefa.TarefasFragment

class PrivateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        changeFragment(DashboardFragment())

        binding.privadoBnv.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_nav_home -> changeFragment(DashboardFragment())
                R.id.bottom_nav_tarefa -> changeFragment(TarefasFragment())
                R.id.bottom_nav_tags -> changeFragment(TagsFragment())
                R.id.bottom_nav_equipe -> changeFragment(EquipeFragment())
                R.id.bottom_nav_notificacao -> changeFragment(NotificacaoFragment())
                else -> {}
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        changeFragment(DashboardFragment())
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.privadoFl.id , fragment)
            .addToBackStack(null).commit()
    }
}