package example.code.some_project.util

sealed class Optional<out T> {
    class Present<T>(private val value: T) : Optional<T>() {
        override val present: Boolean = true
        override fun get() = value
    }
    object Absent : Optional<Nothing>() {
        override val present: Boolean = false
        override fun get() = null
    }

    abstract val present: Boolean
    abstract fun get(): T?

    companion object {

        fun <T> absent(): Optional<T> = Absent

        fun <T> fromNullable(value: T?): Optional<T> =
            if (value == null) {
                Absent
            } else {
                Present(value)
            }
    }
}