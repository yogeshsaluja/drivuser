package com.thinkincab.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.common.swipe_button.OnStateChangeListener;
import com.thinkincab.app.common.swipe_button.SwipeButton;
import com.thinkincab.app.ui.activity.login.EmailActivity;
import com.thinkincab.app.ui.activity.register.RegisterActivity;
import com.thinkincab.app.data.network.model.WalkThrough;
import com.thinkincab.app.ui.activity.register.RegisterPhoneActivity;
import com.thinkincab.app.ui.activity.social.SocialLoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnBoardActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    private MyViewPagerAdapter adapter;
    private int dotsCount;
    private ImageView[] dots;
    @BindView(R.id.swipeBtnSignin)
    SwipeButton swipeBtnSignIn;
    @BindView(R.id.swipeBtnSignup)
    SwipeButton swipeBtnSignUp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_on_board;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

        List<WalkThrough> list = new ArrayList<>();
        list.add(new WalkThrough(R.drawable.ic_car_horizental,
                getString(R.string.walk_1), getString(R.string.walk_1_description)));
        list.add(new WalkThrough(R.drawable.ic_intro_two,
                getString(R.string.walk_2), getString(R.string.walk_2_description)));
        list.add(new WalkThrough(R.drawable.ic_intro_two_two,
                getString(R.string.walk_3), getString(R.string.walk_3_description)));

        list.add(new WalkThrough(R.drawable.bg_walk_three,
                getString(R.string.walk_3), getString(R.string.walk_4_description)));

        list.add(new WalkThrough(R.drawable.bg_walk_three,
                getString(R.string.walk_3), getString(R.string.walk_5_description)));

        list.add(new WalkThrough(R.drawable.bg_walk_three,
                getString(R.string.walk_3), getString(R.string.walk_6_description)));

        adapter = new MyViewPagerAdapter(this, list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        addBottomDots();
        swipeBtnSignIn.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {

                if (active){
                    swipeBtnSignIn.toggleState();
                    startActivity(new Intent(OnBoardActivity.this, RegisterPhoneActivity.class));

                }

            }
        });
        swipeBtnSignUp.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if (active){
                    swipeBtnSignUp.toggleState();
                    startActivity(new Intent(OnBoardActivity.this, RegisterActivity.class));

                }

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++)
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_unselected));
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_selected));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void addBottomDots() {
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];
        if (dotsCount == 0)
            return;

        layoutDots.removeAllViews();

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 4, 4, 4);

            layoutDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_selected));
    }



    public class MyViewPagerAdapter extends PagerAdapter {
        List<WalkThrough> list;
        Context context;

        MyViewPagerAdapter(Context context, List<WalkThrough> list) {
            this.list = list;
            this.context = context;

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item, container, false);
            WalkThrough walk = list.get(position);

            TextView title = itemView.findViewById(R.id.title);
            TextView description = itemView.findViewById(R.id.description);
            ImageView imageView = itemView.findViewById(R.id.img_pager_item);
            LinearLayout introFour = itemView.findViewById(R.id.ll_intro_four);
            LinearLayout introthree = itemView.findViewById(R.id.ll_intro_three);

            LinearLayout introFive = itemView.findViewById(R.id.ll_intro_five);
            LinearLayout introSix = itemView.findViewById(R.id.ll_intro_six);

            if (position==1){
                imageView.setVisibility(View.GONE);
                introthree.setVisibility(View.VISIBLE);
            }else   if (position==2){
                imageView.setVisibility(View.GONE);
                introFour.setVisibility(View.VISIBLE);
            }else if (position==3){
                imageView.setVisibility(View.GONE);
                introFive.setVisibility(View.VISIBLE);
            }
            else if (position==4){
                imageView.setVisibility(View.GONE);
                introFive.setVisibility(View.VISIBLE);
            }else if (position==5){
                imageView.setVisibility(View.GONE);
                introSix.setVisibility(View.VISIBLE);
            }
            title.setText(walk.title);
            description.setText(walk.description);
            Glide.with(context).load(walk.drawable).into(imageView);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
