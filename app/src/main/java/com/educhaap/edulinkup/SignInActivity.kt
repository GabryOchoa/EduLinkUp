package com.educhaap.edulinkup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.educhaap.edulinkup.Controlador.AuthManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {
    private lateinit var btnGooble: Button
    private lateinit var btnMicrososf: Button
    private lateinit var btnIniciarSesion_login : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var authManager: AuthManager
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initComponents()
        authManager = AuthManager(this)
        authManager.checkSesion()

        btnGooble.setOnClickListener {
            authManager.signInWithGoogle()
        }

        btnMicrososf.setOnClickListener {
            authManager.signInMicrosoft()
        }

        btnIniciarSesion_login = findViewById(R.id.btnIniciarSesion_login)

    }

    //Metodo inicia el activity y trae resultados
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AuthManager.RC_SIGN_IN) {
            authManager.handleGoogleSignInResult(requestCode, resultCode, data)
        }
    }

    //Inicializando componentes necesarios
    private fun initComponents() {
        btnGooble = findViewById(R.id.btnGoogle)
        btnMicrososf = findViewById(R.id.btnMicrosoft)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    //Metodo que redirecciona al tipo de usuario
    fun redireccionarTipoUsuario(V: View)
    {
        val intent = Intent(this, UserType::class.java)
        startActivity(intent)
    }
}
