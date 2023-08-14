package com.business.profitlossapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.business.profitlossapp.databinding.FragmentGoodsListBinding
import com.business.profitlossapp.databinding.LayoutAddUpdateCatBinding
import com.business.profitlossapp.room.Goods
import com.business.profitlossapp.room.PLDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GoodsList.newInstance] factory method to
 * create an instance of this fragment.
 */

class GoodsList : Fragment(), GoodsClicked {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentGoodsListBinding
    private lateinit var mainActivity: MainActivity
    var goodsList: ArrayList<Goods> = ArrayList()
    lateinit var adapter: GoodsAdapter
    lateinit var plDatabase: PLDatabase
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
        binding = FragmentGoodsListBinding.inflate(layoutInflater)
        adapter = GoodsAdapter(goodsList, this)
        binding.recyclerGoods.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerGoods.adapter = adapter
        plDatabase = PLDatabase.getDatabase(mainActivity)
        getGoods()
        binding.fabAdd.setOnClickListener {
            var dialog = Dialog(mainActivity)
            var dialogBinding = LayoutAddUpdateCatBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialogBinding.btnAddUpdate.setText(mainActivity.resources.getString(R.string.add))
            dialogBinding.btnDelete.visibility = View.GONE
            dialogBinding.etCategory.doOnTextChanged { text, start, before, count ->
                if(text.isNullOrEmpty()){
                    dialogBinding.tilCategory.error = mainActivity.resources.getString(R.string.enter_category_name)
                }else{
                    dialogBinding.tilCategory.error = null
                }
            }
            dialogBinding.btnAddUpdate.setOnClickListener {
                if(dialogBinding.etCategory.text.isNullOrEmpty()){
                    dialogBinding.tilCategory.error = mainActivity.resources.getString(R.string.enter_category_name)
                }else{
                    class insert : AsyncTask<Void, Void, Void>(){
                        override fun doInBackground(vararg p0: Void?): Void? {
                            var goods = Goods()
                            goods.goods = dialogBinding.etCategory.text.toString()
                            plDatabase.goodsDao().insertGoods(goods)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            getGoods()
                            dialog.dismiss()
                        }
                    }
                    insert().execute()
                }
            }
            dialog.show()
        }
        return binding.root
    }

    fun getGoods(){
        goodsList.clear()
        class get : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
                goodsList.addAll(plDatabase.goodsDao().getGoods())
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
         * @return A new instance of fragment GoodsList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GoodsList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun OnGoodsEditClicked(goods: Goods) {
        var dialog = Dialog(mainActivity)
        var dialogBinding = LayoutAddUpdateCatBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.btnAddUpdate.setText(mainActivity.resources.getString(R.string.update))
        dialogBinding.btnDelete.visibility = View.VISIBLE
        dialogBinding.etCategory.setText(goods.goods)
        dialogBinding.etCategory.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrEmpty()){
                dialogBinding.tilCategory.error = mainActivity.resources.getString(R.string.enter_category_name)
            }else{
                dialogBinding.tilCategory.error = null
            }
        }
        dialogBinding.btnDelete.setOnClickListener {
            dialog.dismiss()
            OnGoodsDeleteClicked(goods)
        }
        dialogBinding.btnAddUpdate.setOnClickListener {
            if(dialogBinding.etCategory.text.isNullOrEmpty()){
                dialogBinding.tilCategory.error = mainActivity.resources.getString(R.string.enter_category_name)
            }else{
                class update : AsyncTask<Void, Void, Void>(){
                    override fun doInBackground(vararg p0: Void?): Void? {
                        goods.goods = dialogBinding.etCategory.text.toString()
                        plDatabase.goodsDao().updateGoods(goods)
                        return null
                    }

                    override fun onPostExecute(result: Void?) {
                        super.onPostExecute(result)
                        getGoods()
                        dialog.dismiss()
                    }
                }
                update().execute()
            }
        }
        dialog.show()
    }

    override fun OnGoodsDeleteClicked(goods: Goods) {
        AlertDialog.Builder(mainActivity).apply {
            setTitle(mainActivity.resources.getString(R.string.delete_category))
            setMessage(mainActivity.resources.getString(R.string.delete_cat_message))
            setPositiveButton(mainActivity.resources.getString(R.string.yes)){_,_->
                class delete : AsyncTask<Void, Void, Void>(){
                    override fun doInBackground(vararg p0: Void?): Void? {
                        plDatabase.goodsDao().deleteGoods(goods)
                        return null
                    }

                    override fun onPostExecute(result: Void?) {
                        super.onPostExecute(result)
                        getGoods()

                    }
                }
                delete().execute()
            }
            setNegativeButton(mainActivity.resources.getString(R.string.no)){_,_->}
        }.show()
    }
}