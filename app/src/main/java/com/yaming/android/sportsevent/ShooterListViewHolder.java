package com.yaming.android.sportsevent;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShooterListViewHolder {
    private TextView teamTextView,playerTextView,seasonTextView,rankTitleTextView,rankTextView;
    private TextView goalTitleTextView,goalTextView,penaltyTitleTextView,penaltyTextView;
    public ShooterListViewHolder(View itemView) {
        teamTextView = (TextView) itemView.findViewById(R.id.teamTextView);
        playerTextView = (TextView) itemView.findViewById(R.id.playerTextView);
        seasonTextView = (TextView) itemView.findViewById(R.id.seasonTextView);
        rankTitleTextView = (TextView) itemView.findViewById(R.id.rankTitleTextView);
        rankTextView = (TextView) itemView.findViewById(R.id.rankTextView);
        goalTitleTextView = (TextView) itemView.findViewById(R.id.goalTitleTextView);
        goalTextView = (TextView) itemView.findViewById(R.id.goalTextView);
        penaltyTitleTextView = (TextView) itemView.findViewById(R.id.penaltyTitleTextView);
        penaltyTextView = (TextView) itemView.findViewById(R.id.penaltyTextView);
    }

    public TextView getTeamTextView() {
        return teamTextView;
    }

    public TextView getPlayerTextView() {
        return playerTextView;
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

    public TextView getGoalTitleTextView() {
        return goalTitleTextView;
    }

    public TextView getGoalTextView() {
        return goalTextView;
    }

    public TextView getPenaltyTitleTextView() {
        return penaltyTitleTextView;
    }

    public TextView getPenaltyTextView() {
        return penaltyTextView;
    }
}