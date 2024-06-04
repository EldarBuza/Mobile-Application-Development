package com.example.prvaspiralaeldarbuzadzic19398

//ghjeu8Rq3A4kpmMhJCEv0k4SDYAmDg_CkkoyG1R4O24 TREFLE ACCESS TOKEN

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class TrefleDAO(){
    private lateinit var context: Context
    constructor(context: Context) : this() {
        this.context = context
    }

    suspend fun getImage(biljka: Biljka): Bitmap {
        val defaultBitmap: Bitmap = BitmapFactory.decodeResource(App.applicationContext().resources, R.drawable.plant2)
        return withContext(Dispatchers.IO) {
            try {
                val latinskiNaziv :String = biljka.naziv.substringAfter("(").substringBefore(")")
                val response = ApiAdapter.retrofit.searchPlants(latinskiNaziv)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.data.isNotEmpty()) {
                        val imageUrl = responseBody.data[0].slikaBiljkeURL
                        if (imageUrl.isNotEmpty()) {
                            Glide.with(context)
                                .asBitmap()
                                .load(imageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .submit()
                                .get()
                        } else {
                            defaultBitmap
                        }
                    } else {
                        defaultBitmap
                    }
                } else {
                    defaultBitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
                defaultBitmap
            }
        }
    }

    suspend fun fixData(biljka: Biljka): Biljka{
        return withContext(Dispatchers.IO){
            try{
                val latinskiNaziv: String = biljka.naziv.substringAfter("(").substringBefore(")")
                val firstResponse = ApiAdapter.retrofit.searchPlants(latinskiNaziv)
                if (firstResponse.isSuccessful) {
                    val firstResponseBody = firstResponse.body()
                    if (firstResponseBody != null && firstResponseBody.data.isNotEmpty()) {
                        val plantData = firstResponseBody.data?.firstOrNull()

                        if (plantData != null) {

                            val secondResponse = ApiAdapter.retrofit.getPlantViaId(plantData.id)


                            if (secondResponse != null) {
                                if (secondResponse.isSuccessful) {
                                    val correctBody = secondResponse.body()
                                    val biljkaDetailed = correctBody?.data
                                    if (biljkaDetailed != null) {
                                        //Provjeri porodicu i zamijeni ukoliko nisu vec jednake
                                        if (biljka.porodica != biljkaDetailed.porodicaData.porodica) {
                                            biljka.porodica = biljkaDetailed.porodicaData.porodica
                                        }
                                        //Rijesavanje dijela jestivnosti
                                        System.out.println(biljkaDetailed.mainSpec.jestivo)
                                        if (!biljkaDetailed.mainSpec.jestivo) {
                                            biljka.jela = emptyList()
                                            if (!biljka.medicinskoUpozorenje.contains("NIJE JESTIVO")) {
                                                biljka.medicinskoUpozorenje = biljka.medicinskoUpozorenje + " NIJE JESTIVO"
                                            }
                                        }
                                        //Provjeravanje atributa toxicity i dodavanje odgovarajućeg medicinskog upozorenja
                                        if (!biljkaDetailed.mainSpec.specifikacije.toksicnost.isNullOrEmpty() && biljkaDetailed.mainSpec.specifikacije.toksicnost != "none") {
                                            if (!biljka.medicinskoUpozorenje.contains("TOKSIČNO")) {
                                                biljka.medicinskoUpozorenje = biljka.medicinskoUpozorenje + " TOKSIČNO"
                                            }
                                        }
                                        //Odredjivanje odgovarajucih zemljista
                                        if (!biljkaDetailed.mainSpec.growthInfo.teksturaZemljista.isNullOrEmpty()) {
                                            val soilTexture =
                                                biljkaDetailed.mainSpec.growthInfo.teksturaZemljista!!.toInt()
                                            var listaZemljista: ArrayList<Zemljiste> = ArrayList<Zemljiste>()
                                            if (soilTexture == 9) {
                                                listaZemljista.add(Zemljiste.SLJUNKOVITO)
                                            } else if (soilTexture == 10) {
                                                listaZemljista.add(Zemljiste.KRECNJACKO)
                                            } else if (soilTexture >= 1 && soilTexture <= 2) {
                                                listaZemljista.add(Zemljiste.GLINENO)
                                            } else if (soilTexture >= 3 && soilTexture <= 4) {
                                                listaZemljista.add(Zemljiste.PJESKOVITO)
                                            } else if (soilTexture >= 5 && soilTexture <= 6) {
                                                listaZemljista.add(Zemljiste.ILOVACA)
                                            } else if (soilTexture >= 7 && soilTexture <= 8) {
                                                listaZemljista.add(Zemljiste.CRNICA)
                                            }
                                            if (listaZemljista.count() == 0) biljka.zemljisniTipovi = emptyList()
                                            else {
                                                biljka.zemljisniTipovi = listaZemljista
                                            }
                                        } else {
                                            biljka.zemljisniTipovi = emptyList()
                                        }
                                        //Odredjivanje odgovarajucih klimatskih tipova
                                        if (!biljkaDetailed.mainSpec.growthInfo.svjetlost.isNullOrEmpty() || !biljkaDetailed.mainSpec.growthInfo.vlaznost.isNullOrEmpty()) {
                                            val light =
                                                biljkaDetailed.mainSpec.growthInfo.svjetlost!!.toInt()
                                            val atmosphericHumidity =
                                                biljkaDetailed.mainSpec.growthInfo.vlaznost!!.toInt()
                                            var listaKlimatskihTipova: ArrayList<KlimatskiTip> = ArrayList<KlimatskiTip>()
                                            if (light >= 6 && light <= 9 && atmosphericHumidity >= 1 && atmosphericHumidity <= 5) {
                                                listaKlimatskihTipova.add(KlimatskiTip.SREDOZEMNA)
                                            }
                                            if (light >= 8 && light <= 10 && atmosphericHumidity >= 7 && atmosphericHumidity <= 10) {
                                                listaKlimatskihTipova.add(KlimatskiTip.TROPSKA)
                                            }
                                            if (light >= 6 && light <= 9 && atmosphericHumidity >= 5 && atmosphericHumidity <= 8) {
                                                listaKlimatskihTipova.add(KlimatskiTip.SUBTROPSKA)
                                            }
                                            if (light >= 4 && light <= 7 && atmosphericHumidity >= 3 && atmosphericHumidity <= 7) {
                                                listaKlimatskihTipova.add(KlimatskiTip.UMJERENA)
                                            }
                                            if (light >= 7 && light <= 9 && atmosphericHumidity >= 1 && atmosphericHumidity <= 2) {
                                                listaKlimatskihTipova.add(KlimatskiTip.SUHA)
                                            }
                                            if (light >= 0 && light <= 5 && atmosphericHumidity >= 3 && atmosphericHumidity <= 7) {
                                                listaKlimatskihTipova.add(KlimatskiTip.PLANINSKA)
                                            }
                                            if (listaKlimatskihTipova.count() == 0) {
                                                biljka.klimatskiTipovi = emptyList()
                                            } else {
                                                biljka.klimatskiTipovi = listaKlimatskihTipova
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                biljka
            }catch(e: Exception){
                e.printStackTrace()
                biljka
            }
        }
    }

    suspend fun getPlantsWithFlowerColor(flower_color: String, substr: String): List<Biljka>{
        return withContext(Dispatchers.IO) {
            var noveBiljke: ArrayList<Biljka> = ArrayList<Biljka>()
            delay(5000)
            var response = ApiAdapter.retrofit.searchPlantsByColor(flower_color, substr)
             if (response.isSuccessful) {
                    var listaBiljaka = response.body()
                    if (listaBiljaka != null) {
                        for (biljka in listaBiljaka.data) {
                            var fullResponse = ApiAdapter.retrofit.getPlantViaId(biljka.id)
                            if (fullResponse.isSuccessful) {
                                var puniPodaciBiljke = fullResponse.body()
                                /*var latinskiNaziv = puniPodaciBiljke?.data?.latinskiNaziv
                                if (latinskiNaziv != null) {
                                    latinskiNaziv = latinskiNaziv.lowercase()
                                }
                                var normalanNaziv = puniPodaciBiljke?.data?.engleskiNaziv
                                if (normalanNaziv != null) {
                                    normalanNaziv = normalanNaziv.lowercase()
                                }
                                System.out.println(latinskiNaziv + " " + substr + " " + normalanNaziv)
                                var substr1 = substr.lowercase()
                                if ((latinskiNaziv == null || !latinskiNaziv.contains(substr1)) && (normalanNaziv == null || !normalanNaziv.contains(
                                        substr1
                                    ))
                                ) {
                                    continue
                                }*/
                                var nazivBiljke =
                                    puniPodaciBiljke?.data?.engleskiNaziv + " (" + puniPodaciBiljke?.data?.latinskiNaziv + ")"
                                println(nazivBiljke)
                                var porodicaBiljke = puniPodaciBiljke?.data?.porodicaData?.porodica
                                if (porodicaBiljke == null) porodicaBiljke = ""
                                var medicinskoUpozorenjeBiljke = ""
                                var medicinskeKoristiBiljke = emptyList<MedicinskaKorist>()
                                var profilOkusaBiljke: ProfilOkusaBiljke? = null
                                var jelaBiljke: List<String> = emptyList()
                                var klimatskiTipoviBiljke: List<KlimatskiTip> = emptyList()
                                var zemljisniTipoviBiljke: List<Zemljiste> = emptyList()
                                var novaBiljka = Biljka(
                                    nazivBiljke,
                                    porodicaBiljke,
                                    medicinskoUpozorenjeBiljke,
                                    medicinskeKoristiBiljke,
                                    profilOkusaBiljke,
                                    jelaBiljke,
                                    klimatskiTipoviBiljke,
                                    zemljisniTipoviBiljke
                                )
                                novaBiljka = fixData(novaBiljka)
                                noveBiljke.add(novaBiljka)

                            }
                        }

                    }


        }

            noveBiljke
        }
    }




}