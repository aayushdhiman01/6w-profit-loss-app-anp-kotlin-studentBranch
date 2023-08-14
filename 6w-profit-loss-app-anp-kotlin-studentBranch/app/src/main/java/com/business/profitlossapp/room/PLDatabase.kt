package com.business.profitlossapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Goods::class, SellPurchaseEntity::class], version = 1)
abstract class PLDatabase() : RoomDatabase(){
    abstract fun goodsDao(): GoodsDao

    companion object{
        var goodsDatabase: PLDatabase?= null
        @Synchronized
        fun getDatabase(context: Context): PLDatabase {
            if(goodsDatabase == null){
                goodsDatabase = Room.databaseBuilder(
                     context,
                     PLDatabase::class.java, "profitLoss"
                 ).build()
            }
            return goodsDatabase!!
        }
    }

}