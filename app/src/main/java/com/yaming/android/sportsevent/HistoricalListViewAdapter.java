package com.yaming.android.sportsevent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class HistoricalListViewAdapter extends ArrayAdapter<HistoricalObject> {

    private Context context;
    private LayoutInflater layoutInflater;
    private int layoutId;
    private List<HistoricalObject> dataList;

    public HistoricalListViewAdapter(Context context, int layoutId, List<HistoricalObject> dataList) {
        super(context, layoutId, dataList);
        this.context=context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.dataList = dataList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        HistoricalListViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutId,parent,false);
            viewHolder = new HistoricalListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoricalListViewHolder) convertView.getTag();
        }

        viewHolder.getDatetimeTextView().setText("");
        viewHolder.getWeekTextView().setText("");
        viewHolder.getStatusTextView().setText("");
        viewHolder.getTitleTextView().setText("");
        viewHolder.getContentTextView().setText("");
        viewHolder.getResultButton().setOnClickListener(null);
        viewHolder.getLiveButton().setOnClickListener(null);

        final HistoricalObject dataObject = dataList.get(position);

        viewHolder.getDatetimeTextView().setText(dataObject.getDate());
        viewHolder.getWeekTextView().setText(dataObject.getWeek());
        viewHolder.getStatusTextView().setText(dataObject.getStatusCn());
        viewHolder.getTitleTextView().setText(dataObject.getTitle());
        viewHolder.getContentTextView().setText(dataObject.getMatchCity() + " " + dataObject.getStadium());
        if (dataObject.getResultUrl().length() > 0) {
            viewHolder.getResultButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dataObject.getResultUrl())));
                }
            });
        }

        if (dataObject.getLiveUrl().length() > 0) {
            viewHolder.getLiveButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dataObject.getLiveUrl())));
                }
            });
        }

        return convertView;
    }

}
