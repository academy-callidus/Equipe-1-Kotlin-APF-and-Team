package br.com.tectoy.tectoysunmi.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import br.com.tectoy.tectoysunmi.databinding.MsitefBinding

class MSitef : BaseActivity(){

    lateinit var binding : MsitefBinding

    private val API_VERSION = "1.04"

    private val CREDITO = "1"
    private val DEBITO = "2"
    private val VOUCHER = "4"
    private val REIMPRESSAO = "18"

    private val SEMPARCELAMENTO = "0"
    private val PARCELADO_LOJA = "1"
    private val PARCELADO_ADM = "2"

    private val DESABILITA_IMPRESSAO = "0"
    private val HABILITA_IMPRESSAO = "1"

    private val VENDA = "1"
    private val CANCELAMENTO = "2"
    private val FUNCOES = "3"
    var acao = "venda"

    //Gson gson = new Gson();
    var i = Intent(Intent.ACTION_VIEW, Uri.parse("pos7api://pos7"))


    var venda = Venda()
    var teste: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}