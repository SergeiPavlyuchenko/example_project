package example.code.some_project.domain.repo.device

import android.content.Intent

// ToDo модифицировать под данный проект, если будет нужен. Если нет - удалить
interface ContactPickerManager {
    fun selectContact(data: Intent): String
}