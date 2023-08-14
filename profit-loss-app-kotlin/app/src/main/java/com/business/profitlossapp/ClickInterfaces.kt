package com.business.profitlossapp

import com.business.profitlossapp.room.DisplayTransaction
import com.business.profitlossapp.room.Goods
import com.business.profitlossapp.room.SellPurchaseEntity

interface ClickInterfaces {
}

interface GoodsClicked{
    fun OnGoodsEditClicked(goods: Goods)
    fun OnGoodsDeleteClicked(goods: Goods)
}

interface TransactionClicked{
    fun OnTransactionEditClicked(goods: DisplayTransaction)
    fun OnTransactionDeleteClicked(goods: DisplayTransaction)
}