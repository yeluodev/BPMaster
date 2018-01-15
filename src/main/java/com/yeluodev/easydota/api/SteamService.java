package com.yeluodev.easydota.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SteamService {

    /**
     * 获取联赛列表
     *
     * @param key
     * @return
     */
    @GET("IDOTA2Match_570/GetLeagueListing/v1")
    Observable<ResponseBody> getLeagueListing(
            @Query("key") String key
    );

    /**
     * 获取正在进行中的DOTA2比赛
     *
     * @param key
     * @param leagueId
     * @param matchId
     * @return
     */
    @GET("IDOTA2Match_570/GetLiveLeagueGames/v1")
    Observable<ResponseBody> getLiveLeagueGames(
            @Query("key") String key,
            @Query("league_id") Long leagueId,  //可选
            @Query("match_id") Long matchId     //可选
    );

    /**
     * 获取比赛详细数据
     *
     * @param key
     * @param matchId
     * @return
     */
    @GET("IDOTA2Match_570/GetMatchDetails/v1")
    Observable<ResponseBody> getMatchDetails(
            @Query("key") String key,
            @Query("match_id") Long matchId   //可选
    );

    /**
     * 获取比赛历史信息
     *
     * @param key
     * @param heroId
     * @param gameMode
     * @param skill
     * @param minPlayers
     * @param accountId
     * @param leagueId
     * @param startAtMatchId
     * @param matchesRequested
     * @return
     */
    @GET("IDOTA2Match_570/GetMatchHistory/v1")
    Observable<ResponseBody> getMatchHistroy(
            @Query("key") String key,
            @Query("hero_id") String heroId,                        //可选
            @Query("game_mode") String gameMode,                    //可选
            @Query("skill") String skill,                           //可选
            @Query("min_players") String minPlayers,                //可选
            @Query("account_id") String accountId,                  //可选
            @Query("league_id") String leagueId,                    //可选
            @Query("start_at_match_id") String startAtMatchId,      //可选
            @Query("matches_requested") String matchesRequested     //可选
    );

    /**
     * 根据match seq number得到比赛历史信息
     *
     * @param key
     * @param startAtMatchSeqNum
     * @param matchesRequested
     * @return
     */
    @GET("IDOTA2Match_570/GetMatchHistoryBySequenceNum/v1")
    Observable<ResponseBody> getMatchHistoryBySequenceNum(
            @Query("key") String key,
            @Query("start_at_match_seq_num") String startAtMatchSeqNum,      //可选
            @Query("matches_requested") String matchesRequested              //可选
    );

    @GET("ISteamUser/GetPlayerSummaries/v2")
    Observable<ResponseBody> getPlayerSummaries(
            @Query("key") String key,
            @Query("steamids") String steamids      //可选
    );


}
