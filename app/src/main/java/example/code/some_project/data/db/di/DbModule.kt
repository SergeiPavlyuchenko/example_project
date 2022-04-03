package example.code.some_project.data.db

import androidx.room.Room
import org.koin.dsl.module

val dbModule = module {

    single {
        Room.databaseBuilder(get(), Database::class.java, "app-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<DatabaseSource> { RoomDatabaseSource(get()) }
}