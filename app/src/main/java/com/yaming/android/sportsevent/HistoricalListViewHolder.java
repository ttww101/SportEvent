package com.yaming.android.sportsevent;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HistoricalListViewHolder {
    private TextView datetimeTextView,weekTextView,statusTextView,titleTextView,contentTextView;
    private Button resultButton,liveButton;
    public HistoricalListViewHolder(View itemView) {
        datetimeTextView = (TextView) itemView.findViewById(R.id.datetimeTextView);
        weekTextView = (TextView) itemView.findViewById(R.id.weekTextView);
        statusTextView = (TextView) itemView.findViewById(R.id.statusTextView);
        titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        contentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
        resultButton = (Button) itemView.findViewById(R.id.resultButton);
        liveButton = (Button) itemView.findViewById(R.id.liveButton);
    }

    public TextView getDatetimeTextView() {
        return datetimeTextView;
    }

    public TextView getWeekTextView() {
        return weekTextView;
    }

    public TextView getStatusTextView() {
        return statusTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getContentTextView() {
        return contentTextView;
    }

    public Button getResultButton() {
        return resultButton;
    }

    public Button getLiveButton() {
        return liveButton;
    }
}