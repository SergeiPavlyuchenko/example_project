package example.code.some_project.presentation.mvp.base

import androidx.annotation.CallSuper
import androidx.annotation.UiThread
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import example.code.some_project.domain.NeedUpdateTokensException
import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.util.rx.DefaultDisposableMaybeObserver
import example.code.some_project.presentation.mvp.util.rx.SimpleDisposableCompletableObserver
import example.code.some_project.presentation.mvp.util.rx.SimpleDisposableMaybeObserver
import example.code.some_project.presentation.mvp.util.rx.SimpleDisposableObserver
import example.code.some_project.presentation.mvp.util.rx.SimpleDisposableSingleObserver
import example.code.some_project.presentation.mvp.util.rx.isNullOrDisposed
import moxy.MvpPresenter
import java.util.concurrent.TimeUnit

abstract class BaseMvpPresenter<V : BaseView>(
    private val logoutInteractor: LogoutInteractor?,
    private val networkManager: NetworkManager
) : MvpPresenter<V>(), BasePresenter {

    open var processCommand: (() -> Unit)? = null

    private val disposeBag = CompositeDisposable()

    private var isResumed: Boolean = false

    protected val startOnResume: MutableList<() -> Unit> = mutableListOf()
    protected var composite: CompositeDisposable? = null

    protected val hasConnection: Boolean get() = networkManager.isNetworkAvailable()

    override fun retry() {}

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.dispose()
    }

    @CallSuper
    override fun onResume() {
        isResumed = true
        setupComposite()
        startOnResume.forEach { it() }
        startOnResume.clear()
    }

    @CallSuper
    override fun onPause() {
        disposeComposite()
        isResumed = false
    }

    @UiThread
    private fun setupComposite() {
        if (composite.isNullOrDisposed()) {
            composite = CompositeDisposable()
        }
    }

    @UiThread
    private fun disposeComposite() {
        if (!composite.isNullOrDisposed()) {
            composite?.dispose()
        }
    }

    fun Disposable.bind(): Disposable {
        checkComposite()

        composite?.add(this)
        return this
    }

    fun executeCompletable(
        completable: Completable,
        onCompleteAction: () -> Unit = {},
        onErrorAction: (Throwable) -> Unit = {},
        onStartAction: () -> Unit = {},
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeScheduler: Scheduler = AndroidSchedulers.mainThread()
    ) = executeCompletable(
        completable,
        SimpleDisposableCompletableObserver(
            viewState, onCompleteAction, onErrorAction, onStartAction
        ),
        subscribeScheduler,
        observeScheduler
    )

    @JvmOverloads
    fun executeCompletable(
        completable: Completable,
        observer: DisposableCompletableObserver = SimpleDisposableCompletableObserver(viewState),
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): Disposable {
        checkComposite()

        val disposable: Disposable = completable
            .onErrorResumeNext {
                if (it is NeedUpdateTokensException && logoutInteractor != null) {
                    logoutInteractor.logout()
                        .andThen(Completable.error(it))
                } else {
                    Completable.error(it)
                }
            }
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribeWith(observer)

        composite?.add(disposable)

        return disposable
    }

    fun <T> executeMaybe(
        maybe: Maybe<T>,
        onSuccessAction: (T) -> Unit = {},
        onCompleteAction: () -> Unit = {},
        onErrorAction: (Throwable) -> Unit = {},
        onStartAction: () -> Unit = {},
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeScheduler: Scheduler = AndroidSchedulers.mainThread()
    ) = executeMaybe(
        maybe,
        SimpleDisposableMaybeObserver(
            viewState, onSuccessAction, onCompleteAction, onErrorAction, onStartAction
        ),
        subscribeScheduler,
        observeScheduler
    )

    @JvmOverloads
    fun <T> executeMaybe(
        maybe: Maybe<T>,
        observer: DefaultDisposableMaybeObserver<T> = SimpleDisposableMaybeObserver<T>(viewState),
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): Disposable {
        checkComposite()

        val disposable: Disposable = maybe
            .onErrorResumeNext(Function {
                if (it is NeedUpdateTokensException && logoutInteractor != null) {
                    logoutInteractor.logout()
                        .andThen(Maybe.error(it))
                } else {
                    Maybe.error(it)
                }
            })
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribeWith(observer)

        composite?.add(disposable)

        return disposable
    }

    fun <T> executeSingle(
        single: Single<T>,
        onSuccessAction: (T) -> Unit = {},
        onErrorAction: (Throwable) -> Unit = {},
        onStartAction: () -> Unit = {},
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): Disposable = executeSingle(
        single,
        SimpleDisposableSingleObserver(viewState, onSuccessAction, onErrorAction, onStartAction),
        subscribeScheduler,
        observeScheduler
    )

    @JvmOverloads
    fun <T> executeSingle(
        single: Single<T>,
        observer: DisposableSingleObserver<T> = SimpleDisposableSingleObserver<T>(viewState),
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): Disposable {
        checkComposite()

        val disposable: Disposable = single
            .onErrorResumeNext {
                if (it is NeedUpdateTokensException && logoutInteractor != null) {
                    logoutInteractor.logout()
                        .andThen(Single.error(it))
                } else {
                    Single.error(it)
                }
            }
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribeWith(observer)

        composite?.add(disposable)

        return disposable
    }

    fun <T> executeObservable(
        observable: Observable<T>,
        onNextAction: (T) -> Unit = {},
        onErrorAction: (Throwable) -> Unit = {},
        onCompleteAction: () -> Unit = {},
        onStartAction: () -> Unit = {},
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeScheduler: Scheduler = AndroidSchedulers.mainThread()
    ) = executeObservable(
        observable,
        SimpleDisposableObserver(
            viewState, onNextAction, onErrorAction, onCompleteAction, onStartAction
        ),
        subscribeScheduler,
        observeScheduler
    )

    @JvmOverloads
    fun <T> executeObservable(
        observable: Observable<T>,
        observer: DisposableObserver<T> = SimpleDisposableObserver<T>(viewState),
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): Disposable {
        checkComposite()

        val disposable: Disposable = observable
            .onErrorResumeNext(Function {
                if (it is NeedUpdateTokensException && logoutInteractor != null) {
                    logoutInteractor.logout()
                        .andThen(Observable.error(it))
                } else {
                    Observable.error(it)
                }
            })
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribeWith(observer)

        composite?.add(disposable)

        return disposable
    }

    private fun checkComposite() {
        if (composite.isNullOrDisposed()) {
            if (isResumed) {
                setupComposite()
            } else {
                throw IllegalStateException("executeCompletable() is called, but presenter is not resumed")
            }
        }
    }

    fun startWhenResumed(action: () -> Unit) {
        if (isResumed) {
            action()
        } else {
            startOnResume.add(action)
        }
    }

    fun startWithDelay(delayInMillis: Long, action: () -> Unit) {
        executeCompletable(
            Completable.timer(delayInMillis, TimeUnit.MILLISECONDS),
            onCompleteAction = action
        )
    }

}

