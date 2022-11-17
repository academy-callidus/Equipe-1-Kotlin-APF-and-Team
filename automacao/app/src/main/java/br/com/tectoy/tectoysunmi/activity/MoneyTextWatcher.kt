package br.com.tectoy.tectoysunmi.activity

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

open class MoneyTextWatcher: TextWatcher{
    private lateinit var editTextWeakReference:WeakReference<EditText>
    constructor(editText:EditText, locale:Locale){
        this.editTextWeakReference = WeakReference<EditText>(editText)
    }

    constructor(editText:EditText){
        println(editText.text.toString())
        this.editTextWeakReference = WeakReference<EditText>(editText)
    }

    override fun beforeTextChanged(s:CharSequence?, start:Int, count:Int, after:Int) {
        TODO("Not yet implemented")
    }

    override fun onTextChanged(s:CharSequence?, start:Int, count:Int, after:Int) {
        TODO("Not yet implemented")
    }

    override fun afterTextChanged(editable: Editable?) {
        var editText:EditText? = editTextWeakReference.get() ?: return
        editText?.removeTextChangedListener(this)
        val parsed:BigDecimal = parseToBigDecimal(editable?.toString()?.replace("R\\$", "") ?: "")
        val formatted:String = NumberFormat.getCurrencyInstance().format(parsed)
        // NumberFormat.getNumberInstance(locale).format(parsed); // sem o simbolo de moeda
        editText?.setText(formatted.replace("R\\$", ""))
        editText?.setSelection(formatted.length -2)
        editText?.addTextChangedListener(this)
    }

    private fun parseToBigDecimal(value:String):BigDecimal{
        val replaceable:String = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().currency.symbol)
        val cleanString:String = value.replace(replaceable,"")
        return BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR)
            .divide(BigDecimal(100), BigDecimal.ROUND_FLOOR)
    }

}