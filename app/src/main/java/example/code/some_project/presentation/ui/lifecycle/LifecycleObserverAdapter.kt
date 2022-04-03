package example.code.some_project.presentation.ui.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class LifecycleObserverAdapter(
    private val onCreate: (() -> Unit)? = null,
    private val onStart: (() -> Unit)? = null,
    private val onResume: (() -> Unit)? = null,
    private val onPause: (() -> Unit)? = null,
    private val onStop: (() -> Unit)? = null,
    private val onDestroy: (() -> Unit)? = null
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        onCreate?.invoke()
    }

    override fun onStart(owner: LifecycleOwner) {
        onStart?.invoke()
    }

    override fun onResume(owner: LifecycleOwner) {
        onResume?.invoke()
    }

    override fun onPause(owner: LifecycleOwner) {
        onPause?.invoke()
    }

    override fun onStop(owner: LifecycleOwner) {
        onStop?.invoke()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy?.invoke()
    }
}
