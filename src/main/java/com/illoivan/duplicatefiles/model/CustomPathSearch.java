package com.illoivan.duplicatefiles.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomPathSearch {
    private List<String> excludePaths;
}