package com.example.prvaspiralaeldarbuzadzic19398

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class App : Application() {

    override fun onCreate() {
        instance = this
        super.onCreate()
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        if (isFirstRun){
            scope.launch {
                val result = spasiBiljke(applicationContext)
                when(result) {
                    is String -> {
                        System.out.println(result)
                        sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
                    }
                    else ->{
                        throw(Exception("Nije dobro proslo"))
                    }
                }
            }
        }
    }

    companion object {
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance as App
        }
    }
    suspend fun spasiBiljke(context: Context): String?{
        return withContext(Dispatchers.IO){
            try {
                var db = BiljkaDatabase.getInstance(context)
                val osnovnaLista: ArrayList<Biljka> = SingletonBiljka.biljkeLista
                for (biljka in osnovnaLista) {
                    db!!.biljkaDao().saveBiljka(biljka)
                }
                return@withContext "success"
            }catch(error: Exception){
                return@withContext null
            }
        }
    }


}
