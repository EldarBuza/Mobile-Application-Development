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
import java.io.ByteArrayOutputStream

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
            holder.profilOkusa.text = biljka.profilOkusa?.opis
            if (biljka.jela.size >= 1) {
                holder.jelo1Item.visibility = View.VISIBLE
                holder.jelo1Item.text = biljka.jela[0]
            }
            else{
                holder.jelo1Item.visibility = View.GONE
            }
            if (biljka.jela.size >= 2) {
                holder.jelo2Item.visibility = View.VISIBLE
                holder.jelo2Item.text = biljka.jela[1]
            }else{
                holder.jelo2Item.visibility = View.GONE
            }
            if (biljka.jela.size >= 3) {
                holder.jelo3Item.visibility = View.VISIBLE
                holder.jelo3Item.text = biljka.jela[2]
            }else{
                holder.jelo3Item.visibility = View.GONE
            }
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
