package com.sylleryum.spotifycleaner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sylleryum.spotifycleaner.model.AccessToken;
import com.sylleryum.spotifycleaner.model.ClearType;
import com.sylleryum.spotifycleaner.model.FullTrackDetails;
import com.sylleryum.spotifycleaner.model.History;
import com.sylleryum.spotifycleaner.model.exception.ClearPlaylistException;
import com.sylleryum.spotifycleaner.model.exception.MissingTokenException;
import com.sylleryum.spotifycleaner.model.jsonResponses.UserPlaylists;
import com.sylleryum.spotifycleaner.model.spotify.playlists.PlaylistItem;
import com.sylleryum.spotifycleaner.model.spotify.singlePlaylist.Item;
import com.sylleryum.spotifycleaner.model.spotify.user.User;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface ServiceApi {


    AccessToken getAccessToken(String theCode) throws MissingTokenException, URISyntaxException;

    User getUserDetails(AccessToken accessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    History getHistory(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    String getLastItemPlayed(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    PlaylistItem createPlaylist(String playlistName, AccessToken accessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    AccessToken setRefresh(String refresh) throws MissingTokenException, URISyntaxException;

    String orderPlaylistRandom(String playlistId, AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    boolean clearHistory(String playlistId, ClearType clearType, AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException, ClearPlaylistException;

    String getCurrentPlaying(AccessToken accessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    /**
     * @param playlistId
     * @return singleton map where key=playlist name and value=list<Item>
     * @throws MissingTokenException
     * @throws URISyntaxException
     */
    Map<String, List<Item>> getPlaylistTracks(String playlistId, AccessToken accessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    List<FullTrackDetails> getUnavailables(String playlistId, AccessToken accessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    boolean pausePlayback(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    boolean nextTrack(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException;

    boolean previousTrack(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException;

    boolean callPost(String url, AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException;

    /**
     *
     * @param accessToken
     * @return returns a sorted order of {@link UserPlaylists} from current user
     * @throws MissingTokenException
     * @throws URISyntaxException
     * @throws JsonProcessingException
     */
    List<UserPlaylists> getPlaylists(AccessToken accessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException;

    //    PlaylistItem createPlaylist(String playlistName) throws MissingTokenException, URISyntaxException;
//    AccessToken beforeCall(AccessToken accessToken) throws MissingTokenException, URISyntaxException;
//    /**
//     * get playlists owned by user
//     *
//     * @return
//     */

//    RestTemplate interceptorRest();
}
