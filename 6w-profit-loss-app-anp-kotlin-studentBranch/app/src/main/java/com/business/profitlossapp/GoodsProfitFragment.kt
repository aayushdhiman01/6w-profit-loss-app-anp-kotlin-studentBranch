package com.business.profitlossapp

import android.R
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.business.profitlossapp.databinding.FragmentGoodsListBinding
import com.business.profitlossapp.databinding.FragmentGoodsProfitBinding
import com.business.profitlossapp.room.DisplayTransaction
import com.business.profitlossapp.room.Goods
import com.business.profitlossapp.room.PLDatabase
import com.business.profitlossapp.room.SellPurchaseEntity
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GoodsProfitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class GoodsProfitFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentGoodsProfitBinding
    var goodsList: ArrayList<Goods> = ArrayList()
 //   var dateList: ArrayList<Dates>=ArrayList()
    var transactions: ArrayList<SellPurchaseEntity> = ArrayList()
    lateinit var mainActivity: MainActivity
    lateinit var plDatabase: PLDatabase
//    lateinit var rgGroup:RadioGroup
//
//    lateinit var rbLOSS:RadioButton

    var selectedDate = Date()
    var selectedGoods = Goods()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoodsProfitBinding.inflate(layoutInflater)
        plDatabase = PLDatabase.getDatabase(mainActivity)
        getGoods()

        binding.RadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            getTransactions()
        }

        binding.spinnerGoods.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGoods = goodsList[position]
                getTransactions()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.rbPROFIT.setOnClickListener {
            showBarChart()
        }
        return binding.root
    }

    fun getGoods(){
        class get : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
//                goodsList.add()
                goodsList.addAll(plDatabase.goodsDao().getGoods())
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                var adapter = ArrayAdapter(mainActivity, R.layout.simple_list_item_1, goodsList)
                binding.spinnerGoods.adapter = adapter
            }
        }
        get().execute()
    }

    fun getTransactions(){
        transactions.clear()
        class get : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
                transactions.addAll(plDatabase.goodsDao().getSelectedTransactions(selectedGoods.id))
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
               showBarChart()
            }
        }
        get().execute()
    }
    
    private fun showBarChart() {
        //if selected == selling
        binding.barChart.invalidate()
        binding.barChart.clear()
        val valueList = ArrayList<Double>()
        val entries: ArrayList<BarEntry> = ArrayList()
        val title = selectedGoods.goods

        //input data
        for (i in 0..transactions.size-1) {
            System.out.println("transactions[i].sellingPrice ${transactions[i].sellingPrice}")
            if(binding.rbPROFIT.isChecked) {
                valueList.add((transactions[i].sellingPrice ?: "0").toDouble())
            }
            else if(binding.rbLOSS.isChecked){
                valueList.add((transactions[i].purchasePrice?:"0").toDouble())
            }
        }

        //fit the data into a bar
        for (i in 0 until valueList.size) {
            val barEntry = BarEntry(i*1f, valueList[i].toFloat())
            entries.add(barEntry)
        }
        val barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        binding.barChart.setData(data)
        binding.barChart.invalidate()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GoodsProfitFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GoodsProfitFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}