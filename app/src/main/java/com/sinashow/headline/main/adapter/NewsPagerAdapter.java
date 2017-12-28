//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sinashow.headline.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.caishi.venus.api.bean.news.ChannelInfo;
import com.sinashow.headline.main.fragment.NewsFragment;

import java.util.HashMap;
import java.util.List;

public class NewsPagerAdapter extends FragmentPagerAdapter {
    private final List<ChannelInfo> a;
    private final HashMap<String, NewsFragment> b = new HashMap();

    public NewsPagerAdapter(FragmentManager var1, List<ChannelInfo> var2) {
        super(var1);
        this.a = var2;
    }

    public int getCount() {
        return this.a == null?0:this.a.size();
    }

    public Fragment getItem(int var1) {
        ChannelInfo var2 = (ChannelInfo)this.a.get(var1);
        NewsFragment var3 = (NewsFragment)this.b.get(var2.id);
        if(var3 == null) {
            var3 = NewsFragment.create(var2.id, var2.name);
            this.b.put(var2.id, var3);
        }

        return var3;
    }

    public long getItemId(int var1) {
        return (long)((ChannelInfo)this.a.get(var1)).id.hashCode();
    }

    public int getItemPosition(Object var1) {
        NewsFragment var2 = (NewsFragment)var1;
        String var3 = var2.getChannelId();

        for(int var4 = 0; var4 < this.a.size(); ++var4) {
            if(((ChannelInfo)this.a.get(var4)).id.equals(var3)) {
                return var4;
            }
        }

        return -2;
    }

    public CharSequence getPageTitle(int var1) {
        return ((ChannelInfo)this.a.get(var1)).name;
    }
}
