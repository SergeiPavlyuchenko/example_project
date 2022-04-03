package example.code.some_project.presentation.ui.base.binding

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import example.code.some_project.presentation.ui.lifecycle.LifecycleObserverAdapter
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentsBindingDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    private val bindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    init {
        LifecycleObserverAdapter(
            onCreate = {
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { lifecycleOwner ->
                    LifecycleObserverAdapter(onDestroy = { binding = null })
                        .let(lifecycleOwner.lifecycle::addObserver)
                }
            }
        ).let(fragment.lifecycle::addObserver)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) return binding
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not initialize binding when Fragment views are destroyed")
        }

        return bindingFactory.invoke(thisRef.requireView())
            .also { this@FragmentsBindingDelegate.binding = it }
    }
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentsBindingDelegate(
        this,
        viewBindingFactory
    )

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}
