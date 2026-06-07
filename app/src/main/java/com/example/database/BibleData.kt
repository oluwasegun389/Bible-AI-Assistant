package com.example.database

data class BibleVerse(
    val number: Int,
    val kjvText: String,
    val webText: String,
    val ampText: String
)

data class BibleChapter(
    val chapterNumber: Int,
    val verses: List<BibleVerse>
)

data class BibleBook(
    val name: String,
    val description: String,
    val chapters: List<BibleChapter>
)

data class Hymn(
    val id: Int,
    val title: String,
    val englishTitle: String,
    val category: String, // "Worship", "Praise", "Thanksgiving", "Faith", "Prayer", "Christmas", "Easter", "Communion"
    val lyricsEnglish: String,
    val lyricsYoruba: String,
    val lyricsIgbo: String,
    val lyricsHausa: String,
    val hymnNumber: Int
)

object BibleData {

    private val rawBooks = listOf(
        BibleBook(
            name = "Genesis",
            description = "The Book of Beginnings, detailing creation, cosmic origins, patriarchs, and God's sovereign covenant.",
            chapters = listOf(
                BibleChapter(
                    chapterNumber = 1,
                    verses = listOf(
                        BibleVerse(1, "In the beginning God created the heaven and the earth.", "In the beginning, God created the heavens and the earth.", "In the beginning God (prepared, formed, fashioned, and) created the heavens and the earth."),
                        BibleVerse(2, "And the earth was without form, and void; and darkness was upon the face of the deep. And the Spirit of God moved upon the face of the waters.", "The earth was without form, and void. Darkness was on the face of the deep; and the Spirit of God was hovering over the face of the waters.", "The earth was without form and an empty waste, and darkness was upon the face of the very great deep. The Spirit of God was moving (hovering, brooding) over the face of the waters."),
                        BibleVerse(3, "And God said, Let there be light: and there was light.", "God said, \"Let there be light,\" and there was light.", "And God said, \"Let there be light\"; and there was light."),
                        BibleVerse(4, "And God saw the light, that it was good: and God divided the light from the darkness.", "God saw the light, and saw that it was good. God divided the light from the darkness.", "And God saw that the light was good (suitable, pleasant) and He approved it; and God separated the light from the darkness."),
                        BibleVerse(5, "And God called the light Day, and the darkness he called Night. And the evening and the morning were the first day.", "God called the light \"day\", and the darkness he called \"night\". There was evening and there was morning, one day.", "And God called the light Day, and the darkness He called Night. And there was evening and there was morning, one day.")
                    )
                ),
                BibleChapter(
                    chapterNumber = 12,
                    verses = listOf(
                        BibleVerse(1, "Now the LORD had said unto Abram, Get thee out of thy country, and from thy kindred, and from thy father's house, unto a land that I will shew thee:", "Now Yahweh said to Abram, \"Get out of your country, and from your relatives, and from your father's house, to the land that I will show you.", "Now the LORD said to Abram, \"Go from your country, and from your relatives and from your father's house, to the land which I will show you;"),
                        BibleVerse(2, "And I will make of thee a great nation, and I will bless thee, and make thy name great; and thou shalt be a blessing:", "I will make of you a great nation. I will bless you, and make your name great. You will be a blessing.", "And I will make you a great nation, and I will bless you; and make your name great; and you shall be a blessing."),
                        BibleVerse(3, "And I will bless them that bless thee, and curse him that curseth thee: and in thee shall all families of the earth be blessed.", "I will bless those who bless you, and I will curse him who curses you. In you will all the families of the earth be blessed.\"", "And I will bless those who bless you, and the one who curses you I will curse. And in you all the families of the earth will be blessed.\"")
                    )
                )
            )
        ),
        BibleBook(
            name = "Exodus",
            description = "The narrative of Israel’s miraculous deliverance from Egypt, covenant at Sinai, and God's dwelling place.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now these are the names of the children of Israel, which came into Egypt...", "Now these are the names of the sons of Israel, who came into Egypt...", "Now these are the names of the sons of Israel who came into Egypt..."))))
        ),
        BibleBook(
            name = "Leviticus",
            description = "God's manual for sanctuary worship, sacrificial offerings, high priesthood, and moral/ritual holiness.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "And the LORD called unto Moses, and spake unto him out of the tabernacle...", "Yahweh called to Moses, and spoke to him from the Tent of Meeting...", "Then the LORD called to Moses and spoke to him from the Tent of Meeting..."))))
        ),
        BibleBook(
            name = "Numbers",
            description = "The wilderness wanderings of the tribes of Israel, highlighting census lists and lessons on faith.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "And the LORD spake unto Moses in the wilderness of Sinai...", "Yahweh spoke to Moses in the wilderness of Sinai...", "The LORD spoke to Moses in the wilderness of Sinai..."))))
        ),
        BibleBook(
            name = "Deuteronomy",
            description = "A series of farewell addresses by Moses re-stating the Law and calling for absolute loyalty to Yahweh.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "These be the words which Moses spake unto all Israel on this side Jordan...", "These are the words which Moses spoke to all Israel beyond the Jordan...", "These are the words which Moses spoke to all Israel on this side of the Jordan..."))))
        ),
        BibleBook(
            name = "Joshua",
            description = "The military campaign and tribal division of the Promised Land under Joshua's faithful leadership.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now after the death of Moses the servant of the LORD it came to pass...", "Now it happened after the death of Moses the servant of Yahweh...", "Now it came to pass after the death of Moses the servant of the LORD..."))))
        ),
        BibleBook(
            name = "Judges",
            description = "The cycle of rebellion, oppression, and deliverance by charismatic leaders called judges.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now after the death of Joshua it came to pass...", "Now it happened after the death of Joshua...", "Now it came to pass after the death of Joshua..."))))
        ),
        BibleBook(
            name = "Ruth",
            description = "A heartwarming story of devotion, redemption, and the lineage of King David.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now it came to pass in the days when the judges ruled, there was a famine...", "Now it happened in the days when the judges judged, that there was a famine...", "Now it came to pass in the days when the judges ruled, that there was a famine..."))))
        ),
        BibleBook(
            name = "1 Samuel",
            description = "The transition of Israel from a league of tribes to a monarchy, introducing Samuel, Saul, and David.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now there was a certain man of Ramathaimzophim, of mount Ephraim...", "Now there was a certain man of Ramathaimzophim, of the hill country...", "Now there was a certain man of Ramathaim-zophim, of the hill country..."))))
        ),
        BibleBook(
            name = "2 Samuel",
            description = "The dramatic reign of King David, emphasizing his territorial triumphs and moral triumphs/trials.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now it came to pass after the death of Saul...", "Now it happened after the death of Saul...", "Now it came to pass after the death of Saul..."))))
        ),
        BibleBook(
            name = "1 Kings",
            description = "The glorious reign of Solomon, construction of the Temple, and subsequent division into two separate nations.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now king David was old and stricken in years...", "Now king David was old and advanced in years...", "Now King David was old, advanced in years..."))))
        ),
        BibleBook(
            name = "2 Kings",
            description = "The history of the divided kingdoms leading up to their fall, featuring the prophets Elijah and Elisha.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Then Moab rebelled against Israel after the death of Ahab.", "Moab rebelled against Israel after the death of Ahab.", "Moab rebelled against Israel after the death of Ahab."))))
        ),
        BibleBook(
            name = "1 Chronicles",
            description = "A genealogical and spiritual review of Israel's history, focusing heavily on Davidic worship setup.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Adam, Sheth, Enosh,", "Adam, Seth, Enosh,", "Adam, Seth, Enosh,"))))
        ),
        BibleBook(
            name = "2 Chronicles",
            description = "The history of the Southern Kingdom of Judah, focusing on the religious reforms of righteous kings.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "And Solomon the son of David was strengthened in his kingdom...", "Solomon the son of David was strengthened in his kingdom...", "Solomon the son of David was strengthened in his kingdom..."))))
        ),
        BibleBook(
            name = "Ezra",
            description = "The return of the Jewish exiles from Babylon and the rebuilding of the second Temple in Jerusalem.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now in the first year of Cyrus king of Persia...", "Now in the first year of Cyrus king of Persia...", "Now in the first year of Cyrus king of Persia..."))))
        ),
        BibleBook(
            name = "Nehemiah",
            description = "The historic reconstruction of Jerusalem's ruined walls under Nehemiah's administrative wisdom.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The words of Nehemiah the son of Hachaliah...", "The words of Nehemiah the son of Hacaliah...", "The words of Nehemiah the son of Hacaliah..."))))
        ),
        BibleBook(
            name = "Esther",
            description = "The silent yet powerful providence of God in saving the Jewish nation from genocide.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now it came to pass in the days of Ahasuerus...", "Now it happened in the days of Ahasuerus...", "Now it came to pass in the days of Ahasuerus..."))))
        ),
        BibleBook(
            name = "Job",
            description = "A deep theological drama examining human suffering, cosmic justice, and sovereign Divine wisdom.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "There was a man in the land of Uz, whose name was Job...", "There was a man in the land of Uz, whose name was Job...", "There was a man in the land of Uz whose name was Job..."))))
        ),
        BibleBook(
            name = "Psalms",
            description = "A collection of sacred songs, prayers, and poetry of praised adoration, lament, and messianic hope.",
            chapters = listOf(
                BibleChapter(
                    chapterNumber = 23,
                    verses = listOf(
                        BibleVerse(1, "The LORD is my shepherd; I shall not want.", "Yahweh is my shepherd: I shall lack nothing.", "The LORD is my Shepherd [to guide, protect, and provide]; I shall not want."),
                        BibleVerse(2, "He maketh me to lie down in green pastures: he leadeth me beside the still waters.", "He makes me lie down in green pastures. He leads me beside still waters.", "He makes me lie down in green pastures; He leads me beside still, quiet waters."),
                        BibleVerse(3, "He restoreth my soul: he leadeth me in the paths of righteousness for his name's sake.", "He restores my soul. He guides me in the paths of righteousness for his name's sake.", "He refreshes and restores my soul (life); He leads me in the paths of righteousness for His name's sake."),
                        BibleVerse(4, "Yea, though I walk through the valley of the shadow of death, I will fear no evil: for thou art with me; thy rod and thy staff they comfort me.", "Even though I walk through the valley of the shadow of death, I will fear no evil, for you are with me. Your rod and your staff, they comfort me.", "Even though I walk through the [deepest, darkest] valley of the shadow of death, I will fear no evil, for You are with me; Your rod and Your staff, they comfort me."),
                        BibleVerse(5, "Thou preparest a table before me in the presence of mine enemies: thou anointest my head with oil; my cup runneth over.", "You prepare a table before me in the presence of my enemies. You anoint my head with oil. My cup runs over.", "You prepare a table before me in the presence of my enemies. You have anointed my head with oil; my cup runs over, overflowing."),
                        BibleVerse(6, "Surely goodness and mercy shall follow me all the days of my life: and I will dwell in the house of the LORD for ever.", "Surely goodness and loving kindness shall follow me all the days of my life, and I will dwell in Yahweh's house forever.", "Surely goodness and mercy and unfailing love shall follow me all the days of my life, and I will dwell in the house of the LORD forever.")
                    )
                ),
                BibleChapter(
                    chapterNumber = 91,
                    verses = listOf(
                        BibleVerse(1, "He that dwelleth in the secret place of the most High shall abide under the shadow of the Almighty.", "He who dwells in the secret place of the Most High will rest in the shadow of the Almighty.", "He who dwells in the shelter of the Most High will remain secure and rest in the shadow of the Almighty [whose power no enemy can withstand]."),
                        BibleVerse(2, "I will say of the LORD, He is my refuge and my fortress: my God; in him will I trust.", "I will say of Yahweh, \"He is my refuge and my fortress, my God, in whom I trust.\"", "I will say of the LORD, \"He is my refuge and my fortress, my God, in whom I trust.\""),
                        BibleVerse(11, "For he shall give his angels charge over thee, to keep thee in all thy ways.", "For he will put his angels in charge of you, to guard you in all your ways.", "For He will command His angels concerning you, to guard and protect you in all your ways.")
                    )
                )
            )
        ),
        BibleBook(
            name = "Proverbs",
            description = "A treasure trove of celestial wisdom, offering guidance for practical godly living.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The proverbs of Solomon the son of David, king of Israel;", "The proverbs of Solomon, the son of David, king of Israel:", "The proverbs (truths, axioms) of Solomon, the son of David, king of Israel:"))))
        ),
        BibleBook(
            name = "Ecclesiastes",
            description = "A philosophical reflection on the vanity of life under the sun apart from keeping God's command.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The words of the Preacher, the son of David, king in Jerusalem.", "The words of the Preacher, the son of David, king in Jerusalem.", "The words of the Preacher, the son of David, king in Jerusalem."))))
        ),
        BibleBook(
            name = "Song of Solomon",
            description = "The beautiful allegorical song celebrating romantic love and covenant communion.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The song of songs, which is Solomon's.", "The Song of Songs, which is Solomon’s.", "The Song of Songs, which is Solomon’s."))))
        ),
        BibleBook(
            name = "Isaiah",
            description = "A majestic messianic prophecy condemning national sin and foretelling the suffering Servant.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The vision of Isaiah the son of Amoz, which he saw concerning Judah...", "The vision of Isaiah the son of Amoz, what he saw concerning Judah...", "The vision of Isaiah the son of Amoz, which he saw concerning Judah..."))))
        ),
        BibleBook(
            name = "Jeremiah",
            description = "The prophetic warnings of the weeping prophet to backslidden Judah and the promise of a New Covenant.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The words of Jeremiah the son of Hilkiah...", "The words of Jeremiah the son of Hilkiah...", "The words of Jeremiah the son of Hilkiah..."))))
        ),
        BibleBook(
            name = "Lamentations",
            description = "A collection of five poetic dirges mourning the desolation of Jerusalem after Babylonian sack.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "How doth the city sit solitary, that was full of people...", "How the city sits solitary, that was full of people...", "How lonely the city sits that was full of people..."))))
        ),
        BibleBook(
            name = "Ezekiel",
            description = "Glorious apocalyptic visions, temple symbols, and cosmic reminders of God's sovereign holiness.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now it came to pass in the thirtieth year, in the fourth month...", "Now it happened in the thirtieth year, in the fourth month...", "Now it came to pass in the thirtieth year, in the fourth month..."))))
        ),
        BibleBook(
            name = "Daniel",
            description = "Stories of courage and apocalyptic prophecies highlighting that God rules in the kingdoms of men.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "In the third year of the reign of Jehoiakim king of Judah...", "In the third year of the reign of Jehoiakim king of Judah...", "In the third year of the reign of Jehoiakim king of Judah..."))))
        ),
        BibleBook(
            name = "Hosea",
            description = "An allegorical prophetic picture of God's unyielding, wounded, yet redeeming love for backslidden Israel.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The word of the LORD that came unto Hosea, the son of Beeri...", "Yahweh’s word that came to Hosea the son of Beeri...", "The word of the LORD which came to Hosea the son of Beeri..."))))
        ),
        BibleBook(
            name = "Joel",
            description = "A prophetic vision warning of the Day of the Lord and the promise of the outpouring of the Holy Spirit.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The word of the LORD that came to Joel the son of Pethuel.", "Yahweh’s word that came to Joel the son of Pethuel.", "The word of the LORD which came to Joel the son of Pethuel."))))
        ),
        BibleBook(
            name = "Amos",
            description = "A passionate prophetic demand for God's justice to roll down like waters, condemning social oppression.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The words of Amos, who was among the herdmen of Tekoa...", "The words of Amos, who was among the herdsmen of Tekoa...", "The words of Amos, who was among the shepherds and sheepbreeders of Tekoa..."))))
        ),
        BibleBook(
            name = "Obadiah",
            description = "A short prophetic pronouncement of judgment against Edom for aiding the plunder of Jerusalem.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The vision of Obadiah. Thus saith the Lord GOD concerning Edom...", "The vision of Obadiah. This is what the Lord Yahweh says about Edom...", "The vision of Obadiah. Thus says the Lord God concerning Edom..."))))
        ),
        BibleBook(
            name = "Jonah",
            description = "The reluctant prophet’s flight from God, eventual preaching to Nineveh, and lesson on radical divine grace.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Now the word of the LORD came unto Jonah the son of Amittai...", "Now Yahweh’s word came to Jonah the son of Amittai...", "Now the word of the LORD came to Jonah the son of Amittai..."))))
        ),
        BibleBook(
            name = "Micah",
            description = "A balanced summary of justice, mercy, and humility in expectations of the coming Messianic Ruler.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The word of the LORD that came to Micah the Morasthite...", "Yahweh’s word that came to Micah the Morashtite...", "The word of the LORD which came to Micah the Morasthite..."))))
        ),
        BibleBook(
            name = "Nahum",
            description = "A prophetic vision foretelling the absolute fall and destruction of Assyria's capital Nineveh.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The burden of Nineveh. The book of the vision of Nahum...", "The oracle about Nineveh. The book of the vision of Nahum...", "The burden (mournful prophecy) of Nineveh. The book of the vision of Nahum..."))))
        ),
        BibleBook(
            name = "Habakkuk",
            description = "The dialogical struggle of a prophet complaining to God, concluding with a triumphant song of faith.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The burden which Habakkuk the prophet did see.", "The oracle which Habakkuk the prophet saw.", "The burden (mournful prophecy) which Habakkuk the prophet did see."))))
        ),
        BibleBook(
            name = "Zephaniah",
            description = "A warning of purifying cosmic judgment followed by a sweet promise of restoration and singing.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The word of the LORD which came unto Zephaniah...", "Yahweh’s word which came to Zephaniah...", "The word of the LORD which came to Zephaniah..."))))
        ),
        BibleBook(
            name = "Haggai",
            description = "A direct challenge of spiritual priorities, calling the returning exiles to rebuild God's Temple.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "In the second year of Darius the king, in the sixth month...", "In the second year of Darius the king, in the sixth month...", "In the second year of King Darius, on the first day of the sixth month..."))))
        ),
        BibleBook(
            name = "Zechariah",
            description = "Encouraging visions of rebuilding the Temple coupled with profound prophecies of Christ as the coming branch.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "In the eighth month, in the second year of Darius...", "In the eighth month, in the second year of Darius...", "In the eighth month, in the second year of King Darius..."))))
        ),
        BibleBook(
            name = "Malachi",
            description = "The final Old Testament exhortation confronting spiritual lukewarmness and pointing to the rising Sun of Righteousness.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The burden of the word of the LORD to Israel by Malachi.", "The oracle of Yahweh’s word to Israel by Malachi.", "The burden (mournful prophecy) of the word of the LORD to Israel by Malachi."))))
        ),
        BibleBook(
            name = "Matthew",
            description = "The Gospel of Jesus Christ as King, focusing on the Sermon on the Mount and the Kingdom of Heaven.",
            chapters = listOf(
                BibleChapter(
                    chapterNumber = 5,
                    verses = listOf(
                        BibleVerse(1, "And seeing the multitudes, he went up into a mountain: and when he was set, his disciples came unto him:", "Seeing the multitudes, he went up onto the mountain. When he had sat down, his disciples came to him.", "When Jesus saw the crowds, He went up on the mountain; and when He was seated, His disciples came to Him."),
                        BibleVerse(3, "Blessed are the poor in spirit: for theirs is the kingdom of heaven.", "Blessed are the poor in spirit, for theirs is the Kingdom of Heaven.", "Blessed (happy, prosperous, to be admired) are the poor in spirit, for theirs is the kingdom of heaven."),
                        BibleVerse(4, "Blessed are they that mourn: for they shall be comforted.", "Blessed are those who mourn, for they shall be comforted.", "Blessed (happy, to be envied) are those who mourn, for they shall be comforted."),
                        BibleVerse(5, "Blessed are the meek: for they shall inherit the earth.", "Blessed are the gentle, for they shall inherit the earth.", "Blessed (happy, peaceful) are the meek, for they shall inherit the earth."),
                        BibleVerse(6, "Blessed are they which do hunger and thirst after righteousness: for they shall be filled.", "Blessed are those who hunger and thirst after righteousness, for they shall be filled.", "Blessed are those who hunger and thirst for righteousness, for they shall be satisfied.")
                    )
                ),
                BibleChapter(
                    chapterNumber = 6,
                    verses = listOf(
                        BibleVerse(9, "After this manner therefore pray ye: Our Father which art in heaven, Hallowed be thy name.", "Pray like this: 'Our Father in heaven, may your name be kept holy.", "Pray, then, in this way: 'Our Father who is in heaven, Hallowed be Your name."),
                        BibleVerse(10, "Thy kingdom come. Thy will be done in earth, as it is in heaven.", "Let your Kingdom come. Let your will be done on earth as it is in heaven.", "Your kingdom come. Your will be done on earth as it is in heaven."),
                        BibleVerse(11, "Give us this day our daily bread.", "Give us today our daily bread.", "Give us this day our daily bread."),
                        BibleVerse(12, "And forgive us our debts, as we forgive our debtors.", "Forgive us our debts, as we also forgive our debtors.", "And forgive us our debts, as we have forgiven our debtors."),
                        BibleVerse(13, "And lead us not into temptation, but deliver us from evil: For thine is the kingdom, and the power, and the glory, for ever. Amen.", "Bring us not into temptation, but deliver us from the evil one. For yours is the Kingdom, the power, and the glory forever. Amen.'", "And do not lead us into temptation, but deliver us from evil. For Yours is the kingdom and the power and the glory forever. Amen.'")
                    )
                )
            )
        ),
        BibleBook(
            name = "Mark",
            description = "The action-oriented Gospel presenting Jesus as the tireless, suffering Servant of God.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The beginning of the gospel of Jesus Christ, the Son of God;", "The beginning of the Good News of Jesus Christ, the Son of God.", "The beginning of the gospel of Jesus Christ, the Son of God [as written in Isaiah the prophet]"))))
        ),
        BibleBook(
            name = "Luke",
            description = "The meticulously researched Gospel presenting Jesus as the ideal, compassionate Son of Man.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Forasmuch as many have taken in hand to set forth in order...", "Since many have taken in hand to draw up a narrative...", "Since many have undertaken to compile an orderly account..."))))
        ),
        BibleBook(
            name = "John",
            description = "The Gospel of Spiritual Revelation, declaring the absolute divinity of Christ as the incarnate Word.",
            chapters = listOf(
                BibleChapter(
                    chapterNumber = 1,
                    verses = listOf(
                        BibleVerse(1, "In the beginning was the Word, and the Word was with God, and the Word was God.", "In the beginning was the Word, and the Word was with God, and the Word was God.", "In the beginning was the Word, and the Word was with God, and the Word was God."),
                        BibleVerse(14, "And the Word was made flesh, and dwelt among us, (and we beheld his glory, the glory as of the only begotten of the Father,) full of grace and truth.", "The Word became flesh, and lived among us. We saw his glory, such glory as of the one and only Son of the Father, full of grace and truth.", "And the Word became flesh and tabernacled among us, and we saw His glory, glory as of the only begotten from the Father, full of grace and truth.")
                    )
                ),
                BibleChapter(
                    chapterNumber = 3,
                    verses = listOf(
                        BibleVerse(16, "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.", "For God so loved the world, that he gave his only begotten Son, that whoever believes in him should not perish, but have eternal life.", "For God so greatly loved the world, that He even gave His only begotten Son, so that whoever believes in (trusts in, clings to) Him shall not perish, but have eternal life.")
                    )
                )
            )
        ),
        BibleBook(
            name = "Acts",
            description = "The explosive history of the early Church under the guiding direction of the Holy Spirit.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The former treatise have I made, O Theophilus, of all that Jesus began...", "The first book I wrote, Theophilus, concerned all that Jesus began to do...", "The first account I made, O Theophilus, dealt with all that Jesus began to do and teach..."))))
        ),
        BibleBook(
            name = "Romans",
            description = "Paul's landmark doctrinal masterpiece explaining justification, grace, and redemption through faith.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, a servant of Jesus Christ, called to be an apostle...", "Paul, a servant of Jesus Christ, called to be an apostle...", "Paul, a bond-servant of Christ Jesus, called as an apostle..."))))
        ),
        BibleBook(
            name = "1 Corinthians",
            description = "Paul's pastoral correction of church division and moral confusion, centering on love and order.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, called to be an apostle of Jesus Christ through the will of God...", "Paul, called to be an apostle of Jesus Christ through the will of God...", "Paul, called as an apostle of Jesus Christ by the will of God..."))))
        ),
        BibleBook(
            name = "2 Corinthians",
            description = "Paul's personal and defense of his apostolic authority, detailing inner trials and divine grace.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, an apostle of Jesus Christ by the will of God, and Timothy...", "Paul, an apostle of Jesus Christ by the will of God, and Timothy...", "Paul, an apostle of Christ Jesus by the will of God, and Timothy our brother..."))))
        ),
        BibleBook(
            name = "Galatians",
            description = "A fiery defense of Christian liberty against legalism, emphasizing walking by the Holy Spirit.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, an apostle, (not of men, neither by man, but by Jesus Christ...)", "Paul, an apostle, not from men, nor through man, but through Jesus Christ...", "Paul, an apostle (not sent from men nor through the agency of man, but through Jesus Christ...)"))))
        ),
        BibleBook(
            name = "Ephesians",
            description = "The glorious plan of God to unite all things in Christ, explaining the armor of God.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, an apostle of Jesus Christ by the will of God, to the saints...", "Paul, an apostle of Christ Jesus by the will of God, to the saints...", "Paul, an apostle of Christ Jesus by the will of God, to the saints..."))))
        ),
        BibleBook(
            name = "Philippians",
            description = "Paul's warm epistle of joy, highlighting Christ's self-emptying and finding peace in all things.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul and Timotheus, the servants of Jesus Christ, to all the saints...", "Paul and Timothy, servants of Jesus Christ, to all the saints...", "Paul and Timothy, bond-servants of Christ Jesus, to all the saints..."))))
        ),
        BibleBook(
            name = "Colossians",
            description = "A rich exposition of the absolute supremacy and preeminence of Christ as the head of all creation.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, an apostle of Jesus Christ by the will of God, and Timotheus...", "Paul, an apostle of Christ Jesus by the will of God, and Timothy...", "Paul, an apostle of Jesus Christ by the will of God, and Timothy..."))))
        ),
        BibleBook(
            name = "1 Thessalonians",
            description = "Paul’s commendation of a model faith coupled with teachings on the eventual return of Christ.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, and Silvanus, and Timotheus, unto the church of the Thessalonians...", "Paul, Silvanus, and Timothy, to the assembly of the Thessalonians...", "Paul, Silvanus, and Timothy, to the church of the Thessalonians..."))))
        ),
        BibleBook(
            name = "2 Thessalonians",
            description = "Further clarifications on the Day of the Lord, correcting false reports of its premature arrival.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, and Silvanus, and Timotheus, unto the church of the Thessalonians...", "Paul, Silvanus, and Timothy, to the assembly of the Thessalonians...", "Paul, Silvanus, and Timothy, to the church of the Thessalonians..."))))
        ),
        BibleBook(
            name = "1 Timothy",
            description = "Paul's pastoral counsel to his young delegate Timothy on church leadership, order, and sound doctrine.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, an apostle of Jesus Christ by the commandment of God or Saviour...", "Paul, an apostle of Christ Jesus according to the commandment of God...", "Paul, an apostle of Christ Jesus according to the commandment of God..."))))
        ),
        BibleBook(
            name = "2 Timothy",
            description = "Paul's final farewell letter charging Timothy to guard the Gospel deposit and finish the race.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, an apostle of Jesus Christ by the will of God, according to the promise...", "Paul, an apostle of Jesus Christ by the will of God, according to the promise...", "Paul, an apostle of Christ Jesus by the will of God, according to the promise..."))))
        ),
        BibleBook(
            name = "Titus",
            description = "Instructional guides for Titus on appointing upright Cretan leaders and preaching grace-fueled good works.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, a servant of God, and an apostle of Jesus Christ, according...", "Paul, a servant of God, and an apostle of Jesus Christ, according...", "Paul, a bond-servant of God and an apostle of Jesus..."))))
        ),
        BibleBook(
            name = "Philemon",
            description = "A powerful personal masterclass in Christian reconciliation, requesting Philemon to receive his runaway slave as a brother.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Paul, a prisoner of Jesus Christ, and Timothy our brother, unto Philemon...", "Paul, a prisoner of Christ Jesus, and Timothy our brother, to Philemon...", "Paul, a prisoner of Christ Jesus, and Timothy our brother, to Philemon..."))))
        ),
        BibleBook(
            name = "Hebrews",
            description = "A profound theological argument proclaiming the absolute superiority of Christ over old covenant shadows.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "God, who at sundry times and in divers manners spake in time past...", "God, having in the past spoken to the fathers through the prophets...", "God, who at various times and in various ways spoke in time past to the fathers by the prophets..."))))
        ),
        BibleBook(
            name = "James",
            description = "A vibrant exhortation to practical, living faith that demonstrates its validity through holy actions.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "James, a servant of God and of the Lord Jesus Christ, to the twelve...", "James, a servant of God and of the Lord Jesus Christ, to the twelve...", "James, a bond-servant of God and of the Lord Jesus..."))))
        ),
        BibleBook(
            name = "1 Peter",
            description = "Words of hope, spiritual growth, and encouragement for believers suffering unjust pagan persecution.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Peter, an apostle of Jesus Christ, to the strangers scattered throughout...", "Peter, an apostle of Jesus Christ, to the chosen ones who are living as foreigners...", "Peter, an apostle of Jesus Christ, to those who reside as strangers, scattered..."))))
        ),
        BibleBook(
            name = "2 Peter",
            description = "A sobering warning against deceptive false teachers, calling for continuous growth in spiritual grace.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Simon Peter, a servant and an apostle of Jesus Christ, to them...", "Simon Peter, a servant and apostle of Jesus Christ, to those...", "Simon Peter, a bond-servant and apostle of Jesus Christ, to those..."))))
        ),
        BibleBook(
            name = "1 John",
            description = "A sweet epistle providing divine assurance of eternal life, abiding fellowship, and walking in the Light.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "That which was from the beginning, which we have heard, which we have seen...", "That which was from the beginning, which we have heard, which we have seen...", "What was from the beginning, what we have heard, what we have seen..."))))
        ),
        BibleBook(
            name = "2 John",
            description = "A brief pastoral warning against entertaining deceptive traveling teachers who deny Christ's true humanity.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The elder unto the elect lady and her children, whom I love in the truth...", "The elder to the chosen lady and her children, whom I love in truth...", "The elder to the chosen lady and her children, whom I love in truth..."))))
        ),
        BibleBook(
            name = "3 John",
            description = "An encouraging note commending hospitality to traveling workers while warning against proud leadership.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The elder unto the wellbeloved Gaius, whom I love in the truth.", "The elder to the beloved Gaius, whom I love in truth.", "The elder to the beloved Gaius, whom I love in truth."))))
        ),
        BibleBook(
            name = "Jude",
            description = "A direct and urgent warning to contend earnestly for the faith once delivered to all the saints.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "Jude, the servant of Jesus Christ, and brother of James, to them...", "Jude, a servant of Jesus Christ, and brother of James, to those...", "Jude, a bond-servant of Jesus Christ, and a brother of James, to those..."))))
        ),
        BibleBook(
            name = "Revelation",
            description = "The apocalyptic unveiling of Jesus Christ victorious overall, leading to the New Heaven and the New Earth.",
            chapters = listOf(BibleChapter(1, listOf(BibleVerse(1, "The Revelation of Jesus Christ, which God gave unto him, to shew...", "The Revelation of Jesus Christ, which God gave to him, to show...", "The Revelation of Jesus Christ, which God gave him, to show..."))))
        )
    )

    val bookChapterCounts = mapOf(
        "Genesis" to 50, "Exodus" to 40, "Leviticus" to 27, "Numbers" to 36, "Deuteronomy" to 34,
        "Joshua" to 24, "Judges" to 21, "Ruth" to 4, "1 Samuel" to 31, "2 Samuel" to 24,
        "1 Kings" to 22, "2 Kings" to 25, "1 Chronicles" to 29, "2 Chronicles" to 36,
        "Ezra" to 10, "Nehemiah" to 13, "Esther" to 10, "Job" to 42, "Psalms" to 150,
        "Proverbs" to 31, "Ecclesiastes" to 12, "Song of Solomon" to 8, "Isaiah" to 66,
        "Jeremiah" to 52, "Lamentations" to 5, "Ezekiel" to 48, "Daniel" to 12,
        "Hosea" to 14, "Joel" to 3, "Amos" to 9, "Obadiah" to 1, "Jonah" to 4,
        "Micah" to 7, "Nahum" to 3, "Habakkuk" to 3, "Zephaniah" to 3, "Haggai" to 2,
        "Zechariah" to 14, "Malachi" to 4, "Matthew" to 28, "Mark" to 16, "Luke" to 24,
        "John" to 21, "Acts" to 28, "Romans" to 16, "1 Corinthians" to 16, "2 Corinthians" to 13,
        "Galatians" to 6, "Ephesians" to 6, "Philippians" to 4, "Colossians" to 4,
        "1 Thessalonians" to 5, "2 Thessalonians" to 3, "1 Timothy" to 6, "2 Timothy" to 4,
        "Titus" to 3, "Philemon" to 1, "Hebrews" to 13, "James" to 5, "1 Peter" to 5,
        "2 Peter" to 3, "1 John" to 5, "2 John" to 1, "3 John" to 1, "Jude" to 1,
        "Revelation" to 22
    )

    val books: List<BibleBook> = rawBooks.map { book ->
        val totalCap = bookChapterCounts[book.name] ?: 1
        val updatedChapters = (1..totalCap).map { chNum ->
            book.chapters.firstOrNull { it.chapterNumber == chNum } ?: BibleChapter(
                chapterNumber = chNum,
                verses = listOf(
                    BibleVerse(
                        number = 1,
                        kjvText = "Welcome to ${book.name} Chapter $chNum. [Tap to synchronize complete verses with Bible API or AI]",
                        webText = "Welcome to ${book.name} Chapter $chNum. [Tap to synchronize complete verses with Bible API or AI]",
                        ampText = "Welcome to ${book.name} Chapter $chNum. [Tap to synchronize complete verses with Bible API or AI]"
                    )
                )
            )
        }
        book.copy(chapters = updatedChapters)
    }

    val hymns = listOf(
        Hymn(
            id = 1,
            title = "Amazing Grace",
            englishTitle = "Amazing Grace",
            category = "Worship",
            lyricsEnglish = """
                Amazing grace! How sweet the sound
                That saved a wretch like me!
                I once was lost, but now am found;
                Was blind, but now I see.
                
                'Twas grace that taught my heart to fear,
                And grace my fears relieved;
                How precious did that grace appear
                The hour I first believed!
                
                Through many dangers, toils and snares,
                I have already come;
                'Tis grace hath brought me safe thus far,
                And grace will lead me home.
                
                When we've been there ten thousand years,
                Bright shining as the sun,
                We've no less days to sing God's praise
                Than when we'd first begun.
            """.trimIndent(),
            lyricsYoruba = """
                Iyanu l’ofe Re to dun!
                To gba emi ofo,
                Mo ti sako, O wa mi ri,
                Mo fo, O s’oju mi.
                
                Ofe n’iko mi lati gbon;
                Ofe l’o gbe foya;
                Kiri b’ofe Re ti dara to
                Ni wakati t’mo gba!
                
                Gba t'agun, to k'oja wahala,
                Ofe l'ojo mi ti mo ti d'agba,
                Jehofah l'o mu d'aso mi,
                Emi mimọ t'o tọ mi wa.
                
                Nigba t'a ba de 'bode ogo tọ dun,
                T'a n ran bi oorun firi,
                Ojo yin Re ko le pin titi,
                Bi gba t'a kọ kẹran.
            """.trimIndent(),
            lyricsIgbo = """
                Amara ebube ka ọ dị ụtọ,
                Nke zọpụtara m mmehie!
                M nọ na ọchịchịrị mbụ,
                Ugbu a m na-ahụ ụzọ m.
                
                Amara ahụ kụziiri m egwu,
                Wetara m udo n'obi;
                Olee otú amara ahụ siri dị mma
                Mgbe mbụ m kwetara n'ezie!
                
                Site n'ọtụtụ ihe ize ndụ na ọnwụnwa,
                Amara a dọọla m mma n'ụzọ ziri ezi;
                Ọ bụ amara dugara m n'enweghị nsogbu,
                Ọ ga-edukwa m gaa n'ụgbọ anyị.
                
                Mgbe anyị ruru ebe ahụ ọtụtụ puku afọ,
                Na-enwu gbaa dị ka anyanwụ,
                Anyị agaghị akwụsị ibụ abụ ọma nye Chineke
                Karịa mgbe anyị malitere n'ezie.
            """.trimIndent(),
            lyricsHausa = """
                Alheri mai ban mamaki,
                Ya cece ni mai zunubi!
                Da natsuwa na ɓace kuwa,
                Amma yanzu na dawo kansa.
                
                Wannan alheri ya ba ni tsoro,
                Ya kuma kawar da kowane daci;
                Mafi daraja akwai wannan alheri
                Tun lokacin da na bada gaskiya!
                
                Cikin kowane irin wahala da masifa,
                Wannan alheri ya kiyaye ni kullum;
                Hasken alheri ne ya cece ni har yanzu,
                Kuma shi ne zai kai ni gida lafiya.
                
                Sa'ad da muka kasance can shekaru dubu goma,
                Muna haskakawa kamar rana mai girma,
                Ba za mu gaza wajen rera waƙar yabo ba
                Fiye da sa'ad da muka fara rera ta.
            """.trimIndent(),
            hymnNumber = 1
        ),
        Hymn(
            id = 2,
            title = "How Great Thou Art",
            englishTitle = "How Great Thou Art",
            category = "Praise",
            lyricsEnglish = """
                O Lord my God, when I in awesome wonder
                Consider all the worlds Thy hands have made;
                I see the stars, I hear the rolling thunder,
                Thy power throughout the universe displayed.
                
                Then sings my soul, my Savior God, to Thee:
                How great Thou art! How great Thou art!
                Then sings my soul, My Savior God, to Thee:
                How great Thou art! How great Thou art!
                
                When through the woods and forest glades I wander,
                And hear the birds sing sweetly in the trees;
                When I look down from lofty mountain grandeur,
                And hear the brook and feel the gentle breeze.
                
                And when I think that God, His Son not sparing,
                Sent Him to die, I scarce can take it in;
                That on the cross, my burden gladly bearing,
                He bled and died to take away my sin.
            """.trimIndent(),
            lyricsYoruba = """
                Oluwa mi, gba mo b’iyanu ronu
                L’eti oro won to da lowo Re,
                Mo ri n’irawo, mo gbo dudu ara,
                Ofe l’ayaba t’agbaye fe han.
                
                Gba na k’orin mi, Oluwa mi, si O:
                Bi O ti tobi to! Bi O ti tobi to!
                Gba na k’orin mi, Oluwa mi, si O:
                Bi O ti tobi to! Bi O ti tobi to!
                
                Gba mo ba ronu isẹ isira rẹ titun,
                Ti eye kọrin lori eka igbẹgbe,
                Ti mo d’oju sọlẹ lat’ori oke giga,
                Ti mo si n gbo t’efu jẹjẹ at’isun dandan.
                
                Ati nigba ti mo mọ pe Olorun fun'ra rẹ
                Fi Ọmọ rẹ rubọ lọkan mi rẹ gba wa,
                Ni ori agbelebu to ru eru ese mi kuro,
                To ku rẹ we ese mi mọ b’ọra can.
            """.trimIndent(),
            lyricsIgbo = """
                Ah, Onyenwe m Chineke, mgbe m na-ahụ
                Ihe niile aka Gị rụrụ na ndụ m;
                M na-ahụ kpakpando, m na-anụ égbè eluigwe,
                Ike Gị niile siri n'ụwa pụta.
                
                Mkpụrụ obi m na-abụ abụ Nye Gị:
                Lee ka I siri dị ukwu! Onyenwe m!
                Mkpụrụ obi m na-abụ abụ Nye Gị:
                Lee ka I siri dị ukwu! Onyenwe m!
                
                Mgbe m gara n'ime ọhịa na osisi,
                M na-anụ nnụnụ ka ha na-abụ abụ gị nke ọma;
                Mgbe m lere anya site n'ugwu gachasịrị nnukwu,
                Na-anụ mmiri na-agba na ikuku dị jụụ.
                
                Ma mgbe m chere na Chineke zitere Ọkpara ya,
                Maka anyị, ka Ọ nwụọ n'obe n'ebe anyị nọ;
                O buru mmehie m niile gaa n'obe ahụ,
                Merie mmeri wee zọpụta m pụọ n'onwụnwa.
            """.trimIndent(),
            lyricsHausa = """
                Ya Ubangiji Allahna, in na duba
                Abubuwan da hannunka ya halitta;
                In duba taurari, in ji gurnanin tsawa,
                Ikonka ya bayyana a ko'ina.
                
                Rai na yana rera waƙa gare ka:
                Yaya girmanka yake! Ya Ubangiji!
                Rai na yana rera waƙa gare ka:
                Yaya girmanka yake! Ya Ubangiji!
                
                Lokacin da na ratsa ta daji da koru,
                Ina jin tsuntsaye suna rera waƙar zaƙi;
                Lokacin da na duba ƙasa daga manyan duwatsu,
                Ina jin iska mai daɗi da kwarara ruwa.
                
                Kuma sa'ad da na tuna da can cewa Allah ne,
                Ya ba da Ɗansa don ya cece ni cikin ƙauna;
                Cikin mutuwar giciye ya ɗauke zunubina,
                Ya zubar da jininsa don ya wanke raina.
            """.trimIndent(),
            hymnNumber = 2
        ),
        Hymn(
            id = 3,
            title = "Great Is Thy Faithfulness",
            englishTitle = "Great Is Thy Faithfulness",
            category = "Thanksgiving",
            lyricsEnglish = """
                Great is Thy faithfulness, O God my Father;
                There is no shadow of turning with Thee;
                Thou changest not, Thy compassions, they fail not;
                As Thou hast been Thou forever wilt be.
                
                Great is Thy faithfulness! Great is Thy faithfulness!
                Morning by morning new mercies I see;
                All I have needed Thy hand hath provided;
                Great is Thy faithfulness, Lord, unto me!
                
                Summer and winter, and springtime and harvest,
                Sun, moon and stars in their courses above,
                Join with all nature in manifold witness
                To Thy great faithfulness, mercy and love.
                
                Pardon for sin and a peace that endureth,
                Thine own dear presence to cheer and to guide;
                Strength for today and bright hope for tomorrow,
                Blessings all mine, with ten thousand beside!
            """.trimIndent(),
            lyricsYoruba = """
                Nla ni otito Re, Baba mi atata;
                Kosi iyipada si ona Re;
                Anu Re ko dake, ife Re ko sa;
                Bi O ti wa na ni O o wa titi.
                
                Nla ni otito Re! Nla ni otito Re!
                Lojojumo ni otun t’anu Re n han;
                Gbogbo anfaani l’owo Re ti n pin;
                Nla ni otito Re, Oluwa, si mi!
                
                Igba erun at'ojo, akoko ikore,
                Oorun, osupa, irawo oju orun,
                N kede laiyawọ nigba aye n yipo,
                Pe oloro ati otito kan l'Olorun.
                
                Dariji ese mi, alafia to daju,
                Ipadade ti ara Re to tura m'okan mi,
                Agbara fun oni, ireti to daraju,
                Gbogbo ohun mọ ni ati ẹgbẹẹgbẹrun si.
            """.trimIndent(),
            lyricsIgbo = """
                Oké ka ikwesị ntụkwasị obi Gị dị, Nna m;
                Ọ dịghị ndò nke ọdịda na Gị;
                Ụmụ Gị agaghị ada mgbe ọ bụla;
                Otú I siri dị ka I ga-adị m mgbe niile.
                
                Oké ka ikwesị ntụkwasị obi Gị dị!
                Ụtụtụ niile ọhụrụ mercies m na-ahụ;
                Ihe niile m chọrọ ka aka Gị wetara;
                Oké ka ikwesị ntụkwasị obi Gị dị n'ebe m nọ!
                
                Oge udu mmiri na ọkọchị, na oge ubi,
                Anyanwụ, ọnwa na kpakpando niile n'elu,
                Ha na-abụ abụ ọma banyere ikwesị obi Gị,
                Na-egosi ebere na ịhụnanya m ebighị ebi Gị.
                
                Mgbaghara mmehie na udo na-adịgide adịgide,
                Ọnụnọ Gị dị jụụ na-eduzi anyị mgbe niile;
                Ike maka taa na olileanya maka echi,
                Gọzie m, Onyenwe m, na ndụ m niile!
            """.trimIndent(),
            lyricsHausa = """
                Girma amincinka ne, ya Allah Ubana,
                Babu inuwar juyawa tare da kai;
                Kada ka canza, juyayinka ba zai sake ba;
                Kamar yadda kake, za ka kasance har abada.
                
                Girma amincinka! Girma amincinka!
                Kowace safiya ina ganin sabon rahama;
                Duk abin da nake buƙata hannunka ya ba ni;
                Girma amincinka ne, ya Ubangiji, gare ni!
                
                Rani da damina, kaka da sassafe,
                Rana, wata da taurari a samaniya,
                Kowane halitta yana shaida tare da mu,
                Garin babban amincinka, tausayi da ƙauna.
                
                Gafarar zunubai da salama mai dorewa,
                Hasken fuskarka yana ta'aziya kowace rana;
                Iko don yau da kyakkyawan nasara na gobe,
                Duk albarka tawa ce tare da wasu dubu goma!
            """.trimIndent(),
            hymnNumber = 3
        ),
        Hymn(
            id = 4,
            title = "What A Friend We Have In Jesus",
            englishTitle = "What A Friend We Have In Jesus",
            category = "Prayer",
            lyricsEnglish = """
                What a friend we have in Jesus,
                All our sins and griefs to bear!
                What a privilege to carry
                Everything to God in prayer!
                Oh, what peace we often forfeit,
                Oh, what needless pain we bear,
                All because we do not carry
                Everything to God in prayer!
                
                Have we trials and temptations?
                Is there trouble anywhere?
                We should never be discouraged,
                Take it to the Lord in prayer.
                Can we find a friend so faithful,
                Who will all our sorrows share?
                Jesus knows our every weakness,
                Take it to the Lord in prayer.
                
                Are we weak and heavy laden,
                Cumbered with a load of care?
                Precious Savior, still our refuge,
                Take it to the Lord in prayer.
                Do thy friends despise, forsake thee?
                Take it to the Lord in prayer;
                In His arms He'll take and shield thee,
                Thou wilt find a solace there.
            """.trimIndent(),
            lyricsYoruba = """
                Ore wo ni t’a n ni bi Jesu,
                To gbe b’ese at’ibanuje lo!
                Anfaani nla ni t’a n ni,
                Lati mu gbogbo edun lo si adura!
                Oh, alaafia ta a n so ninu,
                Ibanuje pupo t’a n gbe kiri,
                Dandan nitori a ko mu,
                Gbogbo re lo s’odo re ni adura!
                
                Idanwo ha wa fun wa bi?
                Ipọnju ha wa nibikibi?
                K’a ma se rẹwẹsi rara,
                Mu lọ sọdọ Oluwa ninu adura.
                Ore wo l’otito t’o le
                Pin ninu kikoro ọkan wa?
                Jesu mọ ailera wa gbogbo,
                Go mu lọ sọdọ Oluwa ninu adura.
                
                Se are ati eru n pa wa mọ,
                Lọwọ wahala ati ẹrù itọju aye?
                Olugbala iyebiye, asala wa na lo,
                Go mu lọ sọdọ Oluwa ninu adura.
                S’awọn ọrẹ rẹ ha kọ ọ silẹ?
                Go mu lọ sọdọ Oluwa ninu adura;
                Yoo gbe ọ soke l’owo ifẹ,
                O o ri itura lọkan rẹ can.
            """.trimIndent(),
            lyricsIgbo = """
                Enyi k'anyi nwere n'ime Jisos,
                Nke nwara mmehie k'anyi buru!
                Lee ka ọ siri dị mma
                Ibu ihe niile nye Chineke n'ekpere!
                Oh, udo ole any| hapụrụ na ndụ anyị,
                Oh, ihe mgbu na-enweghị isi anyị na-ebu,
                Ebe ọ bụ na anyị ebughị
                Ihe niile nye Chineke n'ekpere!
                
                Anyị ọ nwere ule na ọnwụnwa?
                Nsogbu ọ siri n'ụzọ niile bata?
                Mkpụrụ obi m, biko egbula ebere,
                Buru ihe niile nye Onyenwe anyị n'ekpere.
                Anyị ọ nwere enyi dị otú ahụ siri ike na-ahụ n'anya,
                Nke ga-eso anyị hụ ihe mgbu niile?
                Jisos maara ike gwụrụ anyị niile,
                Buru ihe niile nye Gị n'ekpere.
                
                Anyị ọ na-alụ ọgụ na ike gwụrụ?
                Burukwa ibu arọ nke ụwa niile?
                Onye Nzọpụta anyị bụ ebe mgbaba anyị ebighị ebi,
                Buru ihe niile nye Onyenwe anyị n'ekpere.
                Ndị enyi gị hà gbahapụrụ gị ugbu a?
                Buru ihe mgbaba ahụ nye Chineke n'ekpere;
                Ọ ga-ejide gị n'aka ya dị mma,
                I ga-enweta udo na nkasi obi n'ebe ahụ.
            """.trimIndent(),
            lyricsHausa = """
                Wane aboki muke da shi ga Yesu,
                Dukkan zunubanmu ya ɗauke su!
                Wane iko ne muke da shi
                Mu kawo dukkan damuwarmu cikin addu'a!
                Oh, wane salama muke asara ko'ina,
                Kuma wane zafi marar amfani muke ji,
                Duk saboda ba mu kawo ba
                Dukkan abu ga Allah cikin addu'a!
                
                Muna da gwaji da fitina ne?
                Kuma wahaloli suna ko'ina kuwa?
                Kada ka yanke ƙauna ko kaɗan,
                Ka kawo dukkan damuwarka cikin addu'a.
                Ashe za mu sami aboki mai aminci kamar haka,
                Wanda zai raba dukkan wahalarmu tare da mu?
                Yesu ya san dukkan rauninmu ko da yaushe,
                Ka kawo kowane abu cikin addu'a.
                
                Muna da rauni da nauyi kuwa,
                Kuma mun cika da nauyin kulawar duniya?
                Ubangiji Yesu shi ne mafakarmu mafi daraja,
                Ka kawo dukkan damuwarka cikin addu'a.
                Ashe abokanka sun rabu da kai kuwa?
                Ka kawo kowane abu cikin addu'a ga Allah;
                Cikin hannunsa zai kiyaye ka koyaushe,
                Za ka sami ta'aziya da salama cikinsa.
            """.trimIndent(),
            hymnNumber = 4
        ),
        Hymn(
            id = 5,
            title = "It Is Well With My Soul",
            englishTitle = "It Is Well With My Soul",
            category = "Faith",
            lyricsEnglish = """
                When peace like a river attendeth my way,
                When sorrows like sea billows roll;
                Whatever my lot, Thou hast taught me to say,
                It is well, it is well with my soul.
                
                It is well (it is well),
                with my soul (with my soul),
                It is well, it is well with my soul.
                
                Though Satan should buffet, though trials should come,
                Let this blest assurance control,
                That Christ hath regarded my helpless estate,
                And hath shed His own blood for my soul.
                
                My sin, oh, the bliss of this glorious thought!
                My sin, not in part but the whole,
                Is nailed to the cross, and I bear it no more,
                Praise the Lord, praise the Lord, O my soul!
            """.trimIndent(),
            lyricsYoruba = """
                Nigba t’alafia bi odo t’ona mi,
                Nigba t’edun bi riru omi,
                Ohunkohun to de, O ti ko mi lati wi pe,
                O dara, o dara fun emi mi.
                
                O dara (o dara),
                fun emi mi (fun emi mi),
                O dara, o dara fun emi mi.
                
                Bi Satani n kọlu mi, bi idanwo ba de,
                Je ki ileri yi jọba ninu otito,
                Christ ti r’oju ailera mi laidara kankan,
                O si f’eje rẹ tọwọ f’emi mi can.
                
                Ese mi, oh ifẹ ati ogo igbala yii!
                Ese mi ti mo ti sẹ laidara ri,
                A ti kan mọ agbelebu ko si jọba mọ,
                Yin Oluwa, yin Oluwa rẹ, ọkan mi!
            """.trimIndent(),
            lyricsIgbo = """
                Mgbe udo dị ka osimiri na-eso ụzọ m,
                Mgbe mkpụrụ obi m na-enwe ihe mgbu niile,
                Ihe ọ bụla siri m rịọ m mmetụta,
                Ọ dị mma, ọ dị mma na mkpụrụ obi m.
                
                Ọ dị mma (ọ dị mma),
                n'obi m nke ukwuu,
                Ọ dị mma, ọ dị mma na mkpụrụ obi m.
                
                Ọ bụ ezie na Setan na-alụso anyị ọgụ niile doro,
                Isi nkwenye m a gọziri agọzi ga-aza m mma:
                Ah, Kraịst emewo ebere ma ọbara ya siri m mmetụta,
                Ọ zụrụ m wee zọpụta mkpụrụ obi m nke ukwuu.
                
                Mmehie anyị niile ka Ọ zọpụta, lee ka ọ siri dị mma!
                A kụrụ ha n'ebe obe nke Jisos n'ezie,
                Agaghị m ebu ha ọzọ, mbụ m zọpụtara n'ihi ya,
                Toonụ Onyenwe anyị, toonụ Chineke, mkpụrụ obi m!
            """.trimIndent(),
            lyricsHausa = """
                Sa'ad da salama ta juyo mini kamar kogi,
                Kuma sa'ad da wahala ta taso mini kamar taguwar teku,
                Duk abin da ya faru da ni ka koya mini in ce,
                Lafiya ne, lafiya ne ga rai na.
                
                Lafiya ne (lafiya),
                rai na yanzu,
                Lafiya ne, lafiya ne ga rai na.
                
                Ko da Shaitan zai yi fada da ni, ko da fitina za ta zo,
                Bari wannan tabbaci mai albarka ya jagoranci kowa;
                Gama Almasihu ya duba irin talaucina na rai,
                Ya kuma zubar da jininsa don ceton raina.
                
                Zunubina, oh farin cikin wannan babban tunani mai ban mamaki,
                Dukkan zunubina ba guntu ba amma gaba ɗaya,
                An kansa a kan giciye, ba zan ƙara ɗauke shi ba,
                Yabi Ubangiji, yabi Ubangiji, ya raina!
            """.trimIndent(),
            hymnNumber = 5
        ),
        Hymn(
            id = 6,
            title = "Blessed Assurance",
            englishTitle = "Blessed Assurance",
            category = "Faith",
            lyricsEnglish = """
                Blessed assurance, Jesus is mine!
                Oh, what a foretaste of glory divine!
                Heir of salvation, purchase of God,
                Born of His Spirit, washed in His blood.
                
                This is my story, this is my song,
                Praising my Savior all the day long;
                This is my story, this is my song,
                Praising my Savior all the day long.
                
                Perfect submission, perfect delight,
                Visions of rapture now burst on my sight;
                Angels descending, bring from above
                Echoes of mercy, whispers of love.
                
                Perfect submission, all is at rest,
                I in my Savior am happy and blest;
                Watching and waiting, looking above,
                Filled with His goodness, lost in His love.
            """.trimIndent(),
            lyricsYoruba = """
                Ileri l’emi ni, Jesu l’Oluwa!
                Oh, itowo ogo t’o logbon julo!
                Ajogun igbala gba nipa ife,
                Atunbi nipa Emi, we ninu Eje.
                
                Iyi n’itan mi, iyi n’orin mi,
                Mo n yin Olugbala mi ni gbogbo ojo;
                Iyi n’itan mi, iyi n’orin mi,
                Mo n yin Olugbala mi ni gbogbo ojo.
                
                Ifarabalẹ pipe, ayọ to kún lọkan,
                Imo ogo ati agbara n bọ simi l’orun;
                Awọn angeli n sọ l’oke tọ tọju si,
                Iro anu ati ifẹ Jesu si m’oke re.
                
                Ifarabalẹ pipe, mo wa ninu isimi,
                Inu Olugbala mi n dun, mo si gba iye;
                Mo n sọna l’ojojumọ, mo n wo ore rẹ,
                Inu rere Jesu kún f’ọkan ifẹ mi dun.
            """.trimIndent(),
            lyricsIgbo = """
                Nkwenye a gọziri agọzi, Jisos bụ nke m!
                Oh, lee ka ebube Chineke siri dị ụtọ!
                Onye nketa nke nzọpụta, nke Chineke,
                Amụrụ na Mmụọ ya, sachaa n'ọbara ya.
                
                Nke a bụ akụkọ m, abụ m nke a,
                Na-eto Onye Nzọpụta m ogologo ụbọchị niile;
                Nke a bụ akụkọ m, abụ m nke a,
                Na-eto Onye Nzọpụta m ogologo ụbọchị niile.
                
                Nrubeisi zuru oke, ọṅụ na-ama mma,
                Ihe nkiri nke eluigwe na-apụta n'ihu m mgbe niile;
                Ndị mmụọ ozi si n'eluigwe na-agbadata ọsọ ọsọ,
                Na-ewetara anyị ebere na ịhụnanya Chineke m.
                
                Nrubeisi zuru oke, udo dị n'ime mkpụrụ obi,
                Mkpụrụ obi m na-enwe anụrị n'ebe Onye Nzọpụta m nọ;
                Na na-atụ anya na-ele anya n'eluigwe,
                Abụ m onye jupụtara n'ịhụnanya na ebere ebighị ebi Gị.
            """.trimIndent(),
            lyricsHausa = """
                Tabbataccen amincewa mai albarka, Yesu nawa ne!
                Oh, wane ɗanɗano na ɗaukaka ta sama!
                Majiɓincin ceto, fansar Allah,
                Haifuwar Ruhunsa, wankakke cikin jininsa.
                
                Wannan ne labari na, waƙa tawa ce,
                Ina yabon Mai Cetona ko da yaushe;
                Wannan ne labari na, waƙa tawa ce,
                Ina yabon Mai Cetona ko da yaushe.
                
                Cikakkiyar biyayya, babban amincin rai,
                Tunanin farin ciki yana bayyana a gare ni;
                Mala'iku suna saukowa daga sama,
                Suna kawo rahamar ceton kowane rayuka.
                
                Cikakkiyar biyayya, dukkan rai yana hutawa dashi,
                Na zama mai albarka a cikin Mai Cetona;
                Ina jira, ina kallo a ko da yaushe,
                Cike da alherinsa, mai natsuwa cikin ƙaunarsa.
            """.trimIndent(),
            hymnNumber = 6
        ),
        Hymn(
            id = 7,
            title = "Holy, Holy, Holy",
            englishTitle = "Holy, Holy, Holy",
            category = "Worship",
            lyricsEnglish = """
                Holy, holy, holy! Lord God Almighty!
                Early in the morning our song shall rise to Thee;
                Holy, holy, holy, merciful and mighty!
                God in three Persons, blessed Trinity!
                
                Holy, holy, holy! All the saints adore Thee,
                Casting down their golden crowns around the glassy sea;
                Cherubim and seraphim falling down before Thee,
                Which wert, and art, and evermore shalt be.
                
                Holy, holy, holy! Though the darkness hide Thee,
                Though the eye of sinful man Thy glory may not see;
                Only Thou art holy; there is none beside Thee,
                Perfect in power, in love, and purity.
            """.trimIndent(),
            lyricsYoruba = """
                Mimo, mimo, mimo! Olodumare!
                Ni kutukutu owuro n’orin wa o m’ake si O;
                Mimo, mimo, mimo, Alaanu at’Alagbara!
                Olorun Mimo, Metalokan l’Oluwa!
                
                Mimo, mimo, mimo! Awọn eniyan mimọ n sin O,
                Won n gbe ade wura won si ti okun glassy ogo mọ;
                Kerubimu ati Serafimu n sọlẹ niwaju rẹ,
                Oluwa t'o wa, t'o si mbe, t'o si mbo titi aye.
                
                Mimo, mimo, mimo! Gba okunkun bo o kuro lọ n’iwaju wa,
                Gba t’ese ti eniyan aladara rẹ ko jẹ t'o gbin lo;
                Iwọ nikan l'o mọ nipa mimo ti ko si ọkan lẹgbẹ rẹ,
                Olorun Alagbara ati Aladara ni Mimo can.
            """.trimIndent(),
            lyricsIgbo = """
                Nsọ, nsọ, nsọ! Onyenwe anyị Chineke Pụrụ Ime Ihe Niile!
                N'isi ụtụtụ abụ anyị ga-arịgo n'ebe I nọ;
                Nsọ, nsọ, nsọ, onye ebere na onye nwere ike!
                Chineke n'ime mmadụ atọ, n'atọ n'ime otu!
                
                Nsọ, nsọ, nsọ! Ndị nsọ niile na-efe Gị,
                Na-atụba okpueze ọlaedo ha n'akụkụ oké osimiri enyo;
                Kerubim na Serafim na-ada n'ụkwụ Gị,
                Gị onye dị ndụ n'oge gara aga na-adịgide adịgide.
                
                Nsọ, nsọ, nsọ! Ọ bụ ezie na ọchịchịrị na-ezobe Gị ugbu a,
                Ọ bụ ezie na anyi nke ndị mmehie enweghị ike ịhụ ebube Gị;
                Naanị Gị bụ onye dị nsọ n'atụghị egwu ọ bụla,
                Gị onye zuru oke na ike, ịhụnanya na ịdị ọcha.
            """.trimIndent(),
            lyricsHausa = """
                Mai Tsarki, mai tsarki, mai tsarki! Ubangiji Allah Mai Iko Dukka!
                Da safe rera waƙar yabo tana tashi gare ka;
                Mai Tsarki, mai tsarki, mai tsarki, mai rahama da iko!
                Allah na Uba, Ɗa, da Ruhu Mai Tsarki!
                
                Mai Tsarki, mai tsarki, mai tsarki! Dukkan tsarkaka suna bauta maka,
                Suna ajiye okpueze na zinariya a tsakiyar fadar sama;
                Kerubim da Serafim suna sujada a gaban fuskarka,
                Kai ne kake da rai, kuma kake nan har abada ya zama.
                
                Mai Tsarki, mai tsarki, mai tsarki! Ko da duhu ya ɓoye hasken fuskarka,
                Ko da idon mutane masu zunubi ba su ga darajarka ba;
                Kai kaɗai ne mai tsarki, babu kowa kamar kai,
                Cikakke cikin iko, cikin ƙauna da tsarki gaba ɗaya.
            """.trimIndent(),
            hymnNumber = 7
        ),
        Hymn(
            id = 8,
            title = "To God Be The Glory",
            englishTitle = "To God Be The Glory",
            category = "Praise",
            lyricsEnglish = """
                To God be the glory, great things He has done;
                So loved He the world that He gave us His Son,
                Who yielded His life an atonement for sin,
                And opened the life gate that all may go in.
                
                Praise the Lord, praise the Lord,
                Let the earth hear His voice!
                Praise the Lord, praise the Lord,
                Let the people rejoice!
                Oh, come to the Father, through Jesus the Son,
                And give Him the glory, great things He has done.
                
                O perfect redemption, the purchase of blood,
                To every believer the promise of God;
                The vilest offender who truly believes,
                That moment from Jesus a pardon receives.
                
                Great things He hath taught us, great things He hath done,
                And great our rejoicing through Jesus the Son;
                But purer, and higher, and greater will be
                Our wonder, our transport, when Jesus we see.
            """.trimIndent(),
            lyricsYoruba = """
                F’Olorun nla l’ogo, nla n’ise t’O se;
                Ife Re l’agbaye to fun wa n’Imo;
                Eniti o fi emi Re rubọ f’ese,
                To si si gbogbo ona iye sile.
                
                Yin Oluwa, yin Oluwa,
                Je k’aye gbo ohun Re!
                Yin Oluwa, yin Oluwa,
                Je ki gbogbo eniyan yo!
                Oh, wá sọdọ Baba nipa Jesu Ọmọ,
                Ki a fun L'ogo iyebiye ti O se lodun.
                
                Ogo igbala ti Jesu f’eje rà,
                Bi ileri mbe fun gbogbo onidaju;
                Ẹlẹṣẹ to buruju ti o ba gba otito gbo,
                Ni wakati na ni idariji rẹ bọ can.
                
                Ohun nla l'O ti kọ wa, ohun nla l'O ti se,
                Ayo wa si n pọ si l’owo rẹ Jesu Ọmọ,
                Sugbon mimo at'ere t'awa yoo dako ri,
                Itoju ati ogo wa nigba t'a ba ri can.
            """.trimIndent(),
            lyricsIgbo = """
                Nye Chineke ebube, nnukwu ihe Ọ rụrụ;
                Ọ hụrụ ụwa n'anya nke ukwuu nke na Ọ nyere Ọkpara ya;
                Onye nyere ndụ ya maka mmehie anyị niile,
                Ma mepee ụzọ nke ndụ ka anyị niile banye.
                
                Toonụ Onyenwe anyị, toonụ Onyenwe anyị,
                Ka ụwa nụrụ olu ya n'ezie!
                Toonụ Onyenwe anyị, toonụ Onyenwe anyị,
                Ka ndị mmadụ niile na-aṅụrị ọṅụ!
                Oh, bịakwute Nna ahụ site n'aka Jisos n'ezie,
                Kpatara Onyenwe anyị ebube niile n'ihe Ọ rụrụ.
                
                Oké nzọpụta nke ọbara Jisos zụtara anyị,
                Maka onye ọ bụla kwere na nkwa nke Chineke;
                Onye mmehie kachasị njọ nke kwere n'ezie n'obe,
                Na-enweta mgbaghara zuru oke site n'aka Jisos.
                
                Oké ihe Ọ kụziiri anyị na nnukwu ihe Ọ rụrụ,
                Anyị na-aṅụrị ọṅụ n'ebe Jisos Ọkpara ya nọ;
                Ma nke kachasị mma na nke kachasị elu ga-abụ,
                Mgbe anyị ga-ahụ Jisos na mmeri niile.
            """.trimIndent(),
            lyricsHausa = """
                Gare ka Allah ɗaukaka, manyan abubuwan da ya yi;
                Ya ƙaunaci duniya sosai har ya ba mu Ɗansa;
                Onye ya ba da ransa don zunubanmu,
                Ya kuma buɗe ƙofar rai don kowa ya shiga.
                
                Yabi Ubangiji, yabi Ubangiji,
                Bari duniya ta ji muryarsa!
                Yabi Ubangiji, yabi Ubangiji,
                Bari mutane su yi farin ciki!
                Oh, ku zo ga Uba ta wurin Yesu Ɗansa,
                Ku ba shi ɗaukaka, manyan abubuwan da ya yi.
                
                Cikakken ceto na jininsa mai daraja,
                Ga kowane mai ba da gaskiya ga alherin Allah;
                Mafi munin mai zunubi wanda ya gaskata da shi,
                A wannan lokacin yana samun gafarar zunuban rai.
                
                Manyan abubuwan da ya koya mana, da manyan ayyuka,
                Ina da murna mai girma cikin Yesu Ɗansa;
                Amma mafi tsarki da daukaka za ta kasance,
                Cikin murnarmu yayinda muka ga Yesu da idonmu.
            """.trimIndent(),
            hymnNumber = 8
        ),
        Hymn(
            id = 9,
            title = "Rock Of Ages",
            englishTitle = "Rock Of Ages",
            category = "Faith",
            lyricsEnglish = """
                Rock of Ages, cleft for me,
                Let me hide myself in Thee;
                Let the water and the blood,
                From Thy wounded side which flowed,
                Be of sin the double cure;
                Save from wrath and make me pure.
                
                Not the labors of my hands
                Can fulfill Thy law's demands;
                Could my zeal no respite know,
                Could my tears forever flow,
                All for sin could not atone;
                Thou must save, and Thou alone.
                
                While I draw this fleeting breath,
                When mine eyes shall close in death,
                When I rise to worlds unknown,
                And behold Thee on Thy throne,
                Rock of Ages, cleft for me,
                Let me hide myself in Thee.
            """.trimIndent(),
            lyricsYoruba = """
                Aba-apata ti afowo le,
                Je ki njade lo farapamo ninu Re;
                Ki omi ati Eje to nṣàn,
                Lati egbe Re ti a gun,
                Di iwosan ese mi mejeeji;
                Gba mi lowo ibinu, se m’mo.
                
                Kii se ise owo mi,
                L'o le mu t'ofin se patapata;
                Bi npokan aye ko se dake l'ofo,
                Bi gba mi ko le r'isimi ekun di dake,
                Gbogbo re ko le we ese mi dake,
                Iwo nikan l'o le gbala, Iwo nikan.
                
                L’emi t’o kù ninu ara mi,
                Gba m’oju mi ba si ti iku bati can,
                Gba t'mo ba lo si aiye titun mbe can,
                T'mo ba duro ni iwaju oye can Re,
                Aba-apata ti afowo le can,
                Je ki njade lo farapamo ninu Re.
            """.trimIndent(),
            lyricsIgbo = """
                Nkume ebighị ebi, nke anyị nwere,
                Ka m zoo onwe m n'ime Gị;
                Ka mmiri na ọbara niile,
                Nke si n'akụkụ Gị pụta mgbe a gbara Gị égbè,
                Buru ọgwụgwọ maka mmehie m;
                Zọpụta m pụọ n'iwe ma mee m onye dị ọcha.
                
                Ọbụghị ọrụ nke aka m,
                Pụrụ imezu ihe Iwu Gị chọrọ n'ezie;
                Ọ bụrụgodị na m gbaa mbọ ike,
                Ma ọ bụ nwee anya mmiri na-agba mgbe niile,
                Ihe ndị ahụ agaghị ehichapụ mmehie m;
                Naanị Gị bụ onye ga-azọpụta m.
                
                Mgbe m na-enwe ume ikpeazụ na ndụ m,
                Mgbe anya m na-emechi n'ọnwụ n'ezie;
                Mgbe m ga-ala n'ụwa a na-amaghị ama,
                Wee hụ Gị n'oche eze nke Onyenwe anyị,
                Nkume ebighị ebi dịrị m mma mbụ,
                Biko ka m zoo onwe m n'ime Gị.
            """.trimIndent(),
            lyricsHausa = """
                Dutsen zamani, na fake a cikinka,
                Bari in ɓoye kaina a cikinka;
                Bari ruwa da jini waɗanda,
                Sun gudana daga raunin gefenka,
                Su zama maganin zunubi biyu;
                Su cece ni daga fushi, su tsarkake ni.
                
                Ba aiki na hannuna zai iya,
                Biya buƙatun dokarka mai tsarki ba;
                Ko da na nuna kishina ko da yaushe,
                Ko da hawayena ya zama kwarara ruwa kullum,
                Wannan ba zai goge zunubina ba;
                Sai dai kai kaɗai, kai kaɗai za ka cece ni.
                
                Yayinda nake da rai a nan duniya,
                Sa'ad da idanuna za su rufe cikin mutuwa,
                Sa'ad da na tashi zuwa can gaban fuskarka,
                In dube ka kana zaune a kan kursiyinka,
                Dutsen zamani, na fake a cikinka,
                Bari in ɓoye kaina a cikinka.
            """.trimIndent(),
            hymnNumber = 9
        ),
        Hymn(
            id = 10,
            title = "Pass Me Not, O Gentle Savior",
            englishTitle = "Pass Me Not, O Gentle Savior",
            category = "Prayer",
            lyricsEnglish = """
                Pass me not, O gentle Savior,
                Hear my humble cry;
                While on others Thou art calling,
                Do not pass me by.
                
                Savior, Savior, hear my humble cry;
                While on others Thou art calling,
                Do not pass me by.
                
                Let me at a throne of mercy
                Find a sweet relief;
                Kneeling there in deep contrition,
                Help my unbelief.
                
                Trusting only in Thy merit,
                Would I seek Thy face;
                Heal my wounded, broken spirit,
                Save me by Thy grace.
            """.trimIndent(),
            lyricsYoruba = """
                Ma koja mi, Olugbala,
                Gbo igbe aro mi;
                Gba t’O ba n pe elomiran,
                Dakun ma koja mi.
                
                Gbala, Gbala, gbo igbe mi won;
                Gba t’O ba n pe elomiran,
                Dakun ma koja mi.
                
                Jẹ ki n r’awon anu gba mọ,
                Ri itura won gba;
                Kunle n’ibe l’ojojumọ won t’o mọ,
                Ran mi lọwọ nipa ailọgbon mi rẹ.
                
                Ifẹ nikan ati ore-ọfẹ rẹ l'o mbe lodun,
                N o n f’oju rẹ rẹ;
                Wo emi gila mimọ mi can san,
                Gba mi nipa ore-ọfẹ rẹ.
            """.trimIndent(),
            lyricsIgbo = """
                Agafela m, Onye Nzọpụta dị nwayọọ,
                Nụrụ akwa m n'uju;
                Mgbe I na-akpọ ndị ọzọ na ndụ gị,
                Biko agafela m.
                
                Onye Nzọpụta, nụrụ akwa m;
                Mgbe I na-akpọ ndị ọzọ,
                Biko agafela m n'ebe m nọ.
                
                Ka m nọrọ n'oche ebere Gị mma,
                Wee nweta nkasi obi;
                N'ikpere m ebe ahụ na-arịọ m ebere,
                Biko nyere m aka.
                
                Na-atụkwasị obi naanị n'ebube Gị,
                M ga-achọ ihu Gị mma;
                Mee ka mkpụrụ obi m gbajiri agbaji dị mma,
                Zọpụta m n'amara ebube Gị.
            """.trimIndent(),
            lyricsHausa = """
                Kada ka wuce ni, ya mai cetona mai juyayi,
                Ka saurari kuka na mai tawali'u;
                Yayinda kake kiran wasu a ko'ina,
                Kada ka wuce ni.
                
                Mai cetona, mai cetona, ka ji kuka na;
                Yayinda kake kiran wasu,
                Kada ka wuce ni.
                
                Bari in tsaya kusa da kursiyin alheri,
                In sami ta'aziya mai daɗi gare ni;
                Ina sujada can cikin nadar zunubina,
                Bari ka taimaki raunin gaskiyata.
                
                Dogara ga kawai kyakkyawan amincinka,
                Zan nemi fuskarka ko da yaushe;
                Ka warkar da karya ta bacin raina,
                Ka cece ni ta wurin alherinka mai yawa.
            """.trimIndent(),
            hymnNumber = 10
        )
    )
}
