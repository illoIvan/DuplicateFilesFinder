package com.illoivan.duplicatefiles.model;

import java.util.Map;

public interface CustomMetadata {
    void loadMetadata(CustomFile customFile);
    void fillMetadata(Map<String,String> properties);
    String getDescription();
}
