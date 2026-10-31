package com.example.data.islamic

import com.example.data.model.DuaItem
import com.example.data.model.IslamicEvent
import com.example.data.model.PredefinedDhikr
import com.example.data.model.Reciter

object IslamicContentData {

    val reciters: List<Reciter> = listOf(
        Reciter(
            id = "alafasy",
            name = "Mishary Rashid Alafasy",
            arabicName = "مشاري راشد العفاسي",
            style = "Murattal - Melodic & Clear",
            audioBaseUrl = "https://everyayah.com/data/Alafasy_128kbps/055%03d.mp3",
            fullSurahUrl = "https://download.quranicaudio.com/qdc/mishari_al_afasy/murattal/55.mp3"
        ),
        Reciter(
            id = "abdul_basit",
            name = "Abdul Basit Abdul Samad",
            arabicName = "عبد الباسط عبد الصمد",
            style = "Murattal - Classical Reverence",
            audioBaseUrl = "https://everyayah.com/data/Abdul_Basit_Murattal_192kbps/055%03d.mp3",
            fullSurahUrl = "https://download.quranicaudio.com/qdc/abdul_basit/murattal/55.mp3"
        ),
        Reciter(
            id = "ghamdi",
            name = "Saad Al-Ghamdi",
            arabicName = "سعد الغامدي",
            style = "Murattal - Smooth & Soothing",
            audioBaseUrl = "https://everyayah.com/data/Ghamadi_40kbps/055%03d.mp3",
            fullSurahUrl = "https://download.quranicaudio.com/qdc/saad_al_ghamdi/murattal/55.mp3"
        )
    )

    fun getReciter(id: String): Reciter {
        return reciters.find { it.id == id } ?: reciters.first()
    }

    val predefinedDhikrList: List<PredefinedDhikr> = listOf(
        PredefinedDhikr(
            id = "subhanallah",
            arabic = "سُبْحَانَ اللَّهِ",
            transliteration = "SubhanAllah",
            englishMeaning = "Glory be to Allah",
            urduMeaning = "اللہ پاک ہے تمام عیبوں سے",
            defaultTarget = 33,
            reward = "Planting a tree in Jannah and erasing 100 sins with 100 good deeds written."
        ),
        PredefinedDhikr(
            id = "alhamdulillah",
            arabic = "الْحَمْدُ لِلَّهِ",
            transliteration = "Alhamdulillah",
            englishMeaning = "All praise is due to Allah",
            urduMeaning = "تمام تعریفیں اللہ ہی کے لیے ہیں",
            defaultTarget = 33,
            reward = "Fills the Scale of good deeds on the Day of Judgment."
        ),
        PredefinedDhikr(
            id = "allahu_akbar",
            arabic = "اللَّهُ أَكْبَرُ",
            transliteration = "Allahu Akbar",
            englishMeaning = "Allah is the Greatest",
            urduMeaning = "اللہ سب سے بڑا ہے",
            defaultTarget = 34,
            reward = "Acknowledging Allah's absolute majesty above all worldly worries."
        ),
        PredefinedDhikr(
            id = "astaghfirullah",
            arabic = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
            transliteration = "Astaghfirullah wa atoobu ilayh",
            englishMeaning = "I seek Allah's forgiveness and repent to Him",
            urduMeaning = "میں اللہ سے بخشش مانگتا ہوں اور اسی کی طرف رجوع کرتا ہوں",
            defaultTarget = 100,
            reward = "Relief from distress, opening of sustained provision, and forgiveness."
        ),
        PredefinedDhikr(
            id = "la_ilaha_illallah",
            arabic = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ",
            transliteration = "Laa ilaaha illallahu wahdahu laa shareeka lah",
            englishMeaning = "There is no deity worthy of worship except Allah alone",
            urduMeaning = "اللہ کے سوا کوئی عبادت کے لائق نہیں، وہ اکیلا ہے اس کا کوئی شریک نہیں",
            defaultTarget = 100,
            reward = "The best dhikr and key to Paradise; equivalent to freeing 10 slaves."
        ),
        PredefinedDhikr(
            id = "durood_sharif",
            arabic = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ",
            transliteration = "Allahumma salli 'ala Muhammadin wa 'ala aali Muhammad",
            englishMeaning = "O Allah, send blessings upon Muhammad and upon the family of Muhammad",
            urduMeaning = "اے اللہ! درود بھیج محمد (ﷺ) پر اور ان کی آل پر",
            defaultTarget = 33,
            reward = "Whoever sends blessings once, Allah will send ten blessings upon him and erase ten sins."
        ),
        PredefinedDhikr(
            id = "hawqala",
            arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
            transliteration = "Laa hawla wa laa quwwata illaa billaahil-'Aliyyil-'Azeem",
            englishMeaning = "There is no power nor might except with Allah, the Most High, the Most Great",
            urduMeaning = "گناہوں سے بچنے کی طاقت اور نیکی کرنے کی قوت اللہ ہی کی توفیق سے ہے",
            defaultTarget = 33,
            reward = "A treasure from beneath the Throne of Ar-Rahman in Jannah."
        )
    )

    val authenticDuas: List<DuaItem> = listOf(
        DuaItem(
            id = "shukr_dua",
            category = "Gratitude & Favors (شکر)",
            title = "Dua for Gratitude of Allah's Favors",
            arabic = "رَبِّ أَوْزِعْنِي أَنْ أَشْكُرَ نِعْمَتَكَ الَّتِي أَنْعَمْتَ عَلَيَّ وَعَلَىٰ وَالِدَيَّ وَأَنْ أَعْمَلَ صَالِحًا تَرْضَاهُ",
            transliteration = "Rabbi awzi'nee an ashkura ni'matakal-lateee an'amta 'alayya wa 'alaa waalidayya wa an a'mala saalihan tardaahu",
            translationEnglish = "My Lord, enable me to be grateful for Your favor which You have bestowed upon me and upon my parents and to do righteousness of which You approve.",
            translationUrdu = "اے میرے رب! مجھے توفیق دے کہ میں تیری اس نعمت کا شکر ادا کروں جو تو نے مجھ پر اور میرے والدین پر انعام فرمائی اور ایسا نیک عمل کروں جس سے تو راضی ہو جائے۔",
            reference = "Surah An-Naml (27:19) & Surah Al-Ahqaf (46:15)"
        ),
        DuaItem(
            id = "forgiveness_dua",
            category = "Mercy & Forgiveness (مغفرت)",
            title = "Sayyidul Istighfar (Chief of Forgiveness)",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            transliteration = "Allahumma Anta Rabbee laa ilaaha illaa Anta, khalaqtanee wa ana 'abduka, wa ana 'alaa 'ahdika wa wa'dika mastata'tu, a'oozu bika min sharri maa sana'tu, aboo'u laka bini'matika 'alayya, wa aboo'u bizambee faghfir lee fa-innahoo laa yaghfiruz-zunooba illaa Anta",
            translationEnglish = "O Allah, You are my Lord; there is no deity worthy of worship except You. You created me, and I am Your servant, and I uphold Your covenant and promise as much as I am able. I seek refuge in You from the evil of what I have done. I acknowledge Your blessings upon me, and I acknowledge my sin, so forgive me, for indeed none forgives sins except You.",
            translationUrdu = "اے اللہ! تو میرا رب ہے، تیرے سوا کوئی معبود نہیں، تو نے مجھے پیدا کیا اور میں تیرا بندہ ہوں، اور میں اپنی استطاعت کے مطابق تیرے عہد اور وعدے پر قائم ہوں۔ میں اپنے کیے کے شر سے تیری پناہ مانگتا ہوں۔ میں اپنے اوپر تیری نعمتوں کا اعتراف کرتا ہوں اور اپنے گناہوں کا اعتراف کرتا ہوں، پس مجھے بخش دے کیونکہ تیرے سوا گناہوں کو کوئی نہیں بخش سکتا۔",
            reference = "Sahih al-Bukhari 6306"
        ),
        DuaItem(
            id = "peace_guidance",
            category = "Heart & Peace (طمانیت)",
            title = "Dua for Steadfastness & Guidance",
            arabic = "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَىٰ دِينِكَ",
            transliteration = "Yaa Muqallibal-quloob, thabbit qalbee 'alaa deenik",
            translationEnglish = "O Turner of the hearts, keep my heart firm upon Your religion.",
            translationUrdu = "اے دلوں کو پھیرنے والے! میرے دل کو اپنے دین پر ثابت قدم رکھ۔",
            reference = "Jami` at-Tirmidhi 2140"
        ),
        DuaItem(
            id = "morning_evening",
            category = "Protection (حفاظت)",
            title = "Dua for Protection from All Harm",
            arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            transliteration = "Bismillahil-lazee laa yadurru ma'as-mihee shay'un fil-ardi wa laa fis-samaaa'i wa Huwas-Samee'ul-'Aleem",
            translationEnglish = "In the name of Allah, with whose name nothing on earth or in the sky can cause harm, and He is the All-Hearing, the All-Knowing.",
            translationUrdu = "اللہ کے نام کے ساتھ جس کے نام کی برکت سے زمین اور آسمان میں کوئی چیز نقصان نہیں پہنچا سکتی، اور وہی سب کچھ سننے والا اور جاننے والا ہے۔",
            reference = "Sunan Abi Dawud 5088"
        )
    )

    val islamicEvents: List<IslamicEvent> = listOf(
        IslamicEvent("1 Muharram", "Islamic Hijri New Year", "رأس السنة الهجرية", "Commemorating the Prophet's (ﷺ) migration from Makkah to Madinah.", false),
        IslamicEvent("10 Muharram", "Day of Ashura", "يوم عاشوراء", "Commemorating the deliverance of Prophet Musa (AS) and the children of Israel.", false),
        IslamicEvent("12 Rabi' al-Awwal", "Mawlid an-Nabi", "مولد النبي ﷺ", "Birth of the final messenger of Allah, Muhammad (ﷺ).", false),
        IslamicEvent("27 Rajab", "Isra and Mi'raj", "الإسراء والمعراج", "The miraculous Night Journey and Ascension to heaven where the five daily prayers were ordained.", false),
        IslamicEvent("15 Sha'ban", "Shab-e-Barat", "ليلة النصف من شعبان", "Night of seeking forgiveness, blessings, and divine decree.", false),
        IslamicEvent("1 Ramadan", "First Day of Blessed Ramadan", "أول شهر رمضان المبارك", "The month of fasting, Qur'anic revelation, and intense worship.", true),
        IslamicEvent("27 Ramadan", "Laylat al-Qadr (Night of Decree)", "ليلة القدر المباركة", "A night better than a thousand months in worship and reward.", true),
        IslamicEvent("1 Shawwal", "Eid ul-Fitr", "عيد الفطر المبارك", "Celebration marking the conclusion of the holy month of Ramadan.", true),
        IslamicEvent("9 Dhul-Hijjah", "Day of Arafah", "يوم عرفة", "The pinnacle day of Hajj pilgrimmage and supreme day of forgiveness.", false),
        IslamicEvent("10 Dhul-Hijjah", "Eid ul-Adha", "عيد الأضحى المبارك", "Feast of Sacrifice honoring Prophet Ibrahim's (AS) dedication to Allah.", false)
    )

    val dailyReminders: List<String> = listOf(
        "\"And He found you lost and guided you.\" (93:7) — Reflect upon the supreme favors in Surah Ar-Rahman today.",
        "The Prophet (ﷺ) said: 'Everything has an adornment, and the adornment of the Qur'an is Surah Ar-Rahman.' (Bayhaqi)",
        "Gratitude turns what we have into more: \"If you are grateful, I will surely increase you [in favor].\" (Surah Ibrahim 14:7)",
        "Count your blessings before your burdens. Which of the endless favors of your Lord will you deny?",
        "\"Is the reward for good [anything] but good?\" (Surah Ar-Rahman 55:60) — Treat everyone with mercy today.",
        "Peace of heart comes through the remembrance of Allah: \"Unquestionably, by the remembrance of Allah hearts are assured.\" (13:28)",
        "Every single breath, heartbeat, and sunrise is an undeserved gift from Ar-Rahman. Say Alhamdulillah."
    )
}
