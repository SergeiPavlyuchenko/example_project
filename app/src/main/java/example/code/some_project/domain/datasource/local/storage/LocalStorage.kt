package example.code.some_project.domain.datasource.local.storage

import io.reactivex.Completable
import io.reactivex.Single

interface LocalStorage {
    fun write(tableName: String, key: String, data: Any): Completable
    fun <T : Any> writeWithResult(tableName: String, key: String, data: T): Single<T>
    fun writeAll(tableName: String, map: Map<String, Any>): Completable
    fun <T : Any> read(tableName: String, key: String): Single<T>
    fun <T : Any> readAll(tableName: String, filterBy: List<String> = emptyList()): Single<List<T>>
    fun delete(tableName: String, key: String): Completable
    fun erase(tableName: String): Completable
    fun getAllKeys(tableName: String): List<String>
}
