package com.sylleryum.spotifycleaner.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sylleryum.spotifycleaner.helper.TraceIdGenerator;
import com.sylleryum.spotifycleaner.model.AccessToken;
import com.sylleryum.spotifycleaner.model.ClearType;
import com.sylleryum.spotifycleaner.model.FullTrackDetails;
import com.sylleryum.spotifycleaner.model.History;
import com.sylleryum.spotifycleaner.model.exception.ClearPlaylistException;
import com.sylleryum.spotifycleaner.model.jsonResponses.UserPlaylistWrapper;
import com.sylleryum.spotifycleaner.model.jsonResponses.UserPlaylists;
import com.sylleryum.spotifycleaner.model.spotify.singlePlaylist.Item;
import com.sylleryum.spotifycleaner.model.spotify.user.User;
import com.sylleryum.spotifycleaner.model.jsonResponses.OrderPlaylist;
import com.sylleryum.spotifycleaner.model.jsonResponses.UnavailableTracksWrapper;
import com.sylleryum.spotifycleaner.model.exception.MissingArgumentException;
import com.sylleryum.spotifycleaner.model.exception.MissingTokenException;
import com.sylleryum.spotifycleaner.service.ServiceApi;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sylleryum.spotifycleaner.helper.Literals.SESSION_ACCESS_TOKEN;
import static com.sylleryum.spotifycleaner.helper.TraceIdGenerator.METHOD_NAME_NOT_FOUND;

@RestController
@RequestMapping
public class TheController {

    private final ServiceApi serviceApi;

    public TheController(ServiceApi serviceApi) {
        this.serviceApi = serviceApi;
    }

    @GetMapping(path = {"/get-unavailables/{playlistId}", "/get-unavailables"})
    public ResponseEntity<?> getUnavailables(@PathVariable Optional<String> playlistId, HttpSession session) throws Exception {
        if (playlistId.isEmpty()) {
            throw new MissingArgumentException("Missing Playlist ID");
        }
        AccessToken accessToken = (AccessToken) session.getAttribute(SESSION_ACCESS_TOKEN);

        List<FullTrackDetails> unavailables = serviceApi.getUnavailables(playlistId.get(), accessToken);

        return ResponseEntity.ok(new UnavailableTracksWrapper(unavailables));
    }

    @GetMapping(path = {"/order-playlist/{playlistId}/random"})
    public ResponseEntity<?> orderPlaylistRandom(@PathVariable Optional<String> playlistId, HttpSession session) throws MissingArgumentException, MissingTokenException, URISyntaxException, JsonProcessingException {
        if (playlistId.isEmpty()) {
            throw new MissingArgumentException("Missing Playlist ID");
        }
        AccessToken accessToken = (AccessToken) session.getAttribute(SESSION_ACCESS_TOKEN);

        String playlistRandom = serviceApi.orderPlaylistRandom(playlistId.get(), accessToken);

        return ResponseEntity.ok(new OrderPlaylist(playlistRandom, "random"));
    }

    @GetMapping(path = {"/clear-last-played/{playlistId}"})
    public ResponseEntity<?> clearLastPlayed(@PathVariable Optional<String> playlistId, HttpSession session) throws MissingArgumentException, MissingTokenException, URISyntaxException, JsonProcessingException, ClearPlaylistException {
        if (playlistId.isEmpty()) {
            throw new MissingArgumentException("Missing Playlist ID");
        }
        AccessToken accessToken = (AccessToken) session.getAttribute(SESSION_ACCESS_TOKEN);

        boolean success = serviceApi.clearHistory(playlistId.get(), ClearType.LAST_PLAYED, accessToken);
        if (success) {
            return ResponseEntity.ok("done");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }

    @GetMapping(path = {"/clear-current-playing/{playlistId}"})
    public ResponseEntity<?> clearCurrentPlaying(@PathVariable Optional<String> playlistId, HttpSession session) throws MissingArgumentException, MissingTokenException, URISyntaxException, JsonProcessingException, ClearPlaylistException {
        if (playlistId.isEmpty()) {
            throw new MissingArgumentException("Missing Playlist ID");
        }
        AccessToken accessToken  = (AccessToken) session.getAttribute(SESSION_ACCESS_TOKEN);


        boolean success = serviceApi.clearHistory(playlistId.get(), ClearType.CURRENT_PLAYING, accessToken);
        if (success) {
            return ResponseEntity.ok("done");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }


    @GetMapping("/me")
    public ResponseEntity<?> getUser(HttpSession session) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        String traceId = TraceIdGenerator.writeTrace(this.getClass(), StackWalker.getInstance().walk(frames -> frames.findFirst().map(StackWalker.StackFrame::getMethodName)).orElse(METHOD_NAME_NOT_FOUND));
        AccessToken accessToken = (AccessToken) session.getAttribute(SESSION_ACCESS_TOKEN);
        System.out.println("-------------------------");
        if (session.getAttribute("accessToken") != null) {
            System.out.println(session.getAttribute("accessToken"));
        }
        User user = serviceApi.getUserDetails(accessToken);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/pause")
    public ResponseEntity<?> pausePlayback(HttpSession session) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        String traceId = TraceIdGenerator.writeTrace(this.getClass(), StackWalker.getInstance().walk(frames -> frames.findFirst().map(StackWalker.StackFrame::getMethodName)).orElse(METHOD_NAME_NOT_FOUND));
        AccessToken accessToken;
        accessToken = (AccessToken) session.getAttribute(SESSION_ACCESS_TOKEN);

        boolean result = serviceApi.pausePlayback(accessToken);
        if (result){
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }
    @GetMapping("/next")
    public ResponseEntity<?> nextTrack(HttpSession session) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        String traceId = TraceIdGenerator.writeTrace(this.getClass(), StackWalker.getInstance().walk(frames -> frames.findFirst().map(StackWalker.StackFrame::getMethodName)).orElse(METHOD_NAME_NOT_FOUND));
        AccessToken accessToken;
        accessToken = (AccessToken) session.getAttribute(SESSION_ACCESS_TOKEN);

        boolean result = serviceApi.nextTrack(accessToken);
        if (result){
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }

    @GetMapping("/previous")
    public ResponseEntity<?> previousPlayback(HttpSession session) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        String traceId = TraceIdGenerator.writeTrace(this.getClass(), StackWalker.getInstance().walk(frames -> frames.findFirst().map(StackWalker.StackFrame::getMethodName)).orElse(METHOD_NAME_NOT_FOUND));
        AccessToken accessToken;
        accessToken = (AccessToken) session.getAttribute(SESSION_ACCESS_TOKEN);

        boolean result = serviceApi.previousTrack(accessToken);
        if (result){
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }
    @GetMapping(path = {"/playlists", "/"})
    public ResponseEntity<?> getPlaylists(HttpServletRequest request,@RequestParam(name="refresh-token") Optional<String> refreshToken, HttpSession session) throws MissingTokenException, URISyntaxException, JsonProcessingException {
        String traceId = TraceIdGenerator.writeTrace(this.getClass(), StackWalker.getInstance().walk(frames -> frames.findFirst().map(StackWalker.StackFrame::getMethodName)).orElse(METHOD_NAME_NOT_FOUND));
        AccessToken accessToken;
        accessToken = (AccessToken) session.getAttribute(SESSION_ACCESS_TOKEN);

        String requestUrl = request.getRequestURL().toString();
        if (accessToken==null && !refreshToken.isEmpty()){
            accessToken = serviceApi.setRefresh(refreshToken.get());
            session.setAttribute(SESSION_ACCESS_TOKEN, accessToken);
            requestUrl = requestUrl.replace("/playlists","/");
        }

        List<UserPlaylists> playlists = serviceApi.getPlaylists(accessToken);
        String finalRequestUrl = requestUrl;
        List<UserPlaylists> result = playlists.stream().peek(item-> {
            item.setClearCurrent(finalRequestUrl +"clear-current-playing/"+item.getId());
            item.setClearLastPlayed(finalRequestUrl +"clear-last-played/"+item.getId());
        }).collect(Collectors.toList());
//        return ResponseEntity.ok(new UserPlaylistWrapper(result));
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/a")
    public String home(HttpServletResponse response) {
        response.setHeader("Content-Security-Policy", "default-src 'self'; img-src 'self' data:; font-src 'self';");
        return "Hello World!";
    }

}
