package com.illoivan.duplicatefiles.viewmodel;

import com.illoivan.duplicatefiles.model.CustomMetadata;
import com.illoivan.duplicatefiles.model.MP3Metadata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MP3MetadataViewModel implements CustomMetadataView {
    private String genre;
    private String album;
    private String artist;
    private String title;
    private long duration;   
    private String formatTime;

    @Override
    public CustomMetadataView ToView(CustomMetadata metadata) {
        MP3Metadata model = (MP3Metadata) metadata;
        MP3MetadataViewModel view = new MP3MetadataViewModel();
        view.setGenre(model.getGenre());         
        view.setAlbum(model.getAlbum());
        view.setArtist(model.getArtist());
        view.setTitle(model.getTitle());
        view.setDuration(model.getDuration());
        view.setFormatTime(model.getFormatTime());
        return view;
    }

    @Override
    public CustomMetadata ToModel(CustomMetadataView metadata) {
        MP3MetadataViewModel view = (MP3MetadataViewModel) metadata;
        MP3Metadata model = new MP3Metadata();
        model.setGenre(view.getGenre());         
        model.setAlbum(view.getAlbum());
        model.setArtist(view.getArtist());
        model.setTitle(view.getTitle());
        model.setDuration(view.getDuration());
        model.setFormatTime(view.getFormatTime());
        return model;
    }
}
