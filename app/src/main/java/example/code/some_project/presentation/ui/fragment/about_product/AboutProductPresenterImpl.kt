package example.code.some_project.presentation.ui.fragment.about_product

import example.code.some_project.data.util.Language
import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.domain.repo.device.ResourcesManager
import example.code.some_project.presentation.ui.util.Constansts.FULL_OFFER_KZ_URL
import example.code.some_project.presentation.ui.util.Constansts.FULL_OFFER_RU_URL

class AboutProductPresenterImpl(
    logoutInteractor: LogoutInteractor,
    private val resourcesManager: ResourcesManager,
    networkManager: NetworkManager
) : AbstractAboutProductPresenter(logoutInteractor, networkManager) {

    override fun onResume() {
        super.onResume()
        load()
    }

    private fun load() {
        if (!hasConnection) {
            viewState.showNoInternetScreen()
            return
        }

        viewState.hideErrorScreen()

        if (resourcesManager.getLanguage() == Language.KZ) {
            viewState.startWebView(FULL_OFFER_KZ_URL)
        } else {
            viewState.startWebView(FULL_OFFER_RU_URL)
        }
    }

    override fun retry() {
        load()
    }
}