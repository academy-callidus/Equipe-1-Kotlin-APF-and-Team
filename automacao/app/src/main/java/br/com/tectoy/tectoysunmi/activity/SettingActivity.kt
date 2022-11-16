package br.com.tectoy.tectoysunmi.activity

import br.com.tectoy.tectoysunmi.activity.BaseActivity
import android.widget.TextView
import android.os.Bundle
import br.com.tectoy.tectoysunmi.R
import br.com.tectoy.tectoysunmi.utils.TectoySunmiPrint
import sunmi.sunmiui.dialog.ListDialog
import sunmi.sunmiui.dialog.DialogCreater
import sunmi.sunmiui.dialog.ListDialog.ItemClickListener
import android.content.Intent
import android.view.Menu
import android.view.View
import br.com.tectoy.tectoysunmi.activity.PrinterInfoActivity

class SettingActivity : BaseActivity(), View.OnClickListener {
    var method = arrayOf("API")
    private var mTextView1: TextView? = null
    private var mTextView2: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setMyTitle(R.string.setting_title)
        setBack()
        findViewById<View>(R.id.setting_connect).setOnClickListener(this)
        findViewById<View>(R.id.setting_info).setOnClickListener(this)
        mTextView1 = findViewById(R.id.setting_conected)
        mTextView2 = findViewById(R.id.setting_disconected)
        mTextView1?.setOnClickListener(View.OnClickListener {
            TectoySunmiPrint.getInstance().initSunmiPrinterService(this@SettingActivity)
            setService()
        })
        mTextView2?.setOnClickListener(View.OnClickListener {
            TectoySunmiPrint.getInstance().deInitSunmiPrinterService(this@SettingActivity)
            setService()
        })
        (findViewById<View>(R.id.setting_textview1) as TextView).text = "API"
        setService()
    }

    override fun onClick(v: View) {
        val listDialog: ListDialog
        when (v.id) {
            R.id.setting_connect -> {
                listDialog = DialogCreater.createListDialog(
                    this,
                    resources.getString(R.string.connect_method),
                    resources.getString(R.string.cancel),
                    method
                )
                listDialog.setItemClickListener { position ->
                    (findViewById<View>(R.id.setting_textview1) as TextView).text = method[position]
                    setMyTitle(R.string.setting_title)
                    listDialog.cancel()
                }
                listDialog.show()
            }
            R.id.setting_info -> startActivity(
                Intent(
                    this@SettingActivity,
                    PrinterInfoActivity::class.java
                )
            )
            else -> {}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    /**
     * Set print service connection status
     */
    private fun setService() {
        if (TectoySunmiPrint.getInstance().sunmiPrinter == TectoySunmiPrint.FoundSunmiPrinter) {
            mTextView1!!.setTextColor(resources.getColor(R.color.white1))
            mTextView1!!.isEnabled = false
            mTextView2!!.setTextColor(resources.getColor(R.color.white))
            mTextView2!!.isEnabled = true
        } else if (TectoySunmiPrint.getInstance().sunmiPrinter == TectoySunmiPrint.CheckSunmiPrinter) {
            handler?.postDelayed({ setService() }, 2000)
        } else if (TectoySunmiPrint.getInstance().sunmiPrinter == TectoySunmiPrint.LostSunmiPrinter) {
            mTextView1!!.setTextColor(resources.getColor(R.color.white))
            mTextView1!!.isEnabled = true
            mTextView2!!.setTextColor(resources.getColor(R.color.white1))
            mTextView2!!.isEnabled = false
        } else {
            mTextView1!!.setTextColor(resources.getColor(R.color.white1))
            mTextView1!!.isEnabled = true
            mTextView2!!.setTextColor(resources.getColor(R.color.white1))
            mTextView2!!.isEnabled = false
        }
    }
}