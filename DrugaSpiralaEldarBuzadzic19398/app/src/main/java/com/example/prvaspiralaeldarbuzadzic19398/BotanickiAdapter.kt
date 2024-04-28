package com.example.prvaspiralaeldarbuzadzic19398

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BotanickiAdapter(private var biljke: List<Biljka>, private val onClickListener: (Biljka) -> Unit):
    RecyclerView.Adapter<BotanickiAdapter.BotanickiViewHolder>()
{
        public var trenutneBiljke = biljke
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanickiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.botanicki_layout, parent, false)
        return BotanickiViewHolder(view)
    }

    override fun onBindViewHolder(holder: BotanickiViewHolder, position: Int) {
        val biljka = biljke[position]
        holder.nazivBiljke.text = biljka.naziv
        holder.porodica.text = biljka.porodica
        holder.klimatskiTip.text = biljka.klimatskiTipovi[0].opis
        holder.zemljisniTip.text = biljka.zemljisniTipovi[0].naziv

        holder.itemView.setOnClickListener{
            val podaci = biljke[position]
            onClickListener(podaci)
            filtriraj(podaci)}
    }

    fun filtriraj(trenutnaBiljka: Biljka){
        val match = trenutnaBiljka.klimatskiTipovi
        val match2 = trenutnaBiljka.zemljisniTipovi
        val porodica = trenutnaBiljka.porodica
        val filtriranaListaNova = biljke.filter{
                biljka -> biljka.klimatskiTipovi.any {kTip -> kTip in match} || biljka.zemljisniTipovi.any {zTip -> zTip in match2} && biljka.porodica == porodica
        }
        updateBiljke(filtriranaListaNova)
    }
    private fun updateBiljke(biljke: List<Biljka>){
        this.biljke = biljke
        trenutneBiljke = this.biljke
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return biljke.size
    }

    inner class BotanickiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nazivBiljke: TextView = itemView.findViewById(R.id.nazivItem)
        val porodica: TextView = itemView.findViewById(R.id.porodicaItem)
        val klimatskiTip: TextView = itemView.findViewById(R.id.klimatskiTipItem)
        val zemljisniTip: TextView = itemView.findViewById(R.id.zemljisniTipItem)


    }
}
