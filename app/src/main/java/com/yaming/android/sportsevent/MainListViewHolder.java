package com.yaming.android.sportsevent;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainListViewHolder {
    private TextView titleTextView;
    public MainListViewHolder(View itemView) {
        titleTextView = (TextView) itemView.findViewById(R.id.textView);
    }
    public TextView getTitleTextView() {
        return titleTextView;
    }
}