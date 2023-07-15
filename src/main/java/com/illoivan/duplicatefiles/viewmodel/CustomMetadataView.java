package com.illoivan.duplicatefiles.viewmodel;

import com.illoivan.duplicatefiles.model.CustomMetadata;

public interface CustomMetadataView {
    public CustomMetadataView ToView(CustomMetadata metadata);
    public CustomMetadata ToModel(CustomMetadataView metadata);
}
