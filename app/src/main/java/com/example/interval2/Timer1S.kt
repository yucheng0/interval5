package com.example.interval

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.AsyncTask
import android.speech.tts.TextToSpeech
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*


class Timer1S(var TOTALTIME:Int,var LEFTTIME:Int,var RIGHTTIME:Int,var RESTTIME:Int): AsyncTask<Context, Int, Int>() {
    lateinit var tv_totalTime:TextView
    lateinit var tv_leftTime:TextView
    lateinit var tv_rightTime:TextView
     lateinit var tv_restTime:TextView

  companion object {
  //    var num = 0
  /*    var TOTALTIME = 1200
      var LEFTTIME  = 30
      var RIGHTTIME = 30
      var RESTTIME = 60 */

  }

    override fun doInBackground(vararg p0: Context?): Int {
        while(true) {
            Thread.sleep(1000)
  //          num++
            publishProgress()
    }
}

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)

 //       println(num)
          TOTALTIME -= 1
         if (LEFTTIME != 0)
        {
            LEFTTIME -= 1
            tv_totalTime.setTextColor(android.graphics.Color.BLACK)   //設成黑色
            tv_leftTime.setTextColor(android.graphics.Color.RED)   //設成紅色
          tv_rightTime.setTextColor(android.graphics.Color.BLACK)
           tv_restTime.setTextColor(android.graphics.Color.BLACK)
            tv_leftTime.text = LEFTTIME.toString()

            if (LEFTTIME == 0)
            beep1()

        } else if (RIGHTTIME !=0) {
             RIGHTTIME -= 1
            tv_totalTime.setTextColor(android.graphics.Color.BLACK)   //設成黑色
            tv_leftTime.setTextColor(android.graphics.Color.BLACK)
            tv_rightTime.setTextColor(android.graphics.Color.RED)
          tv_restTime.setTextColor(android.graphics.Color.BLACK)
             tv_rightTime.text = RIGHTTIME.toString()
             if (RIGHTTIME == 0)
             beep2()

         } else {
             RESTTIME -= 1
           tv_totalTime.setTextColor(android.graphics.Color.BLACK)   //設成黑色
            tv_leftTime.setTextColor(android.graphics.Color.BLACK)
            tv_rightTime.setTextColor(android.graphics.Color.BLACK)
           tv_restTime.setTextColor(android.graphics.Color.RED)
             tv_restTime.text = RESTTIME.toString()
             if (RESTTIME == 0) {        //到0時重新給初值
                  beeplong()
                  LEFTTIME  = 30
                  RIGHTTIME = 30
                  RESTTIME = 60
             }
         }

        tv_totalTime.text = TOTALTIME.toString()
    }
    fun beep1() {
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)  //beep 1
  //      toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 1000)  // beep 2
    }

    fun beep2() {
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        //      toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)  //beep 1
        toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 1000)  // beep 2
    }

    fun beeplong() {
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 1000)  //beep 1
  //     toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 1000)  // beep 2
    }

}