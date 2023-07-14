package service

import icu.bughub.shared.cache.Word

class StoreService {

    fun selectAll(): List<Word> = databaseSchema.sqlQueries.selectWords().executeAsList()

    fun insert(content: String, from: String, date: String, weekday: String, imageUrl: String) {
        databaseSchema.sqlQueries.insertWord(content, from, date, weekday, imageUrl)
    }

    fun select7Words(): List<Word> = databaseSchema.sqlQueries.select7Words().executeAsList()

}