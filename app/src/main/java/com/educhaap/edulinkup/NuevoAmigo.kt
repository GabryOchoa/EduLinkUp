package com.educhaap.edulinkup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educhaap.edulinkup.Controlador.AdaptadorAmigos
import com.educhaap.edulinkup.Controlador.AmigosServices
import com.educhaap.edulinkup.Controlador.CarrerasServices
import com.educhaap.edulinkup.Controlador.InstitucionesServices
import com.educhaap.edulinkup.Modelo.Amigos
import com.educhaap.edulinkup.Modelo.Carrera
import com.educhaap.edulinkup.Modelo.Institucion
import com.educhaap.edulinkup.Modelo.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class NuevoAmigo : AppCompatActivity() {

    private lateinit var edtBucarAmigos: EditText
    private lateinit var recyclerViewUsuarios: RecyclerView
    private lateinit var adaptadorAmigosFireBase : AdaptadorAmigos

    private var usuariosList = mutableListOf<Usuario>()
    private var institucionesList = mutableListOf<Institucion>()
    private var carrerasList = mutableListOf<Carrera>()
    private var amigosList = mutableListOf<Amigos>()

    private var db = FirebaseFirestore.getInstance()

    //Instancia para obtener los datos del usuario autentificado
    private lateinit var auth: FirebaseAuth
    //Variables del usuario
    var correoUsuario : String = ""
    var uidUsuario : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_amigo)

        edtBucarAmigos = findViewById(R.id.edtBuscarAmigo);
        recyclerViewUsuarios = findViewById(R.id.rvAgregarAmigos)

        recyclerViewUsuarios.layoutManager = LinearLayoutManager(this)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()
        // Obtener el usuario actualmente autenticado
        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let { user ->
            uidUsuario = user.uid
            correoUsuario = user.email ?: "Correo no disponible"

            // Mostrar los datos del usuario en un Toast
            //Toast.makeText(this, "UID: $uidUsuario, Email: $correoUsuario", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, "No hay un usuario autenticado vuelva a iniciar sesión", Toast.LENGTH_SHORT).show()
        }

        //Inicializamos el adaptador
        adaptadorAmigosFireBase = AdaptadorAmigos(uidUsuario,correoUsuario, this,usuariosList, institucionesList, carrerasList)
        recyclerViewUsuarios.adapter = adaptadorAmigosFireBase

        //Llenamos las listas de instituciones y carreras
        getListados()
        updateListadoAmigos(correoUsuario, this)
    }

    //Funcion que llena 2 listas de la clase globales ()
    fun getListados()
    {
        //Objeto de la clase InstitucionesServices
        val institucionesServices = InstitucionesServices(this)
        institucionesList = institucionesServices.getInstituciones()

        val carrerasServices = CarrerasServices(this)
        carrerasList = carrerasServices.getCarreras()

        /*val amigosServices = AmigosServices(this)
        amigosList = amigosServices.getAmigos(correoUsuario)*/

        if(institucionesList.count() > 0 && carrerasList.count() > 0 && amigosList.count() > 0)
        {
            Toast.makeText(this, "Listas llenas", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this, "Listas vacias", Toast.LENGTH_SHORT).show()
        }
    }
    fun startMisAmigosActivity(view : View)
    {
        val intent = Intent(this, MisAmigos::class.java)
        startActivity(intent)
        finish()
    }

    //Funcion que se ejecuta en otro hilo para llamar una funcion asincrona
    fun updateListadoAmigos(correoUsuario : String, context : Context)
    {
        //Llamamos a una funcion suspendida (asincrona)
        var amigosServices = AmigosServices(context)

        //Las funciones asincronas solo pueden ser llamadas dentro de una corrutina
        // Usamos lifecycleScope para lanzar una corrutina
        lifecycleScope.launch {
            val result = amigosServices.getAmigos(correoUsuario)

            if(result != null && result.isNotEmpty())
            {
                Toast.makeText(context, "Lista amigos actualizada", Toast.LENGTH_SHORT).show()
                amigosList.clear()
                amigosList.addAll(result)
                //amigosList = result
            }
            // Actualizamos la UI con el resultado
            //textView.text = result
        }
    }
    //Funcion que busca a los usuarios por nombre
    fun getUsuarios(view: View){
        //Refrescamos la lista de amigos
        updateListadoAmigos(correoUsuario, this)
        val amigosRef = db.collection( "usuarios")
        val UsuarioBusqueda = edtBucarAmigos.text.toString();
        // Crear límites para la consulta de prefijo
        val endPrefijo = UsuarioBusqueda + "\uf8ff"

        try
        {
            amigosRef.orderBy("name")
                .startAt(UsuarioBusqueda)
                .endAt(endPrefijo)
                .get()
                .addOnSuccessListener { resultado ->
                    usuariosList.clear()
                    val usuariosList = mutableListOf<Usuario>()
                    for( registro in resultado ){
                        var NombreUsuario = registro.getString("name")
                        var uid = registro.getString("uid")
                        var correo = registro.getString("email")
                        var codigoInstitucion = registro.getLong("codigoInstitucion")
                        var codigoCarrera = registro.getLong("codigoCarrera")

                        //Buscamos en la lista de objetos de institucion el nombre
                        if(institucionesList.count() > 0 && codigoInstitucion != null && codigoCarrera != null)
                        {
                            if (NombreUsuario != null && uid != null && correo != null)
                            {
                                //Validamos que el correo de la persona no sea el mismo que de la persona logeada
                                if(correo != correoUsuario)
                                {
                                    //Omitimos de la lista a los usuarios que ya existan en nuestra lista de amigos
                                    var amigo = amigosList.find { it.correoAmigo == correo }
                                    if(amigo == null)
                                    {
                                        //usuariosList.add(Usuario(uid, correo, "", NombreUsuario, "", "", "", uid, "", codigoInstitucion.toInt(), codigoCarrera.toInt()))
                                    }
                                }
                            }

                            adaptadorAmigosFireBase = AdaptadorAmigos(uidUsuario,correoUsuario, this,usuariosList, institucionesList, carrerasList)
                            recyclerViewUsuarios.adapter = adaptadorAmigosFireBase
                        }

                    }

                }
                .addOnFailureListener { e ->
                    // Manejar el error
                    Toast.makeText(this, "Error: "+e.message, Toast.LENGTH_SHORT).show()

                }
        }
        catch(ex:Exception)
        {
            Toast.makeText(this, "Error: "+ex.message, Toast.LENGTH_SHORT).show()
        }
    }

    //Funcion que guarda a los usuarios como amigos
    fun insertAmigo(uidUsuario:String, correoUsuario: String, contex: Context, nombreAmigo: String, correAmigo: String, uidUsuarioSeleccionado : String?, carreraAmigo : String, institucionAmigo : String) {

        try
        {
            if(correoUsuario == "" && uidUsuario == "")
            {
                Toast.makeText(contex, "Sesión Expirada vuelva a iniciar sesión", Toast.LENGTH_SHORT).show()
            }
            else
            {
                //Llenamos un objeto
                var uidAmigoSeleccionado : String = uidUsuarioSeleccionado ?: "UID DESCONOCIDO"
                var amigoAgregado = Amigos(correAmigo, nombreAmigo,uidAmigoSeleccionado,correoUsuario,"",uidUsuario,carreraAmigo,institucionAmigo)

                //Insertamos el registro
                db.collection("amigos")
                    .add(amigoAgregado)
                    .addOnSuccessListener {
                        Toast.makeText(contex, "Amigo guardado", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(contex, "Error al guardar amigo "+e.message, Toast.LENGTH_SHORT).show()
                    }
            }

        }
        catch(ex : Exception)
        {
            Toast.makeText(contex, "No hay un usuario autenticado vuelva a iniciar sesión", Toast.LENGTH_SHORT).show()
        }

    }
}