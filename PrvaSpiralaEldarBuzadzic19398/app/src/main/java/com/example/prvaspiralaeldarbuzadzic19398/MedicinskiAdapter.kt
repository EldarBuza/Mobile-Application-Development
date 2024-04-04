package com.example.prvaspiralaeldarbuzadzic19398

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicinskiAdapter(private var biljke: List<Biljka>, private val onClickListener: (Biljka) -> Unit):
    RecyclerView.Adapter<MedicinskiAdapter.MedicinskiViewHolder>()
{
    public var trenutneBiljke = biljke
    private lateinit var filtriranaListaNova: List<Biljka>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinskiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.medicinski_layout, parent, false)
        return MedicinskiViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicinskiViewHolder, position: Int) {
        val biljka = biljke[position]
        holder.nazivBiljke.text = biljka.naziv
        holder.upozorenje.text = biljka.medicinskoUpozorenje
        if (biljka.medicinskeKoristi.size >= 1)
            holder.korist1Item.text = biljka.medicinskeKoristi[0].opis
        if (biljka.medicinskeKoristi.size >= 2)
            holder.korist2Item.text = biljka.medicinskeKoristi[1].opis
        if (biljka.medicinskeKoristi.size >= 3)
            holder.korist3Item.text = biljka.medicinskeKoristi[2].opis
        holder.itemView.setOnClickListener{
            val podaci = biljke[position]
            onClickListener(podaci)
            filtriraj(podaci)}
    }
    fun filtriraj(trenutnaBiljka: Biljka){
        val match = trenutnaBiljka.medicinskeKoristi
        val filtriranaListaNova = biljke.filter{
                biljka -> biljka.medicinskeKoristi.any {korist -> korist in match}
        }
        updateBiljke(filtriranaListaNova)
        trenutneBiljke = filtriranaListaNova
    }
    private fun updateBiljke(biljke: List<Biljka>){
        this.biljke = biljke
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return biljke.size
    }

    inner class MedicinskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nazivBiljke: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenje: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1Item: TextView = itemView.findViewById(R.id.korist1Item)
        val korist2Item: TextView = itemView.findViewById(R.id.korist2Item)
        val korist3Item: TextView = itemView.findViewById(R.id.korist3Item)

    }
}
