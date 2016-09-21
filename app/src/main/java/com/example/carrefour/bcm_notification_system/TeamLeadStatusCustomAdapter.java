package com.example.carrefour.bcm_notification_system;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kyle.mari.torralba on 9/21/2016.
 */
public class TeamLeadStatusCustomAdapter extends BaseAdapter {

    Context context;
    List<StatusItem> statusItem;

    public TeamLeadStatusCustomAdapter(Context context, List<StatusItem> statusItem){
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
            convertView = mInflater.inflate(R.layout.group_list_custom, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.team_lead_name);
        CheckBox chkbox = (CheckBox) convertView.findViewById(R.id.feedback_chkbox);

        StatusItem row_pos = statusItem.get(position);
        // setting the image resource and title

        name.setText(row_pos.getTeamLeadName());
        chkbox.setSelected(row_pos.isChecked());

        return convertView;
    }
}
