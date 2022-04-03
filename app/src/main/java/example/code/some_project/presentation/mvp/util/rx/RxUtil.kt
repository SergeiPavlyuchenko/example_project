package example.code.some_project.presentation.mvp.util.rx

import androidx.annotation.CallSuper
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import example.code.some_project.domain.PatchTokensException
import example.code.some_project.domain.NeedUpdateTokensException
import example.code.some_project.presentation.mvp.base.BaseView
import timber.log.Timber

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

fun Disposable?.isNullOrDisposed(): Boolean = this == null || this.isDisposed

open class DefaultDisposableObserver<T>(private val view: BaseView) : DisposableObserver<T>() {

    override fun onComplete() {
        // no-op by default
    }

    override fun onNext(t: T) {
        // no-op by default
    }

    @CallSuper
    override fun onError(e: Throwable) {
        Timber.e(e)
        FirebaseCrashlytics.getInstance().recordException(e)
        if (e is NeedUpdateTokensException || e is PatchTokensException) {
            view.openAuth()
        }
    }

}

class SimpleDisposableObserver<T> @JvmOverloads constructor(
    view: BaseView,
    private val onNextAction: (T) -> Unit = {},
    private val onErrorAction: (Throwable) -> Unit = {},
    private val onCompleteAction: () -> Unit = {},
    private val onStartAction: () -> Unit = {}
) : DefaultDisposableObserver<T>(view) {

    override fun onNext(t: T) = onNextAction(t)

    override fun onError(e: Throwable) {
        super.onError(e)
        onErrorAction(e)
    }

    override fun onComplete() = onCompleteAction()

    override fun onStart() = onStartAction()
}

open class DefaultDisposableSingleObserver<T>(
    private val view: BaseView
) : DisposableSingleObserver<T>() {

    override fun onSuccess(t: T) {
        // no-op by default
    }

    @CallSuper
    override fun onError(e: Throwable) {
        Timber.e(e)
        FirebaseCrashlytics.getInstance().recordException(e)
        if (e is NeedUpdateTokensException || e is PatchTokensException) {
            view.openAuth()
        }
    }
}

class SimpleDisposableSingleObserver<T> @JvmOverloads constructor(
    view: BaseView,
    private val onSuccessAction: (T) -> Unit = {},
    private val onErrorAction: (Throwable) -> Unit = {},
    private val onStartAction: () -> Unit = {}
) : DefaultDisposableSingleObserver<T>(view) {

    override fun onSuccess(t: T) = onSuccessAction(t)

    override fun onError(e: Throwable) {
        super.onError(e)
        onErrorAction(e)
    }

    override fun onStart() = onStartAction()
}

open class DefaultDisposableMaybeObserver<T>(
    private val view: BaseView
) : DisposableMaybeObserver<T>() {

    override fun onSuccess(t: T) {
        // no-op by default
    }

    override fun onComplete() {
        // no-op by default
    }

    @CallSuper
    override fun onError(e: Throwable) {
        Timber.e(e)
        FirebaseCrashlytics.getInstance().recordException(e)
        if (e is NeedUpdateTokensException || e is PatchTokensException) {
            view.openAuth()
        }
    }
}

class SimpleDisposableMaybeObserver<T> @JvmOverloads constructor(
    view: BaseView,
    private val onSuccessAction: (T) -> Unit = {},
    private val onCompleteAction: () -> Unit = {},
    private val onErrorAction: (Throwable) -> Unit = {},
    private val onStartAction: () -> Unit = {}
) : DefaultDisposableMaybeObserver<T>(view) {

    override fun onSuccess(t: T) = onSuccessAction(t)

    override fun onComplete() = onCompleteAction()

    override fun onError(e: Throwable) {
        super.onError(e)
        onErrorAction(e)
    }

    override fun onStart() = onStartAction()
}

open class DefaultDisposableCompletableObserver(
    private val view: BaseView
) : DisposableCompletableObserver() {

    override fun onComplete() {
        // no-op by default
    }

    override fun onError(e: Throwable) {
        Timber.e(e)
        FirebaseCrashlytics.getInstance().recordException(e)
        if (e is NeedUpdateTokensException || e is PatchTokensException) {
            view.openAuth()
        }
    }

}

class SimpleDisposableCompletableObserver(
    view: BaseView,
    private val onCompleteAction: () -> Unit = {},
    private val onErrorAction: (Throwable) -> Unit = {},
    private val onStartAction: () -> Unit = {}
) : DefaultDisposableCompletableObserver(view) {

    override fun onComplete() = onCompleteAction()

    override fun onError(e: Throwable) {
        super.onError(e)
        onErrorAction(e)
    }

    override fun onStart() = onStartAction()
}