package sy.com.initproject.root.ui.mine;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.pince.frame.BaseFragment;

import sy.com.initproject.R;
import sy.com.initproject.databinding.FragmentMineLayouBinding;

/**
 * @Description:
 * @data：2018/7/17
 * @author: SyShare
 */
public class UserInfoFragment extends BaseFragment<FragmentMineLayouBinding> {
    @Override
    protected int requestLayoutId() {
        return R.layout.fragment_mine_layou;
    }

    @Override
    protected void setViewData(Bundle savedInstanceState) {
        mBinding.tvContent.setAutoLinkMask(Linkify.ALL);
        mBinding.tvContent.setMovementMethod(LinkMovementMethod
                .getInstance());
    }
}
