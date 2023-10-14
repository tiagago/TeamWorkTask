package br.pucminas.teamworktask.ui.privada

import android.content.Intent
import android.os.Bundle
import android.view.View
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.ActivityPrivateBinding
import br.pucminas.teamworktask.ui.GenericActivity
import br.pucminas.teamworktask.ui.privada.equipe.EquipeFragment
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
        configurarBnvListeners()
    }

    fun configurarBnvListeners(){
        binding.apply {
            privadoBnv.inflateMenu(R.menu.bottom_nav)
            showBottomNavigator(true)
            privadoBnv.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bottom_nav_home -> changeFragment(DashboardFragment())
                    R.id.bottom_nav_tarefa -> changeFragment(TarefasFragment())
                    R.id.bottom_nav_tags -> changeFragment(TagsFragment())
                    R.id.bottom_nav_equipe -> changeFragment(EquipeFragment())
                    else -> {}
                }
                true
            }
            privadoLogoutIv.setOnClickListener {
                chamarPopupLogout()
            }
        }
    }

    fun atualizarTopLaout(icon: Int, titulo: String ){
        binding.apply {
            privadoLogoIv.setImageDrawable(getDrawable(icon))
            privadoTituloTv.text = titulo
        }
    }

    fun showBottomNavigator(isVisible: Boolean){
        binding.privadoBnv.visibility = if(isVisible) View.VISIBLE else View.GONE
    }


    override fun onResume() {
        super.onResume()
        if (supportFragmentManager.backStackEntryCount <= 1) {
            changeFragment(DashboardFragment())
        }
    }

    override fun obterFramelayoutID(): Int = binding.privadoFl.id
    // Declare the onBackPressed method when the back button is pressed this method will call

    override fun obterMensagemLogout(): String = getString(R.string.dashboard_logout_descricao)

    override fun obterTituloLogout(): String = getString(R.string.dashboard_logout_title)

    override fun executarLogout() {
        val intent = Intent(this, PublicActivity::class.java)
        startActivity(intent)
        finish()
    }

}