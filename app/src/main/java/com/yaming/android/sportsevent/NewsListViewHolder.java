package com.yaming.android.sportsevent;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewsListViewHolder {
    private TextView datetimeTextView,sourceTextView,titleTextView,contentTextView;
    private Button selectButton;
    public NewsListViewHolder(View itemView) {
        datetimeTextView = (TextView) itemView.findViewById(R.id.datetimeTextView);
        sourceTextView = (TextView) itemView.findViewById(R.id.sourceTextView);
        titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        contentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
        selectButton = (Button) itemView.findViewById(R.id.selectButton);
    }

    public TextView getDatetimeTextView() {
        return datetimeTextView;
    }

    public TextView getSourceTextView() {
        return sourceTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getContentTextView() {
        return contentTextView;
    }

    public Button getSelectButton() {
        return selectButton;
    }
}