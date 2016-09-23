package com.example.carrefour.bcm_notification_system;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

/**
 * Created by kyle.mari.torralba on 9/21/2016.
 */
public class TeamLeadCustomAdapter extends BaseAdapter {

    Context context;
    List<StatusItem> statusItem;

    public TeamLeadCustomAdapter(Context context, List<StatusItem> statusItem){
        this.context = context;
        this.statusItem = statusItem;
    }

    @Override
    public int getCount() {
        return statusItem.size();
    }

    @Override
    public Object getItem(int position) {
        return statusItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return statusItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.member_list_custom, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.team_member_name);
        ToggleButton yesNoToggle = (ToggleButton) convertView.findViewById(R.id.yesNoToggle);
        /*yesNoToggle.setText("No");
        yesNoToggle.setTextOff("No");
        yesNoToggle.setTextOn("Yes");*/

        StatusItem row_pos = statusItem.get(position);
        // setting the image resource and title

        name.setText(row_pos.getContactName());
        yesNoToggle.setSelected(row_pos.isChecked());

        return convertView;
    }
}
