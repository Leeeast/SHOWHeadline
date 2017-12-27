package com.sinashow.headline.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caishi.venus.api.bean.news.ChannelInfo;
import com.sinashow.headline.R;

import java.util.List;

/**
 * Created by victorxie on 7/20/15.
 */
public class DragAdapter extends BaseAdapter {
    /** TAG*/
    private final static String TAG = "DragAdapter";
    /** 是否显示底部的ITEM */
    private boolean isItemShow = false;
    private Context context;
    /** 控制的postion */
    private int holdPosition;
    /** 是否改变 */
    private boolean isChanged = false;
    /** 列表数据是否改变 */
    private boolean isListChanged = false;
    /** 是否可见 */
    boolean isVisible = true;
    /** 可以拖动的列表（即用户选择的频道列表） */
    public List<ChannelInfo> channelList;
    /** TextView 频道内容 */
    private TextView item_text;
    /** 要删除的position */
    public int remove_position = -1;

    public DragAdapter(Context context, List<ChannelInfo> channelList) {
        this.context = context;
        this.channelList = channelList;
    }

    @Override
    public int getCount() {
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public ChannelInfo getItem(int position) {
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item, null);
        item_text = (TextView) view;

        ChannelInfo channel = getItem(position);
        item_text.setText(channel.name);
//        item_text.setBackgroundResource(channel.imageId);
//        if (position == 0){
//			item_text.setTextColor(0xFF999999);
//            item_text.setEnabled(false);
//            item_text.setBackgroundResource(R.drawable.bg_channel_disable);
//        }
        if (isChanged && (position == holdPosition) && !isItemShow) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
            isChanged = false;
            item_text.setBackgroundResource(R.drawable.bg_channel_dash);
        }
        if (!isVisible && (position == -1 + channelList.size())) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
            item_text.setBackgroundResource(R.drawable.bg_channel_dash);
        }
        if(remove_position == position){
            item_text.setText("");
            item_text.setBackgroundResource(R.drawable.bg_channel_dash);
        }
        return view;
    }

    /** 添加频道列表 */
    public void addItem(ChannelInfo channel) {
        channelList.add(channel);
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 拖动变更频道排序 */
    public void exchange(int dragPostion, int dropPostion) {
        if (dropPostion != AdapterView.INVALID_POSITION) {
            holdPosition = dropPostion;
            ChannelInfo dragItem = getItem(dragPostion);
            Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
            if (dragPostion < dropPostion) {
                channelList.add(dropPostion + 1, dragItem);
                channelList.remove(dragPostion);
            } else {
                channelList.add(dropPostion, dragItem);
                channelList.remove(dragPostion + 1);
            }
            isChanged = true;
            isListChanged = true;
        }
        remove_position = -1;
        notifyDataSetChanged();
    }

    /** 获取频道列表 */
    public List<ChannelInfo> getChannnelLst() {
        return channelList;
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 设置频道列表 */
    public void setListData(List<ChannelInfo> list) {
        channelList = list;
    }

    /** 获取是否可见 */
    public boolean isVisible() {
        return isVisible;
    }

    /** 排序是否发生改变 */
    public boolean isListChanged() {
        return isListChanged;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    /** 显示放下的ITEM */
    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }

}
