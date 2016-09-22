package com.example.carrefour.bcm_notification_system;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyle.mari.torralba on 9/21/2016.
 */
public class TeamLeadStatusListFragment extends ListFragment implements OnItemClickListener {

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

        //teamLeadNames = getResources().getStringArray(R.array.team_lead_names);
        String chosenGroupId = "";
        if(!sharedPreferences.getString("contactGroupID", "").equals("")){
            chosenGroupId = sharedPreferences.getString("contactGroupID", "");
        }
        ArrayList<ContactItem> contactListInGroup = getContactListFromGroup(chosenGroupId);
        teamLeadNames = new String[contactListInGroup.size()];
        if(!chosenGroupId.equals("")) {
            for (int i = 0; i < contactListInGroup.size(); i++) {
                teamLeadNames[i] = contactListInGroup.get(i).phDisplayName;
            }
        }

        statusItems = new ArrayList<StatusItem>();

        for (int i = 0; i < teamLeadNames.length; i++) {
            StatusItem item = new StatusItem(teamLeadNames[i], false);

            statusItems.add(item);
        }

        adapter = new TeamLeadStatusCustomAdapter(getActivity(), statusItems);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);



    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), statusItems.size(), Toast.LENGTH_LONG).show();
    }


    public ArrayList<ContactItem> getContactListFromGroup(String groupID){
        ArrayList<ContactItem> contactList = new ArrayList<ContactItem>();
        Uri groupURI = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID
        };
        Cursor c = getActivity().getContentResolver().query(
            groupURI, projection,
            ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" + groupID, null, null);
        while(c.moveToNext()){
            String id = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
            Cursor pCur = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[] { id }, null);
            while(pCur.moveToNext()){
                ContactItem data = new ContactItem();
                data.phDisplayName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                data.phNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactList.add(data);
            }
            pCur.close();
        }
        return contactList;
    }
}
