package sy.com.initproject.root.ui.splash

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_splash.*
import sy.com.initproject.R
import sy.com.initproject.root.lockscreenad.LockScreenService.Companion.LOCK_SCREEN_ACTION
import sy.com.initproject.root.lockscreenad.LockScreenService.Companion.TAG
import sy.com.initproject.root.ui.MainActivity
import sy.com.initproject.root.ui.web.WebActivity

class SplashActivity : AppCompatActivity() {


    var isJumpMain: Boolean = false
    var listData: List<Int> = listOf(R.drawable.ic_01, R.drawable.ic_02, R.drawable.ic_03, R.drawable.ic_04, R.drawable.ic_05)

    companion object {

        const val KEY_TAG = ":jump_boolean"


        @JvmStatic
        fun start(context: Activity?) {
            context?.startActivity(Intent(context, SplashActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = Intent()
        intent.action = LOCK_SCREEN_ACTION
        sendBroadcast(intent)
        super.onCreate(savedInstanceState)
        initWindow()
        setContentView(R.layout.activity_splash)
        initData()
        initView()
        initIntent()
        startTimer()
        initListener()
    }


    private fun initData() {

    }

    private fun initListener() {
        slide_layout.setOnSlitherFinishListener {
            MainActivity.open(this@SplashActivity)
            WebActivity.open(this, "https://blog.csdn.net/syb001chen", null)
            finish()
        }
        slide_layout.setTouchView(window.decorView)
    }

    private fun initIntent() {
        isJumpMain = getIntent().getBooleanExtra(KEY_TAG, false)
    }

    private fun startTimer() {
        val countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                if (isJumpMain) {
                    MainActivity.open(this@SplashActivity)
                    finish()
                }
            }

        }
        countDownTimer.start()
    }

    private fun initView() {
        val size = listData.size
        val index = (Math.random() * size).toInt()
        val res = listData[index]
        Glide.with(this)
                .load(res)
                .thumbnail(0.1f)
                .apply(RequestOptions()
                        .fitCenter())
                .into(img)


        Glide.with(this)
                .load(R.drawable.top)
                .apply(RequestOptions()
                        .fitCenter())
                .thumbnail(0.1f)
                .into(img1)

        Glide.with(this)
                .load(R.drawable.bottom)
                .thumbnail(0.1f)
                .apply(RequestOptions()
                        .fitCenter())
                .into(img2)

        Glide.with(this)
                .load(R.drawable.background)
                .apply(RequestOptions()
                        .centerCrop())
                .thumbnail(0.1f)
                .into(img3)
    }


    private fun initWindow() {
        //注意需要做一下判断
        if (window != null) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            // 锁屏的activity内部也要做相应的配置，让activity在锁屏时也能够显示，同时去掉系统锁屏。
            // 当然如果设置了系统锁屏密码，系统锁屏是没有办法去掉的
            // FLAG_DISMISS_KEYGUARD用于去掉系统锁屏页
            // FLAG_SHOW_WHEN_LOCKED使Activity在锁屏时仍然能够显示

            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                keyguardManager.requestDismissKeyguard(this, object : KeyguardManager.KeyguardDismissCallback() {
                    override fun onDismissCancelled() {
                        super.onDismissCancelled()
                        Log.e(TAG, "onDismissCancelled")
                    }

                    override fun onDismissError() {
                        super.onDismissError()
                        Log.e(TAG, "onDismissError")
                    }
                })
            }

            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        // SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION，开发者容易被其中的HIDE_NAVIGATION所迷惑，
                        // 其实这个Flag没有隐藏导航栏的功能，只是控制导航栏浮在屏幕上层，不占据屏幕布局空间；
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        // SYSTEM_UI_FLAG_HIDE_NAVIGATION，才是能够隐藏导航栏的Flag；
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        // SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN，由上面可知，也不能隐藏状态栏，只是使状态栏浮在屏幕上层。
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE
            }
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        // SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION，开发者容易被其中的HIDE_NAVIGATION所迷惑，
                        // 其实这个Flag没有隐藏导航栏的功能，只是控制导航栏浮在屏幕上层，不占据屏幕布局空间；
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        // SYSTEM_UI_FLAG_HIDE_NAVIGATION，才是能够隐藏导航栏的Flag；
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        // SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN，由上面可知，也不能隐藏状态栏，只是使状态栏浮在屏幕上层。
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE
            }
        }
    }


    override fun onBackPressed() {
        // 不做任何事，为了屏蔽back键
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val key = event.keyCode
        when (key) {
            KeyEvent.KEYCODE_BACK -> {
                return true
            }
            KeyEvent.KEYCODE_MENU -> {
                return true
            }
            else -> {
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onDestroy() {
        val intent = Intent()
        intent.action = LOCK_SCREEN_ACTION
        sendBroadcast(intent)
        super.onDestroy()
    }


    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val intent = Intent()
        intent.action = LOCK_SCREEN_ACTION
        sendBroadcast(intent)
        finish()
    }
}
