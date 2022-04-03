package example.code.some_project.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import example.code.some_project.presentation.ui.activity.viewData.OnboardingScreenData
import example.code.some_project.presentation.ui.fragment.OnboardingFragment

class OnboardingAdapter(
    private val screens: List<OnboardingScreenData>,
    activity: FragmentActivity
): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = screens.size

    override fun createFragment(position: Int): Fragment {
        val screen: OnboardingScreenData = screens[position]
        return OnboardingFragment.newInstance(
            image = screen.image,
            title = screen.title,
            description = screen.description
        )
    }
}