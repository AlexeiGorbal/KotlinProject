package org.example.project.local

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FirebaseLocationService {
    // Ссылка на узел "locations" в вашей базе
    private val dbRef = Firebase.database.reference("locations")

    // 1. Добавление (аналог @Insert)
    suspend fun addLocation(location: LocationInfo) {
        // Сохраняем по ID. Если id типа Long, приводим к строке
        dbRef.child(location.id.toString()).setValue(location)
    }

    // 2. Удаление (аналог @Delete)
    suspend fun removeLocation(id: Long) {
        dbRef.child(id.toString()).removeValue()
    }

    // 3. Получение по ID (аналог @Query по id)
    suspend fun getLocationById(id: Long): LocationInfo? {
        val snapshot = dbRef.child(id.toString()).valueEvents.first()
        return if (snapshot.exists) snapshot.value() else null
    }

    // 4. Получение списка (аналог Flow<List>)
    fun getLocations(): Flow<List<LocationInfo>> {
        return dbRef.valueEvents.map { snapshot ->
            // snapshot.children возвращает список всех объектов в узле
            snapshot.children.map { it.value<LocationInfo>() }
        }
    }
}