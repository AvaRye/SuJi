package com.example.suji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val itemManager = ItemManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rv_home.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = ItemAdapter(itemManager)

        }

        homeLiveData.bindNonNull(this) {

        }

    }
}
