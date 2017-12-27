package com.sinashow.headline.widget;
/**
 * Created by  Monster on 2017/4/20.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caishi.venus.api.bean.news.NewsInfo;
import com.caishi.venus.api.misc.DateUtil;
import com.caishi.venus.image.ImageLoader;
import com.caishi.venus.ui.config.NewsSettings;

import java.util.ArrayList;
import java.util.List;


public class NoTabAdapter extends BaseAdapter {

    private static final int COLOR_READ = 0xFF999999;
    private static final int COLOR_NEW = 0xFF333333;
    private LayoutInflater mInflater;
    private Context mContext;
    private final List<NewsInfo> mNewsList=new ArrayList<>();
    private ImageLoader mImageLoader;
    private static final NewsInfo REFRESH_BAR = new NewsInfo();

    public NoTabAdapter(Context context) {

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mImageLoader = ImageLoader.getInstance();
//        mNewsList.addAll(mList);
    }


    @Override
    public int getCount() {
        return mNewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NewsInfo newsInfo = mNewsList.get(position);
        MyViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    NewsSettings.sLayoutType == NewsSettings.LAYOUT_TYPE.LAYOUT_TYPE_IMAGE_TEXT
                            ? com.caishi.venus.R.layout.news_item_image_text
                            : com.caishi.venus.R.layout.news_item_text_image,
                    parent,
                    false);
            holder = new MyViewHolder();
            ViewStub stub = (ViewStub) convertView.findViewById(com.caishi.venus.R.id.img_news_item_picture);
            holder.image = mImageLoader.createImageView(stub);
            initViewHolder(convertView, holder);
            convertView.setTag(holder);
            }
            else {
                holder = (MyViewHolder) convertView.getTag();
            }

        if (newsInfo.imageDTOList != null && newsInfo.imageDTOList.size() > 0) {
            mImageLoader.initImageView(holder.image, newsInfo.imageDTOList.get(0).url);
        }
        setTitle(holder, newsInfo);
        setNewsStatus(holder, newsInfo);
        return convertView;

}

    private class MyViewHolder {
        TextView origin;
        TextView title;
        ImageView marker;
        TextView time;
        View image;

    }
    public void initViewHolder(View convertView, MyViewHolder holder) {
        holder.title = (TextView) convertView.findViewById(com.caishi.venus.R.id.txt_news_item_title);
        holder.marker = (ImageView) convertView.findViewById(com.caishi.venus.R.id.img_news_item_marker);
        holder.time = (TextView) convertView.findViewById(com.caishi.venus.R.id.txt_news_item_time);
        holder.origin = (TextView) convertView.findViewById(com.caishi.venus.R.id.txt_news_item_source);
    }
    private void setTitle(MyViewHolder holder, NewsInfo newsInfo) {
        holder.title.setText(TextUtils.isEmpty(newsInfo.title) ? newsInfo.summary : newsInfo.title);
    }
    private void setNewsStatus(MyViewHolder holder, NewsInfo newsInfo) {

//        holder.marker.setVisibility(View.VISIBLE);

        holder.origin.setText(newsInfo.origin);
        holder.time.setText(DateUtil.getDateDiff(newsInfo.publishTime));

//        if (newsInfo.isJoke()) {
//            holder.marker.setImageResource(com.caishi.venus.R.mipmap.news_item_duanzi);
//
//        } else if (newsInfo.isGif()) {
//            holder.marker.setImageResource(com.caishi.venus.R.mipmap.news_item_gif);
//
//        } else if (newsInfo.isOrigin()) {
//            holder.marker.setImageResource(com.caishi.venus.R.mipmap.news_item_origin);
//
//        } else if (newsInfo.isBigPic()) {
//            holder.marker.setImageResource(com.caishi.venus.R.mipmap.news_item_pic);
//
//        } else {
//            holder.marker.setVisibility(View.GONE);
//        }

        if (newsInfo.isRead) {
            setNewsIsRead(holder);
        } else {
            holder.title.setTextColor(COLOR_NEW);
            holder.time.setTextColor(COLOR_NEW);
            holder.origin.setTextColor(COLOR_NEW);
        }
    }
    public void setNewsIsRead(Object tag) {
        if (tag instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) tag;
            holder.title.setTextColor(COLOR_READ);
            holder.time.setTextColor(COLOR_READ);
            holder.origin.setTextColor(COLOR_READ);
        }
    }
    /**
     * 将新闻列表插到当前新闻列表的前面，若非首次加载数据，则会添加刷新条
     *
     * @param newsList
     */
    public void addNewsList(List<NewsInfo> newsList) {

        if (newsList != null && newsList.size() > 0) {
            NewsInfo info;
            int count = 0;
            for (int i = newsList.size() - 1; i >= 0; i--) {
                info = newsList.get(i);
                if (isNewsInfoValid(info)) {
                    // 过滤掉title或summary为空的新闻
                    mNewsList.add(0, info);
                    count++;
                }
            }
            if (count > 0) {
                if (mNewsList.size() > count) {
                    // 非首次添加数据，才加刷新条
                    mNewsList.remove(REFRESH_BAR);
                    mNewsList.add(count, REFRESH_BAR);
                }
                notifyDataSetChanged();
            }
        }
    }
    public void appendDataList(List<NewsInfo> list) {

        if (list != null && list.size() > 0) {
            mNewsList.addAll(list);
        }
    }

    public boolean isNewsInfoValid(NewsInfo newsInfo) {

        return !TextUtils.isEmpty(newsInfo.title) || !TextUtils.isEmpty(newsInfo.summary);
    }

}
