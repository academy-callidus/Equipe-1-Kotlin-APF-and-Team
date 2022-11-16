package br.com.tectoy.tectoysunmi.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import br.com.tectoy.tectoysunmi.databinding.ActivityScannerBinding


class ScannerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityScannerBinding
    private lateinit var sunmiScanner: SunmiScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initScanner()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(sunmiScanner != null){
            sunmiScanner.destory()
        }
    }

    override fun dispatchKeyEvent(event : KeyEvent?) : Boolean{
        val action = event?.action
        if (action == KeyEvent.ACTION_DOWN)  {
            if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || event.keyCode == KeyEvent.KEYCODE_BACK
                || event.keyCode == KeyEvent.KEYCODE_MENU
                || event.keyCode == KeyEvent.KEYCODE_HOME
                || event.keyCode == KeyEvent.KEYCODE_POWER
            )
                return super.dispatchKeyEvent(event)

            if (sunmiScanner != null) sunmiScanner.analysisKeyEvent(event)

            return true
        }
        return super.dispatchKeyEvent(event)
    }

    private fun initScanner(){
        sunmiScanner = SunmiScanner(applicationContext)
        sunmiScanner.analysisBroadcast()

        sunmiScanner.setScannerListener(object : SunmiScanner.OnScannerListener{
            override fun onScanData(data: String?, type: SunmiScanner.DATA_DISCRIBUTE_TYPE?) {
                append("Tipo de Dado: $type\nCodigo: $data\n")
            }

            override fun onResponseData(data: String?, type: SunmiScanner.DATA_DISCRIBUTE_TYPE?) {
                TODO("Not yet implemented")
            }

            override fun onResponseTimeout() {
                TODO("Not yet implemented")
            }
        })

    }

    private fun append(message : String?){
        this.runOnUiThread {
            binding.tvNote.append(message)
            binding.scrollView.post{
                binding.scrollView.smoothScrollBy(0, binding.tvNote.bottom)
            }
        }
    }
}