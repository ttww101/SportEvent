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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private int sportId,eventId;
    private String sportName,eventName;
    private int tabCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        sportId = intent.getIntExtra("sportId",0);
        sportName = intent.getStringExtra("sportName");
        eventId = intent.getIntExtra("eventId",0);
        eventName = intent.getStringExtra("eventName");

        TextView topTitleTextView = (TextView) findViewById(R.id.topTitleTextView);
        ImageButton topMenuButton = (ImageButton) findViewById(R.id.topMenuButton);
        ImageButton topBackButton = (ImageButton) findViewById(R.id.topBackButton);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.expandListView);

        topTitleTextView.setText(eventName);

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

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

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

                    Intent intent  = new Intent(EventActivity.this, EventActivity.class);
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

    public static class PlaceholderFragment extends Fragment {
        private ListView newsListView,recentListView,historicalListView,shooterListView,rankingListView;
        private ArrayList<NewsObject> newsDataList;
        private NewsListViewAdapter newsAdapter;
        private boolean newsIsLoading = false;
        private int newsCurrentPage = 1;
        private int newsAllPages = 1;

        private ArrayList<RecentObject> recentDataList;
        private RecentListViewAdapter recentAdapter;
        private boolean recentIsLoading = false;
        private ArrayList<SeasonPage> recentSasonPageList = new ArrayList<>();
        private int recentCurrentIndex = 0;

        private ArrayList<HistoricalObject> historicalDataList;
        private HistoricalListViewAdapter historicalAdapter;
        private boolean historicalIsLoading = false;
        private ArrayList<SeasonPage> historicalSasonPageList = new ArrayList<>();
        private int historicalCurrentIndex = 0;

        private LinearLayout shooterSpinnerLinearLayout;
        private Spinner shooterSpinner;
        private ArrayList<ShooterObject> shooterAllDataList,shooterDataList;
        private ShooterListViewAdapter shooterAdapter;

        private LinearLayout rankingSpinnerLinearLayout;
        private Spinner rankingSpinner;
        private ArrayList<RankingObject> rankingAllDataList,rankingDataList;
        private RankingListViewAdapter rankingAdapter;

        public PlaceholderFragment() {}
        public static PlaceholderFragment newInstance(int sectionNumber,int eventId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("section_number", sectionNumber);
            args.putInt("eventId", eventId);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            int sectionNumber = getArguments().getInt("section_number");
            final int eventId = getArguments().getInt("eventId");
            if (sectionNumber == 1) {
                View rootView = inflater.inflate(R.layout.event_fragment_news, container, false);
                newsListView = (ListView) rootView.findViewById(R.id.listView);
                newsDataList = new ArrayList<>();
                newsAdapter = new NewsListViewAdapter(getContext(),R.layout.event_fragment_news_listview_item,newsDataList);
                newsListView.setAdapter(newsAdapter);
                newsCurrentPage = 1;
                ShowApiKey.getInstance().httpGetNews(getContext(), eventId, newsCurrentPage, new ArrayMap<String, String>(), new HttpThreadCallback() {
                    @Override
                    public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {

                        newsDataList.clear();

                        try {
                            JSONObject jo = new JSONObject(result);
                            JSONObject newsJo = jo.getJSONObject("showapi_res_body");
                            newsAllPages = newsJo.getInt("allPages");
                            JSONArray newsJa = newsJo.getJSONArray("contentlist");
                            for (int i=0;i<newsJa.length();i++) {
                                JSONObject news = newsJa.getJSONObject(i);
                                int sportId = news.getInt("sport_id");
                                int eventId = news.getInt("event_id");
                                String newsId = news.getString("news_id");
                                String newsDatetime = news.getString("time");
                                String newsSource = news.getString("com_cn");
                                String newsUrl = news.getString("news_url");
                                String sportName = news.getString("sport_name");
                                String eventName = news.getString("event_name");
                                String title = news.getString("title");
                                String content = news.getString("content");

                                NewsObject newsObj = new NewsObject();
                                newsObj.setSportId(sportId);
                                newsObj.setEventId(eventId);
                                newsObj.setNewsId(newsId);
                                newsObj.setNewsDatetime(newsDatetime);
                                newsObj.setNewsSource(newsSource);
                                newsObj.setNewsUrl(newsUrl);
                                newsObj.setSportName(sportName);
                                newsObj.setEventName(eventName);
                                newsObj.setTitle(title);
                                newsObj.setContent(content);

                                newsDataList.add(newsObj);
                            }
                        } catch (JSONException e) {}

                        newsAdapter.notifyDataSetChanged();

                    }
                });

                newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if(newsIsLoading) {
                            return;
                        }
                        int lastItemid = newsListView.getLastVisiblePosition();
                        if((lastItemid+1) == totalItemCount && newsCurrentPage < newsAllPages){
                            newsIsLoading = true;
                            newsCurrentPage = newsCurrentPage+1;
                            ShowApiKey.getInstance().httpGetNews(getContext(), eventId, newsCurrentPage, new ArrayMap<String, String>(), new HttpThreadCallback() {
                                @Override
                                public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {

                                    ArrayList<NewsObject> dataList2 = new ArrayList<>();

                                    try {
                                        JSONObject jo = new JSONObject(result);
                                        JSONObject newsJo = jo.getJSONObject("showapi_res_body");
                                        JSONArray newsJa = newsJo.getJSONArray("contentlist");
                                        for (int i=0;i<newsJa.length();i++) {
                                            JSONObject news = newsJa.getJSONObject(i);
                                            int sportId = news.getInt("sport_id");
                                            int eventId = news.getInt("event_id");
                                            String newsId = news.getString("news_id");
                                            String newsDatetime = news.getString("time");
                                            String newsSource = news.getString("com_cn");
                                            String newsUrl = news.getString("news_url");
                                            String sportName = news.getString("sport_name");
                                            String eventName = news.getString("event_name");
                                            String title = news.getString("title");
                                            String content = news.getString("content");

                                            NewsObject newsObj = new NewsObject();
                                            newsObj.setSportId(sportId);
                                            newsObj.setEventId(eventId);
                                            newsObj.setNewsId(newsId);
                                            newsObj.setNewsDatetime(newsDatetime);
                                            newsObj.setNewsSource(newsSource);
                                            newsObj.setNewsUrl(newsUrl);
                                            newsObj.setSportName(sportName);
                                            newsObj.setEventName(eventName);
                                            newsObj.setTitle(title);
                                            newsObj.setContent(content);

                                            dataList2.add(newsObj);
                                        }
                                    } catch (JSONException e) {}

                                    for(int i=0;i<dataList2.size();i++) {
                                        newsDataList.add(dataList2.get(i));
                                    }

                                    newsAdapter.notifyDataSetChanged();

                                    newsIsLoading = false;

                                }
                            });

                        }
                    }
                });
                return rootView;
            } else if (sectionNumber == 2) {
                View rootView = inflater.inflate(R.layout.event_fragment_recent, container, false);
                recentListView = (ListView) rootView.findViewById(R.id.listView);
                recentDataList = new ArrayList<>();
                recentAdapter = new RecentListViewAdapter(getContext(),R.layout.event_fragment_recent_listview_item,recentDataList);
                recentListView.setAdapter(recentAdapter);

                ShowApiKey.getInstance().getRecentSeasonPageList(getContext(), eventId, new SeasonPageListCallback() {
                    @Override
                    public void runCallback(ArrayList<SeasonPage> seasonPageList) {

                        recentSasonPageList = seasonPageList;
                        recentCurrentIndex = 0;

                        ShowApiKey.getInstance().httpGetRecents(getContext(), eventId, recentSasonPageList.get(recentCurrentIndex).getSeason(), recentSasonPageList.get(recentCurrentIndex).getPageNum(), new ArrayMap<String, String>(), new HttpThreadCallback() {
                            @Override
                            public void runCallback(int runStatus01, ArrayMap<String, String> responseHeaderProperties01, String result01, String error01) {

                                try {

                                    JSONObject jo01 = new JSONObject(result01);
                                    JSONObject recentsJo01 = jo01.getJSONObject("showapi_res_body");
                                    recentDataList.clear();

                                    JSONArray recentsJa01 = recentsJo01.getJSONArray("schedule");
                                    for (int i=(recentsJa01.length()-1);i>=0;i--) {
                                        JSONObject recents01 = recentsJa01.getJSONObject(i);
                                        RecentObject recentsObj01 = new RecentObject();
                                        recentsObj01.setImageUrl(recents01.getString("img_url"));
                                        recentsObj01.setEventName(recents01.getString("event_name"));
                                        recentsObj01.setResultUrl(recents01.getString("result_url"));
                                        recentsObj01.setStatus(recents01.getString("status"));
                                        recentsObj01.setHomeTeam(recents01.getString("home_team"));
                                        recentsObj01.setLiveUrl(recents01.getString("live_video"));
                                        recentsObj01.setSportId(recents01.getString("sport_id"));
                                        recentsObj01.setWeek(recents01.getString("week"));
                                        recentsObj01.setMatchCity(recents01.getString("matchCity"));
                                        recentsObj01.setVisitingTeam(recents01.getString("visiting_team"));
                                        recentsObj01.setVisitingScore(recents01.getString("visiting_score"));
                                        recentsObj01.setDate(recents01.getString("date"));
                                        recentsObj01.setEventId(recents01.getString("event_id"));
                                        recentsObj01.setGroup(recents01.getString("group"));
                                        recentsObj01.setStatusCn(recents01.getString("status_cn"));
                                        recentsObj01.setStadium(recents01.getString("stadium"));
                                        recentsObj01.setSportName(recents01.getString("sport_name"));
                                        recentsObj01.setHomeScore(recents01.getString("home_score"));
                                        recentsObj01.setTitle(recents01.getString("title"));
                                        recentsObj01.setRound(recents01.getString("round"));
                                        recentsObj01.setNewsUrl(recents01.getString("news_url"));
                                        recentsObj01.setScheduleId(recents01.getString("schedule_id"));
                                        recentsObj01.setNewsId(recents01.getString("news_id"));
                                        recentsObj01.setSeason(recents01.getString("season"));
                                        recentsObj01.setSeasonCn(recents01.getString("season_cn"));

                                        recentDataList.add(recentsObj01);
                                    }

                                } catch (JSONException e) {}

                                recentAdapter.notifyDataSetChanged();

                            }
                        });

                    }
                });

                recentListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if(recentIsLoading) {
                            return;
                        }
                        int lastItemid = recentListView.getLastVisiblePosition();
                        if((lastItemid+1) == totalItemCount && recentCurrentIndex < (recentSasonPageList.size()-1)){
                            recentIsLoading = true;
                            recentCurrentIndex = recentCurrentIndex+1;

                            ShowApiKey.getInstance().httpGetRecents(getContext(), eventId, recentSasonPageList.get(recentCurrentIndex).getSeason(), recentSasonPageList.get(recentCurrentIndex).getPageNum(), new ArrayMap<String, String>(), new HttpThreadCallback() {
                                @Override
                                public void runCallback(int runStatus01, ArrayMap<String, String> responseHeaderProperties01, String result01, String error01) {

                                    ArrayList<RecentObject> recentDataList2 = new ArrayList<>();

                                    try {

                                        JSONObject jo01 = new JSONObject(result01);
                                        JSONObject recentsJo01 = jo01.getJSONObject("showapi_res_body");

                                        JSONArray recentsJa01 = recentsJo01.getJSONArray("schedule");
                                        for (int i=(recentsJa01.length()-1);i>=0;i--) {
                                            JSONObject recents01 = recentsJa01.getJSONObject(i);
                                            RecentObject recentsObj01 = new RecentObject();
                                            recentsObj01.setImageUrl(recents01.getString("img_url"));
                                            recentsObj01.setEventName(recents01.getString("event_name"));
                                            recentsObj01.setResultUrl(recents01.getString("result_url"));
                                            recentsObj01.setStatus(recents01.getString("status"));
                                            recentsObj01.setHomeTeam(recents01.getString("home_team"));
                                            recentsObj01.setLiveUrl(recents01.getString("live_video"));
                                            recentsObj01.setSportId(recents01.getString("sport_id"));
                                            recentsObj01.setWeek(recents01.getString("week"));
                                            recentsObj01.setMatchCity(recents01.getString("matchCity"));
                                            recentsObj01.setVisitingTeam(recents01.getString("visiting_team"));
                                            recentsObj01.setVisitingScore(recents01.getString("visiting_score"));
                                            recentsObj01.setDate(recents01.getString("date"));
                                            recentsObj01.setEventId(recents01.getString("event_id"));
                                            recentsObj01.setGroup(recents01.getString("group"));
                                            recentsObj01.setStatusCn(recents01.getString("status_cn"));
                                            recentsObj01.setStadium(recents01.getString("stadium"));
                                            recentsObj01.setSportName(recents01.getString("sport_name"));
                                            recentsObj01.setHomeScore(recents01.getString("home_score"));
                                            recentsObj01.setTitle(recents01.getString("title"));
                                            recentsObj01.setRound(recents01.getString("round"));
                                            recentsObj01.setNewsUrl(recents01.getString("news_url"));
                                            recentsObj01.setScheduleId(recents01.getString("schedule_id"));
                                            recentsObj01.setNewsId(recents01.getString("news_id"));
                                            recentsObj01.setSeason(recents01.getString("season"));
                                            recentsObj01.setSeasonCn(recents01.getString("season_cn"));

                                            recentDataList2.add(recentsObj01);
                                        }

                                    } catch (JSONException e) {}

                                    for(int i=0;i<recentDataList2.size();i++) {
                                        recentDataList.add(recentDataList2.get(i));
                                    }

                                    recentAdapter.notifyDataSetChanged();

                                    recentIsLoading = false;

                                }
                            });

                        }
                    }
                });

                return rootView;
            } else if (sectionNumber == 3) {
                View rootView = inflater.inflate(R.layout.event_fragment_historical, container, false);
                historicalListView = (ListView) rootView.findViewById(R.id.listView);
                historicalDataList = new ArrayList<>();
                historicalAdapter = new HistoricalListViewAdapter(getContext(),R.layout.event_fragment_historical_listview_item,historicalDataList);
                historicalListView.setAdapter(historicalAdapter);

                ShowApiKey.getInstance().getHistoricalSeasonPageList(getContext(), eventId, new SeasonPageListCallback() {
                    @Override
                    public void runCallback(ArrayList<SeasonPage> seasonPageList) {

                        historicalSasonPageList = seasonPageList;
                        historicalCurrentIndex = 0;

                        ShowApiKey.getInstance().httpGetHistorical(getContext(), eventId, historicalSasonPageList.get(historicalCurrentIndex).getSeason(), historicalSasonPageList.get(historicalCurrentIndex).getPageNum(), new ArrayMap<String, String>(), new HttpThreadCallback() {
                            @Override
                            public void runCallback(int runStatus01, ArrayMap<String, String> responseHeaderProperties01, String result01, String error01) {

                                try {

                                    JSONObject jo01 = new JSONObject(result01);
                                    JSONObject historicalJo01 = jo01.getJSONObject("showapi_res_body");
                                    historicalDataList.clear();

                                    JSONArray historicalJa01 = historicalJo01.getJSONArray("historySchedule");
                                    for (int i=(historicalJa01.length()-1);i>=0;i--) {
                                        JSONObject historical01 = historicalJa01.getJSONObject(i);
                                        HistoricalObject historicalObj01 = new HistoricalObject();
                                        historicalObj01.setImageUrl(historical01.getString("img_url"));
                                        historicalObj01.setEventName(historical01.getString("event_name"));
                                        historicalObj01.setResultUrl(historical01.getString("result_url"));
                                        historicalObj01.setStatus(historical01.getString("status"));
                                        historicalObj01.setHomeTeam(historical01.getString("home_team"));
                                        historicalObj01.setLiveUrl(historical01.getString("live_video"));
                                        historicalObj01.setSportId(historical01.getString("sport_id"));
                                        historicalObj01.setWeek(historical01.getString("week"));
                                        historicalObj01.setMatchCity(historical01.getString("matchCity"));
                                        historicalObj01.setVisitingTeam(historical01.getString("visiting_team"));
                                        historicalObj01.setVisitingScore(historical01.getString("visiting_score"));
                                        historicalObj01.setDate(historical01.getString("date"));
                                        historicalObj01.setEventId(historical01.getString("event_id"));
                                        historicalObj01.setGroup(historical01.getString("group"));
                                        historicalObj01.setStatusCn(historical01.getString("status_cn"));
                                        historicalObj01.setStadium(historical01.getString("stadium"));
                                        historicalObj01.setSportName(historical01.getString("sport_name"));
                                        historicalObj01.setHomeScore(historical01.getString("home_score"));
                                        historicalObj01.setTitle(historical01.getString("title"));
                                        historicalObj01.setRound(historical01.getString("round"));
                                        historicalObj01.setNewsUrl(historical01.getString("news_url"));
                                        historicalObj01.setScheduleId(historical01.getString("schedule_id"));
                                        historicalObj01.setNewsId(historical01.getString("news_id"));
                                        historicalObj01.setSeason(historical01.getString("season"));
                                        historicalObj01.setSeasonCn(historical01.getString("season_cn"));

                                        historicalDataList.add(historicalObj01);
                                    }

                                } catch (JSONException e) {}

                                historicalAdapter.notifyDataSetChanged();

                            }
                        });

                    }
                });

                historicalListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if(historicalIsLoading) {
                            return;
                        }
                        int lastItemid = historicalListView.getLastVisiblePosition();
                        if((lastItemid+1) == totalItemCount && historicalCurrentIndex < (historicalSasonPageList.size()-1)){
                            historicalIsLoading = true;
                            historicalCurrentIndex = historicalCurrentIndex+1;

                            ShowApiKey.getInstance().httpGetHistorical(getContext(), eventId, historicalSasonPageList.get(historicalCurrentIndex).getSeason(), historicalSasonPageList.get(historicalCurrentIndex).getPageNum(), new ArrayMap<String, String>(), new HttpThreadCallback() {
                                @Override
                                public void runCallback(int runStatus01, ArrayMap<String, String> responseHeaderProperties01, String result01, String error01) {

                                    ArrayList<HistoricalObject> historicalDataList2 = new ArrayList<>();

                                    try {

                                        JSONObject jo01 = new JSONObject(result01);
                                        JSONObject historicalJo01 = jo01.getJSONObject("showapi_res_body");

                                        JSONArray historicalJa01 = historicalJo01.getJSONArray("historySchedule");
                                        for (int i=(historicalJa01.length()-1);i>=0;i--) {
                                            JSONObject historical01 = historicalJa01.getJSONObject(i);
                                            HistoricalObject historicalObj01 = new HistoricalObject();
                                            historicalObj01.setImageUrl(historical01.getString("img_url"));
                                            historicalObj01.setEventName(historical01.getString("event_name"));
                                            historicalObj01.setResultUrl(historical01.getString("result_url"));
                                            historicalObj01.setStatus(historical01.getString("status"));
                                            historicalObj01.setHomeTeam(historical01.getString("home_team"));
                                            historicalObj01.setLiveUrl(historical01.getString("live_video"));
                                            historicalObj01.setSportId(historical01.getString("sport_id"));
                                            historicalObj01.setWeek(historical01.getString("week"));
                                            historicalObj01.setMatchCity(historical01.getString("matchCity"));
                                            historicalObj01.setVisitingTeam(historical01.getString("visiting_team"));
                                            historicalObj01.setVisitingScore(historical01.getString("visiting_score"));
                                            historicalObj01.setDate(historical01.getString("date"));
                                            historicalObj01.setEventId(historical01.getString("event_id"));
                                            historicalObj01.setGroup(historical01.getString("group"));
                                            historicalObj01.setStatusCn(historical01.getString("status_cn"));
                                            historicalObj01.setStadium(historical01.getString("stadium"));
                                            historicalObj01.setSportName(historical01.getString("sport_name"));
                                            historicalObj01.setHomeScore(historical01.getString("home_score"));
                                            historicalObj01.setTitle(historical01.getString("title"));
                                            historicalObj01.setRound(historical01.getString("round"));
                                            historicalObj01.setNewsUrl(historical01.getString("news_url"));
                                            historicalObj01.setScheduleId(historical01.getString("schedule_id"));
                                            historicalObj01.setNewsId(historical01.getString("news_id"));
                                            historicalObj01.setSeason(historical01.getString("season"));
                                            historicalObj01.setSeasonCn(historical01.getString("season_cn"));

                                            historicalDataList2.add(historicalObj01);
                                        }

                                    } catch (JSONException e) {}

                                    for(int i=0;i<historicalDataList2.size();i++) {
                                        historicalDataList.add(historicalDataList2.get(i));
                                    }

                                    historicalAdapter.notifyDataSetChanged();

                                    historicalIsLoading = false;

                                }
                            });

                        }
                    }
                });

                return rootView;
            } else if (sectionNumber == 4) {
                View rootView = inflater.inflate(R.layout.event_fragment_shooter, container, false);
                shooterListView = (ListView) rootView.findViewById(R.id.listView);
                shooterSpinnerLinearLayout = (LinearLayout) rootView.findViewById(R.id.spinnerLinearLayout);
                shooterSpinner = (Spinner) rootView.findViewById(R.id.spinner);
                shooterSpinnerLinearLayout.setVisibility(View.GONE);
                shooterAllDataList = new ArrayList<>();
                shooterDataList = new ArrayList<>();
                shooterAdapter = new ShooterListViewAdapter(getContext(),R.layout.event_fragment_shooter_listview_item,shooterDataList);
                shooterListView.setAdapter(shooterAdapter);

                ShowApiKey.getInstance().httpGetShooter(getContext(), eventId, new ArrayMap<String, String>(), new HttpThreadCallback() {
                    @Override
                    public void runCallback(int runStatus01, ArrayMap<String, String> responseHeaderProperties01, String result01, String error01) {

                        try {

                            JSONObject jo01 = new JSONObject(result01);
                            JSONObject shooterJo01 = jo01.getJSONObject("showapi_res_body");
                            shooterAllDataList.clear();

                            JSONArray shooterJa01 = shooterJo01.getJSONArray("playerRank");
                            for (int i=0;i<shooterJa01.length();i++) {
                                JSONObject shooter01 = shooterJa01.getJSONObject(i);
                                ShooterObject shooterObj01 = new ShooterObject();
                                shooterObj01.setEventName(shooter01.getString("event_name"));
                                shooterObj01.setPlayerName(shooter01.getString("player_name"));
                                shooterObj01.setSportName(shooter01.getString("sport_name"));
                                shooterObj01.setTeamName(shooter01.getString("team_name"));
                                shooterObj01.setPenalty(shooter01.getString("penalty_kick"));
                                shooterObj01.setEventId(shooter01.getString("event_id"));
                                shooterObj01.setRank(shooter01.getString("rank"));
                                shooterObj01.setSportId(shooter01.getString("sport_id"));
                                shooterObj01.setGoal(shooter01.getString("goal"));
                                shooterObj01.setSeason(shooter01.getString("season"));
                                shooterObj01.setSeasonCn(shooter01.getString("season_cn"));

                                shooterAllDataList.add(shooterObj01);
                            }

                        } catch (JSONException e) {}

                        ArrayList<String> seansonList = new ArrayList<>();
                        for(int i=0;i<shooterAllDataList.size();i++) {
                            if (!seansonList.contains(shooterAllDataList.get(i).getSeasonCn())) {
                                seansonList.add(shooterAllDataList.get(i).getSeasonCn());
                            }
                        }
                        if (seansonList.size() > 0) {

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, seansonList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            shooterSpinner.setAdapter(dataAdapter);
                            shooterSpinnerLinearLayout.setVisibility(View.VISIBLE);
                            shooterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    shooterDataList.clear();
                                    for(int i=0;i<shooterAllDataList.size();i++) {
                                        if (shooterAllDataList.get(i).getSeasonCn().equals(shooterSpinner.getSelectedItem())) {
                                            shooterDataList.add(shooterAllDataList.get(i));
                                        }
                                    }
                                    shooterAdapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) { }
                            });
                        } else {
                            shooterSpinnerLinearLayout.setVisibility(View.GONE);
                            shooterDataList.clear();
                            shooterAdapter.notifyDataSetChanged();
                        }

                    }
                });

                return rootView;
            } else if (sectionNumber == 5) {
                View rootView = inflater.inflate(R.layout.event_fragment_ranking, container, false);
                rankingListView = (ListView) rootView.findViewById(R.id.listView);
                rankingSpinnerLinearLayout = (LinearLayout) rootView.findViewById(R.id.spinnerLinearLayout);
                rankingSpinner = (Spinner) rootView.findViewById(R.id.spinner);
                rankingSpinnerLinearLayout.setVisibility(View.GONE);
                rankingAllDataList = new ArrayList<>();
                rankingDataList = new ArrayList<>();
                rankingAdapter = new RankingListViewAdapter(getContext(),R.layout.event_fragment_ranking_listview_item,rankingDataList);
                rankingListView.setAdapter(rankingAdapter);

                ShowApiKey.getInstance().httpGetRanking(getContext(), eventId, new ArrayMap<String, String>(), new HttpThreadCallback() {
                    @Override
                    public void runCallback(int runStatus01, ArrayMap<String, String> responseHeaderProperties01, String result01, String error01) {

                        try {

                            JSONObject jo01 = new JSONObject(result01);
                            JSONObject rankingJo01 = jo01.getJSONObject("showapi_res_body");
                            rankingAllDataList.clear();

                            JSONArray rankingJa01 = rankingJo01.getJSONArray("teamRank");
                            for (int i=0;i<rankingJa01.length();i++) {
                                JSONObject ranking01 = rankingJa01.getJSONObject(i);
                                RankingObject rankingObj01 = new RankingObject();
                                rankingObj01.setSportId(ranking01.getString("sport_id"));
                                rankingObj01.setSportName(ranking01.getString("sport_name"));
                                rankingObj01.setEventId(ranking01.getString("event_id"));
                                rankingObj01.setEventName(ranking01.getString("event_name"));
                                rankingObj01.setSeason(ranking01.getString("season"));
                                rankingObj01.setSeasonCn(ranking01.getString("season_cn"));
                                rankingObj01.setTeamName(ranking01.getString("team_name"));
                                rankingObj01.setTeamRank(ranking01.getString("team_rank"));
                                rankingObj01.setWin(ranking01.getString("win"));
                                rankingObj01.setLose(ranking01.getString("lose"));
                                if (rankingObj01.getSportId().equals("1477")) {
                                    // 籃球
                                    JSONObject basketballJo = ranking01.getJSONObject("basketball");
                                    rankingObj01.setLosePoint(basketballJo.getString("lose_point"));
                                    rankingObj01.setTruePoint(basketballJo.getString("true_point"));
                                    rankingObj01.setStreakLength(basketballJo.getString("streak_length"));
                                    rankingObj01.setTableArea(basketballJo.getString("table_area"));
                                    rankingObj01.setWinPercentage(basketballJo.getString("win_percentage"));
                                    rankingObj01.setPoint(basketballJo.getString("point"));
                                    rankingObj01.setStreakKind(basketballJo.getString("streak_kind"));
                                    rankingObj01.setHome(basketballJo.getString("home"));
                                    rankingObj01.setVisiting(basketballJo.getString("visiting"));
                                } else if (rankingObj01.getSportId().equals("1476")) {
                                    // 足球
                                    JSONObject footballJo = ranking01.getJSONObject("football");
                                    rankingObj01.setTrueGoal(footballJo.getString("true_goal"));
                                    rankingObj01.setCount(footballJo.getString("count"));
                                    rankingObj01.setIntegral(footballJo.getString("integral"));
                                    rankingObj01.setGroup(footballJo.getString("group"));
                                    rankingObj01.setGoal(footballJo.getString("goal"));
                                    rankingObj01.setLoseGoal(footballJo.getString("lose_goal"));
                                    rankingObj01.setAll(footballJo.getString("all"));
                                }

                                rankingAllDataList.add(rankingObj01);
                            }

                        } catch (JSONException e) {}

                        ArrayList<String> seansonList = new ArrayList<>();
                        for(int i=0;i<rankingAllDataList.size();i++) {
                            if (!seansonList.contains(rankingAllDataList.get(i).getSeasonCn())) {
                                seansonList.add(rankingAllDataList.get(i).getSeasonCn());
                            }
                        }
                        if (seansonList.size() > 0) {

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, seansonList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            rankingSpinner.setAdapter(dataAdapter);
                            rankingSpinnerLinearLayout.setVisibility(View.VISIBLE);
                            rankingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    rankingDataList.clear();
                                    for(int i=0;i<rankingAllDataList.size();i++) {
                                        if (rankingAllDataList.get(i).getSeasonCn().equals(rankingSpinner.getSelectedItem())) {
                                            rankingDataList.add(rankingAllDataList.get(i));
                                        }
                                    }
                                    rankingAdapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) { }
                            });
                        } else {
                            rankingSpinnerLinearLayout.setVisibility(View.GONE);
                            rankingDataList.clear();
                            rankingAdapter.notifyDataSetChanged();
                        }

                    }
                });

                return rootView;
            } else {
                View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText("");
                return rootView;
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1,eventId);
        }
        @Override
        public int getCount() {
            return tabCount;
        }
    }

}
