package com.educhaap.edulinkup.Controlador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.educhaap.edulinkup.Modelo.Carrera
import com.educhaap.edulinkup.Modelo.Institucion
import com.educhaap.edulinkup.Modelo.Usuario
import com.educhaap.edulinkup.NuevoAmigo
import com.educhaap.edulinkup.R

class AdaptadorAmigos(val uidUsuario: String, val correoUsuario:String, private val context: Context, private var usuarios: MutableList<Usuario>, private var listaInstituciones : MutableList<Institucion>, private var listaCarreras : MutableList<Carrera>): RecyclerView.Adapter<AdaptadorAmigos.AmigoViewHolder>(){

    // ViewHolder para el RecyclerView
    class AmigoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombre: TextView = itemView.findViewById(R.id.edtNombre)
        val textViewEmail: TextView = itemView.findViewById(R.id.edtEmail)
        val textViewInstitucion: TextView = itemView.findViewById(R.id.tvInstitucion)
        val textViewCarrera: TextView = itemView.findViewById(R.id.tvCarrera)
        val buttomViewAgregarAmigo: TextView = itemView.findViewById(R.id.btnAgregarAmigo)
        var uidUsuarioSeleccionado : String? = ""
    }
    // Método que infla el diseño de los ítems y crea un ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return AmigoViewHolder(itemView)
    }

    // Método que enlaza los datos de una persona con el ViewHolder
    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {
        val amigo = usuarios[position]
        holder.textViewNombre.text = amigo.nombreCompleto
        holder.textViewEmail.text = amigo.email
        holder.uidUsuarioSeleccionado = amigo.uid

        //Buscamos la institucion de este usuario
        /*if(amigo.codigoInstitucion == 0)
        {
            holder.textViewInstitucion.text = "Institución Desconocida"
            holder.textViewCarrera.text = "Carrera no encontrada"
        }
        else
        {
            var institucion = listaInstituciones.find { it.codigoInstitucion == amigo.codigoInstitucion}
            if(institucion != null)
            {
                holder.textViewInstitucion.text = institucion.nombreInstitucion
            }
            else
            {
                holder.textViewInstitucion.text = "Institución Desconocida"
            }
        }

        //Buscamos la carrera de este usuario
        if(amigo.codigoCarrera == 0)
        {
            holder.textViewCarrera.text = "Carrera no encontrada"
        }
        else
        {
            var carrera = listaCarreras.find { it.codigoCarrera == amigo.codigoCarrera && it.codigoInstitucion == amigo.codigoInstitucion}
            if(carrera != null)
            {
                holder.textViewCarrera.text = carrera.nombreCarrera
            }
            else
            {
                holder.textViewCarrera.text = "Carrera no encontrada"
            }
        }*/
        //Creamos un evento al boton del recyclerView
        holder.buttomViewAgregarAmigo.setOnClickListener {
            val nombreAmigo = holder.textViewNombre.text.toString()
            val correoAmigo = holder.textViewEmail.text.toString()
            var NuevoAmigo = NuevoAmigo()
            NuevoAmigo.insertAmigo(uidUsuario, correoUsuario, context,nombreAmigo, correoAmigo, amigo.uid, holder.textViewCarrera.text.toString(), holder.textViewInstitucion.text.toString())

            //Limpiamos el recycler
            clear()
        }
    }

    // Método que devuelve la cantidad de ítems en la lista
    override fun getItemCount() = usuarios.size

    // Método para limpiar el RecyclerView
    fun clear() {
        usuarios.clear()
        notifyDataSetChanged()
    }
}

