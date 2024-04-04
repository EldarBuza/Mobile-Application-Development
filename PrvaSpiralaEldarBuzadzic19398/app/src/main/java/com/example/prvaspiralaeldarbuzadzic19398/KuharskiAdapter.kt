package com.example.prvaspiralaeldarbuzadzic19398

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KuharskiAdapter(private var biljke: List<Biljka>, private val onClickListener: (Biljka) -> Unit):

    RecyclerView.Adapter<KuharskiAdapter.KuharskiViewHolder>()
    {
        public var trenutneBiljke = biljke
        private lateinit var filtriranaListaNova: List<Biljka>
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KuharskiViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.kuharski_layout, parent, false)
            return KuharskiViewHolder(view)
        }

        override fun onBindViewHolder(holder: KuharskiViewHolder, position: Int) {
            val biljka = biljke[position]
            holder.nazivBiljke.text = biljka.naziv
            holder.profilOkusa.text = biljka.profilOkusa.opis
            if (biljka.jela.size >= 1)
                holder.jelo1Item.text = biljka.jela[0]
            if (biljka.jela.size >= 2)
                holder.jelo2Item.text = biljka.jela[1]
            if (biljka.jela.size >= 3)
                holder.jelo3Item.text = biljka.jela[2]
            holder.itemView.setOnClickListener{
                val podaci = biljke[position]
                onClickListener(podaci)
                filtriraj(podaci)}
        }

        fun filtriraj(trenutnaBiljka: Biljka){
            val match = trenutnaBiljka.jela
            val okus = trenutnaBiljka.profilOkusa
             val filtriranaListaNova = biljke.filter{
                biljka -> biljka.jela.any {jelo -> jelo in match} || biljka.profilOkusa == okus
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

        inner class KuharskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nazivBiljke: TextView = itemView.findViewById(R.id.nazivItem)
            val profilOkusa: TextView = itemView.findViewById(R.id.profilOkusaItem)
            val jelo1Item: TextView = itemView.findViewById(R.id.jelo1Item)
            val jelo2Item: TextView = itemView.findViewById(R.id.jelo2Item)
            val jelo3Item: TextView = itemView.findViewById(R.id.jelo3Item)

        }
    }
