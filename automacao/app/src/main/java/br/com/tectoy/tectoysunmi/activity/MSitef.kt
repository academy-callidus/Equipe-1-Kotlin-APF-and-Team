package br.com.tectoy.tectoysunmi.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import br.com.tectoy.tectoysunmi.databinding.MsitefBinding
import br.com.tectoy.tectoysunmi.utils.TectoySunmiPrint
import com.google.gson.Gson
import java.text.DateFormat
import java.util.*
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

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

    val i = Intent(Intent.ACTION_VIEW, Uri.parse("pos7api://pos7"))

    var venda = Venda()
    var teste: String? = null

    private val tectoySunmiPrint: TectoySunmiPrint? = null
    private val r = Random()
    private val dt = Date()
    private val op = r.nextInt(99999).toString()
    private val currentDateTimeString = DateFormat.getDateInstance().format(Date())
    private val currentDateTimeStringT =
        dt.time.hours.toString() + dt.time.minutes.toString() + dt.time.seconds.toString()

    /// Fim Defines Operação

    /// Fim Defines Operação
    private val mLocale = Locale("pt", "BR")


    ///  Defines tef
    private val REQ_CODE = 4321
    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}