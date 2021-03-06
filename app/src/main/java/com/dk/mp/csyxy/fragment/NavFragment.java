package com.dk.mp.csyxy.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.csyxy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 作者：janabo on 2017/6/8 13:56
 *
 */
public class NavFragment extends BaseFragment implements View.OnClickListener{
    NavigationButton nav_item_cyxx;
    NavigationButton nav_item_ydoa;
    NavigationButton nav_item_cygn;
    NavigationButton nav_item_grzx;

    private Context mContext;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;
    private OnNavigationReselectListener mOnNavigationReselectListener;

    private TextView wdxx;

    public NavFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        nav_item_cyxx = findView(R.id.nav_item_cyxx);
        nav_item_ydoa = findView(R.id.nav_item_ydoa);
        nav_item_cygn = findView(R.id.nav_item_cygn);
        nav_item_grzx = findView(R.id.nav_item_grzx);
        nav_item_cyxx.setOnClickListener(this);
        nav_item_ydoa.setOnClickListener(this);
        nav_item_cygn.setOnClickListener(this);
        nav_item_grzx.setOnClickListener(this);

        nav_item_cyxx.init(R.drawable.tab_icon_cyxx,
                R.string.main_tab_name_cyxx,
                CyxxFragment.class);

        nav_item_ydoa.init(R.drawable.tab_icon_ydoa,
                R.string.main_tab_name_ydoa,
                YdoaFragment.class);

        nav_item_cygn.init(R.drawable.tab_icon_cygn,
                R.string.main_tab_name_cygn,
                CygnFragment.class);

        nav_item_grzx.init(R.drawable.tab_icon_me,
                R.string.main_tab_name_grzx,
                CenterPersonFragment.class);

        wdxx = findView(R.id.wdxx);
//        getWdnum();
    }

    @Override
    public void onResume() {
        super.onResume();

        getWdnum();
    }

    private void getWdnum() {
        HttpUtil.getInstance().postJsonObjectRequest("apps/oa/getWaitCount", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String num = result.getJSONObject("data").getString("waitCount");
                    if (StringUtils.isNotEmpty(num)){
                        if (num.length()>3){
                            wdxx.setVisibility(View.VISIBLE);
                            wdxx.setText("99");
                        }else if (num.equals("0")){
                            wdxx.setVisibility(View.GONE);
                        }else {
                            wdxx.setVisibility(View.VISIBLE);
                            wdxx.setText(num);
                        }
                    }else {
                        wdxx.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    wdxx.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(VolleyError error) {
                wdxx.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        NavigationButton nav = (NavigationButton) view;
        doSelect(nav);
        if(view.getId() == R.id.nav_item_ydoa){
            getWdnum();
        }
    }

    public void setup(Context context, FragmentManager fragmentManager, int contentId, OnNavigationReselectListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnNavigationReselectListener = listener;
        clearOldFragment();
        doSelect(nav_item_cyxx);
    }

    public void select(int index) {
        if (nav_item_grzx != null)
            doSelect(nav_item_grzx);
    }

    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0)
            return;
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != this) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit)
            transaction.commitNow();
    }

    private void doSelect(NavigationButton newNavButton) {
        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                onReselect(oldNavButton);
                return;
            }
            oldNavButton.setSelected(false);
        }
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        mCurrentNavButton = newNavButton;

    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx().getName(), null);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }

    private void onReselect(NavigationButton navigationButton) {
        OnNavigationReselectListener listener = mOnNavigationReselectListener;
        if (listener != null) {
            listener.onReselect(navigationButton);
        }
    }

    public interface OnNavigationReselectListener {
        void onReselect(NavigationButton navigationButton);
    }


}
