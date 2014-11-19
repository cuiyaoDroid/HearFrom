package io.rong.imkit.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.tingshuo.hearfrom.R;

import io.rong.imlib.RongIMClient;


public class GroupListAdapter extends BaseAdapter {

    private ArrayList<RongIMClient.Group> groups;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public GroupListAdapter(Context context, ArrayList<RongIMClient.Group> groups) {

        this.groups = groups;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public RongIMClient.Group getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null || convertView.getTag() == null) {

            convertView = mLayoutInflater.inflate(R.layout.rc_item_group_list, null);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RongIMClient.Group group = getItem(position);

        viewHolder.titleTextView.setText(group.getName());
        viewHolder.titleTextView.setTag(group.getId());

        return convertView;
    }

    static class ViewHolder {
        TextView titleTextView;
    }
}
