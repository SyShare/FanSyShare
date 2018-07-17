package sy.com.initproject.root.widgets;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pince.ut.NetUtil;

import sy.com.initproject.R;
import sy.com.initproject.databinding.ViewCustomEmptyBinding;
import sy.com.initproject.root.AppContext;

/**
 * Created by Administrator on 2017/7/25.
 */

public class EmptyCustomView extends LinearLayout implements View.OnClickListener {

    public static final int HIDE_LAYOUT = 3;
    public static final int NETWORK_ERROR = 1;
    public static final int NODATA = 2;
    public static final int NODATA_ENABLE_CLICK = 4;

    private boolean clickEnable = true;

    private OnClickListener listener;
    private int mErrorState;
    private String strNoDataContent = "";

    ViewCustomEmptyBinding binding;

    public EmptyCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public EmptyCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyCustomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        binding = ViewCustomEmptyBinding.inflate(LayoutInflater.from(context), this, true);
        setVisibility(GONE);

        setBackgroundColor(-1);
        setOnClickListener(this);
        binding.img.setOnClickListener(this);
    }

    /**
     * 设置背景
     */
    public void setEmptyIcon(int imgResource) {
        binding.img.setImageResource(imgResource);
    }

    /**
     * 设置内容
     */
    public void setEmptyTips(String noDataContent) {
        strNoDataContent = noDataContent;
        if (binding.emptyText != null) {
            binding.emptyText.setText(strNoDataContent);
        }
    }

    /**
     * 设置内容
     */
    public void setEmptyTips(@StringRes int tipsRes) {
        setEmptyTips(getContext().getString(tipsRes));
    }

    /**
     * 根据状态设置当前view
     *
     * @param i
     */
    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        final boolean disconnected = !NetUtil.isNetworkAvailable(getContext());
        if (disconnected) {
            i = NETWORK_ERROR;
        }
        switch (i) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                binding.emptyText.setText(R.string.network_disconnected_tips);
                binding.img.setBackgroundResource(R.drawable.pic_empty_network);
                binding.img.setVisibility(View.VISIBLE);
                clickEnable = true;
                break;
            case NODATA:
                mErrorState = NODATA;
                binding.img.setBackgroundResource(R.drawable.pic_empty);
                binding.img.setVisibility(View.VISIBLE);
                refreshEmptyView();
                clickEnable = true;
                break;
            case HIDE_LAYOUT:
                setVisibility(View.GONE);
                break;
            case NODATA_ENABLE_CLICK:
                mErrorState = NODATA_ENABLE_CLICK;
                binding.img.setBackgroundResource(R.drawable.pic_empty);
                binding.img.setVisibility(View.VISIBLE);
                refreshEmptyView();
                clickEnable = true;
                break;
            default:
                break;
        }
    }

    private void refreshEmptyView() {
        binding.emptyText.setText(TextUtils.isEmpty(strNoDataContent) ?
                getContext().getString(R.string.list_empty) : strNoDataContent);
    }

    /**
     * 获取当前错误状态
     *
     * @return
     */
    public int getErrorState() {
        return mErrorState;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        if (clickEnable) {
            if (mErrorState == NETWORK_ERROR) {

                Toast.makeText(AppContext.getContext(), "TODO, Network Settings", Toast.LENGTH_LONG).show();
                //SystemSettingsUtils.launchWirelessSettings(getContext(), true);
                return;
            }
            if (listener != null) {
                listener.onClick(v);
            }
        }
    }
}
