package sy.com.initproject.root.ui.skin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.pince.frame.BaseActivity
import com.pince.ut.FileUtils
import com.youlu.skinloader.listener.ILoaderListener
import com.youlu.skinloader.load.SkinManager
import sy.com.initproject.R
import sy.com.initproject.databinding.FragmentChangSkinBinding
import java.io.File

/**
 * Author by Administrator , Date on 2018/11/29.
 * PS: Not easy to write code, please indicate.
 */
class SkinActivity : BaseActivity<FragmentChangSkinBinding>() {
    override fun enableResponseOnSkin(): Boolean {
        return false
    }


    private var SKIN_DIR: String? = null


    companion object {
        private const val TAG = "ChangeSkinFragment"
        private const val SKIN_BROWN_NAME = "skin_brown.skin"
        private const val SKIN_BLACK_NAME = "skin_black.skin"

        @JvmStatic
        fun open(context: Context) {
            context.startActivity(Intent(context, SkinActivity::class.java))
        }
    }


    override fun checkData(bundle: Bundle?): Boolean {
        return true
    }

    override fun requestLayoutId(): Int {
        return R.layout.fragment_chang_skin
    }


    override fun initView(rootView: View?) {
        super.initView(rootView)
        SKIN_DIR = FileUtils.getSkinDirPath(activityContext)
        mBinding.llGreen.setOnClickListener(View.OnClickListener { SkinManager.getInstance().restoreDefaultTheme() })
        mBinding.llBrown.setOnClickListener(View.OnClickListener {
            val skinFullName = SKIN_DIR + File.separator + "skin_brown.skin"
            FileUtils.moveRawToDir(activityContext, "skin_brown.skin", skinFullName)
            val skin = File(skinFullName)
            if (!skin.exists()) {
                Toast.makeText(activityContext, "请检查" + skinFullName + "是否存在", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            SkinManager.getInstance().load(skin.getAbsolutePath(),
                    object : ILoaderListener {
                        override fun onStart() {
                            Log.e(TAG, "loadSkinStart")
                        }

                        override fun onSuccess() {
                            Log.i(TAG, "loadSkinSuccess")
                            Toast.makeText(activityContext, "切换成功", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailed() {
                            Log.i(TAG, "loadSkinFail")
                            Toast.makeText(activityContext, "切换失败", Toast.LENGTH_SHORT).show()
                        }
                    })
        })
        mBinding.llBlack.setOnClickListener(View.OnClickListener {
            val skinFullName = SKIN_DIR + File.separator + "skin_black.skin"
            FileUtils.moveRawToDir(activityContext, "skin_black.skin", skinFullName)
            val skin = File(skinFullName)
            if (!skin.exists()) {
                Toast.makeText(activityContext, "请检查" + skinFullName + "是否存在", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            SkinManager.getInstance().load(skin.getAbsolutePath(),
                    object : ILoaderListener {
                        override fun onStart() {
                            Log.e(TAG, "loadSkinStart")
                        }

                        override fun onSuccess() {
                            Log.e(TAG, "loadSkinSuccess")
                            Toast.makeText(activityContext, "切换成功", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailed() {
                            Log.e(TAG, "loadSkinFail")
                            Toast.makeText(activityContext, "切换失败", Toast.LENGTH_SHORT).show()
                        }
                    })
        })
    }

    override fun setViewData(savedInstanceState: Bundle?) {
    }
}