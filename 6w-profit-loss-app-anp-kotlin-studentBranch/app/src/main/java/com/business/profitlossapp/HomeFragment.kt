package com.business.profitlossapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.business.profitlossapp.databinding.CustomDialogHomeBinding
import com.business.profitlossapp.databinding.FragmentHomeBinding
import com.business.profitlossapp.room.DisplayTransaction
import com.business.profitlossapp.room.Goods
import com.business.profitlossapp.room.PLDatabase
import com.business.profitlossapp.room.SellPurchaseEntity
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment(), TransactionClicked {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding : FragmentHomeBinding
    var goodsList: ArrayList<Goods> = ArrayList()
    var transactionsList: ArrayList<DisplayTransaction> = ArrayList()
    lateinit var plDatabase: PLDatabase
    lateinit var mainActivity: MainActivity
    lateinit var adapter: SellPurchaseAdapter

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
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(layoutInflater)
        adapter = SellPurchaseAdapter(mainActivity, transactionsList, this)
        binding.listView.adapter = adapter
        binding.listView.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
      //  binding.listView.adapter = adapter
        plDatabase = PLDatabase.getDatabase(mainActivity)
        getGoods()
        getTransactions()
        binding.fab.setOnClickListener {
            if(goodsList.isEmpty()){
                Toast.makeText(mainActivity, resources.getString(R.string.add_goods_first), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var selectedGoods = Goods()
            var dialogBinding = CustomDialogHomeBinding.inflate(layoutInflater)
            var dialog = Dialog(requireContext())
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT)
            var adapter = ArrayAdapter(mainActivity, android.R.layout.simple_list_item_1, goodsList)
            dialogBinding.etGoods.adapter = adapter
            selectedGoods = goodsList[0]
            dialogBinding.etGoods.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedGoods = goodsList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            dialogBinding.tvPurchaseDate.setText(mainActivity.simpleDateFormat.format(Calendar.getInstance().time))
            dialogBinding.tvSellingDate.setText(mainActivity.simpleDateFormat.format(Calendar.getInstance().time))
            var purchaseDateCalendar = Calendar.getInstance()
            var sellDateCalendar = Calendar.getInstance()
            dialogBinding.tvPurchaseDate.setOnClickListener {
                var datePicker = DatePickerDialog(mainActivity, {_, year, month,date->
                    purchaseDateCalendar = Calendar.getInstance()
                    purchaseDateCalendar.set(Calendar.YEAR, year)
                    purchaseDateCalendar.set(Calendar.MONTH, month)
                    purchaseDateCalendar.set(Calendar.DATE, date)
                    dialogBinding.tvPurchaseDate.setText(mainActivity.simpleDateFormat.format(purchaseDateCalendar.time))
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE))
                datePicker.show()
            }
            dialogBinding.tvSellingDate.setOnClickListener {
                var datePicker = DatePickerDialog(mainActivity, {_, year, month,date->
                    sellDateCalendar = Calendar.getInstance()
                    sellDateCalendar.set(Calendar.YEAR, year)
                    sellDateCalendar.set(Calendar.MONTH, month)
                    sellDateCalendar.set(Calendar.DATE, date)
                    dialogBinding.tvSellingDate.setText(mainActivity.simpleDateFormat.format(sellDateCalendar.time))
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE))
                datePicker.show()
            }
            dialogBinding.btnAddUpdate.setOnClickListener {
                if (dialogBinding.etPurchaseRate.text.toString().isNullOrEmpty()) {
                    dialogBinding.etPurchaseRate.setError("Enter Purchase Rate")
                }
                else if (dialogBinding.etSellingRate.text.toString().isNullOrEmpty()) {
                    dialogBinding.etSellingRate.setError("Enter Selling Rate")
                }
                else {
                    class insert : AsyncTask<Void, Void, Void>(){
                        override fun doInBackground(vararg p0: Void?): Void? {
                            var sellPurchaseEntity = SellPurchaseEntity()
                            sellPurchaseEntity.purchasePrice = dialogBinding.etPurchaseRate.text.toString()
                            sellPurchaseEntity.sellingPrice = dialogBinding.etSellingRate.text.toString()
                            sellPurchaseEntity.sellingDate = mainActivity.simpleDateFormat.parse(dialogBinding.tvSellingDate.text.toString())
                            sellPurchaseEntity.purchaseDate = mainActivity.simpleDateFormat.parse(dialogBinding.tvPurchaseDate.text.toString())
                            sellPurchaseEntity.categoryId = selectedGoods.id
                            plDatabase.goodsDao().insertSellPurchase(sellPurchaseEntity)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            getTransactions()
                            dialog.dismiss()
                        }
                    }
                    insert().execute()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
        return binding.root
    }

    fun getGoods(){
        class get : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
                goodsList.addAll(plDatabase.goodsDao().getGoods())
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
            }
        }
        get().execute()
    }

    fun getTransactions(){
        transactionsList.clear()
        class get : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
                transactionsList.addAll(plDatabase.goodsDao().getSellPurchase())
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                adapter.notifyDataSetChanged()
            }
        }
        get().execute()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun OnTransactionEditClicked(displayTransaction: DisplayTransaction) {
        if(goodsList.isEmpty()){
            return
        }
        var selectedGoods = Goods()
        var dialogBinding = CustomDialogHomeBinding.inflate(layoutInflater)
        var dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        var adapter = ArrayAdapter(mainActivity, android.R.layout.simple_list_item_1, goodsList)
        dialogBinding.etGoods.adapter = adapter
        var index = goodsList.indexOfFirst { element->element.id == displayTransaction.goods?.id }
        System.out.println(" index $index")
        if(index>-1) {
            selectedGoods = goodsList[index]
            dialogBinding.etGoods.setSelection(index)
        }
        dialogBinding.etGoods.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGoods = goodsList[position]
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        dialogBinding.tvPurchaseDate.setText(mainActivity.simpleDateFormat.format(displayTransaction.sellPurchaseEntity?.purchaseDate))
        dialogBinding.tvSellingDate.setText(mainActivity.simpleDateFormat.format(displayTransaction.sellPurchaseEntity?.sellingDate))
        dialogBinding.etPurchaseRate.setText(displayTransaction.sellPurchaseEntity?.purchasePrice.toString())
        dialogBinding.etSellingRate.setText(displayTransaction.sellPurchaseEntity?.sellingPrice.toString())
        var purchaseDateCalendar = Calendar.getInstance()
        purchaseDateCalendar.setTime(mainActivity.simpleDateFormat.parse(dialogBinding.tvPurchaseDate.text.toString()))
        var sellDateCalendar = Calendar.getInstance()
        sellDateCalendar.setTime(mainActivity.simpleDateFormat.parse(dialogBinding.tvSellingDate.text.toString()))

        dialogBinding.tvPurchaseDate.setOnClickListener {
            var datePicker = DatePickerDialog(mainActivity, {_, year, month,date->
                purchaseDateCalendar = Calendar.getInstance()
                purchaseDateCalendar.set(Calendar.YEAR, year)
                purchaseDateCalendar.set(Calendar.MONTH, month)
                purchaseDateCalendar.set(Calendar.DATE, date)
                dialogBinding.tvPurchaseDate.setText(mainActivity.simpleDateFormat.format(purchaseDateCalendar.time))
            }, purchaseDateCalendar.get(Calendar.YEAR), purchaseDateCalendar.get(Calendar.MONTH),purchaseDateCalendar.get(Calendar.DATE))
            datePicker.show()
        }
        dialogBinding.tvSellingDate.setOnClickListener {
            var datePicker = DatePickerDialog(mainActivity, {_, year, month,date->
                sellDateCalendar = Calendar.getInstance()
                sellDateCalendar.set(Calendar.YEAR, year)
                sellDateCalendar.set(Calendar.MONTH, month)
                sellDateCalendar.set(Calendar.DATE, date)
                dialogBinding.tvSellingDate.setText(mainActivity.simpleDateFormat.format(sellDateCalendar.time))
            }, sellDateCalendar.get(Calendar.YEAR), sellDateCalendar.get(Calendar.MONTH),sellDateCalendar.get(Calendar.DATE))
            datePicker.show()
        }
        dialogBinding.btnAddUpdate.setOnClickListener {
            if (dialogBinding.etPurchaseRate.text.toString().isNullOrEmpty()) {
                dialogBinding.etPurchaseRate.setError("Enter Purchase Rate")
            }
            else if (dialogBinding.etSellingRate.text.toString().isNullOrEmpty()) {
                dialogBinding.etSellingRate.setError("Enter Selling Rate")
            }
            else {
                class update : AsyncTask<Void, Void, Void>(){
                    override fun doInBackground(vararg p0: Void?): Void? {
                        var sellPurchaseEntity = displayTransaction.sellPurchaseEntity
                        sellPurchaseEntity?.let {
                            it.purchasePrice = dialogBinding.etPurchaseRate.text.toString()
                            it.sellingPrice = dialogBinding.etSellingRate.text.toString()
                            it.sellingDate = mainActivity.simpleDateFormat.parse(dialogBinding.tvSellingDate.text.toString())
                            it.purchaseDate = mainActivity.simpleDateFormat.parse(dialogBinding.tvPurchaseDate.text.toString())
                            it.categoryId = selectedGoods.id
                            plDatabase.goodsDao().updateSellPurchase(it)
                        }

                        return null
                    }

                    override fun onPostExecute(result: Void?) {
                        super.onPostExecute(result)
                        getTransactions()
                        dialog.dismiss()
                    }
                }
                update().execute()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun OnTransactionDeleteClicked(sellPurchaseEntity: DisplayTransaction) {
        AlertDialog.Builder(mainActivity).apply {
            setTitle(mainActivity.resources.getString(R.string.delete_transaction))
            setMessage(mainActivity.resources.getString(R.string.delete_transaction_message))
            setPositiveButton(mainActivity.resources.getString(R.string.yes)){_,_->
                class delete : AsyncTask<Void, Void, Void>(){
                    override fun doInBackground(vararg p0: Void?): Void? {
                        sellPurchaseEntity.sellPurchaseEntity?.let {
                            plDatabase.goodsDao().deleteSellPurchase(
                                it
                            )
                        }
                        return null
                    }

                    override fun onPostExecute(result: Void?) {
                        super.onPostExecute(result)
                        getTransactions()

                    }
                }
                delete().execute()
            }
            setNegativeButton(mainActivity.resources.getString(R.string.no)){_,_->}
        }.show()
    }
}