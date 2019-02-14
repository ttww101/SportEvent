package com.yaming.android.sportsevent;

import android.view.View;
import android.widget.TextView;

public class RankingListViewHolder {
    private TextView teamTextView,seasonTextView,rankTitleTextView,rankTextView;
    private TextView winTitleTextView,winTextView,loseTitleTextView,loseTextView,allTitleTextView,allTextView;
    private TextView desc1TextView,desc2TextView,desc3TextView;
    public RankingListViewHolder(View itemView) {
        teamTextView = (TextView) itemView.findViewById(R.id.teamTextView);
        seasonTextView = (TextView) itemView.findViewById(R.id.seasonTextView);
        rankTitleTextView = (TextView) itemView.findViewById(R.id.rankTitleTextView);
        rankTextView = (TextView) itemView.findViewById(R.id.rankTextView);
        winTitleTextView = (TextView) itemView.findViewById(R.id.winTitleTextView);
        winTextView = (TextView) itemView.findViewById(R.id.winTextView);
        loseTitleTextView = (TextView) itemView.findViewById(R.id.loseTitleTextView);
        loseTextView = (TextView) itemView.findViewById(R.id.loseTextView);
        allTitleTextView = (TextView) itemView.findViewById(R.id.allTitleTextView);
        allTextView = (TextView) itemView.findViewById(R.id.allTextView);
        desc1TextView = (TextView) itemView.findViewById(R.id.desc1TextView);
        desc2TextView = (TextView) itemView.findViewById(R.id.desc2TextView);
        desc3TextView = (TextView) itemView.findViewById(R.id.desc3TextView);
    }

    public TextView getTeamTextView() {
        return teamTextView;
    }

    public TextView getSeasonTextView() {
        return seasonTextView;
    }

    public TextView getRankTitleTextView() {
        return rankTitleTextView;
    }

    public TextView getRankTextView() {
        return rankTextView;
    }

    public TextView getWinTitleTextView() {
        return winTitleTextView;
    }

    public TextView getWinTextView() {
        return winTextView;
    }

    public TextView getLoseTitleTextView() {
        return loseTitleTextView;
    }

    public TextView getLoseTextView() {
        return loseTextView;
    }

    public TextView getAllTitleTextView() {
        return allTitleTextView;
    }

    public TextView getAllTextView() {
        return allTextView;
    }

    public TextView getDesc1TextView() {
        return desc1TextView;
    }

    public TextView getDesc2TextView() {
        return desc2TextView;
    }

    public TextView getDesc3TextView() {
        return desc3TextView;
    }
}