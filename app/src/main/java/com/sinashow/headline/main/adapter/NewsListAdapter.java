//
// Source code recreated from a .class file by IntelliJ IDEA
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

import com.caishi.venus.R.id;
import com.caishi.venus.R.layout;
import com.caishi.venus.R.mipmap;
import com.caishi.venus.api.bean.news.ImageInfo;
import com.caishi.venus.api.bean.news.NewsInfo;
import com.caishi.venus.api.misc.DateUtil;
import com.caishi.venus.image.ImageLoader;
import com.caishi.venus.ui.config.NewsSettings;
import com.caishi.venus.ui.config.NewsSettings.LAYOUT_TYPE;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends BaseAdapter {
    private final Activity a;
    private final LayoutInflater b;
    private final List<NewsInfo> c = new ArrayList();
    private final ImageLoader d;
    private static final NewsInfo e = new NewsInfo();

    public NewsListAdapter(Activity var1) {
        this.a = var1;
        this.b = this.a.getLayoutInflater();
        this.d = ImageLoader.getInstance();
    }

    public int getCount() {
        return this.c.size();
    }

    public NewsInfo getItem(int var1) {
        return (NewsInfo) this.c.get(var1);
    }

    public long getItemId(int var1) {
        return (long) var1;
    }

    public int getViewTypeCount() {
        return 7;
    }

    public int getItemViewType(int var1) {
        NewsInfo var2 = (NewsInfo) this.c.get(var1);
        // return var2.isNews()?(var2.isBigPic()?0:1):(var2.isJoke()?2:(var2.isGif()?3:(var2.isRefreshBar()?4:(var2.isThreePic()?5:(var2.isAd()?6:1)))));
        return var2.isNews() ? (var2.isBigPic() ? 0 : (var2.isGif() ? 3 : (var2.isRefreshBar() ? 4 : (var2.isThreePic() ? 5 : (var2.isAd() ? 6 : 1))))) : (var2.isJoke() ? 2 : (var2.isGif() ? 3 : (var2.isRefreshBar() ? 4 : (var2.isThreePic() ? 5 : (var2.isAd() ? 6 : 1)))));
    }

    public View getView(int var1, View var2, ViewGroup var3) {
        int var4 = this.getItemViewType(var1);
        NewsInfo var5 = (NewsInfo) this.c.get(var1);
        switch (var4) {
            case 0:
                return this.d(var5, var2, var1, var3);
            case 1:
                return this.e(var5, var2, var1, var3);
            case 2:
                return this.c(var5, var2, var1, var3);
            case 3:
                return this.e(var5, var2, var1, var3);
            case 4:
                return this.b.inflate(layout.news_item_refresh_bar, (ViewGroup) null);
            case 5:
                return this.b(var5, var2, var1, var3);
            case 6:
                return this.a(var5, var2, var1, var3);
            default:
                return var2;
        }
    }

    private View a(NewsInfo var1, View var2, int var3, ViewGroup var4) {
        NewsListAdapter.a var5;
        if (var2 == null) {
            var2 = this.b.inflate(layout.news_item_ad_large, var4, false);
            var5 = new NewsListAdapter.a(var2);
            var2.setTag(var5);
        } else {
            var5 = (NewsListAdapter.a) var2.getTag();
        }

        if (!TextUtils.isEmpty(var1.title)) {
            var5.f.setVisibility(View.VISIBLE);
            var5.f.setText(var1.title);
        } else {
            var5.f.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(var1.source)) {
            var5.b.setVisibility(View.VISIBLE);
            var5.b.setText(var1.source);
            var5.d.setVisibility(View.GONE);
        } else {
            var5.b.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(var1.sourceImageUrl)) {
                var5.d.setVisibility(View.VISIBLE);
                this.d.initImageView(var5.d, var1.sourceImageUrl);
            }
        }

        if (!TextUtils.isEmpty(var1.imageUrl)) {
            this.d.initImageView(var5.c, var1.imageUrl);
        }

        return var2;
    }

    private View b(NewsInfo var1, View var2, int var3, ViewGroup var4) {
        NewsListAdapter.c var5;
        if (var2 == null) {
            var2 = this.b.inflate(layout.news_item_three_picture, var4, false);
            var5 = new NewsListAdapter.c();
            ViewStub var6 = (ViewStub) var2.findViewById(id.news_item_image);
            ViewStub var7 = (ViewStub) var2.findViewById(id.img_news_item_3_pic_2);
            ViewStub var8 = (ViewStub) var2.findViewById(id.img_news_item_3_pic_3);
            var5.a = this.d.createImageView(var6);
            var5.b = this.d.createImageView(var7);
            var5.c = this.d.createImageView(var8);
            this.a((View) var2, (NewsListAdapter.d) var5);
            var2.setTag(var5);
        } else {
            var5 = (NewsListAdapter.c) var2.getTag();
        }

        if (var1.imageDTOList != null && var1.imageDTOList.size() > 0) {
            if (((ImageInfo) var1.imageDTOList.get(0)).url != null) {
                this.d.initImageView(var5.a, ((ImageInfo) var1.imageDTOList.get(0)).url);
            }

            if (var1.imageDTOList.size() > 1 && ((ImageInfo) var1.imageDTOList.get(1)).url != null) {
                this.d.initImageView(var5.b, ((ImageInfo) var1.imageDTOList.get(1)).url);
            }

            if (var1.imageDTOList.size() > 2 && ((ImageInfo) var1.imageDTOList.get(2)).url != null) {
                this.d.initImageView(var5.c, ((ImageInfo) var1.imageDTOList.get(2)).url);
            }
        }

        var5.f.setText(TextUtils.isEmpty(var1.summary) ? var1.title : var1.summary);
        this.a((NewsListAdapter.d) var5, (NewsInfo) var1);
        return var2;
    }

    private View c(NewsInfo var1, View var2, int var3, ViewGroup var4) {
        NewsListAdapter.d var5;
        if (var2 == null) {
            var2 = this.b.inflate(layout.news_item_joke_view, var4, false);
            var5 = new NewsListAdapter.d();
            this.a(var2, var5);
            var2.setTag(var5);
        } else {
            var5 = (NewsListAdapter.d) var2.getTag();
        }

        var5.f.setText(TextUtils.isEmpty(var1.summary) ? var1.title : var1.summary);
        this.a(var5, var1);
        return var2;
    }

    private View d(NewsInfo var1, View var2, int var3, ViewGroup var4) {
        NewsListAdapter.b var5;
        if (var2 == null) {
            var2 = this.b.inflate(layout.news_item_big_picture, var4, false);
            var5 = new NewsListAdapter.b();
            ViewStub var6 = (ViewStub) var2.findViewById(id.img_news_item_picture);
            var5.a = this.d.createImageView(var6);
            this.a((View) var2, (NewsListAdapter.d) var5);
            var2.setTag(var5);
        } else {
            var5 = (NewsListAdapter.b) var2.getTag();
        }

        if (var1.imageDTOList != null && var1.imageDTOList.size() > 0) {
            this.d.initImageView(var5.a, ((ImageInfo) var1.imageDTOList.get(0)).url);
        }

        this.b(var5, var1);
        this.a((NewsListAdapter.d) var5, (NewsInfo) var1);
        return var2;
    }

    private View e(NewsInfo var1, View var2, int var3, ViewGroup var4) {
        NewsListAdapter.b var5;
        if (var2 == null) {
            var2 = this.b.inflate(NewsSettings.sLayoutType == LAYOUT_TYPE.LAYOUT_TYPE_IMAGE_TEXT ? layout.news_item_image_text : layout.news_item_text_image, var4, false);
            var5 = new NewsListAdapter.b();
            ViewStub var6 = (ViewStub) var2.findViewById(id.img_news_item_picture);
            var5.a = this.d.createImageView(var6);
            this.a((View) var2, (NewsListAdapter.d) var5);
            var2.setTag(var5);
        } else {
            var5 = (NewsListAdapter.b) var2.getTag();
        }

        if (var1.imageDTOList != null && var1.imageDTOList.size() > 0) {
            this.d.initImageView(var5.a, ((ImageInfo) var1.imageDTOList.get(0)).url);
        }

        this.b(var5, var1);
        this.a((NewsListAdapter.d) var5, (NewsInfo) var1);
        return var2;
    }

    private void a(View var1, NewsListAdapter.d var2) {
        var2.f = (TextView) var1.findViewById(id.txt_news_item_title);
        var2.g = (ImageView) var1.findViewById(id.img_news_item_marker);
        var2.h = (TextView) var1.findViewById(id.txt_news_item_time);
        var2.e = (TextView) var1.findViewById(id.txt_news_item_source);
    }

    private void a(NewsListAdapter.d var1, NewsInfo var2) {
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

    private void b(NewsListAdapter.d var1, NewsInfo var2) {
        var1.f.setText(TextUtils.isEmpty(var2.title) ? var2.summary : var2.title);
    }

    public void setNewsIsRead(Object var1) {
        if (var1 instanceof NewsListAdapter.d) {
            NewsListAdapter.d var2 = (NewsListAdapter.d) var1;
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
                    this.c.add(0, var2);
                    ++var3;
                }
            }

            if (var3 > 0) {
                if (this.c.size() > var3) {
                    this.c.remove(e);
                    this.c.add(var3, e);
                }

                this.notifyDataSetChanged();
            }
        }

    }

    public void appendDataList(List<NewsInfo> var1) {
        if (var1 != null && var1.size() > 0) {
            this.c.addAll(var1);
        }

    }

    public boolean isNewsInfoValid(NewsInfo var1) {
        return var1.isAd() || !TextUtils.isEmpty(var1.title) || !TextUtils.isEmpty(var1.summary);
    }

    static {
        e.layoutType = "REFRESH_BAR";
    }

    static class a extends NewsListAdapter.d {
        View a;
        TextView b;
        SimpleDraweeView c;
        SimpleDraweeView d;

        public a(View var1) {
            this.a = var1;
            this.f = (TextView) var1.findViewById(id.txt_news_item_title);
            this.b = (TextView) var1.findViewById(id.txt_news_item_source);
            this.c = (SimpleDraweeView) var1.findViewById(id.img_news_item_picture);
            this.d = (SimpleDraweeView) var1.findViewById(id.img_news_item_picture_source);
        }
    }

    static class c extends NewsListAdapter.d {
        View a;
        View b;
        View c;

        c() {
        }
    }

    static class b extends NewsListAdapter.d {
        View a;

        b() {
        }
    }

    static class d {
        TextView e;
        TextView f;
        ImageView g;
        TextView h;

        d() {
        }
    }
}
