package br.com.tectoy.tectoysunmi.utils

import android.graphics.Bitmap

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

import java.util.HashMap

class BitmapUtil {
    /**
     * Gerar código de barras em bitmap
     * @param content
     * @param format
     * @param width
     * @param height
     * @return
     */
    companion object{
        fun generateBitmap(content:String?,format:Int,width:Int,_height:Int):Bitmap?{
            var height = _height
            content ?: return null
            if(content?.isEmpty()) return null
            var barcodeFormat:BarcodeFormat
            when(format){
                0 -> barcodeFormat = BarcodeFormat.UPC_A
                1 -> barcodeFormat = BarcodeFormat.UPC_E
                2 -> barcodeFormat = BarcodeFormat.EAN_13
                3 -> barcodeFormat = BarcodeFormat.EAN_8
                4 -> barcodeFormat = BarcodeFormat.CODE_39
                5 -> barcodeFormat = BarcodeFormat.ITF
                6 -> barcodeFormat = BarcodeFormat.CODABAR
                7 -> barcodeFormat = BarcodeFormat.CODE_93
                8 -> barcodeFormat = BarcodeFormat.CODE_128
                9 -> barcodeFormat = BarcodeFormat.QR_CODE
                else -> {
                    barcodeFormat = BarcodeFormat.QR_CODE
                    height = width
                }
            }
            var qrCodeWriter = MultiFormatWriter()
            var hints = HashMap<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = "GBK"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            try{
                var encode:BitMatrix = qrCodeWriter.encode(content,barcodeFormat,width,height,hints)
                var pixels:IntArray = IntArray(width*height)
                for (i in 0 until height){
                    for(j in 0 until width){
                        if(encode.get(j,i)){
                            pixels[i*width+j] = 0x00000000
                        } else {
                            pixels[i*width+j] = 0xffffffff.toInt()
                        }
                    }
                }
                return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565)
            } catch(e:WriterException){
                e.printStackTrace()
            }
            return null
        }
    }
}