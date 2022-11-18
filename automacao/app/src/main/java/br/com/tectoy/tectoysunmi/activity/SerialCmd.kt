package br.com.tectoy.tectoysunmi.activity

class SerialCmd {
    val PREFIX_HEX:String="7E0130303030"
    val TOSRAGE_EVER_HEX:String="40"
    val STORAGE_TEMP_HEX:String="23"
    val SUFFIX_HEX:String="3B03"

    val SUBTAG_QUERY_CURRENT_HEX:String="2A"
    val SUBTAG_QUERY_FACTORY_HEX:String="25"
    val SUBTAG_QUERY_RANGE_HEX:String="5E"

    val RES_PREFIX_HEX:String="020130303030"
    val RES_ACK_HEX:String="06"
    val RES_NAK_HEX:String="15"
    val RES_ENQ_HEX:String="05"

    val MIN_SEND_TIME:Int=50
    val MAX_RESPONSE_TIME:Int=5000

    val NLS_SETUPE1:String="#SETUPE1"
    val NLS_SETUPE0:String="#SETUPE0"

    val NLS_KEY_DOWN:String="#SCNTRG1"
    val NLS_KEY_UP:String="#SCNTRG0"

    val NLS_RESTORE:String="@FACDEF"


    /**
     * 设置识读偏好,出厂默认是扫屏模式(曝光等级低,LV5),可以改成纸质模式(曝光等级高,LV0)
     */

    companion object  {
        fun setExposure(level:Int): String { return "@EXPLVL" + level.toString()}

        fun setScanMode(mode:Int,wait:Int,delay:Int):String? {
            when (mode) {
                0 -> {
                    if (wait > 0) {
                        return "@SCNMOD0;ORTSET" + wait.toString()
                    } else {
                        return "@SCNMOD0;ORTSET" + 60000
                    }
                }
                1 -> {
                    if (wait > 0 && delay >= 200) {
                        return "@SCNMOD0;ORTSET" + wait.toString() + ";" + "RRDDUR" + delay.toString()
                    } else if (wait <= 0 && delay >= 200) {
                        return "@SCNMOD0;ORTSET" + 1000 + ";" + "RRDDUR" + delay.toString()
                    } else if (wait > 0 && delay < 200) {
                        return "@SCNMOD0;ORTSET" + wait.toString() + ";" + "RRDDUR" + 1000
                    } else {
                        return "@SCNMOD0;ORTSET" + 1000 + ";" + "RRDDUR" + 1000
                    }
                }
                else -> {
                    return null
                }
            }
        }

        fun queryScanMode(): String {
            return "#SCNMOD*;ORTSET*;RRDDUR*"
        }
    }


}