package com.example.practice1

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mp:MediaPlayer
    private var totaltime:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mp= MediaPlayer.create(this,R.raw.song)
        mp.isLooping=true
        mp.setVolume(0.5f,1.5f)
        totaltime=mp.duration

        //volume bar
        seek_vol.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean)
                {
                    if(fromUser){
                        var volumeNum=progress/100.0f
                        mp.setVolume(volumeNum,volumeNum)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    TODO("Not yet implemented")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    TODO("Not yet implemented")
                }
            }
        )

        //postion bar

        seek_time.max=totaltime
        seek_time.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean)
                {
                    if(fromUser){
                        mp.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    TODO("Not yet implemented")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    TODO("Not yet implemented")
                }
            }

        )

        //thread

        Thread(Runnable {
            while (mp!=null){
                try{
                    var msg=Message()
                    msg.what=mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)

                }catch (e:InterruptedException){

                }
            }
        }).start()

    }

    var handler= object :Handler(){
        override fun handleMessage(msg: Message) {
            var currentPosition=msg.what
            seek_time.progress=currentPosition

            var elapsed=createTimeLable(currentPosition)
            elapsed_time.text=elapsed

            var remaining=createTimeLable(totaltime - currentPosition)
            remaining_time.text="-$remaining"
        }
    }

    fun createTimeLable(time:Int): String{
        var timeLabel=""
        var min=time/1000/60
        var sec=time/1000%60

        timeLabel="$min:"
        if(sec<10) timeLabel+="0"
        timeLabel+=sec
        return timeLabel

    }

    fun play(view: View){

        if(mp.isPlaying)
        {
            //stop
            mp.pause()
            play_btn.setBackgroundResource(R.drawable.ic_baseline_playit)
        }
        else
        {
            //start
            mp.start()
            play_btn.setBackgroundResource(R.drawable.ic_baseline_stopit)

        }

    }
}