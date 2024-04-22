package com.example.comunicacion

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.example.comunicacion.databinding.ActivityLoginBinding
import com.example.comunicacion.model.Usuario
import com.example.comunicacion.ui.activity.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private var usuario: Usuario? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        usuario = intent.getSerializableExtra("usuario") as Usuario?

        if (usuario != null) {
            binding.editUser.setText(usuario!!.correo)
            binding.editPass.setText(usuario!!.pass)
        }

        binding.buttonSignUp.setOnClickListener(this)
        binding.buttonLogin.setOnClickListener(this)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent: Intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("correo", currentUser.email)
            intent.putExtra("perfil", "?")
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.buttonSignUp.id -> {
                // pasar a una segunda pantalla -> Registro
                // origen, destino
                val intent: Intent = Intent(applicationContext, SignupActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding.buttonLogin.id -> {

                if (binding.editUser.text.toString().isNotEmpty() && binding.editPass.text.toString().isNotEmpty())
                {
                    auth.signInWithEmailAndPassword(binding.editUser.text.toString(), binding.editPass.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")

                                val user = auth.currentUser

                                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                                intent.putExtra("correo", binding.editUser.text.toString())

                                if (usuario!=null){
                                    //intent.putExtra("correo", usuario!!.correo)
                                    intent.putExtra("perfil", usuario!!.perfil)
                                } else {
                                    intent.putExtra("perfil", "?")
                                }
                                startActivity(intent)

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Usuario o contraseña incorrectos",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext,
                        "Debes rellenar todos los campos",
                        Toast.LENGTH_SHORT,
                    ).show()
                }


            }
        }
    }
}