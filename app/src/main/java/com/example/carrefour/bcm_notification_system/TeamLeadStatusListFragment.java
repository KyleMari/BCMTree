package com.example.carrefour.bcm_notification_system;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyle.mari.torralba on 9/21/2016.
 */
public class TeamLeadStatusListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] teamLeadNames;
    private List<StatusItem> statusItems;
    TeamLeadStatusCustomAdapter adapter;

    public static final String MY_PREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.group_list_fragment, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        teamLeadNames = getResources().getStringArray(R.array.team_lead_names);

        statusItems = new ArrayList<StatusItem>();

        for (int i = 0; i < teamLeadNames.length; i++) {
            StatusItem item = new StatusItem(teamLeadNames[i], sharedPreferences.getBoolean("teamLeadStatus_" + (i+1), false));
            statusItems.add(item);
        }

        adapter = new TeamLeadStatusCustomAdapter(getActivity(), statusItems);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
