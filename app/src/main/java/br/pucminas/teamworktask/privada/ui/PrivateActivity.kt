package br.pucminas.teamworktask.privada.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.ActivityPrivateBinding
import br.pucminas.teamworktask.privada.ui.equipe.EquipeFragment
import br.pucminas.teamworktask.privada.ui.notificacao.NotificacaoFragment
import br.pucminas.teamworktask.privada.ui.projeto.DashboardFragment
import br.pucminas.teamworktask.privada.ui.tags.TagsFragment
import br.pucminas.teamworktask.privada.ui.tarefa.TarefasFragment
import br.pucminas.teamworktask.publica.ui.PublicActivity

class PrivateActivity : AppCompatActivity() {
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

    fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.privadoFl.id , fragment)
            .addToBackStack(null).commit()
    }
    // Declare the onBackPressed method when the back button is pressed this method will call
    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 1) {
            chamarPopupLogout()
        } else {
            supportFragmentManager.popBackStack();
        }
    }

    fun chamarPopupLogout() {
        // Create the object of AlertDialog Builder class
        val builder = AlertDialog.Builder(this)

        // Set the message show for the Alert time
        builder.setMessage(getString(R.string.dashboard_logout_descricao))

        // Set Alert Title
        builder.setTitle(getString(R.string.dashboard_logout_title))

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false)

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton(R.string.generico_confirmar) {
            // When the user click yes button then app will close
                dialog, which ->
            val intent = Intent(this, PublicActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton(R.string.generico_negar) {
            // If user click no then dialog box is canceled.
                dialog, which ->
            dialog.cancel()
        }

        // Create the Alert dialog
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }

}