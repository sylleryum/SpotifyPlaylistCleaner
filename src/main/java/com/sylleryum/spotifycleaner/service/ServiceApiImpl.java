package com.sylleryum.spotifycleaner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sylleryum.spotifycleaner.config.Endpoints;
import com.sylleryum.spotifycleaner.helper.SpotifyObjectsConverter;
import com.sylleryum.spotifycleaner.helper.TraceIdGenerator;
import com.sylleryum.spotifycleaner.model.*;
import com.sylleryum.spotifycleaner.model.currentPlaying.CurrentPlaying;
import com.sylleryum.spotifycleaner.model.exception.ClearPlaylistException;
import com.sylleryum.spotifycleaner.model.exception.MissingTokenException;
import com.sylleryum.spotifycleaner.model.jsonResponses.UserPlaylists;
import com.sylleryum.spotifycleaner.model.playlistTracks.PlaylistTracks;
import com.sylleryum.spotifycleaner.model.spotify.playlists.PlaylistItem;
import com.sylleryum.spotifycleaner.model.spotify.playlists.PlaylistList;
import com.sylleryum.spotifycleaner.model.spotify.singlePlaylist.Item;
import com.sylleryum.spotifycleaner.model.spotify.singlePlaylist.PreSinglePlaylist;
import com.sylleryum.spotifycleaner.model.spotify.singlePlaylist.SinglePlaylist;
import com.sylleryum.spotifycleaner.model.spotify.user.User;
import com.sylleryum.spotifycleaner.model.trackToDelete.Track;
import com.sylleryum.spotifycleaner.model.trackToDelete.Tracks;
import io.netty.handler.logging.LogLevel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static com.sylleryum.spotifycleaner.helper.TraceIdGenerator.METHOD_NAME_NOT_FOUND;
import static com.sylleryum.spotifycleaner.helper.TraceIdGenerator.writeDebug;

@Service
public class ServiceApiImpl implements ServiceApi {

    //TODO remove
    private RestTemplate template = new RestTemplate();
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ServiceApiImpl.class);
    private AccessToken globalAccessToken;
    private final Endpoints endpoints;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    @Value("${playlist.ignore}")
    private String[] plalistsToIgnore;
    @Value("${spotify.refresh.token}")
    private Optional<String> refreshToken;

    public ServiceApiImpl(Endpoints endpoints, WebClient webClient, ObjectMapper objectMapper) {
        this.endpoints = endpoints;
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }


    @Override
    public AccessToken getAccessToken(String theCode) throws MissingTokenException, URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> bodyParameters = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> requestEntity;
//        cleaner();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", endpoints.authorizationToAccess());

        bodyParameters.add("grant_type", "authorization_code");
        bodyParameters.add("code", theCode);
        bodyParameters.add("redirect_uri", endpoints.redirectUrl);
        requestEntity = new HttpEntity<>(bodyParameters, headers);

        return tokenCall(requestEntity, null);
    }

    /**
     * get users details and set the current user in the service
     *
     * @return User
     */
    @Override
    public User getUserDetails(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);

        User u = callApiGet(endpoints.userDetails, User.class, accessToken);

        return u;
    }

    @Override
    public History getHistory(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);

        History h = callApiGet(endpoints.history, History.class, accessToken);

        return h;
    }


    @Override
    public PlaylistItem createPlaylist(String playlistName, AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);

        User user = getUserDetails(accessToken);

        PlaylistItem result = webClient.post()
                .uri(endpoints.createPlaylist(user.getId()))
//                .headers(httpHeaders -> httpHeaders.addAll(this.headers))
                .header("Authorization", "Bearer " + accessToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new CreatePlaylist(playlistName)), CreatePlaylist.class)
                .retrieve()
                .bodyToMono(PlaylistItem.class).block();

        TraceIdGenerator.writeDebug("Playlist created: " + playlistName, ServiceApiImpl.class, StackWalker.getInstance().walk(frames -> frames.findFirst().map(StackWalker.StackFrame::getMethodName)).orElse(METHOD_NAME_NOT_FOUND));
        return result;
    }

    @Override
    public String orderPlaylistRandom(String playlistId, AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        //getPlaylistTracks(playlistId).values().stream().findFirst().get()
        AccessToken accessToken = beforeCall(currentAccessToken);

        Map<String, List<Item>> playlistTracks = getPlaylist(playlistId, accessToken);
        List<String> uriList = playlistTracks.values().stream().findFirst().get()
                .stream().map(item -> item.getTrack().getUri()).collect(Collectors.toList());

        String playlistName = playlistTracks.keySet().stream().findFirst().orElse("playlist") + " Random";
        PlaylistItem playlistItem = createPlaylist(playlistName, accessToken);

        Collections.shuffle(uriList);

        List<String> tooBigList = new ArrayList<>();
        while (uriList.size() > 1) {
            if (uriList.size() < 99) {
                this.webClient.post()
                        .uri(endpoints.managePlaylist(playlistItem.getId()))
//                        .headers(httpHeaders -> httpHeaders.addAll(this.headers))
                        .header("Authorization", "Bearer " + accessToken.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new Uri(uriList)), Uri.class)
                        .retrieve()
                        .bodyToMono(String.class).block();
                return playlistName;
            }
            tooBigList = uriList.subList(0, 99);
            uriList = uriList.subList(99, uriList.size());
            this.webClient.post()
                    .uri(endpoints.managePlaylist(playlistItem.getId()))
//                    .headers(httpHeaders -> httpHeaders.addAll(this.headers))
                    .header("Authorization", "Bearer " + accessToken.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(new Uri(tooBigList)), Uri.class)
                    .retrieve()
                    .bodyToMono(String.class).block();
        }
        return playlistName;
    }

    /**
     * delete all the tracks from the provided playlistId until before the last played track or current playing track
     *
     * @param playlistId
     * @param clearType
     * @param currentAccessToken
     * @return
     * @throws MissingTokenException
     * @throws URISyntaxException
     * @throws JsonProcessingException
     */
    @Override
    public boolean clearHistory(String playlistId, Enums.ClearType clearType, AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException, ClearPlaylistException {

        try {
            AccessToken accessToken = beforeCall(currentAccessToken);
            List<Track> tempListDelete;
            String lastTrack;

            if (clearType == Enums.ClearType.LAST_PLAYED) {
                lastTrack = this.getLastItemPlayed(accessToken);
            } else {
                lastTrack = this.getCurrentPlaying(accessToken);
            }

            //get items in the given playlist
            Map<String, List<Item>> playlistTracks = this.getPlaylist(playlistId, accessToken);
            List<Item> playlistItems = playlistTracks.entrySet().stream().findFirst().get().getValue();

            //get items in the given playlist UNTIL BEFORE last given song
            List<Track> listDelete = playlistItems.stream()
                    .takeWhile(item -> !item.getTrack().getUri().equals(lastTrack))
                    .map(item -> new Track(item.getTrack().getUri())).collect(Collectors.toList());

            //the given ClearType track isn't listed in the provided playlistId
            if (listDelete.size() >= playlistItems.size()) {
                String type = clearType == Enums.ClearType.LAST_PLAYED ? "last played " : "current playing ";
                throw new ClearPlaylistException("the " + type + "track wasn't found in the provided playlist: " + playlistId);
            }

            while (listDelete.size() > 0) {
                ReturnDelete deleteResult;
                if (listDelete.size() > 100) {
                    tempListDelete = listDelete.subList(0, 100);
                    deleteResult = callApiDelete(endpoints.managePlaylist(playlistId), new Tracks(tempListDelete), ReturnDelete.class, accessToken);
                    listDelete.subList(0, 100).clear();
                } else {
                    deleteResult = callApiDelete(endpoints.managePlaylist(playlistId), new Tracks(listDelete), ReturnDelete.class, accessToken);
                    listDelete.clear();
                }
                if (deleteResult == null) return false;
            }
            return true;
        } catch (MissingTokenException e) {
            throw e;//ignore
        } catch (Exception e) {
            throw e;//debug
        }
    }

    @Override
    public boolean clearCurrent(AccessToken currentAccessToken) throws JsonProcessingException, ClearPlaylistException, MissingTokenException, URISyntaxException {
        AccessToken accessToken = beforeCall(currentAccessToken);
        CurrentPlaying currentPlaying = this.callApiGet(endpoints.getPlaybackState, CurrentPlaying.class, accessToken);

        String currentPlaylistId = currentPlaying.getContext().getUri().split(":")[2];
        return this.clearHistory(currentPlaylistId, Enums.ClearType.CURRENT_PLAYING, accessToken);
    }

    @Override
    public String getCurrentPlaying(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);

        CurrentPlaying currentPlaying = this.callApiGet(endpoints.getPlaybackState, CurrentPlaying.class, accessToken);

        return currentPlaying.getItem().getUri();
    }

    @Override
    public String getLastItemPlayed(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);

        History h = callApiGet(endpoints.history, History.class, accessToken);
        return h.getItems().get(0).getTrack().getUri();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String,String> getPlaylistTracks(String playlistId, Integer limit, AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);

        PlaylistTracks playlistTracks = callApiGet(endpoints.playlistTracks(playlistId, limit), PlaylistTracks.class, accessToken);
        Map<String, String> result = playlistTracks.getItems().stream().collect(Collectors.toMap(
                item -> item.getTrack().getName(),
                item -> item.getTrack().getUri()));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<Item>> getPlaylist(String playlistId, AccessToken currentAccessToken, Boolean includeMarket) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);
        List<Item> itemList = new ArrayList<>();
        String nextOffset;
        User user = getUserDetails(accessToken);

        PreSinglePlaylist preSinglePlaylist = callApiGet(
                endpoints.singlePlaylist(playlistId, includeMarket ? user.getCountry() : ""),
                PreSinglePlaylist.class,
                accessToken);
        SinglePlaylist singlePlaylist = preSinglePlaylist.getTracks();


        nextOffset = singlePlaylist.getNext();
        itemList.addAll(singlePlaylist.getItems());

        while (nextOffset != null) {

            singlePlaylist = callApiGet(nextOffset, SinglePlaylist.class, accessToken);
            itemList.addAll(singlePlaylist.getItems());
            nextOffset = singlePlaylist.getNext();
        }
        singlePlaylist.setName(preSinglePlaylist.getName());
        return Map.of(singlePlaylist.getName(), itemList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<Item>> getPlaylist(String playlistId, AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        return this.getPlaylist(playlistId, currentAccessToken, false);
    }

    @Override
    public List<FullTrackDetails> getUnavailables(String playlistId, AccessToken accessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        //beforeCall invoked inside getPlaylistTracks

        List<Item> itemList = getPlaylist(playlistId, accessToken, true).values().stream().findFirst().get();
        List<FullTrackDetails> unavailableTracks = new ArrayList<>();

        for (Item item : itemList) {
            if (!item.getTrack().getIsPlayable()) {
                unavailableTracks.add(SpotifyObjectsConverter.itemToFullTrackDetails(item));
            }
        }
        System.out.println();
        return unavailableTracks;
    }

    @Override
    public boolean pausePlayback(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);

        try {
            HttpStatus response = webClient.put()
                    .uri(endpoints.pausePlayback)
//                    .header("Authorization", "Bearer "+accessToken.getAccessToken())
                    .header("Authorization", "Bearer " + accessToken.getAccessToken())
                    //.headers(httpHeaders -> httpHeaders.addAll(this.headers))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity()
                    .map(responseEntity -> responseEntity.getStatusCode())
                    .block();
            return response == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean nextTrack(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException {
        AccessToken accessToken = beforeCall(currentAccessToken);
        return callPost(endpoints.nextTrack, accessToken);
    }

    @Override
    public boolean previousTrack(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException {
        AccessToken accessToken = beforeCall(currentAccessToken);
        return callPost(endpoints.previousTrack, accessToken);
    }

    @Override
    public boolean callPost(String url, AccessToken accessToken) throws MissingTokenException, URISyntaxException {

        try {
            HttpStatus response = webClient.post()
                    .uri(url)
//                    .header("Authorization", "Bearer "+accessToken.getAccessToken())
                    .header("Authorization", "Bearer " + accessToken.getAccessToken())
                    //.headers(httpHeaders -> httpHeaders.addAll(this.headers))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity()
                    .map(responseEntity -> responseEntity.getStatusCode())
                    .block();
            return response == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, R> R callPost(String url, T body, AccessToken accessToken){

        try {
            String response = this.webClient.post()
                    .uri(url)
//                    .headers(httpHeaders -> httpHeaders.addAll(this.headers))
                    .header("Authorization", "Bearer " + accessToken.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return (R) response;
        } catch (Exception e){
            String toReturn = "failed request: "+e.toString();
            return (R) toReturn;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserPlaylists> getPlaylists(AccessToken currentAccessToken) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);
        User user = getUserDetails(accessToken);

        PlaylistList playlistList = callApiGet(endpoints.getPlaylists, PlaylistList.class, accessToken);

        List<UserPlaylists> userPlaylists = playlistList.getItems().stream()
                //only playlists created by the current user
                .filter(item -> item.getOwner().getId().equals(user.getId()))
                .filter(item -> !Arrays.asList(plalistsToIgnore).contains(item.getId()))
                .map(item -> new UserPlaylists(item.getName(), item.getId()))
                .collect(Collectors.toList());

        userPlaylists.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return userPlaylists;
    }

    @Override
    public boolean mixPlaylist(AccessToken currentAccessToken,
                               String match,
                               String destinationPlaylistId,
                               Integer amount, Enums.Order order) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        AccessToken accessToken = beforeCall(currentAccessToken);
        List<String> playlistUris = new ArrayList<>();

        try {
            List<UserPlaylists> userPlaylists = this.getPlaylists(accessToken);
            List<UserPlaylists> playlistsFound = userPlaylists.stream().filter(playlist -> playlist.getName().contains(match)).collect(Collectors.toList());

            //going through all the playlists that match the criteria
            for (UserPlaylists playlist : playlistsFound) {
                //get the Uris of the tracks to be added
                Map<String, String> tempPlaylistTracks = this.getPlaylistTracks(playlist.getId(), 10, accessToken);
                playlistUris.addAll(tempPlaylistTracks.values());
            }

            //TODO maior que 100
            String response = this.callPost(endpoints.managePlaylist(destinationPlaylistId),
                    new Uri(playlistUris),
                    accessToken);
            if (response.contains("failed request")) return false;

//            this.callApiDelete();

            return true;
        }catch (Exception e){
            throw e;
        }
    }
    //=========================internal methods

    /**
     * Called through getAccessToken() or beforeCall(), used to get an access token and set this.user
     *
     * @param requestEntityCall
     * @return an access token
     * @throws RestClientResponseException
     * @throws MissingTokenException
     * @throws URISyntaxException
     */
    private AccessToken tokenCall(HttpEntity<MultiValueMap<String, String>> requestEntityCall, AccessToken accessToken) throws RestClientResponseException, MissingTokenException, URISyntaxException {

        AccessToken responseToken = template.postForObject(endpoints.getAccess, requestEntityCall, AccessToken.class);

        if (accessToken != null && accessToken.getRefreshToken() != null) {
            responseToken.setRefreshToken(accessToken.getRefreshToken());
        }
        responseToken.setValidity(3600000 + System.currentTimeMillis());
        String traceId = TraceIdGenerator.writeDebug("\nAccess token: " + responseToken.getAccessToken() +
                        "\nRefresh token: " + responseToken.getRefreshToken() +
                        "\nvalidity: " + responseToken.getValidity(),
                this.getClass(), StackWalker.getInstance().walk(frames -> frames.findFirst().map(StackWalker.StackFrame::getMethodName)).orElse(METHOD_NAME_NOT_FOUND));
        this.globalAccessToken=responseToken;
        return responseToken;

    }


    @Override
    public AccessToken setRefresh(String refresh) throws MissingTokenException, URISyntaxException {
        AccessToken accessToken = new AccessToken();
        accessToken.setRefreshToken(refresh);
        accessToken.setValidity(-1L);

        return beforeCall(accessToken);
    }


    public <T> T callApiGet(String url, Class<T> objectClass, AccessToken accessToken) throws JsonProcessingException {

        try {
            String response = webClient.get()
                    .uri(url)
//                    .header("Authorization", "Bearer "+accessToken.getAccessToken())
                    .header("Authorization", "Bearer " + accessToken.getAccessToken())
                    //.headers(httpHeaders -> httpHeaders.addAll(this.headers))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JavaType javaType = objectMapper.getTypeFactory().constructType(objectClass);
            T result;
            result = objectMapper.readValue(response, javaType);
            System.out.println();
            return result;
        }
        catch (Exception e){
            throw e;
        }
    }

    public <T> T callApiDelete(String url, Tracks tracksToDelete, Class<T> objectClass, AccessToken accessToken) throws JsonProcessingException {
        try {
            WebClient webClientDelete = WebClient.builder().baseUrl(endpoints.baseUrl)
                    .clientConnector(new ReactorClientHttpConnector(
                            HttpClient.create().wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                    )).build();
            String response = webClientDelete.method(HttpMethod.DELETE)
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(tracksToDelete))
                    .header("Authorization", "Bearer " + accessToken.getAccessToken())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JavaType javaType = objectMapper.getTypeFactory().constructType(objectClass);
            T result;
            result = objectMapper.readValue(response.toString(), javaType);
            System.out.println();
            return result;
        } catch (Exception e) {
            throw e;
        }

    }

    private AccessToken beforeCall(AccessToken accessToken) throws MissingTokenException, URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> bodyParameters = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> requestEntity;
//        cleaner();
        if (accessToken == null ) {
            if (this.globalAccessToken==null){
                if (refreshToken.isEmpty()){
                    throw new MissingTokenException("No access or refresh token, please access the following URL to (re)start the authorization: " + endpoints.authorizeUrl());
                }
                accessToken = new AccessToken(refreshToken.get());
            } else {
                accessToken = this.globalAccessToken;
            }
        }

        if (accessToken.getAccessToken() != null && (accessToken.getValidity() > System.currentTimeMillis())) {
            headers.add("Authorization", "Bearer " + accessToken.getAccessToken());
            return accessToken;
        }

//        if (accessToken.getValidity() < System.currentTimeMillis()) {
        writeDebug("refreshing token", this.getClass(), StackWalker.getInstance().walk(frames -> frames.findFirst().map(StackWalker.StackFrame::getMethodName)).orElse(METHOD_NAME_NOT_FOUND));

        headers.add("Authorization", endpoints.authorizationToAccess());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        bodyParameters.add("grant_type", "refresh_token");
        bodyParameters.add("refresh_token", accessToken.getRefreshToken());
        requestEntity = new HttpEntity<>(bodyParameters, headers);
        return tokenCall(requestEntity, accessToken);

    }


//    @Override
//    public RestTemplate interceptorRest() {
//        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
//        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(new LoggingRequestInterceptor());
//        restTemplate.setInterceptors(interceptors);
//        return restTemplate;
//    }

//    private void cleaner() {
//        cleaner(true);
//    }
//
//    private void cleaner(Boolean addSpotifyHeader) {
//        headers.clear();
//        objectMapper = new ObjectMapper();
//        if (accessToken != null && accessToken.getAccessToken() != null && addSpotifyHeader == true) {
//            headers.add("Authorization", "Bearer " + accessToken.getAccessToken());
//        }
//        bodyParameters.clear();
//    }


}
