package example.code.some_project.presentation.ui.fragment.privacy_policy

import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.ui.util.Constansts.OPEN_PDF_FROM
import example.code.some_project.presentation.ui.util.Constansts.PRIVACY_POLICY_URL

class PrivacyPolicyPresenterImpl(
    logoutInteractor: LogoutInteractor,
    networkManager: NetworkManager
): AbstractPrivacyPolicyPresenter(logoutInteractor, networkManager) {

    override fun onResume() {
        super.onResume()
        loadUrl()
    }

    private fun loadUrl() {
        if (!hasConnection) {
            viewState.showNoInternetScreen()
        }

        viewState.hideErrorScreen()

        viewState.startWebView("$OPEN_PDF_FROM$PRIVACY_POLICY_URL")
    }

    override fun retry() = loadUrl()
}