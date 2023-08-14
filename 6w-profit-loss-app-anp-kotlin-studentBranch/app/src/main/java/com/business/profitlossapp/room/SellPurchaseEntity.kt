package com.business.profitlossapp.room

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*


@Entity(foreignKeys = [
    ForeignKey(entity = Goods::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = CASCADE)])
@TypeConverters(DateConverter::class)
class SellPurchaseEntity {
    @PrimaryKey (autoGenerate = true)
    var id :Int =0

    @ColumnInfo(name = "purchaseDate") var purchaseDate : Date?= null
    @ColumnInfo(name = "purchasePrice") var purchasePrice : String ?= null
    @ColumnInfo(name = "sellingDate") var sellingDate : Date ?= null
    @ColumnInfo(name = "sellingPrice") var sellingPrice : String ?= null
    @ColumnInfo(name = "categoryId") var categoryId : Int ?= null
}