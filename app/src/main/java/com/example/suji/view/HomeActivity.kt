package com.example.suji.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.suji.*
import com.example.suji.commons.Item
import com.example.suji.commons.ItemAdapter
import com.example.suji.commons.ItemManager
import com.example.suji.model.*
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private val itemManager = ItemManager()
    private val today: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tv_home_write.setOnClickListener {
            val intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
        }

        val today = SimpleDateFormat("yyyy-MM-", Locale("zh_CN")).format(Date())//2020-05-

        /*
        this.database.use {
            replace(
                DiaryTable.table,
                DiaryTable.id to null,
                DiaryTable.diaryId to "2020-05-23",
                DiaryTable.content to "哈喽啊 树哥"
            )
        }*/
        database.postLiveData(today)


        val todayItems = getItems()

        rv_home.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = ItemAdapter(itemManager)
            itemManager.addAll(todayItems)
        }


    }

    private fun getItems(): List<Item> {
        //本月
        val items = mutableListOf<Item>()
        //先占位
        val firstDay = Calendar.getInstance()
        firstDay.set(Calendar.DAY_OF_MONTH, 1)
        repeat(this.today.get(Calendar.DAY_OF_MONTH)) {
            items.apply {
                when (firstDay.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.SATURDAY, Calendar.SUNDAY -> homeItem {
                        bonus.apply {
                            visibility = View.VISIBLE
                            text = "待"
                            textColor =
                                ContextCompat.getColor(this@HomeActivity, R.color.colorPrimary)
                            setOnClickListener {
                                val intent = Intent(this@HomeActivity, DiaryActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                    else -> homeItem {
                        bonus.apply {
                            visibility = View.VISIBLE
                            text = "待"
                            textColor =
                                ContextCompat.getColor(this@HomeActivity, R.color.colorBlack)
                            setOnClickListener {
                                val intent = Intent(this@HomeActivity, DiaryActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
                firstDay.add(Calendar.DAY_OF_WEEK, 1)
            }
        }

        //再加个明日再续
        items.homeItem {
            bonus.apply {
                isClickable = false
                visibility = View.VISIBLE
                text = "明日再续"
                textColor = when (this@HomeActivity.today.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.FRIDAY, Calendar.SATURDAY -> ContextCompat.getColor(
                        this@HomeActivity,
                        R.color.colorPrimary
                    )
                    else -> ContextCompat.getColor(
                        this@HomeActivity,
                        R.color.colorBlack
                    )
                }
            }
        }

        //再替换
        diaryLiveData.bindNonNull(this) { list ->
            //TODO:数据库处理
        }

        return items
    }

}

