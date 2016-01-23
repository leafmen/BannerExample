/**
 * @author: LiQiang
 * @Email:leafmen@126.com
 * @QQ: 49929500
 * Create at 2016/1/24
 **/
package leafmen.tsz.com.bannerexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

public class MainActivity extends AppCompatActivity {

    BannerView bannerview;
    int[] imageIds;
    String[] titles;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bannerview = (BannerView) findViewById(R.id.bannerview);
        imageIds = new int[]{R.drawable.im1, R.drawable.im2, R.drawable.im3, R.drawable.im4};
        titles = new String[]{"那瞬间的美", "清冷的夏日", "炽热的烈心", "眼神的朦胧"};
        count = imageIds.length;
        bannerview.setView(imageIds, titles, count);
        bannerview.startPlay();
        getScreenPixels();


    }

    private void getScreenPixels() {
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        System.out.println("屏幕宽度: " + screenWidth + "\n屏幕高度： " + screenHeight);
    }
}
