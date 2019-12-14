package com.example.jjviajante

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import com.example.jjviajante.model.AppDatabase
import com.example.jjviajante.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var user: User;
    private lateinit var nome: EditText;
    private lateinit var email: EditText;
    private lateinit var senha: EditText;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        var actionBar = supportActionBar;
        actionBar!!.title = "Sign in";

        _init(intent);

        _saveData();

    }


    private fun _init(intent: Intent) {

        if(intent.getSerializableExtra("user") != null) {
            this.user = intent.getSerializableExtra("user") as User;

            getFields()

            this.nome.setText(user.nome);
            this.email.setText(user.email);
            this.senha.setText(user.senha);
        } else {

            this.user = User()
        }

    }

    private fun _saveData() {

        var btnFloat = findViewById<FloatingActionButton>(R.id.btnLogin);

        btnFloat.setOnClickListener { view ->

            getFields();

            var newUser: User
            var isUpdate = false;

            // Verificação do usuário para atualizar ou inserir
            if(this.user != null && this.user.id >0){
                isUpdate = true;
                newUser = this.user
            }else{
                newUser = User();
            }

            newUser.nome = nome.text.toString();
            newUser.email = email.text.toString();
            newUser.senha = senha.text.toString();

            if(newUser.nome != "" && newUser.email !="" && newUser.senha != "") {

                GlobalScope.launch(context = Dispatchers.Main) {

                    withContext(context = Dispatchers.IO){

                        var dao = AppDatabase.getDb(this@CadastroActivity).await();
                        var id = 0;

                        if(isUpdate){
                            id = dao.update(newUser);
                        }else{
                            id = dao.insert(newUser).toInt();
                        }

                        if( id > 0 ) {
                            var newUser = dao.getUser(id.toLong())!!;
                            var intent = Intent(this@CadastroActivity, MapsActivity::class.java).apply {
                                putExtra("user", newUser)
                            }

                            if(!isUpdate){
                                setResult(Activity.RESULT_OK, intent);
                            }else{
                                startActivity(intent)
                            }

                            finish()
                        }
                    }
                }

                Toast.makeText(this, "Usuario " + user.nome + " adicionado com sucesso!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            }
        }
    }

    fun getFields(){
        nome = findViewById<EditText>(R.id.edtName)
        email = findViewById<EditText>(R.id.edtEmail)
        senha = findViewById<EditText>(R.id.edtPassword)

    }
}
