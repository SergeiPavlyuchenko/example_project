package example.code.some_project.presentation.ui.custom_views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import example.code.some_project.R
import example.code.some_project.databinding.ViewProfileItemBinding

class ProfileItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewProfileItemBinding

    init {
        binding = ViewProfileItemBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setIcon(icon: Int) {
        binding.ivProfileItemImage.setImageDrawable(ContextCompat.getDrawable(this.context, icon))
    }

    fun setText(text: Int) {
        binding.tvProfileItemText.text = resources.getString(text)
    }

    fun setCustomBackground(isTopItem: Boolean = false, isBottomItem: Boolean = false) {
        binding.root.background = when {
            isTopItem -> ContextCompat.getDrawable(this.context, R.drawable.bg_profile_top_item)
            isBottomItem -> ContextCompat.getDrawable(this.context, R.drawable.bg_profile_bottom_item)
            else -> ColorDrawable(Color.WHITE)
        }
    }
}
