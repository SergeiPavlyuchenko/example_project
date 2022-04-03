package example.code.some_project.domain.interactor

import io.reactivex.Single

interface SplashInteractor {

    fun initialize(): Single<Pair<Boolean, Boolean>>

}