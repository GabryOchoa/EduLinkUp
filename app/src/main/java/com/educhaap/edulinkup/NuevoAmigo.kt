package com.educhaap.edulinkup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educhaap.edulinkup.Controlador.AdaptadorAmigos
import com.educhaap.edulinkup.Modelo.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class NuevoAmigo : AppCompatActivity() {

    private lateinit var edtBucarAmigos: EditText
    private lateinit var recyclerViewUsuarios: RecyclerView

    private var usuariosList = mutableListOf<Usuario>()
    private var db = FirebaseFirestore.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_amigo)

        edtBucarAmigos = findViewById(R.id.edtBuscarAmigo);
        recyclerViewUsuarios = findViewById(R.id.rvAgregarAmigos)

        recyclerViewUsuarios.layoutManager = LinearLayoutManager(this)
    }

    fun startMisAmigosActivity(view : View)
    {
        val intent = Intent(this, MisAmigos::class.java)
        startActivity(intent)
        finish()
    }

    fun getUsuarios(view: View){
        val amigosRef = db.collection( "usuarios")
        val UsuarioBusqueda = edtBucarAmigos.text.toString();
        amigosRef.whereEqualTo("name",UsuarioBusqueda)
            .get()
            .addOnSuccessListener { resultado ->
                usuariosList.clear()
                val usuariosList = mutableListOf<Usuario>()
                for( registro in resultado ){
                    var NombreUsuario = registro.getString("name")
                    var uid = registro.getString("uid")
                    var correo = registro.getString("email")

                    if(NombreUsuario != UsuarioBusqueda.trim())
                    {
                        NombreUsuario = null
                        uid = null
                        correo = null

                    }


                    if (NombreUsuario != null && uid != null && correo != null) {
                        usuariosList.add(Usuario(uid, correo, NombreUsuario, "", "", "", uid, ""))
                    }

                }
                val adaptadorAmigosFireBase = AdaptadorAmigos(usuariosList)
                recyclerViewUsuarios.adapter = adaptadorAmigosFireBase


            }
            .addOnFailureListener { e ->
                // Manejar el error
            }
    }
}