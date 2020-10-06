package com.lakshmibros.fpl.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lakshmibros.fpl.data.constants.DBConstants;
import com.lakshmibros.fpl.data.model.DailyTeam;
import com.lakshmibros.fpl.data.model.Match;
import com.lakshmibros.fpl.data.model.Player;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private String TAG = "DatabaseHandler";

    public DatabaseHandler(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FIXTURES_TABLE = "CREATE TABLE IF NOT EXISTS " + DBConstants.FIXTURES_TABLE_NAME + "("
                + "MATCH_NUM INTEGER PRIMARY KEY,"
                + "DATE TEXT,"
                + "TEAM1 TEXT,"
                + "TEAM2 TEXT,"
                + "VENUE TEXT"
                + ")";
        db.execSQL(CREATE_FIXTURES_TABLE);

        String CREATE_PLAYERS_TABLE = "CREATE TABLE IF NOT EXISTS " + DBConstants.PLAYERS_TABLE_NAME + "("
                + "NAME TEXT,"
                + "TEAM TEXT,"
                + "ROLE TEXT,"
                + "COUNTRY TEXT,"
                + "IS_UNCAPPED INT"
                + ")";
        db.execSQL(CREATE_PLAYERS_TABLE);

        String CREATE_DAILY_TEAM_TABLE = "CREATE TABLE IF NOT EXISTS " + DBConstants.DAILY_TEAM_TABLE_NAME + "("
                + "MATCH_NUM INTEGER PRIMARY KEY,"
                + "PLAYER_1 TEXT,"
                + "PLAYER_2 TEXT,"
                + "PLAYER_3 TEXT,"
                + "PLAYER_4 TEXT,"
                + "PLAYER_5 TEXT,"
                + "PLAYER_6 TEXT,"
                + "PLAYER_7 TEXT,"
                + "PLAYER_8 TEXT,"
                + "PLAYER_9 TEXT,"
                + "PLAYER_10 TEXT,"
                + "PLAYER_11 TEXT,"
                + "CAPTAIN TEXT,"
                + "POWER_PLAYER TEXT,"
                + "WINNING_TEAM TEXT,"
                + "FOREIGN KEY (MATCH_NUM) REFERENCES " + DBConstants.FIXTURES_TABLE_NAME + "(MATCH_NUM)"
                + ")";
        db.execSQL(CREATE_DAILY_TEAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Dropping the relevant tables
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.FIXTURES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.PLAYERS_TABLE_NAME);

        // Creating the tables again
        onCreate(db);
    }

    public void insertMatches(ArrayList<Match> matches) {
        if(matches == null) return;

        SQLiteDatabase db = this.getWritableDatabase();
        for (Match match: matches) {
            ContentValues values = new ContentValues();
            values.put("MATCH_NUM", match.getMatchNumber());
            values.put("DATE", match.getDate());
            values.put("TEAM1", match.getTeam1());
            values.put("TEAM2", match.getTeam2());
            values.put("VENUE", match.getVenue());
            db.insert(DBConstants.FIXTURES_TABLE_NAME, null, values);
        }
        db.close();
    }

    public void insertPlayers(ArrayList<Player> players) {
        if(players == null) return;

        SQLiteDatabase db = this.getWritableDatabase();
        for (Player player: players) {
            ContentValues values = new ContentValues();
            values.put("NAME", player.getName());
            values.put("TEAM", player.getTeam());
            values.put("ROLE", player.getRole());
            values.put("COUNTRY", player.getCountry());
            values.put("IS_UNCAPPED", player.isUncapped());
            db.insert(DBConstants.PLAYERS_TABLE_NAME, null, values);
        }
        db.close();
    }

    public void insertDailySelection(DailyTeam ob) {
        if(ob == null) return;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MATCH_NUM", ob.getMatchNumber());
        values.put("PLAYER_1", ob.getTeam().get(0));
        values.put("PLAYER_2", ob.getTeam().get(1));
        values.put("PLAYER_3", ob.getTeam().get(2));
        values.put("PLAYER_4", ob.getTeam().get(3));
        values.put("PLAYER_5", ob.getTeam().get(4));
        values.put("PLAYER_6", ob.getTeam().get(5));
        values.put("PLAYER_7", ob.getTeam().get(6));
        values.put("PLAYER_8", ob.getTeam().get(7));
        values.put("PLAYER_9", ob.getTeam().get(8));
        values.put("PLAYER_10", ob.getTeam().get(9));
        values.put("PLAYER_11", ob.getTeam().get(10));
        values.put("CAPTAIN", ob.getCaptain());
        values.put("POWER_PLAYER", ob.getPowerPlayer());
        values.put("WINNING_TEAM", ob.getWinningTeam());
        db.insert(DBConstants.DAILY_TEAM_TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Match> getMatchForCurrentDate(String currentDate) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Match> matches = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DBConstants.FIXTURES_TABLE_NAME + " where DATE = '" + currentDate + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int matchNum = Integer.parseInt(cursor.getString(0));
                    String date = cursor.getString(1);
                    String team1 = cursor.getString(2);
                    String team2 = cursor.getString(3);
                    String venue = cursor.getString(4);
                    Match match = new Match(matchNum, date, team1, team2, venue);
                    matches.add(match);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception while reading the cursor");
            } finally {
                try {
                    cursor.close();
                    db.close();
                } catch (Exception e) {
                    Log.e(TAG, "Exception while closing the cursor/db");
                }
            }
        }

        return matches;
    }

    public ArrayList<Player> getPlayersForCurrentDate(String team1, String team2, String playerRole) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Player> players = new ArrayList<>();

        String selectQuery =
                "SELECT * FROM " + DBConstants.PLAYERS_TABLE_NAME + " where TEAM in ('" + team1 + "','" + team2 + "') and ROLE = '" + playerRole + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    String team = cursor.getString(1);
                    String role = cursor.getString(2);
                    String country = cursor.getString(3);
                    int isUncapped = cursor.getInt(4);
                    Player player = new Player(name, role, team, country, isUncapped);
                    players.add(player);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception while reading the cursor");
            } finally {
                try {
                    cursor.close();
                    db.close();
                } catch (Exception e) {
                    Log.e(TAG, "Exception while closing the cursor/db");
                }
            }
        }

        return players;
    }
}
