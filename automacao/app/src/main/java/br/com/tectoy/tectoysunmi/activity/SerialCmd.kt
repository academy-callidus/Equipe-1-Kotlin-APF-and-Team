package br.com.tectoy.tectoysunmi.activity
/**
 * created by mayflower on 2020.6.23
 * convertido para Kotlin by PauloCativo on 17/11/2022
 * get me at jiangli@sunmi.com
 * 这里封装了基本的语法和常用指令，别的详见指令集文档
 */
class SerialCmd {
    //命令格式：Prefix Storage Tag SubTag {Data} [, SubTag {Data}] [; Tag SubTag {Data}] […] ; Suffix
    val PREFIX_HEX:String="7E0130303030" //~<SOH>0000
    val TOSRAGE_EVER_HEX:String="40" //@ 是设置永久有效
    val STORAGE_TEMP_HEX:String="23" //# 则是临时设置，断电后失效
    //tag、subtag、data，这三个合起来就是具体的命令；多个命令可以合起来发，也可以挨个发（需间隔50ms或收到返回值后发）
    val SUFFIX_HEX:String="3B03"  //;<ETX>

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
        /**
         * 设置识读偏好,出厂默认是扫屏模式(曝光等级低,LV5),可以改成纸质模式(曝光等级高,LV0)
         */
        fun setExposure(level:Int): String { return "@EXPLVL" + level.toString()}

        /**
         * 以下是其他常用指令，本扫码器独有
         * 设置扫码模式
         * @param mode 0 电平触发模式，手动读码（发1B31才读一次码）
         *             1 自动触发模式，自动读码
         * @param wait 0 亮灯后等待时间默认值，手动读码模式默认60000ms，自动读码模式默认1000ms
         *             $ 等待时间 ms
         * @param delay 0 同码间隔时间默认值(异码间隔默认200，一般不需要改)，自动读码默认1000ms，只对自动读码有效
         *              $ 扫码间隔 ms
         */
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
        /**
         * 查询当前配置
         * @return
         */
        fun queryScanMode(): String {
            return "#SCNMOD*;ORTSET*;RRDDUR*"
        }
    }


}