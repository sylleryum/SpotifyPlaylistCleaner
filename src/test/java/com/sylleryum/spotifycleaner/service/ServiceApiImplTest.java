package com.sylleryum.spotifycleaner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sylleryum.spotifycleaner.model.AccessToken;
import com.sylleryum.spotifycleaner.model.FullTrackDetails;
import com.sylleryum.spotifycleaner.model.exception.MissingTokenException;
import com.sylleryum.spotifycleaner.model.jsonResponses.UserPlaylists;
import com.sylleryum.spotifycleaner.model.spotify.playlists.PlaylistItem;
import com.sylleryum.spotifycleaner.model.spotify.singlePlaylist.Item;
import com.sylleryum.spotifycleaner.model.spotify.user.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ServiceApiImplTest {
    /*unavailable
    0OYGSvSFCW5NZEvn2svFLS*//*
    *//*fav
    5Bo3G9pauPpd0oSPPTVufY*//*
    *//*
    https://accounts.spotify.com/authorize?client_id=ee81813e9b884f6cacdb265e6c6ebd40&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fv1%2Fcallback&scope=user-read-private%20user-read-email%20playlist-read-private%20playlist-modify-public%20playlist-modify-private
    *//*
    @Autowired
    ServiceApi serviceApi;
    static AccessToken accessToken;


    @BeforeAll
    static void init(@Autowired ServiceApi serviceApi) throws MissingTokenException, URISyntaxException {
        accessToken = serviceApi.setRefresh("");
    }

    @Test
    void getUserDetails() throws MissingTokenException, URISyntaxException, JsonProcessingException {
        User userDetails = serviceApi.getUserDetails(accessToken);//act

        assertThat(userDetails.getId()).isNotEmpty();
    }

    @Test
    @DisplayName("getPlaylistTracks() Is singleton and contain tracks")
    void getPlaylistTracks() throws MissingTokenException, URISyntaxException, JsonProcessingException {
        String unavailablePlaylist = "0OYGSvSFCW5NZEvn2svFLS";//arrange

        Map<String, List<Item>> playlistTracks = serviceApi.getPlaylistTracks(unavailablePlaylist, accessToken);//act

        assertThat(playlistTracks.size()).isEqualTo(1);
        assertThat(playlistTracks.values().stream().findFirst().get().size()).isGreaterThan(0);
    }


    @Test
    @DisplayName("there are unavailable tracks")
    void getPositiveUnavailables() throws MissingTokenException, URISyntaxException, JsonProcessingException {
        String unavailablePlaylist = "0OYGSvSFCW5NZEvn2svFLS";
        List<FullTrackDetails> unavailables = serviceApi.getUnavailables(unavailablePlaylist, accessToken);//act

        assertThat(unavailables.size()).isNotZero();
    }

    @Test
    @DisplayName("there is no unavailable tracks")
    void getNegativeUnavailables() throws MissingTokenException, URISyntaxException, JsonProcessingException {
        String noUnavailable = "4GSGiaXmh0WRE9b0pK2r1F";
        List<FullTrackDetails> unavailables = serviceApi.getUnavailables(noUnavailable, accessToken);//act

        assertThat(unavailables.size()).isZero();
    }

    @Test
    void orderPlaylistRandom() throws MissingTokenException, URISyntaxException, JsonProcessingException {
        String unavailablePlaylist = "0OYGSvSFCW5NZEvn2svFLS";
        String randomPlaylistCreated = serviceApi.orderPlaylistRandom(unavailablePlaylist, accessToken);

        assertThat(randomPlaylistCreated).isNotNull();
    }

    @Test
    void getPlaylists() throws MissingTokenException, URISyntaxException, JsonProcessingException {
        List<UserPlaylists> playlists = serviceApi.getPlaylists(accessToken);

        assertThat(playlists.get(0)).isNotNull();
    }


    @Test
    @Disabled
    void createPlaylist() throws MissingTokenException, URISyntaxException, JsonProcessingException {
        PlaylistItem playlist = serviceApi.createPlaylist("test name", accessToken);//act

        assertThat(playlist).isNotNull();
    }

    @Test
    void name() {
        int numero = 5;
        int res = prova(5);

        System.out.println();
    }

    int prova(int num) {
        if (num <= 1) {
            return 1;
        }
        int p = prova(num - 1);
        return p * num;
    }
*/
}