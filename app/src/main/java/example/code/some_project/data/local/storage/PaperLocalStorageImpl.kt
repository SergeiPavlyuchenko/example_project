package example.code.some_project.data.local.storage

import io.paperdb.Paper
import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.DataMissedException
import example.code.some_project.domain.datasource.local.storage.LocalStorage
import timber.log.Timber

class PaperLocalStorageImpl : LocalStorage {

    override fun write(tableName: String, key: String, data: Any): Completable {
        return Completable.fromAction {
            try {
                Paper.book(tableName).write(key, data)
            } catch (e: Exception) {
                Timber.d(e)
                delete(tableName, key)
            }
        }
    }

    override fun <T : Any> writeWithResult(tableName: String, key: String, data: T): Single<T> =
        Completable.fromAction { Paper.book(tableName).write(key, data) }
            .doOnError { throwable ->
                Timber.d(throwable)
                delete(tableName, key)
            }
            .toSingleDefault(data)

    override fun writeAll(tableName: String, map: Map<String, Any>): Completable {
        return Completable.fromAction {
            try {
                for ((key, data) in map) {
                    Paper.book(tableName).write(key, data)
                }
            } catch (e: Exception) {
                Timber.d(e)
                erase(tableName)
            }
        }
    }

    override fun <T : Any> read(tableName: String, key: String): Single<T> {
        return Single.fromCallable {
            try {
                Paper.book(tableName).read<T>(key)?.let {
                    it
                } ?: throw DataMissedException("No data in storage with key $key for table $tableName")
            } catch (e: Exception) {
                Timber.d(e)
                delete(tableName, key)
                throw DataMissedException("Error read data with key $key for table $tableName")
            }
        }
    }

    override fun <T : Any> readAll(tableName: String, filterBy: List<String>): Single<List<T>> {
        return Single.fromCallable {
            try {
                val mutableList = mutableListOf<T>()
                val keys = if (filterBy.isNotEmpty()) filterBy else getAllKeys(tableName)
                for (key in keys) {
                    Paper.book(tableName).read<T>(key)?.let {
                        mutableList.add(it)
                    } ?: throw DataMissedException("No data in storage with key $key for table $tableName")
                }
                mutableList
            } catch (e: Exception) {
                Timber.d(e)
                erase(tableName)
                throw DataMissedException("Error read data for table $tableName")
            }
        }
    }

    override fun delete(tableName: String, key: String): Completable {
        return Completable.fromAction {
            try {
                Paper.book(tableName).delete(key)
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    override fun erase(tableName: String): Completable {
        return Completable.fromAction {
            try {
                Paper.book(tableName).destroy()
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    override fun getAllKeys(tableName: String): List<String> {
        return Paper.book(tableName).allKeys
    }
}
