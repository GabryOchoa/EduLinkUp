package com.educhaap.edulinkup.Controlador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.educhaap.edulinkup.R

class AdaptadorSpinner_Custom (
    context: Context,
    val items : List<String>) : ArrayAdapter<String>(context,0,items) {

    // Método para obtener la vista que se mostrará cuando el Spinner no esté desplegado hace referencia al diseño xml que tomara
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_item_custom)
    }

    // Método para obtener la vista que se mostrará cuando el Spinner esté desplegado
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_dropdown_item_custom)
    }

    //Metodo que infla el layaout personalizado y se establecen los datos de vista
    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup, resource: Int): View {
        // Reutilizar la vista si es posible, de lo contrario inflar una nueva
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        // Obtener referencia al TextView dentro de la vista
        val textView = view.findViewById<TextView>(R.id.spinnerTextViewCustom)
        // Establecer el texto del TextView con el nombre de la institución
        textView.text = items[position]
        //textView.text = instituciones[position].nombreInstitucion
        return view
    }


}