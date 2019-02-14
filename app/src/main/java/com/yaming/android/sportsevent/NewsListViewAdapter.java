package com.yaming.android.sportsevent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class NewsListViewAdapter extends ArrayAdapter<NewsObject> {

    private Context context;
    private LayoutInflater layoutInflater;
    private int layoutId;
    private List<NewsObject> dataList;

    public NewsListViewAdapter(Context context, int layoutId, List<NewsObject> dataList) {
        super(context, layoutId, dataList);
        this.context=context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.dataList = dataList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        NewsListViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutId,parent,false);
            viewHolder = new NewsListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NewsListViewHolder) convertView.getTag();
        }

        viewHolder.getDatetimeTextView().setText("");
        viewHolder.getSourceTextView().setText("");
        viewHolder.getTitleTextView().setText("");
        viewHolder.getContentTextView().setText("");
        viewHolder.getSelectButton().setOnClickListener(null);

        final NewsObject dataObject = dataList.get(position);

        viewHolder.getSourceTextView().setText(dataObject.getNewsSource());
        viewHolder.getDatetimeTextView().setText(dataObject.getNewsDatetime());
        viewHolder.getTitleTextView().setText(dataObject.getTitle());
        viewHolder.getContentTextView().setText(dataObject.getContent());
        viewHolder.getSelectButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dataObject.getNewsUrl())));
            }
        });

        return convertView;
    }

}
