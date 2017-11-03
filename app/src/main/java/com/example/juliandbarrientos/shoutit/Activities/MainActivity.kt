package com.example.juliandbarrientos.shoutit.Activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.example.juliandbarrientos.shoutit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.example.juliandbarrientos.shoutit.Objects.AudioClass
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import android.support.v7.widget.RecyclerView
import com.example.juliandbarrientos.shoutit.Adapters.AudioAdapter


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {




    private var database           : FirebaseDatabase            = FirebaseDatabase.getInstance()
    private var audiDBRef          : DatabaseReference           = database.getReference("Audios")


    private val TAG                : String                      = "MainActivity"
    private val audiosListMut      : MutableList<AudioClass>     = mutableListOf()
    private val readOnlyListAudios : List<AudioClass>            = audiosListMut

    private lateinit var mRecyclerView    : RecyclerView
    private lateinit var mAdapter         : RecyclerView.Adapter<*>
    private lateinit var mLayoutManager   : RecyclerView.LayoutManager
    private lateinit var user             : FirebaseUser
    private lateinit var toggle           : ActionBarDrawerToggle

    // private  var listAudios : List<Audio> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setItem()
        setHandleItem()
    }

    private  fun setItem(){
        this.mRecyclerView =  findViewById(R.id.my_recycler_view)

        this.user = FirebaseAuth.getInstance().currentUser!!

        this.mLayoutManager =  LinearLayoutManager(this)

        this.mAdapter =  AudioAdapter(this, readOnlyListAudios)

        this.toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
    }

    private  fun setHandleItem(){
        nav_view.setNavigationItemSelectedListener(this)

        audiDBRef.addChildEventListener( object : ChildEventListener{

            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val audio : AudioClass? = p0?.getValue(AudioClass::class.java)
                audiosListMut.add(audio!!)
            }
            override fun onChildRemoved(p0: DataSnapshot?) {}
        })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(mLayoutManager)
        mRecyclerView.adapter = mAdapter

        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
            R.id.log_out ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LogInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
