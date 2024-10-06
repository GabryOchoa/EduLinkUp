package com.educhaap.edulinkup.Controlador

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.educhaap.edulinkup.Modelo.Carrera
import com.educhaap.edulinkup.Modelo.Institucion
import com.google.firebase.firestore.FirebaseFirestore

class CarrerasServices (val context : Context) {

    //Instancia para firebase
    private val db = FirebaseFirestore.getInstance()

    //Funcion que consulta a firebase todas las instituciones y retorna una lista de objetos
    fun getCarreras():MutableList<Carrera> {
        //Creamos una lista de instituciones vacia
        val carrerasList = mutableListOf<Carrera>()

        try
        {
            //Consultamos a firebase
            // Referencia a la colección de Firebase
            val carrerasRef = db.collection("carreras")

            //Obtenemos los datos de la coleccion
            carrerasRef.get()
                .addOnSuccessListener { result ->
                    //Recorremos los documentos y agregamos los nombres a la lista
                    for(document in result)
                    {
                        val codigoInstitucion = document.getLong("codigoInstitucion")
                        val codigoCarrera = document.getLong("codigoCarrera")
                        val nombreCarrera = document.getString("nombreCarrera")

                        if(nombreCarrera != null && codigoCarrera != null && codigoInstitucion != null)
                        {
                            //Llenamos un objeto de carrera
                            var carrera = Carrera(codigoCarrera.toInt(),codigoInstitucion.toInt(), nombreCarrera)

                            //Agregamos el objeto carrera a la lista
                            carrerasList.add(carrera)
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
            Log.e("CarrerasServices","Error: "+ex.message)
            Toast.makeText(context, "Error: "+ex.message, Toast.LENGTH_SHORT).show()
        }

        return carrerasList
    }
}