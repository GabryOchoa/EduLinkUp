package com.educhaap.edulinkup.Controlador

import android.content.Context
import android.provider.CalendarContract.Instances
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educhaap.edulinkup.Modelo.Mensaje
import com.educhaap.edulinkup.Modelo.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Time

class ServicesChat(private val context: Context) {

    private lateinit var db: FirebaseFirestore
    private lateinit var listaMensaje: MutableList<Mensaje>
    private lateinit var adapter: AdaptadorMensajes
    val auth = FirebaseAuth.getInstance()
    val senderUid = auth.currentUser?.uid

    //configuracion del recyclerView para envio de mensajes
    fun setupRecyclerViewChat(recyclerView: RecyclerView) {
        listaMensaje = ArrayList()
        adapter = AdaptadorMensajes(listaMensaje)
        db = FirebaseFirestore.getInstance()

        //Mostrar mensajes en el recycler view
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    //Agregando mensajes a la base de datos
    fun enviarMensaje(mensaje: String, currentTime: Long) {
        try {
            val mensajeObj = Mensaje(mensaje, senderUid!!, currentTime)
            db.collection("mensaje").add(mensajeObj).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listarMensaje()
                    Toast.makeText(context, "Mensaje enviado", Toast.LENGTH_SHORT).show()
                } else {
                    handleError("Error al enviar mensaje al usuario solicitado")
                }
            }
        } catch (e: Exception) {
            handleError("Error al momento de enviar mensajes: ${e.message}")
        }
    }

    //listar mensajes en pantalla
    fun listarMensaje() {
        try {
            db.collection("mensaje")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error == null) {
                        listaMensaje.clear()
                        snapshot?.let {
                            val mensajeList = it.toObjects(Mensaje::class.java)
                            listaMensaje.addAll(mensajeList)
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        handleError("Error al listar mensajes de la base de datos")
                    }
                }
        } catch (e: Exception) {
            handleError("Error al momento de listar mensajes: ${e.message}")
        }
    }

    private fun handleError(message: String) {
        Log.e("AuthMange", message)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}