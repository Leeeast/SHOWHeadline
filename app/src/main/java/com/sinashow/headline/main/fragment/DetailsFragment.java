package com.sinashow.headline.main.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2018/1/3.
 */

public class DetailsFragment extends Fragment {
    public static DetailsFragment newInstance(Intent var0, int var1) {
        DetailsFragment var2 = new DetailsFragment();
        Bundle var3 = new Bundle();
        var3.putParcelable("intent", var0);
        var3.putInt("flags", var1);
        var2.setArguments(var3);
        return var2;
    }

}
