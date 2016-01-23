package leafmen.tsz.com.bannerexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BannerView extends FrameLayout {
    private ViewPager viewPager;
    private List<ImageView> imageViews;
    private List<ImageView> imageViewDots;
    private LinearLayout linearLayoutDotArea;
    private TextView textViewBanner;

    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(currentItem);
        }
    };

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.banner_view_layout, this);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.vp_banner);
        textViewBanner = (TextView) findViewById(R.id.tv_banner_title);
        linearLayoutDotArea = (LinearLayout) findViewById(R.id.ll_banner_dot_area);
    }

    public void setView(final int[] imageIds, final String[] titles, int count) {
        initImageViews(imageIds, count);
        initImageViewDots(count);

        viewPager.setAdapter(new BannerViewPagerAdapter(imageViews));
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            private int oldPosition = 0;

            @Override
            public void onPageSelected(int position) {
                imageViewDots.get(position).setImageResource(R.drawable.umeng_socialize_follow_on);
                imageViewDots.get(oldPosition).setImageResource(R.drawable.umeng_socialize_follow_off);

                oldPosition = position;
                currentItem = position;

                textViewBanner.setText(titles[position]);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
//                System.out.println("position=" + position);
//                System.out.println("positionOffset=" + positionOffset);
//                System.out.println("positionOffsetPixels="+positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initImageViews(int[] imageIds, int count) {
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(imageIds[i]);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageViews.add(imageView);
        }
    }

    private void initImageViewDots(int count) {
        imageViewDots = new ArrayList<ImageView>();

        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(getContext());
            if (i == 0) {
                imageView.setImageResource(R.drawable.umeng_socialize_follow_on);
            } else {
                android.widget.LinearLayout.LayoutParams params =
                        new android.widget.LinearLayout.LayoutParams(
                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(CommonUtil.dip2px(getContext(), 5), 0, 0, 0);

                imageView.setImageResource(R.drawable.umeng_socialize_follow_off);
                imageView.setLayoutParams(params);

            }
            linearLayoutDotArea.addView(imageView);
            imageViewDots.add(imageView);
        }

    }

    public void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ViewPagerAutoScrollTask(), 3, 3, TimeUnit.SECONDS);
    }

    public void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    private class ViewPagerAutoScrollTask implements Runnable {

        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }
}
