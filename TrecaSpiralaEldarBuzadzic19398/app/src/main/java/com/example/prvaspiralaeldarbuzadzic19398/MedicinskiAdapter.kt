package com.example.prvaspiralaeldarbuzadzic19398

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicinskiAdapter(private var biljke: ArrayList<Biljka>, private val onClickListener: (Biljka) -> Unit):
    RecyclerView.Adapter<MedicinskiAdapter.MedicinskiViewHolder>()
{
    public var trenutneBiljke = biljke
    private lateinit var filtriranaListaNova: ArrayList<Biljka>
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
        if (biljka.medicinskeKoristi.size >= 2) {
            holder.korist2Item.visibility = View.VISIBLE
            holder.korist2Item.text = biljka.medicinskeKoristi[1].opis
        }
        else holder.korist2Item.visibility = View.GONE
        if (biljka.medicinskeKoristi.size >= 3) {
            holder.korist3Item.visibility = View.VISIBLE
            holder.korist3Item.text = biljka.medicinskeKoristi[2].opis
        }
        else holder.korist3Item.visibility = View.GONE
        holder.itemView.setOnClickListener{
            val podaci = biljke[position]
            onClickListener(podaci)
            filtriraj(podaci)}

        val scope = CoroutineScope(Dispatchers.Main)
        val context : Context = holder.slikaBiljke.context
        //biljka, moze i biljke[position] ali nema razlike koliko vidim
        scope.launch {
            val result = TrefleDAO(context).getImage(biljka)
            holder.slikaBiljke.setImageBitmap(result)
        }

    }
    fun filtriraj(trenutnaBiljka: Biljka){
        val match = trenutnaBiljka.medicinskeKoristi
        val filtriranaListaNova = biljke.filter{
                biljka -> biljka.medicinskeKoristi.any {korist -> korist in match}
        }
        updateBiljke(filtriranaListaNova)
        trenutneBiljke = filtriranaListaNova as ArrayList<Biljka>
    }
    private fun updateBiljke(biljke: List<Biljka>){
        this.biljke = biljke as ArrayList<Biljka>
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
        val slikaBiljke: ImageView = itemView.findViewById(R.id.slikaItem)
    }
}
