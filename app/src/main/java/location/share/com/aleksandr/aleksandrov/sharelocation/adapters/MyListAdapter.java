package location.share.com.aleksandr.aleksandrov.sharelocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.UserInfo;

/**
 * Created by Aleksandr on 11/7/2016.
 */

public class MyListAdapter extends BaseAdapter {

    private TextView textView;
    private ImageView imageView;

    private List<UserInfo> userInfoList = new ArrayList<>();
    private LayoutInflater inflater;

    public MyListAdapter(Context context, List<UserInfo> userInfoList) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userInfoList = userInfoList;
    }

    @Override
    public int getCount() {
        return userInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return userInfoList.get(i);
    }

    private UserInfo getUser(int position) {
        return (UserInfo) getItem(position);
    }

    @Override
    public long getItemId(int i) {
        return userInfoList.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.fragment_list_item_for_friends, parent, false);
            textView = (TextView) rowView.findViewById(R.id.text1);
            imageView = (ImageView) rowView.findViewById(R.id.picture_in_frends_list);
        }
        UserInfo userInfo = getUser(position);
        textView.setText(userInfo.getFio());
        imageView.setImageResource(R.drawable.profile);

        return rowView;
    }
}
