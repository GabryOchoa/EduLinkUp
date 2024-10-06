package com.educhaap.edulinkup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.educhaap.edulinkup.Controlador.AdaptadorInstituciones
import com.educhaap.edulinkup.Controlador.AdaptadorSpinner_Custom
import com.educhaap.edulinkup.Modelo.Institucion

class UserType : AppCompatActivity() {

    private lateinit var spRolTipoUsuario : Spinner
    //Datos del intent
    private lateinit var email : String
    private lateinit var uid : String
    private lateinit var providerID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_type)

        //Inicializamos las referencias con los objetos del XML
        spRolTipoUsuario = findViewById(R.id.spRol_typoUsuario)

        //Lista de strings para el spinner
        val rolesList: List<String> = listOf("Estudiante/Egresado","Docente","Empresa")
        //Mi adaptador recibe el contexto y su lista de objetos
        val adapterRolesSp = AdaptadorSpinner_Custom(this,rolesList)

        // Aplicar el adaptador al Spinner
        spRolTipoUsuario.adapter = adapterRolesSp

        // Obtener el Intent que inició esta actividad
        val intent = intent
        //Validamos que no sean nulos con el operador Elvis
        providerID = intent.getStringExtra("EXTRA_PROVIDER_ID") ?: "Desconocido"
        email = intent.getStringExtra("EXTRA_EMAIL") ?: "Desconocido"
        uid = intent.getStringExtra("EXTRA_UID") ?: "Desconocido"
    }

    fun startActivityAddtionalData(view:View)
    {
        //Obtenemos el elemento seleccionado del spinner
        val rolSeleccionado = spRolTipoUsuario.selectedItem.toString()

        if(rolSeleccionado == "Estudiante/Egresado")
        {
            //Redireccionamos a la pantalla para datos adicionales del estudiante
            if(providerID != "Desconocido" && email != "Desconocido" && uid != "Desconocido")
            {
                //Indicamos la redireccion al objeto intent
                val intent = Intent(this, Addtional_data::class.java)
                //Agregamos parametros al objeto Intent
                intent.putExtra("EXTRA_PROVIDER_ID",providerID)
                intent.putExtra("EXTRA_EMAIL",email)
                intent.putExtra("EXTRA_UID",uid)
                intent.putExtra("EXTRA_ROL",rolSeleccionado)
                //Iniciamos la actividad
                startActivity(intent);
                //Cerramos la actividad actual
                finish()
            }
            else
            {
                val intent = Intent(this, SignInActivity::class.java)
                //Iniciamos la actividad
                startActivity(intent);
                //Cerramos la actividad actual
                finish()

                // Mostrar el valor en un Toast
                Toast.makeText(this, "Ocurrio un error los datos se perdieron", Toast.LENGTH_SHORT).show()
            }

        }
        else if(rolSeleccionado == "Empresa")
        {
            // Mostrar el valor en un Toast
            Toast.makeText(this, "Seleccionado: $rolSeleccionado", Toast.LENGTH_SHORT).show()
        }

        //Intent para la nueva actividad
        val intent = Intent(this, Addtional_data::class.java)
        //Agregamos parametros al intent
    }

    fun startActivitySignInActivity(view:View)
    {
        //Indicamos la redireccion al objeto intent
        val intent = Intent(this, SignInActivity::class.java)
        //Iniciamos la actividad
        startActivity(intent);

        //Cerramos la actividad actual
        finish()
    }
}