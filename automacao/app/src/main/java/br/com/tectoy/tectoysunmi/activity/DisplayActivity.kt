package br.com.tectoy.tectoysunmi.activity

import br.com.tectoy.tectoysunmi.activity.BaseActivity
import android.widget.TextView
import br.com.tectoy.tectoysunmi.activity.VideoDisplay
import br.com.tectoy.tectoysunmi.activity.ScreenManager
import android.os.Bundle
import br.com.tectoy.tectoysunmi.R
import android.os.Environment

class DisplayActivity : BaseActivity() {
    var btn_display: TextView? = null
    private var videoDisplay: VideoDisplay? = null
    private val screenManager = ScreenManager.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        setMyTitle(R.string.lcd_title)
        setBack()
        btn_display = findViewById(R.id.btn_display)
        initview()
    }

    fun initview() {
        screenManager.init(this)
        val displays = screenManager.displays
        val display = screenManager.presentationDisplays
        videoDisplay = VideoDisplay(
            this,
            display,
            Environment.getExternalStorageDirectory().path + "/video_01.mp4"
        )
    }
}