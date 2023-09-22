package br.pucminas.teamworktask.publica.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.ActivityPublicBinding
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.privada.ui.PrivateActivity
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_EMAIL
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_ID
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_NOME
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_SENHA
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.guardarPreferencia
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.guardarPreferenciaInt


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

    fun doLogin(usuario: Usuario){
        guardarPreferenciaInt(this, USUARIO_ID, usuario.id.toInt())
        guardarPreferencia(this, USUARIO_EMAIL, usuario.email)
        guardarPreferencia(this, USUARIO_SENHA, usuario.senha)
        guardarPreferencia(this, USUARIO_NOME, usuario.nomeExibicao)

        val intent = Intent(this, PrivateActivity::class.java)
        startActivity(intent)
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
        builder.setMessage(getString(R.string.pre_login_sair_descricao))

        // Set Alert Title
        builder.setTitle(getString(R.string.pre_login_sair_title))

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false)

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton(R.string.generico_confirmar) {
            // When the user click yes button then app will close
            dialog, which -> finishAffinity()
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