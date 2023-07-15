package com.illoivan.duplicatefiles.repository.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CustomMatch {

    private boolean fileName;
    private boolean fileSize;
    private boolean fileChecksum;
    private boolean showSimilarity;
    private int similarity;
}
