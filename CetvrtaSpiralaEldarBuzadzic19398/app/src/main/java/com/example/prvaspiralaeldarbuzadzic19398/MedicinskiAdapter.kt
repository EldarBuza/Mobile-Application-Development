package com.example.prvaspiralaeldarbuzadzic19398

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        val scope = CoroutineScope(Job() + Dispatchers.Main)
        val context : Context = holder.slikaBiljke.context
        scope.launch {
            val result1 = biljka.id?.let { provjera(App.applicationContext(), it) }
            when(result1) {
                is Boolean->{
                    if (result1){
                        val result2 = getImageFromDatabase(App.applicationContext(), biljka.id!!)
                        when(result2){
                            is Bitmap ->{
                                println("Povlacim sliku iz baze!")
                                holder . slikaBiljke . setImageBitmap (result2)
                            }
                            else ->{
                                val result = TrefleDAO(context).getImage(biljka)
                                biljka.id?.let { dodajSliku(App.applicationContext(), it, result) }
                                holder . slikaBiljke . setImageBitmap (result)

                            }
                        }
                    }else{
                        val result = TrefleDAO(context).getImage(biljka)
                        biljka.id?.let { dodajSliku(App.applicationContext(), it, result) }
                        holder . slikaBiljke . setImageBitmap (result)
                    }
                }
                else ->{
                    val result = TrefleDAO(context).getImage(biljka)
                biljka.id?.let { dodajSliku(App.applicationContext(), it, result) }
                        holder . slikaBiljke . setImageBitmap (result)
                }
            }
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

    suspend fun dodajSliku(context: Context, idBiljke: Long,bitmap: Bitmap): Boolean?{
        return withContext(Dispatchers.IO){
            try {
                var db = BiljkaDatabase.getInstance(context)
                var result = db.biljkaDao().addImage(idBiljke, bitmap)
                return@withContext true
            }catch(error: Exception){
                System.out.println("Isti bitmap pokusano dodati")
                return@withContext null
            }
        }
    }

    suspend fun provjera(context: Context, idBiljke: Long): Boolean?{
        return withContext(Dispatchers.IO){
            try{
                var db = BiljkaDatabase.getInstance(context)
                var result = db.biljkaDao().provjeriPostojiLi(idBiljke)
                if (result.isNotEmpty()){ return@withContext true}
                else {return@withContext false}
            }catch(error: Exception){
                return@withContext null
            }
        }
    }

    suspend fun getImageFromDatabase(context: Context, idBiljke: Long): Bitmap?{
        return withContext(Dispatchers.IO){
            try{
                var db = BiljkaDatabase.getInstance(context)
                var result = db.biljkaDao().provjeriPostojiLi(idBiljke)
                if (result.isNotEmpty()){
                    return@withContext result[0].bitmap
                }else{
                    return@withContext null
                }
            }catch(error: Exception){
                return@withContext null
            }
        }
    }
}
