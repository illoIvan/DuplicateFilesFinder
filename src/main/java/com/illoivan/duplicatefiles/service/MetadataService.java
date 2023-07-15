package com.illoivan.duplicatefiles.service;

import com.illoivan.duplicatefiles.model.CustomFile;
import com.illoivan.duplicatefiles.model.CustomMetadata;
import com.illoivan.duplicatefiles.model.MP3Metadata;

public class MetadataService {

	private MetadataService() {}
	
	public static CustomMetadata createMetadata(CustomFile customFile) {
    	if(customFile.getExtension().equalsIgnoreCase("mp3")) {
    		return new MP3Metadata(customFile);
    	}
    	return null;
	}
}
