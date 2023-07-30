package com.illoivan.duplicatefiles.service.predicate;

import java.util.function.Predicate;

import com.illoivan.duplicatefiles.model.CustomFile;
import com.illoivan.duplicatefiles.model.MP3Metadata;
import com.illoivan.duplicatefiles.repository.search.MP3MetadataSearch;

public class PredicateMP3Metadata {

    private PredicateMP3Metadata() {}

    public static Predicate<CustomFile> getFilter(MP3MetadataSearch mp3Search, CustomFile customFile) {
        MP3Metadata metadata = (MP3Metadata)customFile.getMetadata();
        Predicate<CustomFile> predicate = null;
        
        if(mp3Search.isGenre()) {
            predicate = genre(metadata);
        }
        if(mp3Search.isAlbum()) {
            predicate = (predicate == null) ? album(metadata) : predicate.and(album(metadata));
        }
        if(mp3Search.isArtist()) {
            predicate = (predicate == null) ? artist(metadata) : predicate.and(artist(metadata));
        }
        if(mp3Search.isTitle()) {
            predicate = (predicate == null) ? title(metadata) : predicate.and(title(metadata));
        }
        if(mp3Search.isDuration()) {
            predicate = (predicate == null) ? duration(metadata) : predicate.and(duration(metadata));
        }
        return predicate;
    }
    
    private static Predicate<CustomFile> genre(MP3Metadata metadata) {
        return cf -> {
            MP3Metadata cfMetadata = (MP3Metadata) cf.getMetadata();
            return cfMetadata != null && cfMetadata.getGenre() != null &&
                    cfMetadata.getGenre().equals(metadata.getGenre());
        };
    }
    
    private static Predicate<CustomFile> album(MP3Metadata metadata) {
        return cf -> {
            MP3Metadata cfMetadata = (MP3Metadata) cf.getMetadata();
            return cfMetadata != null && cfMetadata.getAlbum() != null &&
                    cfMetadata.getAlbum().equals(metadata.getAlbum());
        };
    }

    private static Predicate<CustomFile> artist(MP3Metadata metadata) {
        return cf -> {
            MP3Metadata cfMetadata = (MP3Metadata) cf.getMetadata();
            return cfMetadata != null && cfMetadata.getArtist() != null &&
                    cfMetadata.getArtist().equals(metadata.getArtist());
        };
    }

    private static Predicate<CustomFile> title(MP3Metadata metadata) {
        return cf -> {
            MP3Metadata cfMetadata = (MP3Metadata) cf.getMetadata();
            return cfMetadata != null && cfMetadata.getTitle() != null &&
                    cfMetadata.getTitle().equals(metadata.getTitle());
        };
    }

    private static Predicate<CustomFile> duration(MP3Metadata metadata){
        return cf -> {
        	MP3Metadata cfMetadata = (MP3Metadata) cf.getMetadata();
            return cfMetadata != null && cfMetadata.getDuration() == metadata.getDuration();
        };
    }
    
}
