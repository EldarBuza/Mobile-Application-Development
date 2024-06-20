package com.example.prvaspiralaeldarbuzadzic19398

import android.annotation.SuppressLint
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
import java.io.ByteArrayOutputStream

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

    suspend fun dodajSliku(context: Context, idBiljke: Long,bitmap: Bitmap): Boolean?{
        return withContext(Dispatchers.IO){
            try {
                var db = BiljkaDatabase.getInstance(context)
                val cropovanBitmap = resizeAndCompressBitmap(bitmap, 1*1024*1024)
                var result = cropovanBitmap?.let { db.biljkaDao().addImage(idBiljke, it) }
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

    fun resizeAndCompressBitmap(bitmap: Bitmap, maxByteSize: Int): Bitmap? {
        var width = bitmap.width
        var height = bitmap.height
        var scaledBitmap: Bitmap? = bitmap

        // Compress and check size
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        var bitmapByteArray = byteArrayOutputStream.toByteArray()

        // Scale down the bitmap until it fits within the max byte size
        while (bitmapByteArray.size > maxByteSize) {
            width /= 2
            height /= 2
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
            byteArrayOutputStream.reset()
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            bitmapByteArray = byteArrayOutputStream.toByteArray()
        }

        return scaledBitmap
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
