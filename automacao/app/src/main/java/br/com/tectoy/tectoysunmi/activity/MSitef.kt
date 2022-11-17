package br.com.tectoy.tectoysunmi.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import br.com.tectoy.tectoysunmi.R
import br.com.tectoy.tectoysunmi.databinding.MsitefBinding
import br.com.tectoy.tectoysunmi.utils.TectoySunmiPrint
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import sunmi.sunmiui.dialog.DialogCreater
import java.text.DateFormat
import java.util.*
import java.util.regex.Pattern
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
            execulteSTefReimpressao()
        }

        binding.btnPagar.setOnClickListener {
            acao = "venda"
            execulteSTefVenda()
        }

        binding.btnCancelamento.setOnClickListener {
            acao = "cancelamento"
            execulteSTefCancelamento()
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
        } else if ("Carteira Digital" == teste) {}
        intentSitef.putExtra("isDoubleValidation", "0")
        intentSitef.putExtra("caminhoCertificadoCA", "ca_cert_perm")
        //registerActivityForResult()
    }

    private fun execulteSTefCancelamento(){
        val intentSitef = Intent("br.com.softwareexpress.sitef.msitef.ACTIVITY_CLISITEF")
        intentSitef.putExtra("empresaSitef", "00000000")
        intentSitef.putExtra("enderecoSitef", "172.17.102.96")
        intentSitef.putExtra("operador", "0001")
        intentSitef.putExtra("data", currentDateTimeString)
        intentSitef.putExtra("hora", currentDateTimeStringT)
        intentSitef.putExtra("numeroCupom", op)
        intentSitef.putExtra("valor", Mask.unmask(binding.txtValorOperacao.text.toString()))
        intentSitef.putExtra("CNPJ_CPF", "03654119000176")
        intentSitef.putExtra("comExterna", "0")
        intentSitef.putExtra("modalidade", "200")
        intentSitef.putExtra("isDoubleValidation", "0")
        intentSitef.putExtra("caminhoCertificadoCA", "ca_cert_perm")
        //registerActivityForResult()
    }

    //override para registerActivityForResult()

    private fun execulteSTefReimpressao(){
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
        intentSitef.putExtra("modalidade", "114")
        intentSitef.putExtra("isDoubleValidation", "0")
        intentSitef.putExtra("caminhoCertificadoCA", "ca_cert_perm")
        //registerActivityForResult()
    }

    fun validaIp(ipServer : String) : Boolean{
        val p : Pattern = Pattern.compile(
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
            )
        val m = p.matcher(ipServer)
        return m.matches()
    }

    private fun maskTextEdits(){
        binding.txtValorOperacao.addTextChangedListener(object : MoneyTextWatcher(binding.txtValorOperacao){})
    }

    private fun dialogTrasacaoAprovadaMsitef(retornoMsiTef : RetornoMsiTef){
        val alertDialog = AlertDialog.Builder(this@MSitef)
        val cupom = StringBuilder()
        val teste = StringBuilder()

        cupom.append(
            """
                 Via Cliente 
                 ${retornoMsiTef.getVIA_CLIENTE()}
                 """.trimIndent()
        )
        teste.append(
            """
                Via Estabelecimento 
                ${retornoMsiTef.getVIA_ESTABELECIMENTO()}
                """.trimIndent()
        )

        alertDialog.setTitle("Ação executada com sucesso")
        alertDialog.setMessage(cupom.toString())
        alertDialog.setPositiveButton("OK"){
            dialogInterface, i ->
                TectoySunmiPrint.getInstance().setSize(20)
                TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                TectoySunmiPrint.getInstance().printStyleBold(true)
                TectoySunmiPrint.getInstance().printText(cupom.toString())
                TectoySunmiPrint.getInstance().print3Line()

                TectoySunmiPrint.getInstance().feedPaper()
                TectoySunmiPrint.getInstance().printText(teste.toString())
                TectoySunmiPrint.getInstance().print3Line()

                TectoySunmiPrint.getInstance().cutpaper()
        }
        alertDialog.show()
    }

    @Throws(JSONException::class)
    fun respSitefToJson(data: Intent): String? {
        val json = JSONObject()
        json.put("CODRESP", data.getStringExtra("CODRESP"))
        json.put("COMP_DADOS_CONF", data.getStringExtra("COMP_DADOS_CONF"))
        json.put("CODTRANS", data.getStringExtra("CODTRANS"))
        json.put("VLTROCO", data.getStringExtra("VLTROCO"))
        json.put("REDE_AUT", data.getStringExtra("REDE_AUT"))
        json.put("BANDEIRA", data.getStringExtra("BANDEIRA"))
        json.put("NSU_SITEF", data.getStringExtra("NSU_SITEF"))
        json.put("NSU_HOST", data.getStringExtra("NSU_HOST"))
        json.put("COD_AUTORIZACAO", data.getStringExtra("COD_AUTORIZACAO"))
        json.put("NUM_PARC", data.getStringExtra("NUM_PARC"))
        json.put("TIPO_PARC", data.getStringExtra("TIPO_PARC"))
        json.put("VIA_ESTABELECIMENTO", data.getStringExtra("VIA_ESTABELECIMENTO"))
        json.put("VIA_CLIENTE", data.getStringExtra("VIA_CLIENTE"))
        return json.toString()
    }

    private fun dialogImpressao(texto: String, size: Int) {
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this).create()
        val cupom = StringBuilder()
        TectoySunmiPrint.getInstance().printText(texto)
        //cupom.append("Deseja realizar a impressão pela aplicação ?");
        // alertDialog.setTitle("Realizar Impressão");
        //alertDialog.setMessage(cupom.toString());
        //alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
        //   @Override
        //  public void onClick(DialogInterface dialogInterface, int i) {

        //      String textoEstabelecimento = "";
        //     String textoCliente = "";

        //    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT);
        //   TectoySunmiPrint.getInstance().setSize(size);
        //   TectoySunmiPrint.getInstance().printStyleBold(true);

        //    try {
        //        TectoySunmiPrint.getInstance().printerStatus();
        //        if (true) {
        //           if (true) {
        //              textoEstabelecimento = texto.substring(0, texto.indexOf("\f"));
        //             textoCliente = texto.substring(texto.indexOf("\f"));
        //           TectoySunmiPrint.getInstance().printText(textoEstabelecimento);
        //           TectoySunmiPrint.getInstance().print3Line();
        //           TectoySunmiPrint.getInstance().printText(textoCliente);
        //       } else {
        //           TectoySunmiPrint.getInstance().printText(texto);
        //       }
        //       TectoySunmiPrint.getInstance().print3Line();
        //   }
        // } catch (Exception e) {
        //      e.printStackTrace();
        //  }

        //  }

        // });
        //  alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
        //      @Override
        //     public void onClick(DialogInterface dialogInterface, int i) {
        //         //        não executa nada
        ////     }
        // });
        // alertDialog.show();
    }

}
