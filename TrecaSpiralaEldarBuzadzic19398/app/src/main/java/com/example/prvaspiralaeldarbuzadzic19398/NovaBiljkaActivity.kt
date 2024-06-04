package com.example.prvaspiralaeldarbuzadzic19398

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NovaBiljkaActivity : AppCompatActivity() {
    private lateinit var naziv: EditText
    private lateinit var porodica: EditText
    private lateinit var medicinskoUpozorenje: EditText
    private lateinit var jelo: EditText
    private lateinit var medicinskaKorist: ListView
    private lateinit var klimatskiTip: ListView
    private lateinit var zemljisniTip: ListView
    private lateinit var profilOkusa: ListView
    private lateinit var jela: ListView
    private lateinit var dodajBiljku: Button
    private lateinit var uslikajBiljku: Button
    private lateinit var dodajJelo: Button
    private lateinit var slikaIV: ImageView
    private var putanjaSlike: String? = null
    private val REQUEST_IMAGE_CAPTURE = 69420

    fun <T> ArrayAdapter<T>.getItems(): ArrayList<T> {
        val items = ArrayList<T>()
        for (i in 0 until count) {
            getItem(i)?.let { items.add(it) }
        }
        return items
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100 // Replace 100 with your desired request code
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nova_biljka)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        naziv = findViewById(R.id.nazivET)
        porodica = findViewById(R.id.porodicaET)
        medicinskoUpozorenje = findViewById(R.id.medicinskoUpozorenjeET)
        jelo = findViewById(R.id.jeloET)
        medicinskaKorist = findViewById(R.id.medicinskaKoristLV)
        klimatskiTip = findViewById(R.id.klimatskiTipLV)
        zemljisniTip = findViewById(R.id.zemljisniTipLV)
        profilOkusa = findViewById(R.id.profilOkusaLV)
        jela = findViewById(R.id.jelaLV)
        dodajBiljku = findViewById(R.id.dodajBiljkuBtn)
        uslikajBiljku = findViewById(R.id.uslikajBiljkuBtn)
        dodajJelo = findViewById(R.id.dodajJeloBtn)
        slikaIV = findViewById(R.id.slikaIV)
        //Postavljanje adaptera za sve ListView-ove
        val medicinskaKoristStavke: Array<MedicinskaKorist> = MedicinskaKorist.values()
        val medicinskaKoristOpisi = medicinskaKoristStavke.map{it.opis}
        val medicinskaKoristAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            medicinskaKoristOpisi
        )
        medicinskaKorist.adapter = medicinskaKoristAdapter
        medicinskaKorist.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val klimatskiTipStavke: Array<KlimatskiTip> = KlimatskiTip.values()
        val klimatskiTipOpisi = klimatskiTipStavke.map{it.opis}
        val klimatskiTipAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            klimatskiTipOpisi
        )
        klimatskiTip.adapter = klimatskiTipAdapter
        klimatskiTip.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val zemljisniTipStavke: Array<Zemljiste> = Zemljiste.values()
        val zemljisniTipOpisi = zemljisniTipStavke.map{it.naziv}
        val zemljisniTipAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            zemljisniTipOpisi
        )
        zemljisniTip.adapter = zemljisniTipAdapter
        zemljisniTip.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val profilOkusaStavke: Array<ProfilOkusaBiljke> = ProfilOkusaBiljke.values()
        val profilOkusaOpisi = profilOkusaStavke.map{it.opis}
        val profilOkusaAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, profilOkusaOpisi)
        profilOkusa.adapter = profilOkusaAdapter
        profilOkusa.choiceMode = ListView.CHOICE_MODE_SINGLE

        val jelaListView: ListView = findViewById(R.id.jelaLV)
        val jelaStavke = arrayListOf<String>()
        val adapterJela: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList())
        jela.adapter = adapterJela
        //Postavljanje listenera za dodajJelo Button
        var selectedPosition = -1

        fun isDuplicateJelo(newJelo: String): Boolean {
            val normalizedNewJelo = newJelo.lowercase()
            for (jelo in adapterJela.getItems()) {
                if (jelo.lowercase() == normalizedNewJelo) {
                    return true
                }
            }
            return false
        }

        dodajJelo.setOnClickListener {
            val novoJelo = jelo.text.toString()
            var errorDetektovan: Boolean = false
            if (dodajJelo.text.toString().equals("Dodaj jelo") && (novoJelo.length < 2 || novoJelo.length > 20)){
                jelo.setError("Dužina teksta mora biti u segmentu [2,20]")
                errorDetektovan = true
            }
                if (novoJelo.isNotEmpty()) {
                    if (selectedPosition != -1 && !errorDetektovan) {
                            adapterJela.remove(adapterJela.getItem(selectedPosition))
                            adapterJela.insert(novoJelo, selectedPosition)
                            dodajJelo.text = "Dodaj jelo"
                    } else {
                        if (!errorDetektovan) {
                            if (!isDuplicateJelo(novoJelo)) {
                                adapterJela.add(novoJelo)
                            }else{
                                jelo.setError("Nije moguće dodavanje duplikata istog jela!")
                                errorDetektovan = true
                            }
                        }
                    }
                    if (!errorDetektovan) {
                        jela.clearChoices()
                        selectedPosition = -1 // Resetiramo odabranu poziciju
                        adapterJela.notifyDataSetChanged()
                        jelo.setText("")
                    }
                }
                else if (novoJelo.isEmpty() && !errorDetektovan){
                    if (selectedPosition != -1){
                        adapterJela.remove(adapterJela.getItem(selectedPosition))
                        dodajJelo.text = "Dodaj jelo"
                        jela.clearChoices()
                        selectedPosition = -1 // Resetiramo odabranu poziciju
                        adapterJela.notifyDataSetChanged()
                        jelo.setText("")
                    }
                }
            }


        jela.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            selectedPosition = position // Postavljamo trenutnu poziciju kao odabranu
            jelo.setText(adapterJela.getItem(position))
            dodajJelo.text = "Izmijeni jelo"
        }


        dodajBiljku.setOnClickListener{
            var detektovanError: Boolean = false
            val nazivT = naziv.text.toString()
            val porodicaT = porodica.text.toString()
            val medicinskoUpozorenjeT = medicinskoUpozorenje.text.toString()
            //Provjeravamo prvo validnost EditText polja
            if (nazivT.length < 2 || nazivT.length > 40){
                naziv.setError("Dužina teksta mora biti u segmentu [2,40]")
                detektovanError = true
            }
            if (porodicaT.length < 2 || porodicaT.length > 20){
                porodica.setError("Dužina teksta mora biti u segmentu [2,20]")
                detektovanError = true
            }
            if (medicinskoUpozorenjeT.length < 2 || medicinskoUpozorenjeT.length > 20){
                medicinskoUpozorenje.setError("Dužina teksta mora biti u segmentu [2,20]")
                detektovanError = true
            }


            if (!detektovanError) {
                val selectedItems = medicinskaKorist.checkedItemPositions
                var isAtLeastOneItemSelected = false
                for (i in 0 until medicinskaKorist.count) {
                    if (selectedItems.get(i)) {
                        isAtLeastOneItemSelected = true
                        break
                    }
                }
                if (!isAtLeastOneItemSelected){
                    Toast.makeText(this, "Odaberite barem jednu medicinsku korist", Toast.LENGTH_SHORT).show()
                    detektovanError = true
                }
            }


            if (!detektovanError){
                val selectedItems = klimatskiTip.checkedItemPositions
                var isAtLeastOneItemSelected = false
                for (i in 0 until klimatskiTip.count) {
                    if (selectedItems.get(i)) {
                        isAtLeastOneItemSelected = true
                        break
                    }
                }
                if (!isAtLeastOneItemSelected){
                    Toast.makeText(this, "Odaberite barem jedan klimatski tip", Toast.LENGTH_SHORT).show()
                    detektovanError = true
                }
            }


            if (!detektovanError){
                val selectedItems = zemljisniTip.checkedItemPositions
                var isAtLeastOneItemSelected = false
                for (i in 0 until zemljisniTip.count) {
                    if (selectedItems.get(i)) {
                        isAtLeastOneItemSelected = true
                        break
                    }
                }
                if (!isAtLeastOneItemSelected){
                    Toast.makeText(this, "Odaberite barem jednu zemljišni tip", Toast.LENGTH_SHORT).show()
                    detektovanError = true
                }
            }


            if (!detektovanError){
                val listaJela = adapterJela.getItems()
                if (listaJela.count() == 0){
                    Toast.makeText(this, "Dodajte barem jedno jelo", Toast.LENGTH_SHORT).show()
                    detektovanError = true
                }
            }


            if (!detektovanError){
                val selectedItems = profilOkusa.checkedItemPositions
                var isAtLeastOneItemSelected = false
                for (i in 0 until profilOkusa.count) {
                    if (selectedItems.get(i)) {
                        isAtLeastOneItemSelected = true
                        break
                    }
                }
                if (!isAtLeastOneItemSelected){
                    Toast.makeText(this, "Odaberite profil okusa", Toast.LENGTH_SHORT).show()
                    detektovanError = true
                }
            }


            //Ubacimo biljku ukoliko nije detektovan nijedan Error tokom validacije
            if (!detektovanError){
                // nazivT, porodicaT, medicinskoUpozorenjeT
                //Lista jela koju smo unijeli
                val listaJela = adapterJela.getItems()

                //Dobavimo selected medicinske koristi
                val selectedItemsMK = medicinskaKorist.checkedItemPositions
                var selectedMK : ArrayList<MedicinskaKorist> = ArrayList()
                for (i in 0 until medicinskaKorist.count){
                    if (selectedItemsMK.get(i)){
                        selectedMK.add(MedicinskaKorist.entries[i])
                    }
                }

                //Dobavimo selected klimatske tipove
                val selectedItemsKT = klimatskiTip.checkedItemPositions
                var selectedKT : ArrayList<KlimatskiTip> = ArrayList()
                for (i in 0 until klimatskiTip.count){
                    if (selectedItemsKT.get(i)){
                        selectedKT.add(KlimatskiTip.entries[i])
                    }
                }

                //Dobavimo selected zemljišne tipove
                val selectedItemsZT = zemljisniTip.checkedItemPositions
                var selectedZT: ArrayList<Zemljiste> = ArrayList()
                for (i in 0 until zemljisniTip.count){
                    if (selectedItemsZT.get(i)){
                        selectedZT.add(Zemljiste.entries[i])
                    }
                }

                //Dobavimo profil okusa
                val selectedItemPO = profilOkusa.checkedItemPositions
                lateinit var selectedPO: ProfilOkusaBiljke
                for (i in 0 until profilOkusa.count){
                    if (selectedItemPO.get(i)){
                        selectedPO = ProfilOkusaBiljke.entries[i]
                        break
                    }
                }

                var novaDodanaBiljka = Biljka(nazivT, porodicaT, medicinskoUpozorenjeT, selectedMK, selectedPO, listaJela, selectedKT, selectedZT)
                val scope = CoroutineScope(Dispatchers.Main)
                //biljka, moze i biljke[position] ali nema razlike koliko vidim
                scope.launch {
                    val result = TrefleDAO(this@NovaBiljkaActivity).fixData(novaDodanaBiljka)
                    novaDodanaBiljka = result
                }
                SingletonBiljka.biljkeLista.add(novaDodanaBiljka)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        uslikajBiljku.setOnClickListener{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Request the CAMERA permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    // Permission has been granted, proceed with camera capture Intent
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    // Start the camera capture Intent
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                }
            } else {
                // Permission has been granted, proceed with camera capture Intent
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // Start the camera capture Intent
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val slika = data?.extras?.get("data") as Bitmap
            // Prikaz slike u ImageView elementu
            slikaIV.setImageBitmap(slika)
        }
    }



}