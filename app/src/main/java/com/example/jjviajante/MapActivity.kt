package com.example.jjviajante

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.jjviajante.model.User

class MapActivity : AppCompatActivity() {

    private lateinit var user: User;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

    }

    override fun onResume() {
        super.onResume()
        this.user = intent.getSerializableExtra("user") as User

        var actionBar = supportActionBar;
        actionBar!!.title = this.user.nome;
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_manu, menu);
        return true
    }

    // Menu da ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == R.id.settings){
            var intent = Intent(this, CadastroActivity::class.java).apply {
                putExtra("user", user)
            }

            startActivity(intent)

        } else if (id == R.id.logout){

            val sharedPref = getSharedPreferences("User_preference",MODE_PRIVATE) ?: return false
            sharedPref.edit().clear().commit()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
