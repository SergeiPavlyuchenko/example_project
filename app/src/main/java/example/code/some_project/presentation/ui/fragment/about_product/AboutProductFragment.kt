package example.code.some_project.presentation.ui.fragment.about_product

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import example.code.some_project.R
import example.code.some_project.databinding.FragmentAboutProductBinding
import example.code.some_project.presentation.ui.base.BaseMvpFragment
import example.code.some_project.presentation.ui.listener.ActivityInterfaceListener
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get

class AboutProductFragment: BaseMvpFragment<FragmentAboutProductBinding>(), AboutProductView {
    override val presenter: AbstractAboutProductPresenter by moxyPresenter { get() }
    override val contentViewId: Int = R.id.llAboutProductContentView
    private val activityListener get() = baseActivity as? ActivityInterfaceListener

    override fun createViewBinding() = FragmentAboutProductBinding.inflate(layoutInflater)

    override fun initUi() {

    }

    override fun initUx() {
        binding.run {
            tbAboutProduct.setNavigationOnClickListener {
                activityListener?.onActionClicked()
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun startWebView(url: String) = binding.run {
        wvAboutProduct.settings.javaScriptEnabled = true
        wvAboutProduct.webViewClient = object : WebViewClient() {
            /**
             * Вызывается на старых устройствах
             */
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            /**
             * Вызывается на новых устройствах
             */
            override fun shouldOverrideUrlLoading(
                view: WebView,
                resourceRequest: WebResourceRequest
            ): Boolean {
                val newUrl = resourceRequest.url.toString()
                view.loadUrl(newUrl)
                return false
            }
        }
        wvAboutProduct.loadUrl(url)
    }
}