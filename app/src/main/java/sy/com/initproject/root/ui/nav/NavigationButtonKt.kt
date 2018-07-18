package sy.com.initproject.root.ui.nav

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import sy.com.initproject.databinding.LayoutNavItemBinding

class NavigationButtonKt : FrameLayout {

    private lateinit var binding: LayoutNavItemBinding

    private var mFragment: Fragment? = null
    private var mClx: Class<*>? = null
    private var mTag: String? = null

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        binding = LayoutNavItemBinding.inflate(LayoutInflater.from(context), this, true)
    }


    fun init(@DrawableRes resId: Int, @StringRes strId: Int, clx: Class<*>) {
        binding.navIvIcon.setImageResource(resId)
        binding.navTvTitle.setText(strId)
        mClx = clx
        mTag = mClx?.getName()
    }

    fun getClx(): Class<*>? {
        return mClx
    }

    fun getFragment(): Fragment? {
        return mFragment
    }

    fun setFragment(fragment: Fragment) {
        this.mFragment = fragment
    }

    override fun getTag(): String? {
        return mTag
    }
}