package com.yaming.android.sportsevent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class ShooterListViewAdapter extends ArrayAdapter<ShooterObject> {

    private Context context;
    private LayoutInflater layoutInflater;
    private int layoutId;
    private List<ShooterObject> dataList;

    public ShooterListViewAdapter(Context context, int layoutId, List<ShooterObject> dataList) {
        super(context, layoutId, dataList);
        this.context=context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.dataList = dataList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ShooterListViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutId,parent,false);
            viewHolder = new ShooterListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ShooterListViewHolder) convertView.getTag();
        }

        viewHolder.getTeamTextView().setText("");
        viewHolder.getPlayerTextView().setText("");
        viewHolder.getSeasonTextView().setText("");
        viewHolder.getRankTextView().setText("");
        viewHolder.getGoalTextView().setText("");
        viewHolder.getPenaltyTextView().setText("");

        final ShooterObject dataObject = dataList.get(position);

        viewHolder.getPlayerTextView().setText(dataObject.getPlayerName());
        viewHolder.getTeamTextView().setText("( "+dataObject.getTeamName()+" )");
        viewHolder.getSeasonTextView().setText(dataObject.getSeasonCn());
        viewHolder.getRankTextView().setText(dataObject.getRank());
        viewHolder.getGoalTextView().setText(dataObject.getGoal());
        viewHolder.getPenaltyTextView().setText(dataObject.getPenalty());

        return convertView;
    }

}
