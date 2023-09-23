package br.pucminas.teamworktask.ui.privada

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.ActivityPrivateBinding
import br.pucminas.teamworktask.ui.GenericActivity
import br.pucminas.teamworktask.ui.privada.equipe.EquipeFragment
import br.pucminas.teamworktask.ui.privada.notificacao.NotificacaoFragment
import br.pucminas.teamworktask.ui.privada.projeto.DashboardFragment
import br.pucminas.teamworktask.ui.privada.tags.TagsFragment
import br.pucminas.teamworktask.ui.privada.tarefa.TarefasFragment
import br.pucminas.teamworktask.ui.publica.PublicActivity

class PrivateActivity : GenericActivity() {
    private lateinit var binding: ActivityPrivateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

    override fun obterFramelayoutID(): Int = binding.privadoFl.id
    // Declare the onBackPressed method when the back button is pressed this method will call

    override fun obterMensagemLogout(): String = getString(R.string.dashboard_logout_descricao)

    override fun obterTituloLogout(): String = getString(R.string.dashboard_logout_title)

    override fun executarLogout(){
        val intent = Intent(this, PublicActivity::class.java)
        startActivity(intent)
        finish()
    }

}