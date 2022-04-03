package example.code.some_project.presentation.ui.fragment.sync_contacts

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import kotlinx.android.synthetic.main.fragment_sync_contacts.*
import example.code.some_project.R
import example.code.some_project.databinding.FragmentSyncContactsBinding
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenter
import example.code.some_project.presentation.ui.base.BaseMvpFragment
import example.code.some_project.presentation.ui.extensions.setOnClick


class SyncContactsFragment : BaseMvpFragment<FragmentSyncContactsBinding>() {

    override val presenter: BaseFragmentPresenter? = null
    override val contentViewId: Int = R.id.flSyncContactsContentView

    override fun createViewBinding() = FragmentSyncContactsBinding.inflate(layoutInflater)

    override fun initUx() {
        tbSyncContacts.setNavigationOnClickListener { goBack() }
        with(binding.llSyncContactsItem.syncButton) {
            text = getString(R.string.sync_contacts_button_text)
            setOnClick(::navigateToSettings)
        }
    }

    private fun navigateToSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context?.packageName, null)
        }.let(::startActivity)
    }
}