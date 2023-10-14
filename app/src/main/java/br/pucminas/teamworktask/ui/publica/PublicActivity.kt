package br.pucminas.teamworktask.ui.publica

import android.content.Intent
import android.os.Bundle
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.ActivityPublicBinding
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.ui.privada.PrivateActivity
import br.pucminas.teamworktask.ui.GenericActivity
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_ID
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_LOGIN
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_NOME
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_SENHA
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.guardarPreferencia
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.guardarPreferenciaInt


class PublicActivity : GenericActivity() {
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

    /**********************************************************************************
     **** Efetuar o Login do usuario após sucesso de chamada de login com sucesso  ****
     **********************************************************************************/
    fun doLogin(usuario: Usuario){
        guardarPreferenciaInt(this, USUARIO_ID, usuario.id.toInt())
        guardarPreferencia(this, USUARIO_LOGIN, usuario.login)
        guardarPreferencia(this, USUARIO_SENHA, usuario.senha)
        guardarPreferencia(this, USUARIO_NOME, usuario.nomeExibicao)

        val intent = Intent(this, PrivateActivity::class.java)
        startActivity(intent)
    }

    override fun obterFramelayoutID(): Int = binding.publicFl.id

    override fun obterMensagemLogout(): String = getString(R.string.pre_login_sair_descricao)

    override fun obterTituloLogout(): String = getString(R.string.pre_login_sair_title)

    override fun executarLogout(){
        finishAffinity()
    }

}