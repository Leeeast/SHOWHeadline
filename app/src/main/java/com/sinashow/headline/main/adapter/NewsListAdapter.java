//
// Source code recreated from imageView .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sinashow.headline.main.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caishi.venus.R.mipmap;
import com.caishi.venus.api.bean.news.ImageInfo;
import com.caishi.venus.api.bean.news.NewsInfo;
import com.caishi.venus.api.misc.DateUtil;
import com.caishi.venus.image.ImageLoader;
import com.caishi.venus.ui.config.NewsSettings;
import com.caishi.venus.ui.config.NewsSettings.LAYOUT_TYPE;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sinashow.headline.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsListAdapter extends BaseAdapter {
    private final Activity mActivity;
    private final LayoutInflater mLayoutInflater;
    private final List<NewsInfo> newsInfoList = new ArrayList();
    private final ImageLoader mImageLoader;
    private static final NewsInfo NEWS_INFO = new NewsInfo();
    private Random mRandom;

    public NewsListAdapter(Activity activity) {
        this.mActivity = activity;
        this.mLayoutInflater = this.mActivity.getLayoutInflater();
        this.mImageLoader = ImageLoader.getInstance();
        this.mRandom = new Random();
    }

    public int getCount() {
        return this.newsInfoList.size();
    }

    public NewsInfo getItem(int position) {
        return newsInfoList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount() {
        return 7;
    }

    public int getItemViewType(int position) {
        NewsInfo newsInfo = (NewsInfo) this.newsInfoList.get(position);
        //return newsInfo.isNews() ? (newsInfo.isBigPic() ? 0 : 1) : (newsInfo.isJoke() ? 2 : (newsInfo.isGif() ? 3 : (newsInfo.isRefreshBar() ? 4 : (newsInfo.isThreePic() ? 5 : (newsInfo.isAd() ? 6 : 1)))));
        if (newsInfo.isRefreshBar()) {
            return 4;
        } else {
            if (position % 2 == 0) {
                return 0;
            } else if (position % 5 == 0) {
                return 2;
            } else if (position % 7 == 0) {
                return 5;
            } else {
                return 1;
            }
//            position = mRandom.nextInt(100);
//            if (position % 2 == 0) {
//                return 0;
//            } else if (position % 5 == 0) {
//                return 2;
//            } else if (position % 7 == 0) {
//                return 5;
//            } else if (position % 9 == 0) {
//                return 6;
//            } else {
//                return 1;
//            }
        }
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        NewsInfo newsInfo = (NewsInfo) this.newsInfoList.get(position);
        switch (viewType) {
            case 0:
                return this.inflateTypeBigPic(newsInfo, convertView, position, parent);
            case 1:
                return this.inflateTypeTxtImg(newsInfo, convertView, position, parent);
            case 2:
                return this.inflateTypeJoke(newsInfo, convertView, position, parent);
            case 3:
                return this.inflateTypeTxtImg(newsInfo, convertView, position, parent);
            case 4:
                return this.mLayoutInflater.inflate(R.layout.news_item_refresh_bar, (ViewGroup) null);
            case 5:
                return this.inflateTypeThreePic(newsInfo, convertView, position, parent);
            case 6:
                return this.inflateTypeAdLarge(newsInfo, convertView, position, parent);
            default:
                return convertView;
        }
    }

    private View inflateTypeAdLarge(NewsInfo newsInfo, View convertView, int position, ViewGroup parent) {
        NewsListAdapter.a var5;
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.news_item_ad_large, parent, false);
            var5 = new NewsListAdapter.a(convertView);
            convertView.setTag(var5);
        } else {
            var5 = (NewsListAdapter.a) convertView.getTag();
        }

        if (!TextUtils.isEmpty(newsInfo.title)) {
            var5.f.setVisibility(View.VISIBLE);
            var5.f.setText(newsInfo.title);
        } else {
            var5.f.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(newsInfo.source)) {
            var5.b.setVisibility(View.VISIBLE);
            var5.b.setText(newsInfo.source);
            var5.d.setVisibility(View.GONE);
        } else {
            var5.b.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(newsInfo.sourceImageUrl)) {
                var5.d.setVisibility(View.VISIBLE);
                this.mImageLoader.initImageView(var5.d, newsInfo.sourceImageUrl);
            }
        }

        if (!TextUtils.isEmpty(newsInfo.imageUrl)) {
            this.mImageLoader.initImageView(var5.c, newsInfo.imageUrl);
        }

        return convertView;
    }

    private View inflateTypeThreePic(NewsInfo newsInfo, View convertView, int position, ViewGroup parent) {
        NewsListAdapter.c var5;
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.news_item_three_picture, parent, false);
            var5 = new NewsListAdapter.c();
            ViewStub var6 = (ViewStub) convertView.findViewById(R.id.news_item_image);
            ViewStub var7 = (ViewStub) convertView.findViewById(R.id.img_news_item_3_pic_2);
            ViewStub var8 = (ViewStub) convertView.findViewById(R.id.img_news_item_3_pic_3);
            var5.a = this.mImageLoader.createImageView(var6);
            var5.b = this.mImageLoader.createImageView(var7);
            var5.c = this.mImageLoader.createImageView(var8);
            this.a((View) convertView, (NewsHolder) var5);
            convertView.setTag(var5);
        } else {
            var5 = (NewsListAdapter.c) convertView.getTag();
        }

        if (newsInfo.imageDTOList != null && newsInfo.imageDTOList.size() > 0) {
            if (((ImageInfo) newsInfo.imageDTOList.get(0)).url != null) {
                this.mImageLoader.initImageView(var5.a, ((ImageInfo) newsInfo.imageDTOList.get(0)).url);
            }

            if (newsInfo.imageDTOList.size() > 1 && ((ImageInfo) newsInfo.imageDTOList.get(1)).url != null) {
                this.mImageLoader.initImageView(var5.b, ((ImageInfo) newsInfo.imageDTOList.get(1)).url);
            }

            if (newsInfo.imageDTOList.size() > 2 && ((ImageInfo) newsInfo.imageDTOList.get(2)).url != null) {
                this.mImageLoader.initImageView(var5.c, ((ImageInfo) newsInfo.imageDTOList.get(2)).url);
            }
        }

        var5.f.setText(TextUtils.isEmpty(newsInfo.summary) ? newsInfo.title : newsInfo.summary);
        this.a((NewsHolder) var5, (NewsInfo) newsInfo);
        return convertView;
    }

    private View inflateTypeJoke(NewsInfo newsInfo, View convertView, int position, ViewGroup parent) {
        NewsHolder var5;
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.news_item_joke_view, parent, false);
            var5 = new NewsHolder();
            this.a(convertView, var5);
            convertView.setTag(var5);
        } else {
            var5 = (NewsHolder) convertView.getTag();
        }

        var5.f.setText(TextUtils.isEmpty(newsInfo.summary) ? newsInfo.title : newsInfo.summary);
        this.a(var5, newsInfo);
        return convertView;
    }

    private View inflateTypeBigPic(NewsInfo newsInfo, View convertView, int position, ViewGroup parent) {
        PicHolder bigPicHolder;
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.news_item_big_picture, parent, false);
            bigPicHolder = new PicHolder();
            ViewStub viewStub = (ViewStub) convertView.findViewById(R.id.img_news_item_picture);
            bigPicHolder.imageView = this.mImageLoader.createImageView(viewStub);
            this.a(convertView, bigPicHolder);
            convertView.setTag(bigPicHolder);
        } else {
            bigPicHolder = (PicHolder) convertView.getTag();
        }

        if (newsInfo.imageDTOList != null && newsInfo.imageDTOList.size() > 0) {
            this.mImageLoader.initImageView(bigPicHolder.imageView, ((ImageInfo) newsInfo.imageDTOList.get(0)).url);
        }

        this.b(bigPicHolder, newsInfo);
        this.a((NewsHolder) bigPicHolder, (NewsInfo) newsInfo);
        return convertView;
    }

    private View inflateTypeTxtImg(NewsInfo newsInfo, View convertView, int position, ViewGroup parent) {
        PicHolder var5;
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(NewsSettings.sLayoutType == LAYOUT_TYPE.LAYOUT_TYPE_IMAGE_TEXT ? R.layout.news_item_image_text : R.layout.news_item_text_image, parent, false);
            var5 = new PicHolder();
            ViewStub var6 = (ViewStub) convertView.findViewById(R.id.img_news_item_picture);
            var5.imageView = this.mImageLoader.createImageView(var6);
            this.a((View) convertView, (NewsHolder) var5);
            convertView.setTag(var5);
        } else {
            var5 = (PicHolder) convertView.getTag();
        }

        if (newsInfo.imageDTOList != null && newsInfo.imageDTOList.size() > 0) {
            this.mImageLoader.initImageView(var5.imageView, ((ImageInfo) newsInfo.imageDTOList.get(0)).url);
        }

        this.b(var5, newsInfo);
        this.a((NewsHolder) var5, (NewsInfo) newsInfo);
        return convertView;
    }

    private void a(View var1, NewsHolder var2) {
        var2.f = (TextView) var1.findViewById(R.id.txt_news_item_title);
        var2.g = (ImageView) var1.findViewById(R.id.img_news_item_marker);
        var2.h = (TextView) var1.findViewById(R.id.txt_news_item_time);
        var2.e = (TextView) var1.findViewById(R.id.txt_news_item_source);
    }

    private void a(NewsHolder var1, NewsInfo var2) {
        if (var2 == null) return;
        var1.g.setVisibility(View.VISIBLE);
        var1.e.setText(var2.origin);
        var1.h.setText(DateUtil.getDateDiff(var2.publishTime));
        if (var2.isJoke()) {
            var1.g.setImageResource(mipmap.news_item_duanzi);
        } else if (var2.isGif()) {
            var1.g.setImageResource(mipmap.news_item_gif);
        } else if (var2.isOrigin()) {
            var1.g.setImageResource(mipmap.news_item_origin);
        } else if (var2.isBigPic()) {
            var1.g.setImageResource(mipmap.news_item_pic);
        } else {
            var1.g.setVisibility(View.GONE);
        }

        if (var2.isRead) {
            this.setNewsIsRead(var1);
        } else {
            var1.f.setTextColor(-13421773);
            var1.h.setTextColor(-6710887);
            var1.e.setTextColor(-6710887);
        }

    }

    private void b(NewsHolder var1, NewsInfo var2) {
        var1.f.setText(TextUtils.isEmpty(var2.title) ? var2.summary : var2.title);
    }

    public void setNewsIsRead(Object var1) {
        if (var1 instanceof NewsHolder) {
            NewsHolder var2 = (NewsHolder) var1;
            if (var2.f == null || var2.h == null || var2.e == null) return;
            var2.f.setTextColor(-6710887);
            var2.h.setTextColor(-6710887);
            var2.e.setTextColor(-6710887);
        }

    }

    public void addNewsList(List<NewsInfo> var1) {
        if (var1 != null && var1.size() > 0) {
            int var3 = 0;

            for (int var4 = var1.size() - 1; var4 >= 0; --var4) {
                NewsInfo var2 = (NewsInfo) var1.get(var4);
                if (this.isNewsInfoValid(var2)) {
                    this.newsInfoList.add(0, var2);
                    ++var3;
                }
            }

            if (var3 > 0) {
                if (this.newsInfoList.size() > var3) {
                    this.newsInfoList.remove(NEWS_INFO);
                    this.newsInfoList.add(var3, NEWS_INFO);
                }

                this.notifyDataSetChanged();
            }
        }

    }

    public void appendDataList(List<NewsInfo> var1) {
        if (var1 != null && var1.size() > 0) {
            this.newsInfoList.addAll(var1);
        }

    }

    public boolean isNewsInfoValid(NewsInfo var1) {
        return var1.isAd() || !TextUtils.isEmpty(var1.title) || !TextUtils.isEmpty(var1.summary);
    }

    static {
        NEWS_INFO.layoutType = "REFRESH_BAR";
    }

    static class a extends NewsHolder {
        View a;
        TextView b;
        SimpleDraweeView c;
        SimpleDraweeView d;

        public a(View var1) {
            this.a = var1;
            this.f = (TextView) var1.findViewById(R.id.txt_news_item_title);
            this.b = (TextView) var1.findViewById(R.id.txt_news_item_source);
            this.c = (SimpleDraweeView) var1.findViewById(R.id.img_news_item_picture);
            this.d = (SimpleDraweeView) var1.findViewById(R.id.img_news_item_picture_source);
        }
    }

    static class c extends NewsHolder {
        View a;
        View b;
        View c;

        c() {
        }
    }

    static class PicHolder extends NewsHolder {
        View imageView;

        PicHolder() {
        }
    }

    static class NewsHolder {
        TextView e;
        TextView f;
        ImageView g;
        TextView h;

        NewsHolder() {
        }
    }
}
