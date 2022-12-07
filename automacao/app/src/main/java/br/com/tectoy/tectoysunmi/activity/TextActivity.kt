package br.com.tectoy.tectoysunmi.activity

import br.com.tectoy.tectoysunmi.activity.BaseActivity
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import com.sunmi.extprinterservice.ExtPrinterService
import android.os.Bundle
import br.com.tectoy.tectoysunmi.R
import android.util.DisplayMetrics
import br.com.tectoy.tectoysunmi.activity.TextActivity
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import sunmi.sunmiui.dialog.ListDialog
import sunmi.sunmiui.dialog.DialogCreater
import sunmi.sunmiui.dialog.ListDialog.ItemClickListener
import br.com.tectoy.tectoysunmi.utils.TectoySunmiPrint
import br.com.tectoy.tectoysunmi.utils.KTectoySunmiPrinter
import br.com.tectoy.tectoysunmi.utils.BluetoothUtil
import br.com.tectoy.tectoysunmi.utils.ESCUtil
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.hardware.usb.UsbDevice
import android.app.Activity
import android.hardware.usb.UsbManager
import android.text.TextUtils
import android.content.Intent
import android.content.ServiceConnection
import android.content.ComponentName
import android.content.Context
import android.graphics.Rect
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AlertDialog
import java.io.IOException

/**
 * Exemplo de impressão de texto
 */
class TextActivity() : BaseActivity(), CompoundButton.OnCheckedChangeListener {
    private var mTextView1: TextView? = null
    private var mTextView2: TextView? = null
    var mCheckBox1: CheckBox? = null
    var mCheckBox2: CheckBox? = null
    private var mEditText: EditText? = null
    private var mLayout: LinearLayout? = null
    var mLinearLayout: LinearLayout? = null
    private var record = 0
    private var isBold = false
    private var isUnderLine = false
    var height = 0
    private var extPrinterService: ExtPrinterService? = null
    private val mStrings = arrayOf(
        "CP437",
        "CP850",
        "CP860",
        "CP863",
        "CP865",
        "CP857",
        "CP737",
        "Windows-1252",
        "CP866",
        "CP852",
        "CP858",
        "CP874",
        "CP855",
        "CP862",
        "CP864",
        "GB18030",
        "BIG5",
        "KSC5601",
        "utf-8"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
        setMyTitle(R.string.text_title)
        setBack()
        val dm = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels // Largura da tela
        height = dm.heightPixels // Largura da tela
        isVertical = height > width
        isK1 = isHaveCamera && isVertical
        if (true && height > 1856.also { isK1 = true}) {
            connectKPrintService()
        }
        record = 17
        isBold = false
        isUnderLine = true
        mTextView1 = findViewById(R.id.text_text_character)
        mTextView2 = findViewById(R.id.text_text_size)
        mCheckBox1 = findViewById(R.id.text_bold)
        mCheckBox2 = findViewById(R.id.text_underline)
        mEditText = findViewById(R.id.text_text)
        mLinearLayout = findViewById(R.id.text_all)
        mLayout = findViewById(R.id.text_set)
        mLinearLayout?.getViewTreeObserver()?.addOnGlobalLayoutListener(OnGlobalLayoutListener {
            val r = Rect()
            with(mLinearLayout) {
                this?.getWindowVisibleDisplayFrame(r)
            }
            if (r.bottom < 800) {
                with(mLayout) { this?.setVisibility(View.GONE) }
            } else {
                with(mLayout) { this?.setVisibility(View.VISIBLE) }
            }
        })
        mCheckBox1?.setOnCheckedChangeListener(this)
        mCheckBox2?.setOnCheckedChangeListener(this)
        findViewById<View>(R.id.text_character).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val listDialog = DialogCreater.createListDialog(
                    this@TextActivity,
                    resources.getString(R.string.characterset),
                    resources.getString(R.string.cancel),
                    mStrings
                )
                listDialog.setItemClickListener(object : ItemClickListener {
                    override fun OnItemClick(position: Int) {
                        with(mTextView1) { this?.setText(mStrings[position]) }
                        record = position
                        listDialog.cancel()
                    }
                })
                listDialog.show()
            }
        })
        findViewById<View>(R.id.text_size).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                showSeekBarDialog(
                    this@TextActivity,
                    resources.getString(R.string.size_text),
                    12,
                    36,
                    mTextView2
                )
            }
        })
    }

    fun onClick(view: View?) {
        val content = mEditText!!.text.toString()
        val size = mTextView2!!.text.toString().toInt().toFloat()
        if (true && height > 1856.also { isK1 = true }) {
            kPrinterPresenter!!.printStyleBold(isBold)
            TectoySunmiPrint.getInstance().printStyleUnderLine(isUnderLine)
            kPrinterPresenter!!.text(content)
            TectoySunmiPrint.getInstance().print3Line()
            kPrinterPresenter!!.cutpaper(KTectoySunmiPrinter.HALF_CUTTING, 10)
        } else {
            TectoySunmiPrint.getInstance().printStyleBold(isBold)
            TectoySunmiPrint.getInstance().printStyleUnderLine(isUnderLine)

            TectoySunmiPrint.getInstance().printTextWithSize(content, size)
            TectoySunmiPrint.getInstance().print3Line()
            TectoySunmiPrint.getInstance().cutpaper()
        }
    }

    private fun printByBluTooth(content: String) {
        try {
            if (isBold) {
                BluetoothUtil.sendData(ESCUtil.boldOn())
            } else {
                BluetoothUtil.sendData(ESCUtil.boldOff())
            }
            if (isUnderLine) {
                BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn())
            } else {
                BluetoothUtil.sendData(ESCUtil.underlineOff())
            }
            if (record < 17) {
                BluetoothUtil.sendData(ESCUtil.singleByte())
                BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(record)))
            } else {
                BluetoothUtil.sendData(ESCUtil.singleByteOff())
                BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(record)))
            }
            BluetoothUtil.sendData(content.toByteArray(charset(mStrings[record])))
            BluetoothUtil.sendData(ESCUtil.nextLine(3))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun codeParse(value: Int): Byte {
        var res: Byte = 0x00
        when (value) {
            0 -> res = 0x00
            1, 2, 3, 4 -> res = (value + 1).toByte()
            5, 6, 7, 8, 9, 10, 11 -> res = (value + 8).toByte()
            12 -> res = 21
            13 -> res = 33
            14 -> res = 34
            15 -> res = 36
            16 -> res = 37
            17, 18, 19 -> res = (value - 17).toByte()
            20 -> res = 0xff.toByte()
            else -> {}
        }
        return res
    }

    /**
     * seekbar dialog
     *
     * @param context
     * @param title
     * @param min
     * @param max
     * @param set
     */
    private fun showSeekBarDialog(
        context: Context,
        title: String,
        min: Int,
        max: Int,
        set: TextView?
    ) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.widget_seekbar, null)
        builder.setView(view)
        builder.setCancelable(false)
        val dialog = builder.create()
        val tv_title = view.findViewById<TextView>(R.id.sb_title)
        val tv_start = view.findViewById<TextView>(R.id.sb_start)
        val tv_end = view.findViewById<TextView>(R.id.sb_end)
        val tv_result = view.findViewById<TextView>(R.id.sb_result)
        val tv_ok = view.findViewById<TextView>(R.id.sb_ok)
        val tv_cancel = view.findViewById<TextView>(R.id.sb_cancel)
        val sb = view.findViewById<SeekBar>(R.id.sb_seekbar)
        tv_title.text = title
        tv_start.text = min.toString() + ""
        tv_end.text = max.toString() + ""
        tv_result.text = set!!.text
        tv_cancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dialog.cancel()
            }
        })
        tv_ok.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                set.text = tv_result.text
                dialog.cancel()
            }
        })
        sb.max = max - min
        sb.progress = set.text.toString().toInt() - min
        sb.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val rs = min + progress
                tv_result.text = rs.toString() + ""
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        dialog.show()
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.text_bold -> isBold = isChecked
            R.id.text_underline -> isUnderLine = isChecked
            else -> {}
        }
    }

    val isHaveCamera: Boolean
        get() {
            val deviceHashMap = (getSystemService(USB_SERVICE) as UsbManager).deviceList
            for (entry: Map.Entry<*, *> in deviceHashMap.entries) {
                val usbDevice = entry.value as UsbDevice
                if (!TextUtils.isEmpty(usbDevice.getInterface(0).name) && usbDevice.getInterface(0).name!!
                        .contains("Orb")
                ) {
                    return true
                }
                if (!TextUtils.isEmpty(usbDevice.getInterface(0).name) && usbDevice.getInterface(0).name!!
                        .contains("Astra")
                ) {
                    return true
                }
            }
            return false
        }

    private fun connectKPrintService() {
        val intent = Intent()
        intent.setPackage("com.sunmi.extprinterservice")
        intent.action = "com.sunmi.extprinterservice.PrinterService"
        bindService(intent, connService, BIND_AUTO_CREATE)
    }

    private val connService: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            extPrinterService = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            extPrinterService = ExtPrinterService.Stub.asInterface(service)
            kPrinterPresenter = KTectoySunmiPrinter(this@TextActivity, extPrinterService)
        }
    }

    companion object {
        var isK1 = false
        var isVertical = false
        var kPrinterPresenter: KTectoySunmiPrinter? = null
    }
}