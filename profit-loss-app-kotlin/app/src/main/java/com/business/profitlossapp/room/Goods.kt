package com.business.profitlossapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Goods {
    @PrimaryKey (autoGenerate = true)
    var id :Int =0
    @ColumnInfo(name = "goods") var goods : String ?= null

    override fun toString(): String {
        return goods?:""
    }
}