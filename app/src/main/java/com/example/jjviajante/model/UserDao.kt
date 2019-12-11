package com.example.jjviajante.model

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE email LIKE :email AND senha LIKE :senha LIMIT 1")
    fun login(email: String, senha: String): User?

    @Query("SELECT * FROM user LIMIT 1")
    fun findAll(): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User): Long

    @Update
    fun update(user: User): Int

    @Delete
    fun delete(user: User): Int

    @Query("SELECT * FROM user WHERE id LIKE :id LIMIT 1")
    fun getUser(id: Long): User?

    companion object {
        fun _saveInShare(context: Context, user: User) {

            val sharedPref = context.getSharedPreferences("User_preference", AppCompatActivity.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putString("email", user.email);
                putString("senha", user.senha);
                putString("nome", user.nome);
                putLong("id", user.id);
                commit()
            }
        }
    }
}