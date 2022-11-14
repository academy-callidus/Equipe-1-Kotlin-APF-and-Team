package br.com.tectoy.tectoysunmi.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import br.com.tectoy.tectoysunmi.R
import br.com.tectoy.tectoysunmi.databinding.MsitefBinding
import br.com.tectoy.tectoysunmi.utils.TectoySunmiPrint
import com.google.gson.Gson
import sunmi.sunmiui.dialog.DialogCreater
import java.nio.channels.InterruptedByTimeoutException
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
        binding = MsitefBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tipo.setOnClickListener{
            val mStrings = arrayOf(
                resources.getString(R.string.nao_definido),
                resources.getString(R.string.credito),
                resources.getString(R.string.debito),
                resources.getString(R.string.carteira_digital)
            )
            val listDialog = DialogCreater.createListDialog(
                this@MSitef,
                resources.getString(R.string.array_qrcode),
                resources.getString(R.string.cancel),
                mStrings
            )
            listDialog.setItemClickListener {
                binding.spTipoPagamento.text = mStrings[it]
                teste = mStrings[it]
                listDialog.cancel()
                println(binding.spTipoPagamento)
                println("teste")
            }
            listDialog.show()
        }

        binding.btnRepressao.setOnClickListener {
            acao = "reimpressao"
            //TODO : execulteSTefReimpressao()
        }

        binding.btnPagar.setOnClickListener {
            acao = "venda"
            execulteSTefVenda()
        }

        binding.btnCancelamento.setOnClickListener {
            acao = "cancelamento"
            //TODO : execulteSTefCancelamento()
        }
    }

    private fun execulteSTefVenda(){
        val intentSitef = Intent("br.com.softwareexpress.sitef.msitef.ACTIVITY_CLISITEF")
        intentSitef.putExtra("empresaSitef", "00000000")
        intentSitef.putExtra("enderecoSitef", "172.17.102.96")
        intentSitef.putExtra("operador", "0001")
        intentSitef.putExtra("data", "20200324")
        intentSitef.putExtra("hora", "130358")
        intentSitef.putExtra("numeroCupom", op)
        intentSitef.putExtra("valor", Mask.unmask(binding.txtValorOperacao.text.toString()))
        intentSitef.putExtra("CNPJ_CPF", "03654119000176")
        intentSitef.putExtra("comExterna", "0")
        if (binding.chcUSB.isChecked) {
            intentSitef.putExtra("pinpadMac", "00:00:00:00:00:00")
        }
        if (teste == "Não Definido") {
            intentSitef.putExtra("modalidade", "0")
        } else if ("Crédito" == teste) {
            intentSitef.putExtra("modalidade", "3")
            if (binding.edtParcelas.toString() == "0" || binding.edtParcelas.toString() == "1") {
                intentSitef.putExtra("transacoesHabilitadas", "26")
            } else if (true) {
                // Essa informações habilida o parcelamento Loja
                intentSitef.putExtra("transacoesHabilitadas", "27")
            }
            intentSitef.putExtra("numParcelas", binding.edtParcelas.toString())
        } else if ("Débito" == teste) {
            intentSitef.putExtra("modalidade", "2")
            //intentSitef.putExtra("transacoesHabilitadas", "16");
        } else if ("Carteira Digital" == teste) {
        }
        intentSitef.putExtra("isDoubleValidation", "0")
        intentSitef.putExtra("caminhoCertificadoCA", "ca_cert_perm")

    }


}