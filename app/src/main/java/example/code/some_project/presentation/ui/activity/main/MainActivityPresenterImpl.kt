package example.code.some_project.presentation.ui.activity.main

import example.code.some_project.domain.repo.NetworkManager
import moxy.InjectViewState

@InjectViewState
class MainActivityPresenterImpl(
    networkManager: NetworkManager
) : AbstractMainActivityPresenter(networkManager) {

}