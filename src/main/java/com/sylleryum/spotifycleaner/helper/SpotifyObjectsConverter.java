package com.sylleryum.spotifycleaner.helper;

import com.sylleryum.spotifycleaner.model.FullTrackDetails;
import com.sylleryum.spotifycleaner.model.spotify.singlePlaylist.Item;

import java.util.List;
import java.util.stream.Collectors;

public class SpotifyObjectsConverter {

    public static List<FullTrackDetails> itemListToFullTrackDetails(List<Item> itemList) {
        return itemList.stream()
                .map(item ->
                        new FullTrackDetails(item.getTrack().getUri(), item.getTrack().getArtists().get(0).getName(), item.getTrack().getName()))
                .collect(Collectors.toList());
    }

    public static FullTrackDetails itemToFullTrackDetails(Item item) {
        return new FullTrackDetails(item.getTrack().getUri(), item.getTrack().getArtists().get(0).getName(), item.getTrack().getName());
    }
}
