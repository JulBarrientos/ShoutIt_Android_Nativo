package com.example.juliandbarrientos.shoutit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast

import com.facebook.*

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*

import java.util.*


@SuppressLint("Registered")
/**
 * Created by julian.d.barrientos on 9/28/2017.
 */
class LogInActivity : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val TAG = "SignInActivity"
    private val RC_SIGN_IN = 9001

    var mGoogleApiClient : GoogleApiClient? = null
    private var mAuth: FirebaseAuth? = null


    var LLBotonera : LinearLayout? = null
    var progressBar : ProgressBar? = null

    lateinit var facebook_login: Button
    lateinit var google_login : Button
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        setItems()
        setHandleItem()

        FacebookSdk.sdkInitialize(applicationContext)
        this.callbackManager = CallbackManager.Factory.create()
        faceBookInitialize()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_clien_auth))
                .requestEmail()
                .build()
        this.mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        this.mAuth = FirebaseAuth.getInstance()

    }

    private fun setItems(){
        this.LLBotonera = findViewById<LinearLayout>(R.id.botonera)
        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.facebook_login = findViewById<Button>(R.id.facebook_login)
        this.google_login = findViewById<Button>(R.id.google_log_in)
    }

    private fun setHandleItem(){
        this.facebook_login.setOnClickListener(this)
        this.google_login.setOnClickListener(this)
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.getCurrentUser()
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        //updateUI(currentUser)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        this.mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = mAuth!!.currentUser
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                //updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }

            // ...
        }
    }

    override fun onClick(v:View) {
        LLBotonera!!.visibility = View.GONE
        progressBar!!.visibility = View.VISIBLE
        when(v.id){
            R.id.google_log_in ->
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient), RC_SIGN_IN)
            R.id.facebook_login ->
                LoginManager.getInstance().logInWithReadPermissions(this@LogInActivity,  Arrays.asList("public_profile"))
            else -> { // Note the block
                print("x is neither 1 nor 2")
            }
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount? = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }

    }

    //*********************** Face book start*****************************//

    fun faceBookInitialize() {
        ////////////////////////////////////////////FACEBOOK///////////////////////////////////////////////////////////////////////////

        println("yess on fcbk")
        LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)


            }

            override fun onCancel() {
                //TODO Auto-generated method stub
                println("=========================onCancel")
                Toast.makeText(this@LogInActivity, "Cancel", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException) {
                //TODO Auto-generated method stub
                println("=========================onError" + error.toString())
                Toast.makeText(this@LogInActivity, "onError", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun handleFacebookAccessToken( token:AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:" + token)

        val  credential :AuthCredential = FacebookAuthProvider.getCredential(token.token)

        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this, {
                    task : Task<AuthResult> ->kotlin.run {
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user :FirebaseUser = mAuth!!.currentUser!!
                        val intent = Intent(this@LogInActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        // updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(this@LogInActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        //   updateUI(null);
                    }
                }
                })


    }
}


