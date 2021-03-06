package sy.com.initproject.root.ui.preview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.pince.frame.BaseActivity
import com.pince.ut.AppUtil
import com.pince.ut.BitmapUtils
import com.pince.ut.JsonUtil
import com.pince.ut.constans.FileConstants
import rx.Observable
import rx.functions.Action1
import rx.functions.Func1
import sy.com.initproject.R
import sy.com.initproject.databinding.ActivityPictureBinding
import sy.com.initproject.root.models.GirlBean
import java.io.File

/**
 * @date：2018/7/18
 * @author: SyShare
 */
class PictureActivity : BaseActivity<ActivityPictureBinding>() {


    private var mPhotoViewAttacher: PhotoViewAttacher? = null
    private var mImageUrl: String? = null
    private var mImageTitle: String? = null

    companion object {
        const val EXTRA_IMAGE_URL = "image_url"
        const val EXTRA_IMAGE_TITLE = "image_title"
        const val TRANSIT_PIC = "picture"
        const val EXTRA_PIC_LIST = ":EXTRA_PIC_LIST"


        @JvmStatic
        fun newIntent(context: Context, url: String, desc: String,list: List<GirlBean>): Intent {
            val intent = Intent(context, PictureActivity::class.java)
            intent.putExtra(PictureActivity.EXTRA_IMAGE_URL, url)
            intent.putExtra(PictureActivity.EXTRA_IMAGE_TITLE, desc)
            return intent
        }
    }

    override fun isToolBarEnable(): Boolean {
        return false
    }

    override fun checkData(bundle: Bundle?): Boolean {
        mImageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        mImageTitle = intent.getStringExtra(EXTRA_IMAGE_TITLE)
        return !TextUtils.isEmpty(mImageUrl)
    }


    override fun requestLayoutId(): Int {
        return R.layout.activity_picture
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)

//        ViewCompat.setTransitionName(mBinding.picture, TRANSIT_PIC)
        Glide.with(this).load(mImageUrl).into(mBinding.picture)
        title = mImageTitle
//        setupPhotoAttacher()
        mBinding.picture.setOnLongClickListener {
            AlertDialog.Builder(this@PictureActivity)
                    .setMessage(getString(R.string.ask_saving_picture))
                    .setNegativeButton(android.R.string.cancel
                    ) { dialog, which -> dialog.dismiss() }
                    .setPositiveButton(android.R.string.ok
                    ) { dialog, which ->
                        saveImageToGallery()
                        dialog.dismiss()
                    }
                    .show()
            // @formatter:on
            true
        }
        mBinding.picture.setOnPhotoTapListener(object : OnPhotoTapListener{
            override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
                finish()
            }

        })
    }

    private fun setupPhotoAttacher() {
        mPhotoViewAttacher = PhotoViewAttacher(mBinding.picture)
        // @formatter:off
        mPhotoViewAttacher?.setOnLongClickListener { v ->
            AlertDialog.Builder(this@PictureActivity)
                    .setMessage(getString(R.string.ask_saving_picture))
                    .setNegativeButton(android.R.string.cancel
                    ) { dialog, which -> dialog.dismiss() }
                    .setPositiveButton(android.R.string.ok
                    ) { dialog, which ->
                        saveImageToGallery()
                        dialog.dismiss()
                    }
                    .show()
            // @formatter:on
            true
        }
        mPhotoViewAttacher?.setOnClickListener {
            finish()
        }

    }

    private fun saveImageToGallery() {
        val bitmap = BitmapUtils.drawableToBitmap(mBinding.picture.drawable)
        val path = FileConstants.CACHE_SHOT_DIR + System.currentTimeMillis() + ".jpg"
        Observable.just(path, Func1<String, File> { s ->
            BitmapUtils.saveBitmap2JPG(bitmap, s)
        }, Action1<File> { file -> AppUtil.insertImageToSystem(this@PictureActivity, file) })
    }


    override fun setViewData(savedInstanceState: Bundle?) {
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}