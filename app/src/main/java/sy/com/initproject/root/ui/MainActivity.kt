package sy.com.initproject.root.ui

import android.os.Bundle
import android.view.View
import com.pince.frame.BaseActivity
import com.pince.permission.PermissionCallback
import sy.com.initproject.R
import sy.com.initproject.databinding.ActivityMainBinding
import sy.com.initproject.root.interf.OnTabReselectListener
import sy.com.initproject.root.ui.nav.NavFragmentKt
import sy.com.initproject.root.ui.nav.NavigationButtonKt

class MainActivity : BaseActivity<ActivityMainBinding>(), NavFragmentKt.OnNavigationReselectListener {

    private var mNavBar: NavFragmentKt? = null

    override fun checkData(bundle: Bundle?): Boolean {
        return true
    }

    override fun requestLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun initView(contentView: View?) {
        super.initView(contentView)
        val manager = supportFragmentManager
        mNavBar = manager.findFragmentById(R.id.fag_nav) as NavFragmentKt
        mNavBar?.setup(this, manager, R.id.main_container, this)
    }

    override fun setViewData(savedInstanceState: Bundle?) {
        getPermissionHelper().request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permissions_denied_storage), object : PermissionCallback() {
            override fun onGranted() {

            }
        })
    }


    override fun onReselect(navigationButton: NavigationButtonKt) {
        val fragment = navigationButton.getFragment()
        if (fragment != null && fragment is OnTabReselectListener) {
            val listener = fragment as OnTabReselectListener?
            listener!!.onTabReselect()
        }
    }

}
