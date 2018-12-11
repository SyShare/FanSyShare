package sy.com.initproject.root.lockscreenad

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import sy.com.initproject.root.ui.splash.SplashActivity

/**
 * Author by Administrator , Date on 2018/11/30.
 * PS: Not easy to write code, please indicate.
 */
class LockScreenService : Service() {


    companion object {
        const val TAG: String = "LockScreenService"
        const val LOCK_SCREEN_ACTION = "cn.syb.lock"


        /**
         * 比如，广播，耳机声控，通知栏广播，来电或者拔下耳机广播开启服务
         * @param context       上下文
         * @param type          类型
         */
        @JvmStatic
        fun startCommand(context: Context, type: String) {
            val intent = Intent(context, LockScreenService::class.java)
            intent.action = type
            context.startService(intent)
        }
    }

    private val lockBroadcastReceiver by lazy {
        LockBroadcastReceiver()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return PlayBinder()
    }


    inner class PlayBinder : Binder() {
        val service: LockScreenService
            get() = this@LockScreenService
    }

    override fun onCreate() {
        super.onCreate()
        initLockBroadcastReceiver()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.action != null) {
            when (intent.action) {
                //添加锁屏界面
                LOCK_SCREEN_ACTION -> {
                    Log.e(TAG, "PlayService---LOCK_SCREEN")
                }
                //当屏幕灭了，添加锁屏页面
                Intent.ACTION_SCREEN_OFF -> {
                    requestPermission()
                    Log.e(TAG, "PlayService" + "---当屏幕灭了")
                }
                Intent.ACTION_SCREEN_ON -> Log.e(TAG, "PlayService" + "---当屏幕亮了")
                else -> {
                }
            }
        }
        return Service.START_NOT_STICKY
    }

    /**
     * 初始化IntentFilter添加action意图
     * 主要是监听屏幕亮了与灭了
     */
    private fun initLockBroadcastReceiver() {
        val filter = IntentFilter()
        //来电/耳机
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        //锁屏
        filter.addAction(LOCK_SCREEN_ACTION)
        //当屏幕灭了
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        //当屏幕亮了
        filter.addAction(Intent.ACTION_SCREEN_ON)
        registerReceiver(lockBroadcastReceiver, filter)
    }

    /**
     * 打开锁屏页面，这块伤透了脑筋
     * 不管是播放状态是哪一个，只要屏幕灭了到亮了，就展现这个锁屏页面
     * 有些APP限制了状态，比如只有播放时才走这个逻辑
     */
    private fun startLockAudioActivity() {
        try {
            val lockScreen = Intent(this, SplashActivity::class.java)
            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(lockScreen)
        } catch (e: Exception) {
            Toast.makeText(this, "启动失败", Toast.LENGTH_LONG).show()
        }

    }

    private fun requestPermission() {
        startLockAudioActivity()
    }
}