package com.example.interval

import android.graphics.Color
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.interval2.*
import com.example.interval2.databinding.FragmentMainBinding
import com.google.gson.Gson

import kotlinx.android.synthetic.main.fragment_main.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myViewModel = activity?.run {
            ViewModelProvider(this).get(MyViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val dataBinding = FragmentMainBinding.inflate(inflater, container, false)

//todo

        dataBinding.setData(myViewModel)
        dataBinding.setLifecycleOwner(this)
        //       return inflater.inflate(R.layout.fragment_main, container, false)
        return dataBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        readFile()

        //  val myViewModel = ViewModelProvider(FragmentActivity).get(MyViewModel::class.java)

        val myViewModel = activity?.run {
            ViewModelProvider(this).get(MyViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        myViewModel.texttospeech("welcome")

        //       tv_totalTime.text  = myViewModel.TOTALTIME.value.toString()
//        tv_leftTime.text = myViewModel.LEFTTIME.value.toString()
        //       tv_rightTime.text = myViewModel.RIGHTTIME.value.toString()
        //       tv_accTime.text = myViewModel.ACCTIMESTRING.value.toString()
        //       tv_restTime.text = myViewModel.RESTTIME.value.toString()
        //       tv_rep.text = myViewModel.REP.value.toString()
//監控有一個按鍵menu -> 換頁
        myViewModel.pageEnabled.observe(viewLifecycleOwner, Observer {
            if (myViewModel.pageEnabled.value == true) {
                myViewModel.pageEnabled.value = false
                findNavController().navigate(R.id.setFragment)
            }
        })


        //   val dataBinding =
        //   DataBindingUtil.inflate(LayoutInflater,R.layout.fragment_main,null,false)
//如果協程正在執行就先cancel它，預防重復開協程，這步驟很重要不然會數值跳很快
        // 當我旋轉螢幕時
        if (myViewModel.startBit == true)
            myViewModel.job.cancel()
//監聽num數值是否有變化
        myViewModel.num.observe(viewLifecycleOwner, Observer {
            if (myViewModel.startBit == true) {

                myViewModel.initialize()       //啟動協程1s
                when {
                    myViewModel.LEFTTIME.value != 0 -> {
                        myViewModel.LEFTTIME.value = myViewModel.LEFTTIME.value?.minus(1)
                        tv_totalTime.setTextColor(Color.BLACK)   //設成黑色
                        tv_leftTime.setTextColor(Color.RED)   //設成紅色
                        tv_rightTime.setTextColor(Color.BLACK)
                        tv_restTime.setTextColor(Color.BLACK)
                        //          tv_leftTime.text = myViewModel.LEFTTIME.value.toString()
                        if (myViewModel.LEFTTIME.value == 0) {
                            if (myViewModel.SELECTED == false)
                                beep1()
                            else
                                myViewModel.texttospeech("Change to Left Leg")
                        }
                    }

                    myViewModel.RIGHTTIME.value != 0 -> {
                        myViewModel.RIGHTTIME.value = myViewModel.RIGHTTIME.value?.minus(1)
                        tv_totalTime.setTextColor(Color.BLACK)   //設成黑色
                        tv_leftTime.setTextColor(Color.BLACK)
                        tv_rightTime.setTextColor(Color.RED)
                        tv_restTime.setTextColor(Color.BLACK)
                        //         tv_rightTime.text = myViewModel.RIGHTTIME.value.toString()
                        if (myViewModel.RIGHTTIME.value == 0) {
                            if (myViewModel.SELECTED == false)
                                beep2()
                            else
                                myViewModel.texttospeech("Change to rest period")
                        }

                    }
                    myViewModel.RESTTIME.value != 0 -> {
                        myViewModel.RESTTIME.value = myViewModel.RESTTIME.value?.minus(1)
                        tv_totalTime.setTextColor(Color.BLACK)   //設成黑色
                        tv_leftTime.setTextColor(Color.BLACK)
                        tv_rightTime.setTextColor(Color.BLACK)
                        tv_restTime.setTextColor(Color.RED)
                        //          tv_restTime.text = myViewModel.RESTTIME.value.toString()
                        if (myViewModel.RESTTIME.value == 0) {   //到0時重新給初值
                            if (myViewModel.SELECTED == false)
                                beeplong()
                            else
                                myViewModel.texttospeech("Change to right Leg")

                            myViewModel.LEFTTIME.value = myViewModel.s2
                            myViewModel.RIGHTTIME.value = myViewModel.s3
                            myViewModel.RESTTIME.value = myViewModel.s4
                            myViewModel.REP.value = myViewModel.REP.value?.plus(1)

                        }
                    }

                }   //when end

                myViewModel.TOTALTIME.value = myViewModel.TOTALTIME.value?.minus(1)
                //          tv_totalTime.text = myViewModel.TOTALTIME.value.toString()
                myViewModel.ACCTIME.value = myViewModel.ACCTIME.value?.plus(1)
                when {
                    myViewModel.ACCTIME.value != 0 -> {
                        var buff0: Int = it
                        var buff1: Int = buff0.rem(60)  //min
                        var buff2: Int = buff0.div(60)  //hour
                        var buff3 = ""
                        if (buff1 <= 9) {
                            buff3 = "0${buff1.toString()}"
                        } else {
                            buff3 = "${buff1.toString()}"
                        }

                        myViewModel.ACCTIMESTRING.value = "${buff2.toString()}:${buff3}"
                        //          tv_accTime.text = myViewModel.ACCTIMESTRING.value
                    }
                }


                if (myViewModel.TOTALTIME.value == 0) {
                    val toast = Toast.makeText(context, "時間到", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                    toast.show()

                    myViewModel.TOTALTIME.value = myViewModel.s1
                    myViewModel.LEFTTIME.value = myViewModel.s2
                    myViewModel.RIGHTTIME.value = myViewModel.s3
                    myViewModel.RESTTIME.value = myViewModel.s4
                    myViewModel.ACCTIME.value = 0
                    myViewModel.REP.value = 0

                    tv_totalTime.setTextColor(Color.BLACK)   //設成黑色
                    tv_leftTime.setTextColor(Color.BLACK)
                    tv_rightTime.setTextColor(Color.BLACK)
                    tv_restTime.setTextColor(Color.BLACK)
                    myViewModel.pauseProc()
                }
            }   // startbit end

        })  //observe num

        /*      btnStart.setOnClickListener {

              } */

        btnStart.setOnClickListener {
            if (myViewModel.startBit == false) {
                myViewModel.startBit = true
                myViewModel.num.value = myViewModel.num.value  //將所有資料更新一次
                //       myViewModel.startProc()
            } else {
                Toast.makeText(context, "不要再按了", Toast.LENGTH_SHORT).show()
            }

        }

        btnExit.setOnClickListener {
            System.exit(0)
        }

        btnPause.setOnClickListener {
            myViewModel.pauseProc()
        }

        //      dataBinding.setData(myViewModel)
        //     dataBinding.setLifecycleOwner(this)
    }

    private fun beep1() {
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)  //beep 1
        //      toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 1000)  // beep 2
    }

    private fun beep2() {
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        //      toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)  //beep 1
        toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 1000)  // beep 2
    }

    private fun beeplong() {
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 1000)  //beep 1
        //     toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 1000)  // beep 2
    }


    fun readFile() {
        val fileName = "my_file"
        val gson = Gson()
        var sb = StringBuffer()

        val myViewModel = activity?.run {
            ViewModelProvider(this).get(MyViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        if (myViewModel.poweronEnabled == false) {
            myViewModel.poweronEnabled = true
            //進來先讀出值
            // read file....................
            try {
                val inputStream = getActivity()?.openFileInput(fileName)
                val bytes = ByteArray(1024)
                //    val sb = StringBuffer()
                while (inputStream?.read(bytes) != -1) {         //1次讀1024byte
                    sb.append(String(bytes))
                }

                var ss: String = ""                         // 必須取出正確的值才行
                for (i in sb) {                             //這一部分是字串錯誤, 看起來是對的但是長度卻有 1024
                    if (i == '}') {                         // 重新存入一個新的字串內
                        ss = ss + i
                        break                               // 跳出回圈
                    } else {
                        ss = ss + i
                    }
                }

                var data3 = gson.fromJson(ss, DataSetRecord::class.java)
                myViewModel.s1 = data3.totaltime
                myViewModel.s2 = data3.leftlegtime
                myViewModel.s3 = data3.righlegtime
                myViewModel.s4 = data3.resttime
                myViewModel.s5 = data3.selected

                myViewModel.TOTALTIME.value = data3.totaltime
                myViewModel.LEFTTIME.value = data3.leftlegtime
                myViewModel.RIGHTTIME.value = data3.righlegtime
                myViewModel.RESTTIME.value = data3.resttime
                myViewModel.SELECTED = data3.selected

            } catch (e: Exception) {
                e.printStackTrace()
                println("檔案不存在")
// 設初值
                myViewModel.s1 = TOTALTIMECONST
                myViewModel.s2 = LEFTTIMECONST
                myViewModel.s3 = RIGHTTIMECONST
                myViewModel.s4 = RESTTIMECONST
                myViewModel.s5 = false
                myViewModel.TOTALTIME.value = TOTALTIMECONST
                myViewModel.LEFTTIME.value = LEFTTIMECONST
                myViewModel.RIGHTTIME.value = RIGHTTIMECONST
                myViewModel.RESTTIME.value = RESTTIMECONST
                myViewModel.SELECTED = false
            }

        }

    }    //readfile end


    /*  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu,menu)  //裝上menu
    return super.onCreateOptionsMenu(menu)
} */


}

