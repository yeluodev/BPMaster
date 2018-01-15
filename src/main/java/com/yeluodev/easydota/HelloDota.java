package com.yeluodev.easydota;

import com.google.gson.Gson;
import com.yeluodev.easydota.api.EasyDota;
import com.yeluodev.easydota.db.DBManager;
import com.yeluodev.easydota.entity.MatchDetailEntity;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.yeluodev.easydota.api.EasyDota.KEY;

public class HelloDota {

    private static Connection conn;
    private static Long matchId = 1L;

    public static void main(String[] args){
        conn = DBManager.connectDB();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                testGetMatchDetails("match_details",5796L,6000L);
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                testGetMatchDetails("match_details2",7484L,8000L);
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                testGetMatchDetails("match_details3",9801L,10000L);
//            }
//        }).start();
        testGetMatchHistroyByLeagueId();

    }

    private static void testGetLeagueListing(){
        EasyDota.getInstance().getSteamService().getLeagueListing(KEY)
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody s) {
                        try {
                            String res = s.string().replaceAll("\\n", "");
                            System.out.println(res);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private static void testGetMatchHistroyByLeagueId(){
        EasyDota.getInstance().getSteamService().getMatchHistroy(KEY, null, null, null, null, null, "9584", null, null)
                .flatMap((Function<ResponseBody, ObservableSource<JSONObject>>) responseBody -> {
                    try {
                        String res = responseBody.string().replaceAll("\\n", "");
                        JSONObject jsonObject = new JSONObject(res);
                        JSONArray matches = jsonObject.optJSONObject("result").optJSONArray("matches");
                        if(matches!=null && matches.length()>0){
                            ArrayList list = new ArrayList();
                            for (int i=0;i<matches.length();i++){
                                list.add(matches.optJSONObject(i));
                            }
                            return Observable.fromIterable(list);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Observable.empty();
                })
                .subscribe(new DisposableObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        try {
                            String matchId = jsonObject.optString("match_id");
                            long id = Long.valueOf(matchId);
                            if(matchId !=null && !matchId.equals("")){
                                testGetMatchDetails("match_details4",id,id);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private static void insertMatchToDB(String tableName,MatchDetailEntity entity,String[] arr){
        String sql = "insert into easydota2." +
                tableName +
                "(`radiant_win`, `duration`, `pre_game_duration`, `start_time`, `match_id`," +
                " `match_seq_num`, `tower_status_radiant`, `tower_status_dire`, `barracks_status_radiant`, `barracks_status_dire`," +
                " `cluster`, `first_blood_time`, `lobby_type`, `human_players`, `leagueid`," +
                " `positive_votes`, `negative_votes`, `game_mode`, `flags`, `engine`," +
                " `radiant_score`, `dire_score`, `radiant_team_id`, `radiant_name`, `radiant_logo`," +
                " `radiant_team_complete`, `dire_team_id`, `dire_name`, `dire_logo`, `dire_team_complete`," +
                " `radiant_captain`, `dire_captain`, `players`, `picks_bans`, `radiant_team`," +
                " `dire_team`) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement ptst = conn.prepareStatement(sql);
            ptst.setInt(1, entity.isRadiant_win()?1:0);
            ptst.setInt(2, entity.getDuration());
            ptst.setInt(3, entity.getPre_game_duration());
            ptst.setInt(4, entity.getStart_time());
            ptst.setLong(5, entity.getMatch_id());
            ptst.setLong(6, entity.getMatch_seq_num());
            ptst.setInt(7, entity.getTower_status_radiant());
            ptst.setInt(8, entity.getTower_status_dire());
            ptst.setInt(9, entity.getBarracks_status_radiant());
            ptst.setInt(10, entity.getBarracks_status_dire());
            ptst.setInt(11, entity.getCluster());
            ptst.setInt(12, entity.getFirst_blood_time());
            ptst.setInt(13, entity.getLobby_type());
            ptst.setInt(14, entity.getHuman_players());
            ptst.setInt(15, entity.getLeagueid());
            ptst.setInt(16, entity.getPositive_votes());
            ptst.setInt(17, entity.getNegative_votes());
            ptst.setInt(18, entity.getGame_mode());
            ptst.setInt(19, entity.getFlags());
            ptst.setInt(20, entity.getEngine());
            ptst.setInt(21, entity.getRadiant_score());
            ptst.setInt(22, entity.getDire_score());

            ptst.setInt(23, entity.getRadiant_team_id());
            ptst.setString(24, entity.getRadiant_name());
            ptst.setLong(25, entity.getRadiant_logo());
            ptst.setInt(26, entity.getRadiant_team_complete());
            ptst.setInt(27, entity.getDire_team_id());
            ptst.setString(28, entity.getDire_name());
            ptst.setLong(29, entity.getDire_logo());
            ptst.setInt(30, entity.getDire_team_complete());
            ptst.setInt(31, entity.getRadiant_captain());
            ptst.setInt(32, entity.getDire_captain());
            ptst.setString(33, arr[0]);
            ptst.setString(34, arr[1]);
            ptst.setString(35, arr[2]);
            ptst.setString(36, arr[3]);

            ptst.executeUpdate();
            ptst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testGetMatchDetails(String tableName,long matchId,long endMatchId) {
        if(matchId>endMatchId){
            return;
        }

        EasyDota.getInstance().getSteamService().getMatchDetails(KEY, matchId)
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody s) {
                        try {
                            String res = s.string().replaceAll("\\n", "");
                            System.out.println(res);
                            JSONObject object = new JSONObject(res).optJSONObject("result");
                            if(object!=null){
                                Gson gson = new Gson();
                                MatchDetailEntity match = gson.fromJson(object.toString(),MatchDetailEntity.class);
                                if(match!=null && !object.has("error")){
                                    String[] arr = new String[4];
                                    arr[0] = object.optString("players");
                                    arr[1] = object.optString("picks_bans");
                                    arr[2] = object.optString("radiant_team");
                                    arr[3] = object.optString("dire_team");
                                    insertMatchToDB(tableName,match,arr);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        testGetMatchDetails(tableName,matchId+1,endMatchId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        Log.d("yeluo", "complete");
                    }
                });
    }
}
