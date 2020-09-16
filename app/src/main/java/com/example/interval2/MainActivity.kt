package com.example.interval2

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.interval.MainFragment
import com.example.interval.MyViewModel
import com.example.interval.SetFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

//集合ViewModel, Livedata, Databinding, coroutines.const, toast 於一體的程式
// toast 可以調整位置顯示
// ? 綁定的函式好像不能使用toast , 它的第一個Context 不知道是誰？


const val TOTALTIMECONST = 1200          //1200
const val LEFTTIMECONST = 30            // 30
const val RIGHTTIMECONST = 30            // 30
const val RESTTIMECONST = 60            // 60
const val ACCTIMECONST = 0            // 0
const val REPCONST = 0            // 0
val TAG = "myTag"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
       setContentView(R.layout.activity_main)
        val myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.context = this      //這是重點,不傳會當機


        viewPage2.adapter = object: FragmentStateAdapter(this){
            override fun getItemCount()= 2

            override fun createFragment(position: Int): Fragment =
                when(position) {
                    0-> MainFragment()
                   else -> SetFragment()
                }
        }

        TabLayoutMediator(tabLayout,viewPage2) { tab, position ->
            when (position) {
                0 -> tab.text = "主頁"
                1 -> tab.text = "設定"
                else -> tab.text = "其它"
            }
        }.attach()

 //       val myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

    }

    //按下Menu 的按鍵的動作, 重新再來
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      val myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        if (item.itemId == R.id.menuRest) {
            println ("3=$myViewModel")
            myViewModel.TOTALTIME.value = myViewModel.s1
            myViewModel.LEFTTIME.value = myViewModel.s2
            myViewModel.RIGHTTIME.value = myViewModel.s3
            myViewModel.RESTTIME.value = myViewModel.s4
            myViewModel.REP.value = 0
            myViewModel.ACCTIME.value = 0
            tv_totalTime.setTextColor(android.graphics.Color.BLACK)   //設成黑色
            tv_leftTime.setTextColor(android.graphics.Color.BLACK)
            tv_rightTime.setTextColor(android.graphics.Color.BLACK)
            tv_restTime.setTextColor(android.graphics.Color.BLACK)
            myViewModel.pauseProc()
        }
        if (item.itemId == R.id.menu_page){
     //       myViewModel.pageEnabled.value =  true
            viewPage2.setCurrentItem(1)         //跳到page1
        }
            return super.onOptionsItemSelected(item)
        }


    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)  //裝上menu
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        viewPage2.setCurrentItem(0)         //跳到page0
        return super.onSupportNavigateUp()
    }

}



