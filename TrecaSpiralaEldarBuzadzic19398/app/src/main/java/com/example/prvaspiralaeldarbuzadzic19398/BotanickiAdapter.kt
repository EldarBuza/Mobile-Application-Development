package com.example.prvaspiralaeldarbuzadzic19398

import android.annotation.SuppressLint
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

class BotanickiAdapter(private var biljke: List<Biljka>, private val pretragaUpaljena: Boolean,private val onClickListener: (Biljka) -> Unit):
    RecyclerView.Adapter<BotanickiAdapter.BotanickiViewHolder>()
{
        public var trenutneBiljke = biljke
        lateinit var botView: BotanickiViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanickiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.botanicki_layout, parent, false)
        return BotanickiViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: BotanickiViewHolder, position: Int) {
        val biljka = biljke[position]
        holder.nazivBiljke.text = biljka.naziv
        holder.porodica.text = biljka.porodica
        if (biljka.klimatskiTipovi.size != 0) {
            holder.klimatskiTip.visibility = View.VISIBLE
            holder.klimatskiTip.text = biljka.klimatskiTipovi[0].opis
        }else{
            holder.klimatskiTip.visibility = View.GONE
        }
        if (biljka.zemljisniTipovi.size != 0) {
            holder.klimatskiTip.visibility = View.VISIBLE
            holder.zemljisniTip.text = biljka.zemljisniTipovi[0].naziv
        }else{
            holder.zemljisniTip.visibility = View.GONE
        }
        if (!pretragaUpaljena) {
            holder.itemView.setOnClickListener {
                val podaci = biljke[position]
                onClickListener(podaci)
                filtriraj(podaci)
            }
        }
        val scope = CoroutineScope(Dispatchers.Main)
        val context : Context = holder.slikaBiljke.context
        scope.launch {
            val result = TrefleDAO(context).getImage(biljka)
            holder.slikaBiljke.setImageBitmap(result)
        }
    }


    fun filtriraj(trenutnaBiljka: Biljka){
        val match = trenutnaBiljka.klimatskiTipovi
        val match2 = trenutnaBiljka.zemljisniTipovi
        val porodica = trenutnaBiljka.porodica
        val filtriranaListaNova = biljke.filter{
                biljka -> biljka.klimatskiTipovi.any {kTip -> kTip in match} && biljka.zemljisniTipovi.any {zTip -> zTip in match2} && biljka.porodica == porodica
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
        val slikaBiljke: ImageView = itemView.findViewById(R.id.slikaItem)


    }
}
