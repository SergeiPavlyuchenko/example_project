package example.code.some_project.presentation.ui.base

import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseActivityPresenter
import example.code.some_project.presentation.mvp.base.BaseActivityView
import example.code.some_project.presentation.mvp.base.BaseMvpPresenter

abstract class BaseActivityPresenterBase<T : BaseActivityView>(
    networkManager: NetworkManager
) : BaseMvpPresenter<T>(null, networkManager), BaseActivityPresenter