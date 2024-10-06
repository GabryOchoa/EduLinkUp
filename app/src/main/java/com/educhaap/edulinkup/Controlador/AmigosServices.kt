package com.educhaap.edulinkup.Controlador

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.educhaap.edulinkup.Modelo.Amigos
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AmigosServices (val context : Context){

    //Instancia para firebase
    private val db = FirebaseFirestore.getInstance()


    //Funcion asincrona
    suspend fun getAmigos(correoUsuario : String) : MutableList<Amigos>
    {
        //Lista de amigos
        val amigosUsuarioList = mutableListOf<Amigos>()

        try
        {
            if(correoUsuario != null && correoUsuario != "")
            {
                val amigosRef = db.collection( "amigos").whereEqualTo("correoUsuario",correoUsuario).get().await()
                    for( registro in amigosRef ){
                        var NombreAmigo = registro.getString("nameAmigo")
                        var uidAmigo = registro.getString("uidAmigo")
                        var correoAmigo = registro.getString("correoAmigo")
                        var nombreCarreraAmigo = registro.getString("carreraAmigo")
                        var nombreInstitucionAmigo = registro.getString("institucionAmigo")

                        if (NombreAmigo != null && uidAmigo != null && correoAmigo != null && nombreCarreraAmigo != null && nombreInstitucionAmigo != null) {
                            amigosUsuarioList.add(Amigos(correoAmigo,NombreAmigo,uidAmigo,correoUsuario,"","",nombreCarreraAmigo,nombreInstitucionAmigo))
                        }

                    }
            }
            else
            {
                Toast.makeText(context, "No hay un usuario autenticado vuelva a iniciar sesión correo no se encuentra", Toast.LENGTH_SHORT).show()
            }
        }
        catch(ex:Exception)
        {
            //Mostramos un mensaje de error en la consola
            Log.e("AmigosServices","Error: "+ex.message)
            Toast.makeText(context, "Error: "+ex.message, Toast.LENGTH_SHORT).show()
        }

        return amigosUsuarioList
    }

    //Funcion asincrona vieja
    suspend fun getAmigos1(correoUsuario : String) : MutableList<Amigos>
    {
        //Lista de amigos
        val amigosUsuarioList = mutableListOf<Amigos>()

        try
        {
            if(correoUsuario != null && correoUsuario != "")
            {
                val amigosRef = db.collection( "amigos")
                amigosRef.whereEqualTo("correoUsuario",correoUsuario)
                    .get()
                    .addOnSuccessListener { resultado ->
                        for( registro in resultado ){
                            var NombreAmigo = registro.getString("nameAmigo")
                            var uidAmigo = registro.getString("uidAmigo")
                            var correoAmigo = registro.getString("correoAmigo")
                            var nombreCarreraAmigo = registro.getString("carreraAmigo")
                            var nombreInstitucionAmigo = registro.getString("institucionAmigo")

                            if (NombreAmigo != null && uidAmigo != null && correoAmigo != null && nombreCarreraAmigo != null && nombreInstitucionAmigo != null) {
                                amigosUsuarioList.add(Amigos(correoAmigo,NombreAmigo,uidAmigo,correoUsuario,"","",nombreCarreraAmigo,nombreInstitucionAmigo))
                            }

                        }

                    }
                    .addOnFailureListener { e ->
                        // Manejar el error
                        Toast.makeText(context, "Ocurrio un error al cargar lista amigos: "+e.message, Toast.LENGTH_SHORT).show()
                    }
            }
            else
            {
                Toast.makeText(context, "No hay un usuario autenticado vuelva a iniciar sesión correo no se encuentra", Toast.LENGTH_SHORT).show()
            }
        }
        catch(ex:Exception)
        {
            //Mostramos un mensaje de error en la consola
            Log.e("AmigosServices","Error: "+ex.message)
            Toast.makeText(context, "Error: "+ex.message, Toast.LENGTH_SHORT).show()
        }

        return amigosUsuarioList
    }
}