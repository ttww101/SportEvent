package com.yaming.android.sportsevent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class MainListViewAdapter extends ArrayAdapter<MainObject> {

    private Context context;
    private LayoutInflater layoutInflater;
    private int layoutId;
    private List<MainObject> dataList;

    public MainListViewAdapter(Context context, int layoutId, List<MainObject> dataList) {
        super(context, layoutId, dataList);
        this.context=context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.dataList = dataList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        MainListViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutId,parent,false);
            viewHolder = new MainListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MainListViewHolder) convertView.getTag();
        }

        viewHolder.getTitleTextView().setText("");
        viewHolder.getTitleTextView().setOnClickListener(null);

        final MainObject dataObject = dataList.get(position);

        viewHolder.getTitleTextView().setText(dataObject.getTitle());
        viewHolder.getTitleTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dataObject.getContentUrl())));
            }
        });

        return convertView;
    }

}
