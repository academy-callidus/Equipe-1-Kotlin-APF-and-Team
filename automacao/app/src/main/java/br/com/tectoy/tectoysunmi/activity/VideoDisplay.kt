package br.com.tectoy.tectoysunmi.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Display
import android.view.SurfaceView
import android.widget.FrameLayout
import java.io.File
import br.com.tectoy.tectoysunmi.R
import br.com.tectoy.tectoysunmi.activity.player.IMPlayListener
import br.com.tectoy.tectoysunmi.activity.player.IMPlayer
import br.com.tectoy.tectoysunmi.activity.player.MPlayer
import br.com.tectoy.tectoysunmi.activity.player.MPlayerException
import br.com.tectoy.tectoysunmi.activity.player.MinimalDisplay

class VideoDisplay2(context:Context, display:Display, path:String) : BasePresentation(context,display){
    private lateinit var mPlayerView:SurfaceView
    private var player:MPlayer? = null
    override fun onSelect(isShow: Boolean) {
        TODO("Not yet implemented")
    }

}