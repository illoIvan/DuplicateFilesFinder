package com.illoivan.duplicatefiles.repository.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class IgnoreOptions {
    private boolean zeroBytes;
    private boolean systemFiles;
    private boolean isSizeUnder;
    private boolean isSizeOver;
    private int sizeUnder;
    private int sizeOver;
}
