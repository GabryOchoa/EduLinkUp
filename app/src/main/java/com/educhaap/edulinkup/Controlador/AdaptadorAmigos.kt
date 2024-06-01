package com.educhaap.edulinkup.Controlador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.educhaap.edulinkup.Modelo.Usuario
import com.educhaap.edulinkup.R

class AdaptadorAmigos(private val usuarios: List<Usuario>): RecyclerView.Adapter<AdaptadorAmigos.AmigoViewHolder>(){

    // ViewHolder para el RecyclerView
    class AmigoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombre: TextView = itemView.findViewById(R.id.edtNombre)
        val textViewEmail: TextView = itemView.findViewById(R.id.edtEmail)
    }
    // Método que infla el diseño de los ítems y crea un ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return AmigoViewHolder(itemView)
    }

    // Método que enlaza los datos de una persona con el ViewHolder
    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {
        val amigo = usuarios[position]
        holder.textViewNombre.text = amigo.name
        holder.textViewEmail.text = amigo.email
    }

    // Método que devuelve la cantidad de ítems en la lista
    override fun getItemCount() = usuarios.size
}

