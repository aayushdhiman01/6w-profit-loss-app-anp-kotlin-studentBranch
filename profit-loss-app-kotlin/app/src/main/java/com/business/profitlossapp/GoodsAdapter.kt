package com.business.profitlossapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.business.profitlossapp.room.Goods

class GoodsAdapter(var goodsList: ArrayList<Goods>, var goodsClicked: GoodsClicked) : RecyclerView.Adapter<GoodsAdapter.ViewHolder>() {
    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var tvGood : TextView = view.findViewById(R.id.tvGoodsName)
        var ivEdit : ImageView = view.findViewById(R.id.ivEdit)
        var ivDelete : ImageView = view.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_goods, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGood.setText(goodsList[position].goods)
        holder.ivEdit.setOnClickListener { goodsClicked.OnGoodsEditClicked(goodsList[position]) }
        holder.ivDelete.setOnClickListener { goodsClicked.OnGoodsDeleteClicked(goodsList[position]) }
    }

    override fun getItemCount(): Int {
        return goodsList.size
    }
}