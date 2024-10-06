package com.educhaap.edulinkup.Controlador

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.educhaap.edulinkup.Modelo.Institucion
import com.google.firebase.firestore.FirebaseFirestore

//Esta clase recibe el contexto para que pueda ejecutar acciones en el contexto que se le es enviado
//Este contexto es definido en el constructor primario de la clase asi se define el constructor primario
class InstitucionesServices (val context : Context) {

    //Instancia para firebase
    private val db = FirebaseFirestore.getInstance()

    //Funcion que consulta a firebase todas las instituciones y retorna una lista de objetos
    fun getInstituciones():MutableList<Institucion> {
        //Creamos una lista de instituciones vacia
        val institucionesList = mutableListOf<Institucion>()

        try
        {
            //Consultamos a firebase
            // Referencia a la colección de Firebase
            val institucionesRef = db.collection("instituciones")

            //Obtenemos los datos de la coleccion
            institucionesRef.get()
                .addOnSuccessListener { result ->
                    //Recorremos los documentos y agregamos los nombres a la lista
                    for(document in result)
                    {
                        var codigo = document.getLong("codigoInstitucion")
                        val nombre = document.getString("nombreInstitucion")
                        val abrevitura = document.getString("abreviatura")

                        if(codigo != null && nombre != null && abrevitura != null)
                        {
                            //Agregamos la institucion a la lista
                            institucionesList.add(Institucion(codigo.toInt(),nombre,abrevitura))
                        }
                    }

                }
                .addOnFailureListener() { exception ->
                    // Manejar el error en caso de que ocurra
                    exception.printStackTrace()
                }
        }
        catch(ex:Exception)
        {
            //Mostramos un mensaje de error en la consola
            Log.e("InstitucionServices","Error: "+ex.message)
            Toast.makeText(context, "Error: "+ex.message, Toast.LENGTH_SHORT).show()
        }

        return institucionesList
    }
}