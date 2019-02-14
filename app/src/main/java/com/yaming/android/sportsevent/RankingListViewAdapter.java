package com.yaming.android.sportsevent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class RankingListViewAdapter extends ArrayAdapter<RankingObject> {

    private Context context;
    private LayoutInflater layoutInflater;
    private int layoutId;
    private List<RankingObject> dataList;

    public RankingListViewAdapter(Context context, int layoutId, List<RankingObject> dataList) {
        super(context, layoutId, dataList);
        this.context=context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.dataList = dataList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RankingListViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutId,parent,false);
            viewHolder = new RankingListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RankingListViewHolder) convertView.getTag();
        }

        viewHolder.getTeamTextView().setText("");
        viewHolder.getSeasonTextView().setText("");
        viewHolder.getRankTextView().setText("");
        viewHolder.getWinTextView().setText("");
        viewHolder.getLoseTextView().setText("");
        viewHolder.getAllTextView().setText("");
        viewHolder.getDesc1TextView().setText("");
        viewHolder.getDesc2TextView().setText("");
        viewHolder.getDesc3TextView().setText("");
        viewHolder.getAllTitleTextView().setVisibility(View.GONE);
        viewHolder.getAllTextView().setVisibility(View.GONE);


        final RankingObject dataObject = dataList.get(position);

        String area = "";
        if (dataObject.getSportId().equals("1477")) {
            // 籃球
            area = dataObject.getTableArea();

            viewHolder.getAllTitleTextView().setVisibility(View.GONE);
            viewHolder.getAllTextView().setVisibility(View.GONE);
            viewHolder.getDesc1TextView().setText("主场："+dataObject.getHome()+"\n客场："+dataObject.getVisiting()+"\n胜率："+dataObject.getWinPercentage());
            viewHolder.getDesc2TextView().setText("得分："+dataObject.getPoint()+"\n失分："+dataObject.getLosePoint()+"\n胜分："+dataObject.getTruePoint());
            if (dataObject.getStreakKind().equals("win")) {
                viewHolder.getDesc3TextView().setText("目前\n连胜\n"+dataObject.getStreakLength()+"场");
            } else {
                viewHolder.getDesc3TextView().setText("目前\n连负\n"+dataObject.getStreakLength()+"场");
            }

        } else if (dataObject.getSportId().equals("1476")) {
            // 足球
            viewHolder.getAllTitleTextView().setVisibility(View.VISIBLE);
            viewHolder.getAllTextView().setVisibility(View.VISIBLE);
            viewHolder.getAllTextView().setText(dataObject.getAll());
            viewHolder.getDesc1TextView().setText("进球数："+dataObject.getGoal()+"\n积分："+dataObject.getIntegral());
            viewHolder.getDesc2TextView().setText("失球数："+dataObject.getLoseGoal()+"\n场次："+dataObject.getCount());
            viewHolder.getDesc3TextView().setText("净胜球："+dataObject.getTrueGoal());
        } else {
            viewHolder.getAllTitleTextView().setVisibility(View.GONE);
            viewHolder.getAllTextView().setVisibility(View.GONE);
            viewHolder.getDesc1TextView().setText("");
            viewHolder.getDesc2TextView().setText("");
            viewHolder.getDesc3TextView().setText("");
        }

        if (area.length() > 0) {
            viewHolder.getTeamTextView().setText(dataObject.getTeamName() + " ( "+ dataObject.getTableArea() +" )");
        } else {
            viewHolder.getTeamTextView().setText(dataObject.getTeamName());
        }

        viewHolder.getSeasonTextView().setText(dataObject.getSeasonCn());
        viewHolder.getRankTextView().setText(dataObject.getTeamRank());
        viewHolder.getWinTextView().setText(dataObject.getWin());
        viewHolder.getLoseTextView().setText(dataObject.getLose());

        return convertView;
    }

}
