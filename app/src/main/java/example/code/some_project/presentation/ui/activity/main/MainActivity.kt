package example.code.some_project.presentation.ui.activity.main

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import example.code.some_project.R
import example.code.some_project.databinding.ActivityMainBinding
import example.code.some_project.presentation.ui.base.BaseMvpActivity
import example.code.some_project.presentation.ui.base.binding.viewBinding
import example.code.some_project.presentation.ui.fragment.contacts.ContactsFragment
import example.code.some_project.presentation.ui.fragment.profile.ProfileFragment
import example.code.some_project.presentation.ui.util.Constansts.EXTRA_MENU_ID
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get

class MainActivity : BaseMvpActivity(), MainActivityView {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    override val presenter: AbstractMainActivityPresenter by moxyPresenter { get() }
    override val contentViewId: Int = R.id.flMainContentView

    private val contactsFragment: ContactsFragment by lazy {
        ContactsFragment()
    }
    private val profileFragment: ProfileFragment by lazy {
        ProfileFragment()
    }

    override fun initUi() {
        setContentView(binding.root)
    }

    override fun initUx() {
        with(binding) {
            vBottomNavigation.setOnItemSelectedListener(::onMenuSelected)
        }
    }

    override fun onResume() {
        super.onResume()
        if (supportFragmentManager.findFragmentById(R.id.flMainContentView) == null) {
            binding.vBottomNavigation.selectedItemId = getMenuId()
        }
    }

    @IdRes
    private fun getMenuId(): Int =
        intent?.getIntExtra(EXTRA_MENU_ID, R.id.menu_contacts) ?: R.id.menu_contacts

    private fun onMenuSelected(item: MenuItem): Boolean {
        setupMenuFragment(item.itemId)
        return true
    }

    private fun setupMenuFragment(@IdRes menuId: Int) {
        when (menuId) {
            R.id.menu_contacts -> contactsFragment
            R.id.menu_profile -> profileFragment
            else -> null
        }?.let { setupRootContentFragment(it) }
    }

    private fun setupRootContentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.flMainContentView, fragment).commitNow()
    }

    fun setupNextContentFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flMainContentView, fragment)
            .addToBackStack(tag)
            .commit()
    }

    companion object {
        fun createIntent(
            context: Context,
            @IdRes menuId: Int = R.id.menu_contacts,
        ) = Intent(context, MainActivity::class.java)
            .putExtra(EXTRA_MENU_ID, menuId)
    }

}