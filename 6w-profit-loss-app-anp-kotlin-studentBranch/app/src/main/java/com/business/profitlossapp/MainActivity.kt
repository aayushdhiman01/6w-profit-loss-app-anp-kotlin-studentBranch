package com.business.profitlossapp

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.business.profitlossapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.navController)
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->{
                    navController.navigate(R.id.homeFragment)
                }
                R.id.good->{
                    navController.navigate(R.id.goodsList)
                }
                R.id.companyInfo->{
                    navController.navigate(R.id.companyInfoFragments)
                }
                R.id.goodsProfit->{
                    navController.navigate(R.id.goodsProfitFragment)
                }
                else->{
                    navController.navigate(R.id.homeFragment)

                }
            }
            return@setOnItemSelectedListener true
        }
        sharedPreferences = getSharedPreferences(resources.getString(R.string.app_name),
            Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
}