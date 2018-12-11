package sy.com.initproject.root.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.pince.frame.BaseActivity
import com.pince.permission.PermissionCallback
import sy.com.initproject.root.lockscreenad.LockScreenService
import sy.com.initproject.R
import sy.com.initproject.databinding.ActivityMainBinding
import sy.com.initproject.root.interf.OnTabReselectListener
import sy.com.initproject.root.ui.nav.NavFragmentKt
import sy.com.initproject.root.ui.nav.NavigationButtonKt
import sy.com.initproject.root.ui.skin.SkinActivity

class MainActivity : BaseActivity<ActivityMainBinding>(), NavFragmentKt.OnNavigationReselectListener {
    override fun enableResponseOnSkin(): Boolean {
        return true
    }

    private var mNavBar: NavFragmentKt? = null


    override fun changeTheme() {
        super.changeTheme()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    companion object {
        @JvmStatic
        fun open(context: Context){
            context.startActivity(Intent(context,MainActivity::class.java))
        }
    }

    override fun checkData(bundle: Bundle?): Boolean {
        return true
    }

    override fun isToolBarEnable(): Boolean {
        return false
    }

    override fun homeAsUpEnable(): Boolean {
        return false
    }

    override fun requestLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun initView(contentView: View?) {
        super.initView(contentView)
        val manager = supportFragmentManager
        mNavBar = manager.findFragmentById(R.id.fag_nav) as NavFragmentKt
        mNavBar?.setup(this, manager, R.id.main_container, this)

        mBinding.btnSkin.setOnClickListener {
            SkinActivity.open(activityContext)
        }

        registerBroadCast()
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


    private fun registerBroadCast() {
        val intent = Intent()
        intent.action = LockScreenService.LOCK_SCREEN_ACTION
        sendBroadcast(intent)
    }

    override fun requestMenuId(): Int {
        return R.menu.menu_main_toolbar
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_skin) {
            SkinActivity.open(activityContext)
            return true
        } else if (item?.itemId == R.id.action_about) {
            mNavBar?.doSelectMine()
            return true
        }
        return super.onMenuItemClick(item)
    }

}
