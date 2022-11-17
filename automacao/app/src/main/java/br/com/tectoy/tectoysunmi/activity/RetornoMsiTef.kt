package br.com.tectoy.tectoysunmi.activity

class RetornoMsiTef {
    private val CODRESP: String? = null
    private val COMP_DADOS_CONF: String? = null
    private var CODTRANS: String? = null
    private val VLTROCO: String? = null
    private val REDE_AUT: String? = null
    private val BANDEIRA: String? = null
    private val NSU_SITEF: String? = null
    private val NSU_HOST: String? = null
    private val COD_AUTORIZACAO: String? = null
    private val TIPO_PARC: String? = null
    private val NUM_PARC: String? = null
    private val VIA_ESTABELECIMENTO: String? = null
    private val VIA_CLIENTE: String? = null

    fun getNSUHOST(): String? {
        return NSU_HOST
    }

    fun getSitefTipoParcela(): String? {
        return TIPO_PARC
    }

    fun getNSUSitef(): String? {
        return NSU_SITEF
    }

    fun getCodTrans(): String? {
        return CODTRANS
    }

    fun setCodTrans(_cODTRANS: String?) {
        CODTRANS = _cODTRANS
    }

    fun getNameTransCod(): String? {
        var retorno = "Valor invalido"
        when (TIPO_PARC) {
            "00" -> retorno = "A vista"
            "01" -> retorno = "Pré-Datado"
            "02" -> retorno = "Parcelado Loja"
            "03" -> retorno = "Parcelado Adm"
        }
        return retorno
    }

    fun getvlTroco(): String? {
        return VLTROCO
    }

    fun getParcelas(): String? {
        return NUM_PARC ?: ""
    }

    fun getCodAutorizacao(): String? {
        return COD_AUTORIZACAO
    }

    fun textoImpressoEstabelecimento(): String? {
        return VIA_ESTABELECIMENTO
    }

    fun textoImpressoCliente(): String? {
        return VIA_CLIENTE
    }

    fun getCompDadosConf(): String? {
        return COMP_DADOS_CONF
    }

    fun getCodResp(): String? {
        return CODRESP
    }

    fun getRedeAut(): String? {
        return REDE_AUT
    }

    fun getBandeira(): String? {
        return BANDEIRA
    }

    fun getVIA_CLIENTE(): String? {
        return VIA_CLIENTE
    }

    fun getVIA_ESTABELECIMENTO(): String? {
        return VIA_ESTABELECIMENTO
    }
}