package com.example.covid19tracker

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ArrayAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

class Adapter(context: Context?, private val modelList: List<Model>) : ArrayAdapter<Model?>(
    context!!, R.layout.testing, modelList
) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.testing, null, true)
        val state = view.findViewById<TextView>(R.id.city)
        val active = view.findViewById<TextView>(R.id.active)
        val cured = view.findViewById<TextView>(R.id.cured)
        val death = view.findViewById<TextView>(R.id.death)
        val total = view.findViewById<TextView>(R.id.total)
        val incactive = view.findViewById<TextView>(R.id.incactive)
        val inccured = view.findViewById<TextView>(R.id.inccured)
        val incdeath = view.findViewById<TextView>(R.id.incdeath)

        state.text = modelList[position].name
        active.text = modelList[position].active
        cured.text = modelList[position].cured
        death.text = modelList[position].death
        total.text = modelList[position].total
        incactive.text = modelList[position].incAct
        inccured.text = modelList[position].incRec
        incdeath.text = modelList[position].incDec
        return view
    }
}