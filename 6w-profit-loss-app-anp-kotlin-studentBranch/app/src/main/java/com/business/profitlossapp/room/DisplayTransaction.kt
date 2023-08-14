package com.business.profitlossapp.room

import androidx.room.Embedded
import androidx.room.Relation


class DisplayTransaction {
        @Embedded
        var sellPurchaseEntity: SellPurchaseEntity? = null

        @Relation(parentColumn = "categoryId", entityColumn = "id")
        var goods: Goods? = null

}