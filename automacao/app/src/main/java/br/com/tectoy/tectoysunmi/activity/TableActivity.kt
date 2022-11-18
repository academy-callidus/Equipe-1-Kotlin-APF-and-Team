package br.com.tectoy.tectoysunmi.activity;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import br.com.tectoy.tectoysunmi.R
import br.com.tectoy.tectoysunmi.databinding.ActivityTableBinding
import br.com.tectoy.tectoysunmi.utils.TectoySunmiPrint
import com.google.android.material.tabs.TabLayout.TabView
import sunmi.sunmiui.button.ButtonRectangular
import java.util.*


class TableActivity : BaseActivity(){
    lateinit var footView: ButtonRectangular
    lateinit var ta: TableAdapter
    protected lateinit var datalist:LinkedList<TableItem>

    private lateinit var binding: ActivityTableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setMyTitle(R.string.tab_title)
        setBack()

        initListView()
    }

    private fun initListView() {
        footView = ButtonRectangular(this)
        footView.setTitleText(resources.getString(R.string.add_line))
        footView.setTextColorEnabled(R.color.black)
        footView.setOnClickListener {
            addOneData(datalist)
            ta.notifyDataSetChanged()
        }
        binding.tableList.addFooterView(footView)
        datalist = LinkedList()
        addOneData(datalist)
        ta = TableAdapter()
        binding.tableList.adapter = ta
    }

    fun onClick(v:View?){
        for (tableItem in datalist){
            TectoySunmiPrint.getInstance().printTable(tableItem.text,
                tableItem.width, tableItem.align)
        }
        TectoySunmiPrint.getInstance().feedPaper()
    }

    private fun addOneData(data: LinkedList<TableItem>) {
        val ti = TableItem();
        data.add(ti)
    }


    inner class ViewHolder : View.OnFocusChangeListener, AdapterView.OnItemSelectedListener {
        lateinit var mText:TextView

        lateinit var mText1:EditText
        lateinit var mText2:EditText
        lateinit var mText3:EditText

        lateinit var width1:EditText
        lateinit var width2:EditText
        lateinit var width3:EditText

        lateinit var align1:AppCompatSpinner
        lateinit var align2:AppCompatSpinner
        lateinit var align3:AppCompatSpinner

        var view:EditText? = null
        var line:Int = 0


        override fun onFocusChange(v: View, hasFocus: Boolean) {
            Log.d("Geovani",v.id.toString()+">>"+hasFocus)
            if(v.hasFocus()){
                view = v as EditText
                return
            }
            val ti:TableItem = datalist[line]
            when(v.id){
                R.id.it_text3  -> {  (ti.text)[2] = ((v as EditText).text.toString())  }
                R.id.it_text2  -> {  (ti.text)[1] = ((v as EditText).text.toString())  }
                R.id.it_text1  -> {  (ti.text)[0] = ((v as EditText).text.toString())  }

                R.id.it_width3 -> {  (ti.width)[2] = ((v as EditText).text.toString()).toInt()  }
                R.id.it_width2 -> {  (ti.width)[1] = ((v as EditText).text.toString()).toInt()  }
                R.id.it_width1 -> {  (ti.width)[0] = ((v as EditText).text.toString()).toInt()  }
                else           -> return
            }
        }

        fun setCallback(){
            if (   mText1 == null || mText2 == null || mText3 == null
                || width1 == null || width2 == null || width3 == null
                || align1 == null || align2 == null || align3 == null) {
                return
            }
            mText1.onFocusChangeListener = this
            mText2.onFocusChangeListener = this
            mText3.onFocusChangeListener = this

            width1.onFocusChangeListener = this
            width2.onFocusChangeListener = this
            width3.onFocusChangeListener = this

            align1.onFocusChangeListener = this
            align2.onFocusChangeListener = this
            align3.onFocusChangeListener = this
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val ti:TableItem = datalist[line]
            when(parent?.id){
                R.id.it_align3 -> {(ti.align)[2] = position}
                R.id.it_align2 -> {(ti.align)[1] = position}
                R.id.it_align1 -> {(ti.align)[0] = position}
                else           -> return
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }

    inner class TableAdapter : BaseAdapter(){

        override fun getCount(): Int {
            return datalist.size
        }

        override fun getItem(position: Int): Int {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val vh: ViewHolder
            val cv = LayoutInflater.from(this@TableActivity).inflate(R.layout.item_table, null)
            if(convertView == null){
                vh = ViewHolder()
                vh.mText = cv.findViewById(R.id.it_title)
                vh.mText1 = cv.findViewById(R.id.it_text1)
                vh.mText2 = cv.findViewById(R.id.it_text2)
                vh.mText3 = cv.findViewById(R.id.it_text3)
                vh.width1 = cv.findViewById(R.id.it_width1)
                vh.width2 = cv.findViewById(R.id.it_width2)
                vh.width3 = cv.findViewById(R.id.it_width3)
                vh.align1 = cv.findViewById(R.id.it_align1)
                vh.align2 = cv.findViewById(R.id.it_align2)
                vh.align3 = cv.findViewById(R.id.it_align3)
                vh.setCallback()
                cv.tag = vh
            } else {
                vh = convertView.tag as ViewHolder
            }
            vh.line = position
            vh.mText.text = "Row. ${(position.inc())}"
            if(vh.view != null)
                vh.view?.requestFocus()

            return cv
        }
    }

    //https://kotlinlang.org/docs/arrays.html#primitive-type-arrays
    protected data class TableItem(var text:Array<String> = arrayOf("test", "test", "test"),
                                 var width:IntArray = intArrayOf(1, 1, 1),
                                 var align:IntArray = intArrayOf(0, 0, 0)) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TableItem

            if (!text.contentEquals(other.text)) return false
            if (!width.contentEquals(other.width)) return false
            if (!align.contentEquals(other.align)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = text.contentHashCode()
            result = 31 * result + width.contentHashCode()
            result = 31 * result + align.contentHashCode()
            return result
        }

    }
}