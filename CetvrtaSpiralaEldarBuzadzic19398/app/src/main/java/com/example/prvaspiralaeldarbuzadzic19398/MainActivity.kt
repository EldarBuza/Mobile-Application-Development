package com.example.prvaspiralaeldarbuzadzic19398

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var biljkeRecyclerView: RecyclerView
    private lateinit var kuharskiAdapter: KuharskiAdapter
    private lateinit var medicinskiAdapter: MedicinskiAdapter
    private lateinit var botanickiAdapter: BotanickiAdapter
    private lateinit var lastFocus: String
    private lateinit var resetButton: Button
    private lateinit var novaBiljkaButton: Button
    private lateinit var brzaPretragaButton: Button
    private lateinit var pretragaText: EditText
    private lateinit var bojaSpinner: Spinner


    private var pretragaIzvrsena: Boolean = false
    private var REQUEST_CODE = 12345
    var biljke = ArrayList<Biljka>()
    var tBiljke = biljke


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                val tempBiljke =  ArrayList<Biljka>(ucitajBiljke(this@MainActivity))
                when(tempBiljke){
                    is ArrayList<Biljka> -> {
                        medicinskiAdapter = MedicinskiAdapter(tempBiljke) { trenutnaBiljka ->
                            medicinskiAdapter.filtriraj((trenutnaBiljka))
                        }
                        println(biljke.size)
                        biljkeRecyclerView.adapter = medicinskiAdapter
                        lastFocus = "Medicinski"
                        spinner.setSelection(0)
                        medicinskiAdapter.notifyDataSetChanged()
                    }
                }
            }

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            var tempBiljke = ucitajBiljke(this@MainActivity)
            when(tempBiljke){
                is List<Biljka> -> {biljke = ArrayList<Biljka>(tempBiljke)
                    setContentView(R.layout.main_layout)
                    resetButton = findViewById(R.id.resetBtn)
                    novaBiljkaButton = findViewById(R.id.novaBiljkaBtn)
                    spinner = findViewById(R.id.modSpinner)
                    biljkeRecyclerView = findViewById(R.id.biljkeRV)
                    brzaPretragaButton = findViewById(R.id.brzaPretraga)
                    bojaSpinner = findViewById(R.id.bojaSPIN)
                    pretragaText = findViewById(R.id.pretragaET)
                    val modovi = arrayOf("Medicinski", "Kuharski", "Botanički")
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, modovi)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                    val boje = arrayOf("", "red", "blue", "yellow", "orange", "purple", "brown", "green")
                    val adapterBoja = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, boje)
                    adapterBoja.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    bojaSpinner.adapter = adapterBoja
                    // Postavljanje defaultnog moda
                    spinner.setSelection(0)
                    // Postavljanje RecyclerView layout managera i adaptera
                    biljkeRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                    medicinskiAdapter = MedicinskiAdapter(biljke){trenutnaBiljka -> medicinskiAdapter.filtriraj(trenutnaBiljka)}
                    biljkeRecyclerView.adapter = medicinskiAdapter
                    biljke = medicinskiAdapter.trenutneBiljke
                    pretragaText.visibility = View.GONE
                    brzaPretragaButton.visibility = View.GONE
                    bojaSpinner.visibility = View.GONE
                    lastFocus = "Medicinski"


                    resetButton.setOnClickListener(object: View.OnClickListener{
                        override fun onClick(v: View?){
                            if (lastFocus == "Medicinski"){
                                pretragaText.visibility = View.GONE
                                brzaPretragaButton.visibility = View.GONE
                                bojaSpinner.visibility = View.GONE
                                val scope = CoroutineScope(Job() + Dispatchers.Main)
                                scope.launch {
                                    val tempBiljke = ucitajBiljke(this@MainActivity)
                                    when(tempBiljke){
                                        is List<Biljka> ->{biljke = ArrayList(tempBiljke)
                                            medicinskiAdapter = MedicinskiAdapter(biljke) { trenutnaBiljka ->
                                                medicinskiAdapter.filtriraj(trenutnaBiljka)
                                            }
                                            biljkeRecyclerView.adapter = medicinskiAdapter
                                        }
                                        else ->{
                                            throw Exception("Nije se pravilno izvrsilo!")
                                        }
                                    }
                                }
                            }
                            else if (lastFocus == "Kuharski"){
                                pretragaText.visibility = View.GONE
                                brzaPretragaButton.visibility = View.GONE
                                bojaSpinner.visibility = View.GONE
                                val scope = CoroutineScope(Job() + Dispatchers.Main)
                                scope.launch {
                                    val tempBiljke = ucitajBiljke(this@MainActivity)
                                    when(tempBiljke) {
                                        is List<Biljka> -> { biljke = ArrayList(tempBiljke)
                                            kuharskiAdapter = KuharskiAdapter(biljke) { trenutnaBiljka ->
                                                kuharskiAdapter.filtriraj(trenutnaBiljka)
                                            }
                                            biljkeRecyclerView.adapter = kuharskiAdapter
                                        }
                                        else ->{
                                            throw Exception("Nije se pravilno izvrsilo!")}

                                    }
                                }
                            }
                            else if (lastFocus == "Botanički"){
                                pretragaText.visibility = View.VISIBLE
                                brzaPretragaButton.visibility = View.VISIBLE
                                bojaSpinner.visibility = View.VISIBLE
                                val scope = CoroutineScope(Job() + Dispatchers.Main)
                                scope.launch {
                                    val tempBiljke = ucitajBiljke(this@MainActivity)
                                    when(tempBiljke) {
                                        is List<Biljka> -> {biljke = ArrayList(tempBiljke)
                                            botanickiAdapter = BotanickiAdapter(
                                                biljke,
                                                false
                                            ) { trenutnaBiljka -> botanickiAdapter.filtriraj(trenutnaBiljka) }
                                            biljkeRecyclerView.adapter = botanickiAdapter
                                        }
                                        else ->{
                                            throw Exception("Nije se pravilno izvrsilo!")}
                                    }
                                }
                            }
                        }
                    })



                    novaBiljkaButton.setOnClickListener {



                        val intent = Intent(this@MainActivity, NovaBiljkaActivity::class.java)
                        startActivityForResult(intent, REQUEST_CODE)



                    }

                    brzaPretragaButton.setOnClickListener{
                        val scope = CoroutineScope(Dispatchers.Main)
                        scope.launch {
                            val boja = bojaSpinner.selectedItem.toString()
                            val tekstPretrage = pretragaText.text.toString()
                            if (!boja.equals("") && !tekstPretrage.equals("")){
                                val noveBiljkeZaPrikazati = TrefleDAO(this@MainActivity).getPlantsWithFlowerColor(boja, tekstPretrage)
                                val noOpClickListener: (Biljka) -> Unit = {}
                                botanickiAdapter = BotanickiAdapter(noveBiljkeZaPrikazati, true,noOpClickListener)
                                botanickiAdapter
                                biljkeRecyclerView.adapter = botanickiAdapter
                                pretragaText.setText("")
                                bojaSpinner.setSelection(0)
                            }
                            else if (tekstPretrage.equals("")){
                                Toast.makeText(this@MainActivity, "Polje za pretragu je prazno!", Toast.LENGTH_SHORT).show()
                                pretragaText.error = "Polje za pretragu je prazno"
                            }
                        }
                        pretragaIzvrsena = true
                    }

                    // Postavljanje listenera na promjenu odabira u Spinner-u
                    spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            when (position) {
                                0 -> {
                                    if (lastFocus == "Kuharski"){
                                        biljke = kuharskiAdapter.trenutneBiljke as ArrayList<Biljka>
                                    }
                                    else if (lastFocus == "Botanički") {
                                        pretragaText.visibility = View.GONE
                                        brzaPretragaButton.visibility = View.GONE
                                        bojaSpinner.visibility = View.GONE
                                        if (pretragaIzvrsena){
                                            pretragaIzvrsena = false
                                            biljke = SingletonBiljka.biljkeLista
                                        }else {
                                            biljke = botanickiAdapter.trenutneBiljke as ArrayList<Biljka>
                                        }
                                    }
                                    medicinskiAdapter = MedicinskiAdapter(biljke){trenutnaBiljka -> medicinskiAdapter.filtriraj(trenutnaBiljka)}
                                    biljke = medicinskiAdapter.trenutneBiljke
                                    biljkeRecyclerView.adapter = medicinskiAdapter
                                    lastFocus = "Medicinski"
                                }
                                1 -> {
                                    if (lastFocus == "Medicinski"){
                                        biljke = medicinskiAdapter.trenutneBiljke
                                    }
                                    else if (lastFocus == "Botanički") {
                                        pretragaText.visibility = View.GONE
                                        brzaPretragaButton.visibility = View.GONE
                                        bojaSpinner.visibility = View.GONE
                                        if (pretragaIzvrsena){
                                            pretragaIzvrsena = false
                                            biljke = SingletonBiljka.biljkeLista
                                        }else {
                                            biljke = botanickiAdapter.trenutneBiljke as ArrayList<Biljka>
                                        }
                                    }
                                    kuharskiAdapter = KuharskiAdapter(biljke){trenutnaBiljka -> kuharskiAdapter.filtriraj(trenutnaBiljka)}
                                    biljke = kuharskiAdapter.trenutneBiljke as ArrayList<Biljka>
                                    biljkeRecyclerView.adapter = kuharskiAdapter
                                    lastFocus = "Kuharski"
                                }
                                2 -> {
                                    if (lastFocus == "Kuharski"){
                                        biljke = kuharskiAdapter.trenutneBiljke as ArrayList<Biljka>
                                    }
                                    else if (lastFocus == "Medicinski") {
                                        biljke = medicinskiAdapter.trenutneBiljke
                                    }
                                    pretragaText.visibility = View.VISIBLE
                                    brzaPretragaButton.visibility = View.VISIBLE
                                    bojaSpinner.visibility = View.VISIBLE
                                    botanickiAdapter = BotanickiAdapter(biljke, false){trenutnaBiljka -> botanickiAdapter.filtriraj(trenutnaBiljka)}
                                    biljke = botanickiAdapter.trenutneBiljke as ArrayList<Biljka>
                                    biljkeRecyclerView.adapter = botanickiAdapter
                                    lastFocus = "Botanički"
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    })
                }
                else -> println("Nije se izvrsilo")
            }
        }

        println(biljke.size)
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        /*setContentView(R.layout.main_layout)
        resetButton = findViewById(R.id.resetBtn)
        novaBiljkaButton = findViewById(R.id.novaBiljkaBtn)
        spinner = findViewById(R.id.modSpinner)
        biljkeRecyclerView = findViewById(R.id.biljkeRV)
        brzaPretragaButton = findViewById(R.id.brzaPretraga)
        bojaSpinner = findViewById(R.id.bojaSPIN)
        pretragaText = findViewById(R.id.pretragaET)
        val modovi = arrayOf("Medicinski", "Kuharski", "Botanički")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, modovi)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val boje = arrayOf("", "red", "blue", "yellow", "orange", "purple", "brown", "green")
        val adapterBoja = ArrayAdapter(this, android.R.layout.simple_spinner_item, boje)
        adapterBoja.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bojaSpinner.adapter = adapterBoja
        // Postavljanje defaultnog moda
        spinner.setSelection(0)
        // Postavljanje RecyclerView layout managera i adaptera
        biljkeRecyclerView.layoutManager = LinearLayoutManager(this)

        medicinskiAdapter = MedicinskiAdapter(biljke){trenutnaBiljka -> medicinskiAdapter.filtriraj(trenutnaBiljka)}
        biljkeRecyclerView.adapter = medicinskiAdapter
        biljke = medicinskiAdapter.trenutneBiljke
        pretragaText.visibility = View.GONE
        brzaPretragaButton.visibility = View.GONE
        bojaSpinner.visibility = View.GONE
        lastFocus = "Medicinski"


        resetButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                if (lastFocus == "Medicinski"){
                    pretragaText.visibility = View.GONE
                    brzaPretragaButton.visibility = View.GONE
                    bojaSpinner.visibility = View.GONE
                    val scope = CoroutineScope(Job() + Dispatchers.Main)
                    scope.launch {
                        val tempBiljke = ucitajBiljke(this@MainActivity)
                        when(tempBiljke){
                            is List<Biljka> ->{biljke = ArrayList(tempBiljke)
                        medicinskiAdapter = MedicinskiAdapter(biljke) { trenutnaBiljka ->
                            medicinskiAdapter.filtriraj(trenutnaBiljka)
                        }
                        biljkeRecyclerView.adapter = medicinskiAdapter
                    }
                            else ->{
                                throw Exception("Nije se pravilno izvrsilo!")
                            }
                        }
                    }
                }
                else if (lastFocus == "Kuharski"){
                    pretragaText.visibility = View.GONE
                    brzaPretragaButton.visibility = View.GONE
                    bojaSpinner.visibility = View.GONE
                    val scope = CoroutineScope(Job() + Dispatchers.Main)
                    scope.launch {
                        val tempBiljke = ucitajBiljke(this@MainActivity)
                        when(tempBiljke) {
                            is List<Biljka> -> { biljke = ArrayList(tempBiljke)
                                kuharskiAdapter = KuharskiAdapter(biljke) { trenutnaBiljka ->
                                    kuharskiAdapter.filtriraj(trenutnaBiljka)
                                }
                                biljkeRecyclerView.adapter = kuharskiAdapter
                            }
                            else ->{
                                throw Exception("Nije se pravilno izvrsilo!")}

                        }
                    }
                }
                else if (lastFocus == "Botanički"){
                    pretragaText.visibility = View.VISIBLE
                    brzaPretragaButton.visibility = View.VISIBLE
                    bojaSpinner.visibility = View.VISIBLE
                    val scope = CoroutineScope(Job() + Dispatchers.Main)
                    scope.launch {
                        val tempBiljke = ucitajBiljke(this@MainActivity)
                        when(tempBiljke) {
                            is List<Biljka> -> {biljke = ArrayList(tempBiljke)
                                botanickiAdapter = BotanickiAdapter(
                                    biljke,
                                    false
                                ) { trenutnaBiljka -> botanickiAdapter.filtriraj(trenutnaBiljka) }
                                biljkeRecyclerView.adapter = botanickiAdapter
                            }
                            else ->{
                                throw Exception("Nije se pravilno izvrsilo!")}
                        }
                    }
                }
            }
        })



        novaBiljkaButton.setOnClickListener {



            val intent = Intent(this, NovaBiljkaActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)



        }

        brzaPretragaButton.setOnClickListener{
            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                val boja = bojaSpinner.selectedItem.toString()
                val tekstPretrage = pretragaText.text.toString()
                if (!boja.equals("") && !tekstPretrage.equals("")){
                    val noveBiljkeZaPrikazati = TrefleDAO(this@MainActivity).getPlantsWithFlowerColor(boja, tekstPretrage)
                    val noOpClickListener: (Biljka) -> Unit = {}
                    botanickiAdapter = BotanickiAdapter(noveBiljkeZaPrikazati, true,noOpClickListener)
                    botanickiAdapter
                    biljkeRecyclerView.adapter = botanickiAdapter
                    pretragaText.setText("")
                    bojaSpinner.setSelection(0)
                }
                else if (tekstPretrage.equals("")){
                    Toast.makeText(this@MainActivity, "Polje za pretragu je prazno!", Toast.LENGTH_SHORT).show()
                    pretragaText.error = "Polje za pretragu je prazno"
                }
            }
            pretragaIzvrsena = true
        }

        // Postavljanje listenera na promjenu odabira u Spinner-u
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        if (lastFocus == "Kuharski"){
                            biljke = kuharskiAdapter.trenutneBiljke as ArrayList<Biljka>
                        }
                        else if (lastFocus == "Botanički") {
                            pretragaText.visibility = View.GONE
                            brzaPretragaButton.visibility = View.GONE
                            bojaSpinner.visibility = View.GONE
                            if (pretragaIzvrsena){
                                pretragaIzvrsena = false
                                biljke = SingletonBiljka.biljkeLista
                            }else {
                                biljke = botanickiAdapter.trenutneBiljke as ArrayList<Biljka>
                            }
                        }
                        medicinskiAdapter = MedicinskiAdapter(biljke){trenutnaBiljka -> medicinskiAdapter.filtriraj(trenutnaBiljka)}
                        biljke = medicinskiAdapter.trenutneBiljke
                        biljkeRecyclerView.adapter = medicinskiAdapter
                        lastFocus = "Medicinski"
                    }
                    1 -> {
                        if (lastFocus == "Medicinski"){
                            biljke = medicinskiAdapter.trenutneBiljke
                        }
                        else if (lastFocus == "Botanički") {
                            pretragaText.visibility = View.GONE
                            brzaPretragaButton.visibility = View.GONE
                            bojaSpinner.visibility = View.GONE
                            if (pretragaIzvrsena){
                                pretragaIzvrsena = false
                                biljke = SingletonBiljka.biljkeLista
                            }else {
                                biljke = botanickiAdapter.trenutneBiljke as ArrayList<Biljka>
                            }
                        }
                        kuharskiAdapter = KuharskiAdapter(biljke){trenutnaBiljka -> kuharskiAdapter.filtriraj(trenutnaBiljka)}
                        biljke = kuharskiAdapter.trenutneBiljke as ArrayList<Biljka>
                        biljkeRecyclerView.adapter = kuharskiAdapter
                        lastFocus = "Kuharski"
                    }
                    2 -> {
                        if (lastFocus == "Kuharski"){
                            biljke = kuharskiAdapter.trenutneBiljke as ArrayList<Biljka>
                        }
                        else if (lastFocus == "Medicinski") {
                            biljke = medicinskiAdapter.trenutneBiljke
                        }
                        pretragaText.visibility = View.VISIBLE
                        brzaPretragaButton.visibility = View.VISIBLE
                        bojaSpinner.visibility = View.VISIBLE
                        botanickiAdapter = BotanickiAdapter(biljke, false){trenutnaBiljka -> botanickiAdapter.filtriraj(trenutnaBiljka)}
                        biljke = botanickiAdapter.trenutneBiljke as ArrayList<Biljka>
                        biljkeRecyclerView.adapter = botanickiAdapter
                        lastFocus = "Botanički"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })*/

    }


    suspend fun ucitajBiljke(context: Context): List<Biljka>?{
        return withContext(Dispatchers.IO){
            try {
                var db = BiljkaDatabase.getInstance(context)
                var sveBiljke = db!!.biljkaDao().getAllBiljkas()
                println("Ucitalo se " + sveBiljke.size + " biljaka.")
                return@withContext sveBiljke
            }catch(error: Exception){
                println("Nije se ucitala nijedna biljka")
                return@withContext null
            }
        }
    }

    suspend fun izbrisiSve(context: Context): String?{
        return withContext(Dispatchers.IO){
            try {
                var db = BiljkaDatabase.getInstance(context)
                db.biljkaDao().clearData()
                return@withContext "success"
            }catch(error: Exception){
                return@withContext null
            }
        }
    }

    suspend fun brojOfflineBiljaka(context: Context): Int?{
        return withContext(Dispatchers.IO){
            try{
                var db = BiljkaDatabase.getInstance(context)
                val result = db.biljkaDao().getOfflineBiljkas()
                return@withContext result.size
            }catch(error: Exception){
                return@withContext null
            }
        }
    }

    suspend fun popraviOfflineBiljke(context: Context): Int?{
        return withContext(Dispatchers.IO){
            try{
                var db = BiljkaDatabase.getInstance(context)
                val result = db.biljkaDao().fixOfflineBiljka()
                return@withContext result
            }catch(error:Exception){
                return@withContext null
            }
        }
    }





}




