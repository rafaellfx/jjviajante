package com.example.jjviajante

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.jjviajante.model.AppDatabase
import com.example.jjviajante.model.User
import com.example.jjviajante.model.UserDao
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se logado vai para a tela direto dos maps
        _isLog();

        setContentView(R.layout.activity_main);

        var actionBar = supportActionBar;
        actionBar!!.title = "Login";


        var tvSignIn: TextView = this.findViewById(R.id.tvSignIn);

        // Listiner do button para ir na tela de cadastro
        tvSignIn.setOnClickListener { view ->
            startActivityForResult( Intent(this, CadastroActivity::class.java),1);
        }

    }

    override fun onResume() {
        super.onResume()
        // Se logado vai para a tela direto dos maps
        _isLog();
        _login();
    }

    private fun _login() {
        var btnLogin : Button = findViewById(R.id.btnLogin);

        // Listener do button login
        btnLogin.setOnClickListener { view ->

            var edtEmail: EditText = findViewById(R.id.editEmail)
            var edtSenha: EditText = findViewById(R.id.editPassword)
            var email = edtEmail.text.toString()
            var senha = edtSenha.text.toString()
            var isLogin = false

          GlobalScope.launch(context = Dispatchers.Main) {

                var newUser = withContext(context = Dispatchers.IO) {

                    var dao = AppDatabase.getDb(this@MainActivity).await();
                    var user = dao.login(email, senha);

                    if (user != null) {
                        isLogin = true
                        // metodo estatico para gravar o user no share
                        UserDao._saveInShare(this@MainActivity,user)
                        var intent: Intent = Intent(this@MainActivity, MapsActivity::class.java);

                        // Passa o obj usuário
                        intent.putExtra("user", user);

                        startActivity(intent);
                        finish()
                    }

                }

                if(!isLogin) {
                    Toast.makeText(
                        this@MainActivity,
                        "Senha ou usuário estão incorrete,Porfavor tente novamente!",
                        Toast.LENGTH_LONG
                    ).show();
                    edtEmail.setText("")
                    edtSenha.setText("")
                }
            }
        }
    }


    fun _isLog() {

        val sharedPref = getSharedPreferences("User_preference",MODE_PRIVATE) ?: return

        var user = User();
        user.id = sharedPref.getLong("id", 0);
        user.nome = sharedPref.getString("nome", null).toString();
        user.email = sharedPref.getString("email", null).toString();
        user.senha = sharedPref.getString("senha", null).toString();

        if(user.id > 0){

            var intent: Intent = Intent(this, MapsActivity::class.java).apply {
                putExtra("user", user)
            }
            startActivity(intent);
            finish();
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){

                var user = data!!.getSerializableExtra("user") as User;
                var edtEmail: EditText = findViewById(R.id.editEmail)
                var edtPassword: EditText = findViewById(R.id.editPassword)

                edtEmail.setText(user.email);
                edtPassword.setText(user.senha);

                // metodo estatico para gravar o user no share
                UserDao._saveInShare(this,user)

            }
        }
    }

}

