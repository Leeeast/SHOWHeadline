package com.sinashow.headline.main;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.GenericDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.sinashow.headline.R;

import java.util.ArrayList;

public class ImageShowActivity extends AppCompatActivity {
    public static final String KEY_URL_LIST = "urlList";
    public static final String KEY_SELECTED_INDEX = "selectedIndex";
    private int selected = 0;
    private ArrayList<String> imgsUrl;
    private ViewPager image_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initView();
        initData();
        initViewPager();
    }

    private void initData() {
        Intent intent = getIntent();
        imgsUrl = intent.getStringArrayListExtra(KEY_URL_LIST);
        selected = intent.getIntExtra(KEY_SELECTED_INDEX, 0);

    }

    private void initView() {
        image_pager = (ViewPager) findViewById(R.id.pager_image_show);
        image_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initViewPager() {
        if (imgsUrl != null && imgsUrl.size() != 0) {
            PagerAdapter adapter = new PagerAdapter() {

                LayoutInflater inflater = LayoutInflater.from(ImageShowActivity.this);

                @Override
                public int getCount() {
                    return imgsUrl.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {

                    GenericDraweeView v = (GenericDraweeView) inflater.inflate(R.layout.image_show_item, null);
                    setImageUrl(v,imgsUrl.get(position));
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    container.addView(v);
                    return v;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            };
            image_pager.setAdapter(adapter);
            image_pager.setCurrentItem(selected);
        }
    }

    public void setImageUrl(GenericDraweeView draweeView, String url) {


        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions(2048, 2048))
                .build();
        ControllerListener listener = new BaseControllerListener<Object>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable Object imageInfo,
                    @Nullable Animatable animatable) {

            }

            @Override
            public void onRelease(String id) {

            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setRetainImageOnFailure(true)
                .setAutoPlayAnimations(true)
                .setControllerListener(listener)
                .build();
        draweeView.setController(controller);
    }


}
