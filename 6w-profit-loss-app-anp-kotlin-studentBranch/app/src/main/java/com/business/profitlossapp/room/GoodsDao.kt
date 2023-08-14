package com.business.profitlossapp.room

import androidx.room.*
import com.business.profitlossapp.room.Goods

@Dao
interface GoodsDao {
    @Query("SELECT * FROM Goods")
    fun getGoods(): List<Goods>

    @Insert
    fun insertGoods(vararg goods: Goods)

    @Update
    fun updateGoods(vararg goods: Goods)

    @Delete
    fun deleteGoods(vararg goods: Goods)


    @Query("SELECT * FROM SellPurchaseEntity")
    fun getSellPurchase(): List<DisplayTransaction>

    @Insert
    fun insertSellPurchase(vararg goods: SellPurchaseEntity)

    @Update
    fun updateSellPurchase(vararg goods: SellPurchaseEntity)

    @Delete
    fun deleteSellPurchase(vararg goods: SellPurchaseEntity)


    @Query("SELECT * FROM SellPurchaseEntity where categoryId = :id ")
    fun getSelectedTransactions(id:Int): List<SellPurchaseEntity>
}