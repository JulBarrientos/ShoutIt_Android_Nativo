package com.example.juliandbarrientos.shoutit.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import com.example.juliandbarrientos.shoutit.R
import org.w3c.dom.Text
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signin.*
import android.support.annotation.NonNull
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import android.R.attr.password
import android.R.attr.password
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var mAuth: FirebaseAuth? = null

    lateinit var btnCrear : Button
    lateinit var edtText: EditText
    lateinit var passText : EditText

    override fun onClick(p0: View?) {

        mAuth!!.createUserWithEmailAndPassword(this.edtText.text.toString(), this.passText.text.toString())
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                       // Log.d(FragmentActivity.TAG, "createUserWithEmail:onComplete:" + task.isSuccessful())

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(this@SignInActivity, "La cagaste",
                                    Toast.LENGTH_SHORT).show()
                        }
                        if (task.isSuccessful()) {
                            Toast.makeText(this@SignInActivity, "Cuenta creada",
                                    Toast.LENGTH_SHORT).show()
                        }

                        // ...
                    }
                })
    }



    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener { mAuthListener }

    }

    override fun onStop(){
        super.onStop()
        if (mAuthListener != null){
            mAuth!!.removeAuthStateListener { mAuthListener }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        setItems()
        setHandleItems()
        mAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                //Log.d(FragmentActivity.TAG, "onAuthStateChanged:signed_in:" + user.uid)
            } else {
                // User is signed out
                //Log.d(FragmentActivity.TAG, "onAuthStateChanged:signed_out")
            }
            // ...
        }
    }

    fun setItems (){
        this.btnCrear = findViewById(R.id.btnCrear)
        this.edtText = findViewById(R.id.edtUser)
        this.passText = findViewById(R.id.edtPass)
    }
    fun setHandleItems() {
        this.btnCrear.setOnClickListener(this)
    }

}

