package com.example.interval

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.interval2.*
import com.example.interval2.databinding.FragmentSetBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_set.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SetFragment : Fragment() {
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
        val dataBinding = FragmentSetBinding.inflate(inflater, container, false)
//todo

        dataBinding.setData(myViewModel)
        dataBinding.setLifecycleOwner(this)
        //       return inflater.inflate(R.layout.fragment_main, container, false)
        return dataBinding.root
        // Inflate the layout for this fragment


        //     return inflater.inflate(R.layout.fragment_set, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fileName = "my_file"
        val gson = Gson()
        var sb = StringBuffer()

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

            var jsonData3 = gson.fromJson(ss, DataSetRecord::class.java)
            setTotalTime.setText(jsonData3.totaltime.toString())
            setLeftLegTime.setText(jsonData3.leftlegtime.toString())
            setRightLegTime.setText(jsonData3.righlegtime.toString())
            setRestTime.setText(jsonData3.resttime.toString())
            Log.d(TAG, "jsonData3.selected =${jsonData3.selected} ")
            checkBox.isChecked = jsonData3.selected

        } catch (e: Exception) {
            e.printStackTrace()
            val myViewModel = activity?.run {
                ViewModelProvider(this).get(MyViewModel::class.java)
            } ?: throw Exception("Invalid Activity")

            println("檔案不存在2")
            // 設初值
            myViewModel.s1 = TOTALTIMECONST
            myViewModel.s2 = LEFTTIMECONST
            myViewModel.s3 = RIGHTTIMECONST
            myViewModel.s4 = RESTTIMECONST
            myViewModel.TOTALTIME.value = TOTALTIMECONST
            myViewModel.LEFTTIME.value = LEFTTIMECONST
            myViewModel.RIGHTTIME.value = RIGHTTIMECONST
            myViewModel.RESTTIME.value = RESTTIMECONST
            setTotalTime.setText(TOTALTIMECONST.toString())
            setLeftLegTime.setText(LEFTTIMECONST.toString())
            setRightLegTime.setText(RIGHTTIMECONST.toString())
            setRestTime.setText(RESTTIMECONST.toString())
        }


        //================================================================================  */

        btnOk.setOnClickListener {
            // 回存到viewmodel 內, 統一管理數據
            val myViewModel = activity?.run {
                ViewModelProvider(this).get(MyViewModel::class.java)
            } ?: throw Exception("Invalid Activity")

            Log.d(TAG, "checkBox.isChecked = ${checkBox.isChecked} ")
            // check box
            var selectedVoice = checkBox.isChecked


//檔案不存在,導航會當機
            // set Json
            myViewModel.s1  = setTotalTime.text.toString().toInt()
            myViewModel.s2 = setLeftLegTime.text.toString().toInt()
            myViewModel.s3 = setRightLegTime.text.toString().toInt()
            myViewModel.s4 = setRestTime.text.toString().toInt()
            myViewModel.s5 = checkBox.isChecked   //20200831
            var s11 = myViewModel.s1.toString().toInt()
            var s12 = myViewModel.s2.toString().toInt()
            var s13 = myViewModel.s3.toString().toInt()
            var s14= myViewModel.s4.toString().toInt()
            var s15 = myViewModel.s5

            val data0 = DataSetRecord(s11,s12,s13,s14,s15)

            //        val data1 =  DataSetRecord(1000, 30, 30, 60)
            //        val dataArray = listOf<DataSetRecord>(data0,data1)

            var jsonData = gson.toJson(data0)
       //     println("jsonData = ${jsonData}")

            // write file
            try {
                val outputStream = getActivity()?.openFileOutput(fileName, Context.MODE_PRIVATE)
                outputStream?.write(jsonData.toByteArray())
                outputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            } //catch end
//載入值
            myViewModel.TOTALTIME.value = s11
            myViewModel.LEFTTIME.value = s12
            myViewModel.RIGHTTIME.value = s13
            myViewModel.RESTTIME.value = s14
            myViewModel.SELECTED = s15
            //=============================================================================
        //    findNavController().navigate(R.id.mainFragment)


   //         tabLayout.getTabAt(0)?.select()

   //        viewPage2.setCurrentItem(0)  會當機
        }   //btnok end */

    }   //on created

}
