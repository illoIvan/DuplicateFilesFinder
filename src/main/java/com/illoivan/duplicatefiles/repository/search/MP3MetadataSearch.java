package com.illoivan.duplicatefiles.repository.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MP3MetadataSearch {

    private boolean genre;
    private boolean album;
    private boolean artist;
    private boolean title;
    private boolean duration;
}
