package com.example.juliandbarrientos.shoutit.Utils

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.juliandbarrientos.shoutit.Objects.AudioClass
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.example.juliandbarrientos.shoutit.Views.VisualizerView
import android.widget.TextView
import bolts.Bolts
import com.example.juliandbarrientos.shoutit.R
import org.firezenk.audiowaves.Visualizer


/**
 * Created by julian.d.barrientos on 11/9/2017.
 */
class ManejoAudio (user: FirebaseUser, mStorageRef: StorageReference, audiDBRef: DatabaseReference): MediaPlayer.OnCompletionListener{

        var titleDialog   : String = "Recording"
        var messageDialog : String = ""
        private lateinit var recorder : MediaRecorder
        private lateinit var player   : MediaPlayer
        private lateinit var file     : File
        private val _user        : FirebaseUser      = user
        private val _mStorageRef : StorageReference  = mStorageRef
        private var _audiDBRef   : DatabaseReference = audiDBRef

        private lateinit var simpleAlert : AlertDialog

        private lateinit var waveRecorder   : Visualizer

        private lateinit var context : Activity
        private val DIRECTORY_NAME_TEMP = "AudioTemp"
        private val REPEAT_INTERVAL = 40

        private lateinit var txtRecuscord      : TextView
        private lateinit var visualizerView : VisualizerView
        private val handle         : Handler  = Handler()

        private var runningFlag : Boolean = false

        private var fileName : String = ""
        private var dateNow  : String = ""
        private lateinit var path     : File

        fun init(context : Activity){
            this.context = context
            recorder = MediaRecorder()

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            path = File (Environment.getExternalStorageDirectory().path)
        }
        fun startRecording(){
            showDialog()
            dateNow = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format( Date())
            fileName = "audio" + dateNow.replace(":",".")+".3gp"

            try {
                file = File.createTempFile (fileName, ".3gp", path)
            } catch (e: IOException) {
                Log.d(TAG,e.message)
            }

            recorder.setOutputFile(file.absolutePath)
            try {
                recorder.prepare ()
            } catch (e: IOException) {
                Log.d(TAG,e.message)
            }
            recorder.start()
            runningFlag = true
            handle.postDelayed({
                if(runningFlag)
                    stopRecording(false)
            }, 4500)
        }

        fun stopRecording(abort : Boolean){
            runningFlag = false
            recorder.stop ()
            recorder.release ()
            handle.removeCallbacksAndMessages(null)
            if(!abort)
                uploadAudio( AudioClass(fileName, _user.uid, dateNow))
        }

        fun playAudio(path: String){
            player = MediaPlayer ()
            player.setOnCompletionListener (this)
            try {
                player.setDataSource(path)
            } catch (e: IOException) {
            }

            try {
                player.prepare ()
            } catch (e: IOException) {
            }
            player.start()
        }

        fun stopAudio() {
            player.stop()
            player.release()
        }
        fun uploadAudio(audio:AudioClass){
            _mStorageRef.child("Audios/"+audio._name).putFile(Uri.fromFile(file))
            _audiDBRef.push().setValue(audio)
        }

        override fun onCompletion(mp: MediaPlayer?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        private fun showDialog(){
            simpleAlert = AlertDialog.Builder(context).create()
            simpleAlert.setTitle(titleDialog)
            val inflater : LayoutInflater =  context.layoutInflater
            val dialogView : View = inflater.inflate(R.layout.activity_recording,null)
            simpleAlert.setView(dialogView)
            waveRecorder = dialogView.findViewById(R.id.waveSound)
            waveRecorder.startListening()
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "STOP", {
                dialogInterface, i ->
                stopRecording(false)

            })
            simpleAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "ABORT", {
                dialogInterface, i ->
                stopRecording(true)
            })

            simpleAlert.show()
        }
}