package com.lakshmibros.fpl.ui.team;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.lakshmibros.fpl.R;
import com.lakshmibros.fpl.data.model.Player;
import com.lakshmibros.fpl.ui.main.SectionsPagerAdapter;
import com.lakshmibros.fpl.utils.DatabaseHandler;

import java.util.ArrayList;

public class TeamSelectionActivity extends AppCompatActivity {

    private String team1;
    private String team2;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String validate
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        team1 = "";
        team2 = "";

        db = new DatabaseHandler(this);
        if(extras != null) {
            team1 = extras.getString("TEAM1");
            team2 = extras.getString("TEAM2");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get batsmen for the teams playing today
        ArrayList<Player> batsmen = db.getPlayersForCurrentDate(team1, team2, "Batsman");
        ArrayList<Player> keepers = db.getPlayersForCurrentDate(team1, team2, "Keeper");

        // Get bowlers for the teams playing today
        ArrayList<Player> bowlers = db.getPlayersForCurrentDate(team1, team2, "Bowler");

        // Get all-rounders for the teams playing today
        ArrayList<Player> allrounders = db.getPlayersForCurrentDate(team1, team2, "Allrounder");
    }
}