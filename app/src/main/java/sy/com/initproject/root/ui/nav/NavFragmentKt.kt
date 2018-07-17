package sy.com.initproject.root.ui.nav

import android.content.Context
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.pince.frame.BaseFragment
import com.pince.ut.shape.BorderShape
import sy.com.initproject.R
import sy.com.initproject.databinding.FragmentHomeNavBinding
import sy.com.initproject.root.ui.jokes.JokeFragment
import sy.com.initproject.root.ui.mine.UserInfoFragment
import sy.com.initproject.root.ui.news.NewsFragment

class NavFragmentKt : BaseFragment<FragmentHomeNavBinding>(), View.OnClickListener {

    private var mContext: Context? = null
    private var mContainerId: Int = 0
    private var mFragmentManager: FragmentManager? = null
    private var mCurrentNavButton: NavigationButtonKt? = null
    private var mOnNavigationReselectListener: OnNavigationReselectListener? = null

    override fun requestLayoutId(): Int {
        return R.layout.fragment_home_nav
    }


    override fun initView(rootView: View?) {
        super.initView(rootView)

        val lineDrawable = ShapeDrawable(BorderShape(RectF(0f, 1f, 0f, 0f)))
        lineDrawable.paint.color = resources.getColor(R.color.colorDivider_cccccc)
        val layerDrawable = LayerDrawable(arrayOf(ColorDrawable(resources.getColor(R.color.white)), lineDrawable))
        rootView?.setBackgroundDrawable(layerDrawable)

//        mBinding.navItemExplore.setOnClickListener(this)
        mBinding.navItemMe.setOnClickListener(this)
        mBinding.navItemNews.setOnClickListener(this)
        mBinding.navItemTweet.setOnClickListener(this)
    }

    override fun setViewData(savedInstanceState: Bundle?) {
        mBinding.navItemNews.init(R.drawable.tab_icon_new,
                R.string.main_tab_name_news,
                NewsFragment::class.java)

        mBinding.navItemTweet.init(R.drawable.tab_icon_tweet,
                R.string.main_tab_name_jokes,
                JokeFragment::class.java)

//        mBinding.navItemExplore.init(R.drawable.tab_icon_explore,
//                R.string.main_tab_name_novel,
//                JokeFragment::class.java)

        mBinding.navItemMe.init(R.drawable.tab_icon_me,
                R.string.main_tab_name_my,
                UserInfoFragment::class.java)
    }


    override fun onClick(v: View?) {
        if (v is NavigationButtonKt) {
            doSelect(v)
        }
    }


    fun setup(context: Context, fragmentManager: FragmentManager, contentId: Int, listener: OnNavigationReselectListener) {
        mContext = context
        mFragmentManager = fragmentManager
        mContainerId = contentId
        mOnNavigationReselectListener = listener

        // do clear
        clearOldFragment()
        // do select first
        doSelect(mBinding.navItemNews)
    }

    private fun clearOldFragment() {
        val transaction = mFragmentManager?.beginTransaction()
        val fragments = mFragmentManager?.getFragments()
        if (transaction == null || fragments == null || fragments.size == 0)
            return
        var doCommit = false
        for (fragment in fragments) {
            if (fragment !== this) {
                transaction.remove(fragment)
                doCommit = true
            }
        }
        if (doCommit)
            transaction.commitNow()
    }

    /**
     * 选中
     */
    private fun doSelect(newNavButton: NavigationButtonKt) {
        var oldNavButton: NavigationButtonKt? = null
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton
            if (oldNavButton === newNavButton) {
                onReselect(oldNavButton)
                return
            }
            oldNavButton!!.isSelected = false
        }
        newNavButton.isSelected = true
        doTabChanged(oldNavButton, newNavButton)
        mCurrentNavButton = newNavButton
    }


    /**
     * 复选
     */
    private fun onReselect(navigationButton: NavigationButtonKt) {
        val listener = mOnNavigationReselectListener
        listener?.onReselect(navigationButton)
    }

    /**
     * 切换显示Fragment
     */
    private fun doTabChanged(oldNavButton: NavigationButtonKt?, newNavButton: NavigationButtonKt?) {
//        val ft = mFragmentManager?.beginTransaction()
//        if (oldNavButton != null) {
//            if (oldNavButton.getFragment() != null) {
//                ft?.hide(oldNavButton.getFragment())
//            }
//        }
//        if (newNavButton != null) {
//            if (newNavButton.getFragment() == null) {
//                val fragment = Fragment.instantiate(mContext,
//                        newNavButton.getClx()?.name, null)
//                ft?.add(mContainerId, fragment, newNavButton.tag)
//                newNavButton.setFragment(fragment)
//            } else {
//                ft?.attach(newNavButton.getFragment())
//            }
//        }
//        ft?.commit()


        val ft = mFragmentManager?.beginTransaction()

        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft?.hide(oldNavButton.getFragment())
            }
        }

        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                val fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx()?.name, null)
                ft?.add(mContainerId, fragment, newNavButton.tag)
                newNavButton.setFragment(fragment)
            } else {
                ft?.show(newNavButton.getFragment())
            }
        }
        ft?.commit()
    }


    interface OnNavigationReselectListener {
        fun onReselect(navigationButton: NavigationButtonKt)
    }
}