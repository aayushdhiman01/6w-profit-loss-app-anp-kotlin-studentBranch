package com.business.profitlossapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.business.profitlossapp.room.DisplayTransaction
import java.text.SimpleDateFormat

class SellPurchaseAdapter(var context: Context, var sellPurchaseEntity: ArrayList<DisplayTransaction>, var transactionClicked: TransactionClicked) : RecyclerView.Adapter<SellPurchaseAdapter.ViewHolder>() {
    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var tvGoods : TextView = view.findViewById(R.id.tvGoods)
        var tvPurchaseDate : TextView = view.findViewById(R.id.tvPurchaseDate)
        var tvPurchaseRate : TextView = view.findViewById(R.id.tvPurchaseRate)
        var tvSellDate : TextView = view.findViewById(R.id.tvSellDate)
        var tvSellRate : TextView = view.findViewById(R.id.tvSellRate)
        var lauout : LinearLayout = view.findViewById(R.id.linearlayout)
//        var tvProfitOrLoss : TextView = view.findViewById(R.id.tvProfitOrLoss)
        var ivEdit : TextView = view.findViewById(R.id.ivEdit)
        var ivDelete : TextView = view.findViewById(R.id.ivDelete)
//        var ivProfitLoss : ImageView = view.findViewById(R.id.ivProfitLoss)
        var cdCard: CardView = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_transaction, parent, false)
        return ViewHolder(view)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGoods.setText(sellPurchaseEntity[position].goods?.goods.toString())
        holder.tvPurchaseDate.setText("Buy Price: "+SimpleDateFormat("yyyy-MM-dd").format(sellPurchaseEntity[position].sellPurchaseEntity?.purchaseDate))
        holder.tvPurchaseRate.setText("₹"+sellPurchaseEntity[position].sellPurchaseEntity?.purchasePrice)
        holder.tvSellDate.setText("Sell Price: "+SimpleDateFormat("yyyy-MM-dd").format(sellPurchaseEntity[position].sellPurchaseEntity?.sellingDate))
        holder.tvSellRate.setText("₹"+sellPurchaseEntity[position].sellPurchaseEntity?.sellingPrice)
        holder.ivEdit.setOnClickListener { transactionClicked.OnTransactionEditClicked(sellPurchaseEntity[position]) }
        holder.ivDelete.setOnClickListener { transactionClicked.OnTransactionDeleteClicked(sellPurchaseEntity[position]) }

        if(((sellPurchaseEntity[position].sellPurchaseEntity?.sellingPrice?:"0").toDouble())>((sellPurchaseEntity[position].sellPurchaseEntity?.purchasePrice?:"0").toDouble())){
            holder.lauout.setBackgroundResource(R.drawable.rounded)
        }else if(((sellPurchaseEntity[position].sellPurchaseEntity?.sellingPrice?:"0").toDouble())==((sellPurchaseEntity[position].sellPurchaseEntity?.purchasePrice?:"0").toDouble())){
            holder.lauout.setBackgroundResource(R.drawable.rounded)
        }else{
            holder.lauout.setBackgroundResource(R.drawable.rounded2)
        }
    }

    override fun getItemCount(): Int {
        return sellPurchaseEntity.size
    }
}