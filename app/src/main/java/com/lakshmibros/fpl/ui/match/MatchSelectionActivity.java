package com.lakshmibros.fpl.ui.match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.lakshmibros.fpl.R;
import com.lakshmibros.fpl.data.model.Match;
import com.lakshmibros.fpl.data.model.Player;
import com.lakshmibros.fpl.ui.team.TeamSelectionActivity;
import com.lakshmibros.fpl.utils.DatabaseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MatchSelectionActivity extends AppCompatActivity {

    private String TAG = "MatchSelectionActivity";
    private DatabaseHandler db;
    private ArrayList<Match> matchesToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_selection);

        final Button singleHeaderMatch = findViewById(R.id.single_header_match);
        final Button doubleHeaderMatch1 = findViewById(R.id.double_header_match_1);
        final Button doubleHeaderMatch2 = findViewById(R.id.double_header_match_2);
        final ProgressBar loadingProgressBar = findViewById(R.id.db_loading);

        singleHeaderMatch.setVisibility(View.GONE);
        doubleHeaderMatch1.setVisibility(View.GONE);
        doubleHeaderMatch2.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.GONE);

        db = new DatabaseHandler(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("isDbSetupDone", false)) {
            loadingProgressBar.setVisibility(View.VISIBLE); // Show progress bar
            databaseSetup();
            loadingProgressBar.setVisibility(View.GONE); // Hide progress bar
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isDbSetupDone", true);
            editor.apply();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        matchesToday = db.getMatchForCurrentDate(currentDate);

        if(matchesToday != null) {
            switch(matchesToday.size()) {
                case 1:
                    singleHeaderMatch.setVisibility(View.VISIBLE);
                    doubleHeaderMatch1.setVisibility(View.INVISIBLE);
                    doubleHeaderMatch1.setEnabled(false);
                    Match match = matchesToday.get(0);
                    String displayString = "Match " + match.getMatchNumber() + ": " + match.getTeam1() + " VS " + match.getTeam2();
                    singleHeaderMatch.setText(displayString);
                    break;
                case 2:
                    doubleHeaderMatch1.setVisibility(View.VISIBLE);
                    Match match1 = matchesToday.get(0);
                    String displayString1 = "Match " + match1.getMatchNumber() + ": " + match1.getTeam1() + " VS " + match1.getTeam2();
                    doubleHeaderMatch1.setText(displayString1);

                    doubleHeaderMatch2.setVisibility(View.VISIBLE);
                    Match match2 = matchesToday.get(1);
                    String displayString2 = "Match " + match2.getMatchNumber() + ": " + match2.getTeam1() + " VS " + match2.getTeam2();
                    doubleHeaderMatch2.setText(displayString2);
                    break;
            }
        }

        singleHeaderMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeamSelectionActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TEAM1", matchesToday.get(0).getTeam1());
                extras.putString("TEAM2", matchesToday.get(0).getTeam2());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        doubleHeaderMatch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeamSelectionActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TEAM1", matchesToday.get(0).getTeam1());
                extras.putString("TEAM2", matchesToday.get(0).getTeam2());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        doubleHeaderMatch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeamSelectionActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TEAM1", matchesToday.get(1).getTeam1());
                extras.putString("TEAM2", matchesToday.get(1).getTeam2());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    private void databaseSetup() {
        // Insert the schedule into DB
        ArrayList<Match> matches = readScheduleFromAssets();
        db.insertMatches(matches);

        // Insert the players into DB
        ArrayList<Player> players = readPlayersFromAssets();
        db.insertPlayers(players);
    }

    private ArrayList<Match> readScheduleFromAssets() {
        BufferedReader reader = null;
        ArrayList<Match> matches = new ArrayList<>();

        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("Schedule.txt")));
            String line;
            int matchNum = 0;
            while ((line = reader.readLine()) != null) {
                matchNum++;
                String[] parts = line.split(",");
                if(parts.length == 4) {
                    String date = parts[0];
                    String team1 = parts[1];
                    String team2 = parts[2];
                    String venue = parts[3];
                    Match match = new Match(matchNum, date, team1, team2, venue);
                    matches.add(match);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Exception while reading the file from assets folder");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing the file");
                }
            }
        }

        return matches;
    }

    private ArrayList<Player> readPlayersFromAssets() {
        BufferedReader reader = null;
        ArrayList<Player> players = new ArrayList<>();

        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("Players.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length == 5) {
                    String name = parts[0];
                    String role = parts[1];
                    String team = parts[2];
                    String country = parts[3];
                    String uncapped = parts[4];
                    int isUncapped = 0;
                    if(country.equalsIgnoreCase("IND") && uncapped.equalsIgnoreCase(("Yes")))
                        isUncapped = 1;
                    Player player = new Player(name, role, team, country, isUncapped);
                    players.add(player);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Exception while reading the file from assets folder");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing the file");
                }
            }
        }

        return players;
    }
}