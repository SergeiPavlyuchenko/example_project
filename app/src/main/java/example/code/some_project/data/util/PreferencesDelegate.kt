package example.code.some_project.data.util

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/*
Ключи лучше присваивать руками, чтобы исключить случай разных имен при разных версиях префов
* */

fun SharedPreferences.string(
    defaultValue: String = "",
    key: (KProperty<*>) -> String = { it.name }
) = object : ReadWriteProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>) =
        getString(key(property), defaultValue).orEmpty()

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: String
    ) = edit().putString(key(property), value).apply()
}

fun SharedPreferences.int(
    key: (KProperty<*>) -> String = { it.name },
    defaultValue: Int = 0
) = object : ReadWriteProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int =
        if (contains(key(property))) {
            getInt(key(property), defaultValue)
        } else defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) = edit().run {
        putInt(key(property), value)
    }.apply()
}

fun SharedPreferences.boolean(
    key: (KProperty<*>) -> String = { it.name },
    defaultValue: Boolean = false
) = object : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>) =
        getBoolean(key(property), defaultValue)

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: Boolean
    ) = edit().putBoolean(key(property), value).apply()
}

fun SharedPreferences.long(
    key: (KProperty<*>) -> String = { it.name },
    defaultValue: Long = 0L
) = object : ReadWriteProperty<Any, Long> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Long =
        if (contains(key(property))) {
            getLong(key(property), defaultValue)
        } else defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) = edit().run {
        putLong(key(property), value)
    }.apply()
}
