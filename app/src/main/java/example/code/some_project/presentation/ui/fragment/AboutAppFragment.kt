package example.code.some_project.presentation.ui.fragment

import example.code.some_project.BuildConfig
import example.code.some_project.R
import example.code.some_project.databinding.FragmentAboutAppBinding
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenter
import example.code.some_project.presentation.ui.base.BaseMvpFragment

class AboutAppFragment : BaseMvpFragment<FragmentAboutAppBinding>() {

    override val presenter: BaseFragmentPresenter? = null
    override val contentViewId: Int = R.id.llAboutAppContentView
    override var isDarkTheme: Boolean = true
    override var contentThemeDark: Boolean = true

    override fun createViewBinding() = FragmentAboutAppBinding.inflate(layoutInflater)

    override fun initUi() {
        val text = getString(R.string.about_app_version, BuildConfig.VERSION_CODE.toString()) +
                "\n" + getString(R.string.about_app_from_date, BuildConfig.BUILD_DATE)
        binding.tvAboutAppDescription.text = text
    }

    override fun initUx() {
        binding.tbAboutApp.setNavigationOnClickListener { goBack() }
    }
}