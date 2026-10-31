package com.example.data.quran

import com.example.data.model.Verse
import com.example.data.model.WordMeaning

val versesPart1: List<Verse> = listOf(
    Verse(
        verseNumber = 1,
        arabicText = "الرَّحْمَٰنُ ۝١",
        englishTranslation = "The Most Merciful",
        urduTranslation = "نہایت مہربان (اللہ)",
        sindhiTranslation = "وڏو ٻاجھارو (الله)",
        transliteration = "Ar-Rahmaan",
        keyWords = listOf(
            WordMeaning("الرَّحْمَٰنُ", "Ar-Rahmaan", "The Most Merciful", "نہایت مہربان")
        )
    ),
    Verse(
        verseNumber = 2,
        arabicText = "عَلَّمَ الْقُرْآنَ ۝٢",
        englishTranslation = "Taught the Qur'an,",
        urduTranslation = "اسی نے قرآن کی تعلیم دی،",
        sindhiTranslation = "جنھن قرآن سيکاريو،",
        transliteration = "'Allamal-Qur'aan",
        keyWords = listOf(
            WordMeaning("عَلَّمَ", "'Allama", "Taught", "سکھایا"),
            WordMeaning("الْقُرْآنَ", "Al-Qur'aan", "The Qur'an", "قرآن")
        )
    ),
    Verse(
        verseNumber = 3,
        arabicText = "خَلَقَ الْإِنسَانَ ۝٣",
        englishTranslation = "Created man,",
        urduTranslation = "انسان کو پیدا کیا،",
        sindhiTranslation = "انسان کي بڻايائين،",
        transliteration = "Khalaqal-insaan",
        keyWords = listOf(
            WordMeaning("خَلَقَ", "Khalaqa", "Created", "پیدا کیا"),
            WordMeaning("الْإِنسَانَ", "Al-Insaan", "Man", "انسان")
        )
    ),
    Verse(
        verseNumber = 4,
        arabicText = "عَلَّمَهُ الْبَيَانَ ۝٤",
        englishTranslation = "[And] taught him speech.",
        urduTranslation = "اسے بولنا سکھایا۔",
        sindhiTranslation = "کيس ڳالھائڻ سيکاريائين.",
        transliteration = "'Allamahul-bayaan",
        keyWords = listOf(
            WordMeaning("عَلَّمَهُ", "'Allamahu", "Taught him", "اسے سکھایا"),
            WordMeaning("الْبَيَانَ", "Al-Bayaan", "Speech / Expression", "بیان / بولنا")
        )
    ),
    Verse(
        verseNumber = 5,
        arabicText = "الشَّمْسُ وَالْقَمَرُ بِحُسْبَانٍ ۝٥",
        englishTranslation = "The sun and the moon [move] by precise calculation,",
        urduTranslation = "سورج اور چاند ایک حساب مقرر سے چل رہے ہیں،",
        sindhiTranslation = "سج ۽ چنڊ ھڪ حساب سان آھن،",
        transliteration = "Ash-shamsu wal-qamaru bihusbaan",
        keyWords = listOf(
            WordMeaning("الشَّمْسُ", "Ash-Shams", "The sun", "سورج"),
            WordMeaning("وَالْقَمَرُ", "Wal-Qamar", "And the moon", "اور چاند"),
            WordMeaning("بِحُسْبَانٍ", "Bihusbaan", "By precise calculation", "حساب سے")
        )
    ),
    Verse(
        verseNumber = 6,
        arabicText = "وَالنَّجْمُ وَالشَّجَرُ يَسْجُدَانِ ۝٦",
        englishTranslation = "And the stars and trees prostrate,",
        urduTranslation = "اور ستارے اور درخت دونوں سجدہ کر رہے ہیں،",
        sindhiTranslation = "۽ ول ۽ وڻ سجدو ڪن ٿا،",
        transliteration = "Wan-najmu wash-shajaru yasjudaan",
        keyWords = listOf(
            WordMeaning("النَّجْمُ", "An-Najm", "The stars / herbs", "ستارے"),
            WordMeaning("الشَّجَرُ", "Ash-Shajar", "The trees", "درخت"),
            WordMeaning("يَسْجُدَانِ", "Yasjudaan", "Both prostrate", "دونوں سجدہ کرتے ہیں")
        )
    ),
    Verse(
        verseNumber = 7,
        arabicText = "وَالسَّمَاءَ رَفَعَهَا وَوَضَعَ الْمِيزَانَ ۝٧",
        englishTranslation = "And the heaven He raised and imposed the balance,",
        urduTranslation = "اور اسی نے آسمان کو بلند کیا اور ترازو (عدل و انصاف) قائم کیا،",
        sindhiTranslation = "۽ آسمان کي کيس مٿاھون ڪيائين ۽ ترازو رکيائين،",
        transliteration = "Was-samaaa'a rafa'ahaa wa wada'al-meezaan",
        keyWords = listOf(
            WordMeaning("السَّمَاءَ", "As-Samaaa'", "The heaven", "آسمان"),
            WordMeaning("رَفَعَهَا", "Rafa'ahaa", "He raised it", "اس کو بلند کیا"),
            WordMeaning("الْمِيزَانَ", "Al-Meezaan", "The balance / justice", "میزان / ترازو")
        )
    ),
    Verse(
        verseNumber = 8,
        arabicText = "أَلَّا تَطْغَوْا فِي الْمِيزَانِ ۝٨",
        englishTranslation = "That you not transgress within the balance.",
        urduTranslation = "کہ تولنے میں حد سے تجاوز نہ کرو۔",
        sindhiTranslation = "ته متان ترازوءَ ۾ زيادتي ڪريو.",
        transliteration = "Allaa tatghaw fil-meezaan"
    ),
    Verse(
        verseNumber = 9,
        arabicText = "وَأَقِيمُوا الْوَزْنَ بِالْقِسْطِ وَلَا تُخْسِرُوا الْمِيزَانَ ۝٩",
        englishTranslation = "And establish weight in justice and do not make deficient the balance.",
        urduTranslation = "اور انصاف کے ساتھ تول ٹھیک رکھو اور ترازو میں کم نہ تولو۔",
        sindhiTranslation = "۽ انصاف سان تور کي برپا ڪريو ۽ تور کي نه گھٽايو.",
        transliteration = "Wa aqeemul-wazna bilqisti wa laa tukhsirul-meezaan"
    ),
    Verse(
        verseNumber = 10,
        arabicText = "وَالْأَرْضَ وَضَعَهَا لِلْأَنَامِ ۝١٠",
        englishTranslation = "And the earth He laid [out] for the creatures.",
        urduTranslation = "اور اسی نے زمین کو مخلوقات کے لیے بچھایا۔",
        sindhiTranslation = "۽ زمين کي مخلوق لاءِ وڇايائين.",
        transliteration = "Wal-arda wada'ahaa lil-anaam"
    ),
    Verse(
        verseNumber = 11,
        arabicText = "فِيهَا فَاكِهَةٌ وَالنَّخْلُ ذَاتُ الْأَكْمَامِ ۝١١",
        englishTranslation = "Therein is fruit and palm trees having sheaths [of dates],",
        urduTranslation = "اس میں میوے اور کھجور کے درخت ہیں جن پر غلاف چڑھے ہوتے ہیں،",
        sindhiTranslation = "ان ۾ ميوا ۽ ڳڀ وارين کجين جا وڻ آھن،",
        transliteration = "Feehaa faakihatuñw-wan-nakhlu zaatul-akmaam"
    ),
    Verse(
        verseNumber = 12,
        arabicText = "وَالْحَبُّ ذُو الْعَصْفِ وَالرَّيْحَانُ ۝١٢",
        englishTranslation = "And grain having husks and scented plants.",
        urduTranslation = "اور بھوسے والا اناج اور خوشبودار پھول۔",
        sindhiTranslation = "۽ بُھه وارو اَن ۽ خوشبودار ٻوٽا.",
        transliteration = "Wal-habbu zul-'asfi war-rayhaan"
    ),
    Verse(
        verseNumber = 13,
        arabicText = "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ ۝١٣",
        englishTranslation = "So which of the favors of your Lord would you deny?",
        urduTranslation = "پس (اے جن و انس!) تم اپنے پروردگار کی کون کون سی نعمت کو جھٹلاؤ گے؟",
        sindhiTranslation = "پوءِ (اي جنؤ ۽ انسانؤ!) پنھنجي پالڻھار جي ڪھڙين ڪھڙين نعمتن کي ڪوڙو ڀائيندا؟",
        transliteration = "Fabi-ayyi aalaaa'i Rabbikumaa tukazzibaan",
        isRefrain = true
    ),
    Verse(
        verseNumber = 14,
        arabicText = "خَلَقَ الْإِنسَانَ مِن صَلْصَالٍ كَالْفَخَّارِ ۝١٤",
        englishTranslation = "He created man from clay like [that of] pottery.",
        urduTranslation = "اس نے انسان کو ٹھیکرے جیسی کھنکھناتی مٹی سے پیدا کیا۔",
        sindhiTranslation = "انسان کي ٺڪر جھڙي سُڪل مٽيءَ مان بڻايائين.",
        transliteration = "Khalaqal-insaana min salsaalin kal-fakh-khaar"
    ),
    Verse(
        verseNumber = 15,
        arabicText = "وَخَلَقَ الْجَانَّ مِن مَّارِجٍ مِّن نَّارٍ ۝١٥",
        englishTranslation = "And He created the jinn from a smokeless flame of fire.",
        urduTranslation = "اور جنات کو آگ کے شعلے سے پیدا کیا۔",
        sindhiTranslation = "۽ جنن کي باھ جي کُليل شعلي مان پيدا ڪيائين.",
        transliteration = "Wa khalaqal-jaaan-na mim-maarijim-min-naar"
    ),
    Verse(
        verseNumber = 16,
        arabicText = "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ ۝١٦",
        englishTranslation = "So which of the favors of your Lord would you deny?",
        urduTranslation = "پس تم اپنے پروردگار کی کون کون سی نعمت کو جھٹلاؤ گے؟",
        sindhiTranslation = "پوءِ پنھنجي پالڻھار جي ڪھڙين ڪھڙين نعمتن کي ڪوڙو ڀائيندا؟",
        transliteration = "Fabi-ayyi aalaaa'i Rabbikumaa tukazzibaan",
        isRefrain = true
    ),
    Verse(
        verseNumber = 17,
        arabicText = "رَبُّ الْمَشْرِقَيْنِ وَرَبُّ الْمَغْرِبَيْنِ ۝١٧",
        englishTranslation = "[He is] Lord of the two sunrises and Lord of the two sunsets.",
        urduTranslation = "وہ دونوں مشرقوں اور دونوں مغربوں کا رب ہے۔",
        sindhiTranslation = "ٻنھي اوڀرن جو پالڻھار ۽ ٻنھي اولھن جو پالڻھار آھي.",
        transliteration = "Rabbul-mashriqayni wa Rabbul-maghribayn"
    ),
    Verse(
        verseNumber = 18,
        arabicText = "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ ۝١٨",
        englishTranslation = "So which of the favors of your Lord would you deny?",
        urduTranslation = "پس تم اپنے پروردگار کی کون کون سی نعمت کو جھٹلاؤ گے؟",
        sindhiTranslation = "پوءِ پنھنجي پالڻھار جي ڪھڙين ڪھڙين نعمتن کي ڪوڙو ڀائيندا؟",
        transliteration = "Fabi-ayyi aalaaa'i Rabbikumaa tukazzibaan",
        isRefrain = true
    ),
    Verse(
        verseNumber = 19,
        arabicText = "مَرَجَ الْبَحْرَيْنِ يَلْتَقِيَانِ ۝١٩",
        englishTranslation = "He released the two seas, meeting [side by side];",
        urduTranslation = "اسی نے دو دریا رواں کیے جو آپس میں ملتے ہیں؛",
        sindhiTranslation = "ٻنھي سمنڊن کي ڇوڙيائين جو پاڻ ۾ ملن ٿا؛",
        transliteration = "Marajal-bahrayni yaltaqiyaan"
    ),
    Verse(
        verseNumber = 20,
        arabicText = "بَيْنَهُمَا بَرْزَخٌ لَّا يَبْغِيَانِ ۝٢٠",
        englishTranslation = "Between them is a barrier which neither of them can transgress.",
        urduTranslation = "ان کے درمیان ایک پردہ حائل ہے جس سے وہ حد سے نہیں بڑھتے۔",
        sindhiTranslation = "ان ٻنھي جي وچ ۾ ھڪ پردو آھي جو ھڪ ٻئي تي وڌي نه ٿا سگھن.",
        transliteration = "Baynahumaa barzakhul-laa yabghiyaan"
    ),
    Verse(
        verseNumber = 21,
        arabicText = "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ ۝٢١",
        englishTranslation = "So which of the favors of your Lord would you deny?",
        urduTranslation = "پس تم اپنے پروردگار کی کون کون سی نعمت کو جھٹلاؤ گے؟",
        sindhiTranslation = "پوءِ پنھنجي پالڻھار جي ڪھڙين ڪھڙين نعمتن کي ڪوڙو ڀائيندا؟",
        transliteration = "Fabi-ayyi aalaaa'i Rabbikumaa tukazzibaan",
        isRefrain = true
    ),
    Verse(
        verseNumber = 22,
        arabicText = "يَخْرُجُ مِنْهُمَا اللُّؤْلُؤُ وَالْمَرْجَانُ ۝٢٢",
        englishTranslation = "From both of them emerge pearl and coral.",
        urduTranslation = "ان دونوں میں سے موتی اور مرجان نکلتے ہیں۔",
        sindhiTranslation = "انھن ٻنھي مان موتي ۽ مرجان نڪرن ٿا.",
        transliteration = "Yakhruju minhumar-lu'lu'u wal-marjaan"
    ),
    Verse(
        verseNumber = 23,
        arabicText = "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ ۝٢٣",
        englishTranslation = "So which of the favors of your Lord would you deny?",
        urduTranslation = "پس تم اپنے پروردگار کی کون کون سی نعمت کو جھٹلاؤ گے؟",
        sindhiTranslation = "پوءِ پنھنجي پالڻھار جي ڪھڙين ڪھڙين نعمتن کي ڪوڙو ڀائيندا؟",
        transliteration = "Fabi-ayyi aalaaa'i Rabbikumaa tukazzibaan",
        isRefrain = true
    ),
    Verse(
        verseNumber = 24,
        arabicText = "وَلَهُ الْجَوَارِ الْمُنشَآتُ فِي الْبَحْرِ كَالْأَعْلَامِ ۝٢٤",
        englishTranslation = "And to Him belong the ships with sails raised high in the sea like mountains.",
        urduTranslation = "اور اسی کے ہیں جہاز جو سمندر میں پہاڑوں کی طرح اونچے چلتے ہیں۔",
        sindhiTranslation = "۽ سمنڊ ۾ جبلن جھڙا اڀا جھاز به ان جا ئي آھن.",
        transliteration = "Wa lahul-jawaaril-munsha'aatu fil-bahri kal-a'laam"
    ),
    Verse(
        verseNumber = 25,
        arabicText = "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ ۝٢٥",
        englishTranslation = "So which of the favors of your Lord would you deny?",
        urduTranslation = "پس تم اپنے پروردگار کی کون کون سی نعمت کو جھٹلاؤ گے؟",
        sindhiTranslation = "پوءِ پنھنجي پالڻھار جي ڪھڙين ڪھڙين نعمتن کي ڪوڙو ڀائيندا؟",
        transliteration = "Fabi-ayyi aalaaa'i Rabbikumaa tukazzibaan",
        isRefrain = true
    ),
    Verse(
        verseNumber = 26,
        arabicText = "كُلُّ مَنْ عَلَيْهَا فَانٍ ۝٢٦",
        englishTranslation = "Everyone upon the earth will perish,",
        urduTranslation = "جو کوئی زمین پر ہے فنا ہونے والا ہے،",
        sindhiTranslation = "زمين تي جيڪي به آھن سي سڀ ناس ٿيڻ وارا آھن،",
        transliteration = "Kullu man 'alayhaa faan"
    )
)
