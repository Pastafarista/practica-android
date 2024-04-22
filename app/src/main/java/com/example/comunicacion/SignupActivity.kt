package com.example.comunicacion

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import com.example.comunicacion.databinding.ActivitySignupBinding
import com.example.comunicacion.model.Usuario
import com.example.comunicacion.ui.activity.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        // go to main activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.botonRegistrar.setOnClickListener {

            if (binding.editNombre.text.isNotEmpty() && binding.editCorreo.text.isNotEmpty()
                && binding.editPass.text.isNotEmpty() && binding.editPass2.text.isNotEmpty()
                && binding.editPass.text.toString() == binding.editPass2.text.toString()
                && binding.editPass.text.length >= 6
                && binding.editCorreo.text.contains("@") && binding.editCorreo.text.contains(".")
            ) {
                // realizar cambio pasando un objeto de tipo usuario
                // val perfil = binding.spinnerPerfil.selectedItem.toString()
                val perfil =
                    binding.spinnerPerfil.adapter.getItem(binding.spinnerPerfil.selectedItemPosition)


                val radioSeleccionado: RadioButton =
                    findViewById(binding.radioGroup.checkedRadioButtonId);

                val usuario: Usuario =
                    Usuario(
                        binding.editNombre.text.toString(),
                        binding.editCorreo.text.toString(),
                        binding.editPass.text.toString(),
                        perfil.toString(),
                        radioSeleccionado.text.toString()
                    )

                auth.createUserWithEmailAndPassword(usuario.correo, usuario.pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(
                                baseContext,
                                "Authentication success.",
                                Toast.LENGTH_SHORT,
                            ).show()

                            val user = auth.currentUser

                            val intent: Intent = Intent(this, LoginActivity::class.java)
                            intent.putExtra("usuario", usuario)
                            startActivity(intent)
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Error al crear el usuario en la base de datos",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }

            } else if (binding.editPass.text.length < 6) {
                Toast.makeText(
                    baseContext,
                    "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            else if (binding.editPass.text.toString() != binding.editPass2.text.toString()) {
                Toast.makeText(
                    baseContext,
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            else if (binding.editNombre.text.isEmpty() || binding.editCorreo.text.isEmpty()
                || binding.editPass.text.isEmpty() || binding.editPass2.text.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "Debes llenar todos los campos",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            else if (!binding.editCorreo.text.contains("@") || !binding.editCorreo.text.contains(".")) {
                Toast.makeText(
                    baseContext,
                    "Correo inválido",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            else {
                Toast.makeText(
                    baseContext,
                    "Error desconocido",
                    Toast.LENGTH_SHORT,
                ).show()
            }

        }

    }
}