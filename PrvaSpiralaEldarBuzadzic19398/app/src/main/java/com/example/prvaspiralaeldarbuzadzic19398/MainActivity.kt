package com.example.prvaspiralaeldarbuzadzic19398

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var biljkeRecyclerView: RecyclerView
    private lateinit var kuharskiAdapter: KuharskiAdapter
    private lateinit var medicinskiAdapter: MedicinskiAdapter
    private lateinit var botanickiAdapter: BotanickiAdapter
    private lateinit var lastFocus: String
    private lateinit var resetButton: Button
    var biljke = getBiljkePreset()
    var filtriraneBiljke = getBiljkePreset()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        resetButton = findViewById(R.id.resetBtn)
        spinner = findViewById(R.id.modSpinner)
        biljkeRecyclerView = findViewById(R.id.biljkeRV)
        val modovi = arrayOf("Medicinski", "Kuharski", "Botanički")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, modovi)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Postavljanje defaultnog moda
        spinner.setSelection(0)

        // Postavljanje RecyclerView layout managera i adaptera
        biljkeRecyclerView.layoutManager = LinearLayoutManager(this)

        medicinskiAdapter = MedicinskiAdapter(biljke){trenutnaBiljka -> medicinskiAdapter.filtriraj(trenutnaBiljka)}
        biljkeRecyclerView.adapter = medicinskiAdapter
        biljke = medicinskiAdapter.trenutneBiljke
        lastFocus = "Medicinski"

        resetButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                if (lastFocus == "Medicinski"){
                    biljke = getBiljkePreset()
                    medicinskiAdapter = MedicinskiAdapter(biljke){trenutnaBiljka -> medicinskiAdapter.filtriraj(trenutnaBiljka)}
                    biljkeRecyclerView.adapter = medicinskiAdapter
                }
                else if (lastFocus == "Kuharski"){
                    biljke = getBiljkePreset()
                    kuharskiAdapter = KuharskiAdapter(biljke){trenutnaBiljka -> kuharskiAdapter.filtriraj(trenutnaBiljka)}
                    biljkeRecyclerView.adapter = kuharskiAdapter
                }
                else if (lastFocus == "Botanički"){
                    biljke = getBiljkePreset()
                    botanickiAdapter = BotanickiAdapter(biljke){trenutnaBiljka -> botanickiAdapter.filtriraj(trenutnaBiljka)}
                    biljkeRecyclerView.adapter = botanickiAdapter
                }
            }
        })
        // Postavljanje listenera na promjenu odabira u Spinner-u
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        if (lastFocus == "Kuharski"){
                            biljke = kuharskiAdapter.trenutneBiljke
                        }
                        else if (lastFocus == "Botanički") {
                            biljke = botanickiAdapter.trenutneBiljke
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
                            biljke = botanickiAdapter.trenutneBiljke
                        }
                        kuharskiAdapter = KuharskiAdapter(biljke){trenutnaBiljka -> kuharskiAdapter.filtriraj(trenutnaBiljka)}
                        biljke = kuharskiAdapter.trenutneBiljke
                        biljkeRecyclerView.adapter = kuharskiAdapter
                        lastFocus = "Kuharski"
                    }
                    2 -> {
                        if (lastFocus == "Kuharski"){
                            biljke = kuharskiAdapter.trenutneBiljke
                        }
                        else if (lastFocus == "Medicinski") {
                            biljke = medicinskiAdapter.trenutneBiljke
                        }
                        botanickiAdapter = BotanickiAdapter(biljke){trenutnaBiljka -> botanickiAdapter.filtriraj(trenutnaBiljka)}
                        biljke = botanickiAdapter.trenutneBiljke
                        biljkeRecyclerView.adapter = botanickiAdapter
                        lastFocus = "Botanički"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
    }




    private fun getBiljkePreset(): List<Biljka> {
        val biljke = listOf(
            Biljka(
                naziv = "Bosiljak (Ocimum basilicum)",
                porodica = "Lamiaceae (usnate)",
                medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
                medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
                profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
                jela = listOf("Salata od paradajza", "Punjene tikvice"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljište.PJESKOVITO, Zemljište.ILOVACA)
            ),
            Biljka(
                naziv = "Nana (Mentha spicata)",
                porodica = "Lamiaceae (metvice)",
                medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
                medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA),
                profilOkusa = ProfilOkusaBiljke.MENTA,
                jela = listOf("Jogurt sa voćem", "Gulaš"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
                zemljisniTipovi = listOf(Zemljište.GLINENO, Zemljište.CRNICA)
            ),
            Biljka(
                naziv = "Kamilica (Matricaria chamomilla)",
                porodica = "Asteraceae (glavočike)",
                medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
                medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Čaj od kamilice"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljište.PJESKOVITO, Zemljište.KRECNJACKO)
            ),
            Biljka(
                naziv = "Ružmarin (Rosmarinus officinalis)",
                porodica = "Lamiaceae (metvice)",
                medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
                medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Pečeno pile", "Grah","Gulaš"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
                zemljisniTipovi = listOf(Zemljište.SLJUNKOVITO, Zemljište.KRECNJACKO)
            ),
            Biljka(
                naziv = "Lavanda (Lavandula angustifolia)",
                porodica = "Lamiaceae (metvice)",
                medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
                medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Jogurt sa voćem"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
                zemljisniTipovi = listOf(Zemljište.PJESKOVITO, Zemljište.KRECNJACKO)
            ),
/*1. moja*/ Biljka(
                naziv = "Kurkuma (Curcuma longa)",
                porodica = "Zingiberaceae (Đumbirovke)",
                medicinskoUpozorenje = "Osobe koje uzimaju lijekove za razrjeđivanje krvi trebaju izbjegavati konzumaciju kurkume u velikim količinama.",
                medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PODRSKAIMUNITETU),
                profilOkusa = ProfilOkusaBiljke.KORIJENASTO,
                jela = listOf("Curry", "Smoothie", "Čaj"),
                klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljište.ILOVACA)
            ),
/*2. moja*/ Biljka(
                naziv = "Limun (Citrus limon)",
                porodica = "Rutaceae (Rutovke)",
                medicinskoUpozorenje = "Limun može iritirati želudac kod osoba koje pate od gastroezofagealnog refluksa.",
                medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PODRSKAIMUNITETU),
                profilOkusa = ProfilOkusaBiljke.CITRUSNI,
                jela = listOf("Jogurt sa voćem", "Čaj"),
                klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljište.PJESKOVITO)
            ),
/*3. moja*/ Biljka(
                naziv = "Šafran (Crocus sativus)",
                porodica = "Iridaceae (Perunike)",
                medicinskoUpozorenje = "Limun može iritirati želudac kod osoba koje pate od gastroezofagealnog refluksa.",
                medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPROBAVE, MedicinskaKorist.REGULACIJAPRITISKA),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Rižoto", "Gulaš"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
                zemljisniTipovi = listOf(Zemljište.PJESKOVITO)
            ),
/*4. moja*/ Biljka(
                naziv = "Ruža (Rosa)",
                porodica = "Rosaceae (Ružičnjaci)",
                medicinskoUpozorenje = "Nema poznatih ozbiljnih nuspojava.",
                medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE, MedicinskaKorist.PROTUUPALNO),
                profilOkusa = ProfilOkusaBiljke.SLATKI,
                jela = listOf("Čaj", "Sirup", "Kolač"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
                zemljisniTipovi = listOf(Zemljište.PJESKOVITO)
            ),
/*5. moja*/ Biljka(
                naziv = "Crni kim (Nigella sativa)",
                porodica = "Ranunculaceae (Žabnjace)",
                medicinskoUpozorenje = " Osobe koje pate od krvarenja poremećaja ili koje uzimaju lijekove za razrjeđivanje krvi trebaju izbjegavati konzumaciju crnog kima.",
                medicinskeKoristi = listOf(MedicinskaKorist.PODRSKAIMUNITETU, MedicinskaKorist.REGULACIJAPROBAVE),
                profilOkusa = ProfilOkusaBiljke.GORKO,
                jela = listOf("Pecivo", "Začin"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
                zemljisniTipovi = listOf(Zemljište.CRNICA)
            )
        )
        return biljke

    }

}




