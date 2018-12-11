package sy.com.initproject.root.lockscreenad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Author by Administrator , Date on 2018/11/30.
 * PS: Not easy to write code, please indicate.
 */
class LockBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        if (action != null && action.isNotEmpty()) {
            when (action) {
                //锁屏时处理的逻辑
                LockScreenService.LOCK_SCREEN_ACTION -> {
                    LockScreenService.startCommand(context, LockScreenService.LOCK_SCREEN_ACTION)
                    Log.e(LockScreenService.TAG,"AudioBroadcastReceiver" + "---LOCK_SCREEN")
                }
                //当屏幕灭了
                Intent.ACTION_SCREEN_OFF -> {
                    LockScreenService.startCommand(context, Intent.ACTION_SCREEN_OFF)
                    Log.e(LockScreenService.TAG,"AudioBroadcastReceiver" + "---当屏幕灭了")
                }
                //当屏幕亮了
                Intent.ACTION_SCREEN_ON -> {
                    LockScreenService.startCommand(context, Intent.ACTION_SCREEN_ON)
                    Log.e(LockScreenService.TAG,"AudioBroadcastReceiver" + "---当屏幕亮了")
                }
                else -> {
                }
            }
        }

    }
}