package com.educhaap.edulinkup.Controlador

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educhaap.edulinkup.ChatActivity
import com.educhaap.edulinkup.Modelo.Usuario
import com.educhaap.edulinkup.R
import com.educhaap.edulinkup.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ServicesMain(private val context: Context) {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private lateinit var adapter: AdaptadorUsuarios
    private lateinit var chatList: MutableList<Usuario>

    //Metodo para cerrar sesion de usuario
    fun signOutAndStartSignInActivity(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //pasando configuracion de google sign in
        mGoogleSignInClient = GoogleSignIn.getClient(context,gso)

        auth.signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(context, SignInActivity::class.java)
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }
    }

    //implementacion del actionbar en main Activity

    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater): Boolean {
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_new_group -> {
                Toast.makeText(context, "Nuevo Grupo seleccionado", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_profile -> {
                Toast.makeText(context, "Perfil seleccionado", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_logout -> {
                Toast.makeText(context, "Cerrar Sesión seleccionado", Toast.LENGTH_SHORT).show()
                signOutAndStartSignInActivity()
                true
            }
            else -> onOptionsItemSelected(item)
        }
    }

    //Metodos para mostrar datos al recycler view
    fun setupRecyclerView(recyclerView: RecyclerView) {
        chatList = ArrayList()
        adapter = AdaptadorUsuarios(
            context = context,
            chatList = chatList,
            onUserClick = ::handleUserClick
        )
        db = FirebaseFirestore.getInstance()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        loadChatData()
    }

    private fun handleUserClick(user: Usuario) {
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("name", user.name)
        intent.putExtra("uid",user.uid)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }

    private fun loadChatData() {

        auth = FirebaseAuth.getInstance()

        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            db.collection("usuarios").get()
                .addOnSuccessListener { documents ->
                    chatList.clear()
                    for (document in documents) {
                        val user = document.toObject(Usuario::class.java)
                        if (user.uid != currentUserId) {
                            chatList.add(user)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error al cargar usuarios: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}