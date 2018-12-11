package sy.com.initproject.root.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.support.annotation.RequiresApi
import sy.com.initproject.root.AppContext
import kotlin.properties.Delegates


/**
 * Author by Administrator , Date on 2018/12/5.
 * PS: Not easy to write code, please indicate.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
object NetStatusMonitor {

    interface Listener{
        fun onLost()
        fun onAvailable()
        fun onNetStateChange(oldState: Int, newState: Int)
    }

    val WIFI = 1;
    val MOBILE = 2;
    val WIFI_MOBILE = 3;
    val UNKNOW = 0

    var available = false
    var netState: Int by Delegates.observable(UNKNOW) { property, oldValue, newValue ->
        listener?.onNetStateChange(oldValue, newValue)
    }

    private var listener : Listener? = null

    fun setNetStatusListener(listener: Listener){
        this.listener = listener
    }

    init {
        val cm = AppContext.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        fun setType() {
            val activeNetwork = cm.activeNetworkInfo
            val isMobile = activeNetwork.type == ConnectivityManager.TYPE_MOBILE
            val isWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable
            if (isWifi && isMobile)
                netState = WIFI_MOBILE
            else if (isWifi && !isMobile)
                netState = WIFI
            else if (isMobile && !isWifi)
                netState = MOBILE
            else
                netState = UNKNOW
        }

        cm.requestNetwork(NetworkRequest.Builder().build(), object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                available = true
                setType()
                listener?.onAvailable()
            }

            override fun onLost(network: Network?) {
                available = false
                listener?.onLost()
            }
        })

    }
}



