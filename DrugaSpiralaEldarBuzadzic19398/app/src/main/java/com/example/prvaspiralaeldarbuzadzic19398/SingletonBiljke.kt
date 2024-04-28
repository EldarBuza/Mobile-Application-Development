package com.example.prvaspiralaeldarbuzadzic19398

object SingletonBiljka {
    var biljkeLista: ArrayList<Biljka> = getBiljkePreset()

    private fun getBiljkePreset(): ArrayList<Biljka> {
        var biljke = listOf(
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
        return ArrayList<Biljka>(biljke)

    }
}