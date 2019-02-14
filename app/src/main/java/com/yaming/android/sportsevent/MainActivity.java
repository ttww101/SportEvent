package com.yaming.android.sportsevent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView topTitleTextView = (TextView) findViewById(R.id.topTitleTextView);
        ImageButton topMenuButton = (ImageButton) findViewById(R.id.topMenuButton);
        ImageButton topBackButton = (ImageButton) findViewById(R.id.topBackButton);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.expandListView);

        topMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDrawer();
            }
        });

        topBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    // back process
                    finish();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        StringBuilder eventsBuf = new StringBuilder();
        try {
            InputStream sportsJson = getAssets().open("events.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(sportsJson, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                eventsBuf.append(str);
            }
            in.close();
        } catch (IOException e) {}

        ArrayList<ExpandGroupObject> expandGroupDataList = new ArrayList<>();

        try {
            JSONObject sportsJo = new JSONObject(eventsBuf.toString());
            JSONArray eventsJa = sportsJo.getJSONArray("eventsClassify");
            ArrayList<String> sportIdList = new ArrayList<>();
            ArrayList<String> eventIdList = new ArrayList<>();
            for (int i=0;i<eventsJa.length();i++) {
                JSONObject eventsJo = eventsJa.getJSONObject(i);
                int sportId = eventsJo.getInt("sport_id");
                String sportName = eventsJo.getString("sport_name");
                int eventId = eventsJo.getInt("event_id");
                String eventName = eventsJo.getString("event_name");
                if (!sportIdList.contains(sportId+"")) {
                    sportIdList.add(sportId+"");
                    ExpandGroupObject group01 = new ExpandGroupObject();
                    group01.setSportId(sportId);
                    group01.setSportName(sportName);
                    expandGroupDataList.add(group01);
                };
                int sportIndex = sportIdList.indexOf(sportId+"");
                if (!eventIdList.contains(eventId+"")) {
                    eventIdList.add(eventId+"");
                    ExpandItemObject group01item01 = new ExpandItemObject();
                    group01item01.setEventId(eventId);
                    group01item01.setEventName(eventName);
                    expandGroupDataList.get(sportIndex).items.add(group01item01);
                };
            }

        } catch (JSONException e) {}

        ExpandableListAdapter expandListAdapter = new ExpandableListAdapter(this, expandGroupDataList, R.layout.main_expandlistview_group, R.layout.main_expandlistview_item);

        ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.expandListView);
        expandListView.setAdapter(expandListAdapter);

        for (int i=0;i<expandGroupDataList.size();i++) {
            expandListView.expandGroup(i);
        }



        ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<MainObject> dataList = new ArrayList<>();
        final MainListViewAdapter adapter = new MainListViewAdapter(this,R.layout.main_listview_item,dataList);
        listView.setAdapter(adapter);
        ShowApiKey.getInstance().httpGetHupuNews(this, new ArrayMap<String, String>(), new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                dataList.clear();

                try {
                    JSONObject jo = new JSONObject(result);
                    JSONObject mainJo = jo.getJSONObject("showapi_res_body");
                    JSONArray mainJa1 = mainJo.getJSONArray("Headline");
                    for (int i=0;i<mainJa1.length();i++) {
                        JSONObject main = mainJa1.getJSONObject(i);
                        MainObject mainObj = new MainObject();
                        mainObj.setTitle(main.getString("title"));
                        mainObj.setContentUrl(main.getString("href"));

                        dataList.add(mainObj);
                    }

                    JSONArray mainJa2 = mainJo.getJSONArray("Information_list");
                    for (int i=0;i<mainJa2.length();i++) {
                        JSONObject main = mainJa2.getJSONObject(i);
                        MainObject mainObj = new MainObject();
                        mainObj.setTitle(main.getString("title"));
                        mainObj.setContentUrl(main.getString("href"));

                        dataList.add(mainObj);
                    }
                } catch (JSONException e) {}

                adapter.notifyDataSetChanged();

            }
        });


    }

    public class ExpandGroupObject {
        private int sportId;
        private String sportName;
        private ArrayList<ExpandItemObject> items;

        public ExpandGroupObject() {
            this.sportId = 0;
            this.sportName = "";
            this.items = new ArrayList<>();
        }

        public int getSportId() {
            return sportId;
        }
        public void setSportId(int sportId) {
            this.sportId = sportId;
        }
        public String getSportName() {
            return sportName;
        }
        public void setSportName(String sportName) {
            this.sportName = sportName;
        }
        public ArrayList<ExpandItemObject> getItems() {
            return items;
        }
        public void setItems(ArrayList<ExpandItemObject> items) {
            this.items = items;
        }
    }

    public class ExpandItemObject {
        private int eventId;
        private String eventName;

        public ExpandItemObject() {
            this.eventId = 0;
            this.eventName = "";
        }

        public int getEventId() {
            return eventId;
        }
        public void setEventId(int eventId) {
            this.eventId = eventId;
        }
        public String getEventName() {
            return eventName;
        }
        public void setEventName(String eventName) {
            this.eventName = eventName;
        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private List<ExpandGroupObject> mDataList;
        private int mGroupLayoutId,mItemLayoutId;

        public ExpandableListAdapter(Context context, List<ExpandGroupObject> dataList, int groupLayoutId, int itemLayoutId) {
            this.mContext = context;
            this.mDataList = dataList;
            this.mGroupLayoutId = groupLayoutId;
            this.mItemLayoutId = itemLayoutId;
        }

        @Override
        public ExpandItemObject getChild(int groupPosition, int childPosititon) {
            return mDataList.get(groupPosition).items.get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(mItemLayoutId, null);
            }

            TextView subTextView = (TextView) convertView.findViewById(R.id.textView);

            subTextView.setText(getChild(groupPosition, childPosition).getEventName());

            subTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }

                    Intent intent  = new Intent(MainActivity.this, EventActivity.class);
                    intent.putExtra("sportId", mDataList.get(groupPosition).sportId);
                    intent.putExtra("sportName", mDataList.get(groupPosition).sportName);
                    intent.putExtra("eventId", mDataList.get(groupPosition).getItems().get(childPosition).getEventId());
                    intent.putExtra("eventName", mDataList.get(groupPosition).getItems().get(childPosition).getEventName());
                    startActivity(intent);

                }
            });

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mDataList.get(groupPosition).getItems().size();
        }

        @Override
        public ExpandGroupObject getGroup(int groupPosition) {
            return mDataList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return mDataList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(mGroupLayoutId, null);
            }

            TextView titleTextView = (TextView) convertView.findViewById(R.id.textView);
            titleTextView.setTypeface(null, Typeface.BOLD);
            titleTextView.setText(getGroup(groupPosition).getSportName());

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true; // 不往下傳遞
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void switchDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
