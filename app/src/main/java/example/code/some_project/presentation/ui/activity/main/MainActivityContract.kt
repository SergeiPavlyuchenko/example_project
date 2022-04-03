package example.code.some_project.presentation.ui.activity.main

import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseActivityPresenter
import example.code.some_project.presentation.mvp.base.BaseActivityView
import example.code.some_project.presentation.ui.base.BaseActivityPresenterBase

interface MainActivityView: BaseActivityView {
}

interface MainActivityPresenter: BaseActivityPresenter

abstract class AbstractMainActivityPresenter(
    networkManager: NetworkManager
): BaseActivityPresenterBase<MainActivityView>(
    networkManager
), MainActivityPresenter