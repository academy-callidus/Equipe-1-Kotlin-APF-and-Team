package br.com.tectoy.tectoysunmi.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*


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
        fun generateBitmap(content:String?, format:Int, width:Int, height:Int): Bitmap? {
            content ?: return null
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
            val qrCodeWriter:MultiFormatWriter = MultiFormatWriter()
            var hints = HashMap<EncodeHintType, Objects>()
            hints.put(EncodeHintType.CHARACTER_SET, "GBK")
            //mocado
            return null
        }
    }
}