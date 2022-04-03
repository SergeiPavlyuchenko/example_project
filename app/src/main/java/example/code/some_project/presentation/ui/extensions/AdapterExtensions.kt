package example.code.some_project.presentation.ui.extensions

import androidx.recyclerview.widget.RecyclerView

val RecyclerView.Adapter<*>.lastItemPosition get() = itemCount - 1