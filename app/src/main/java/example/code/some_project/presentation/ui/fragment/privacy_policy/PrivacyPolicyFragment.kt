package example.code.some_project.presentation.ui.fragment.privacy_policy

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import example.code.some_project.R
import example.code.some_project.databinding.FragmentPrivacyPolicyBinding
import example.code.some_project.presentation.ui.base.BaseMvpFragment
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get

class PrivacyPolicyFragment:BaseMvpFragment<FragmentPrivacyPolicyBinding>(), PrivacyPolicyView {

    override val presenter: AbstractPrivacyPolicyPresenter by moxyPresenter { get() }
    override val contentViewId: Int = R.id.llPrivacyPolicyContentView

    override fun createViewBinding() = FragmentPrivacyPolicyBinding.inflate(layoutInflater)

    override fun initUx() {
        binding.run {
            tbPrivacyPolicy.setNavigationOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun startWebView(url: String) = binding.run {
        wvPrivacyPolicy.settings.javaScriptEnabled = true
        wvPrivacyPolicy.webViewClient = object : WebViewClient() {
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
        wvPrivacyPolicy.loadUrl(url)
    }
}