package com.illoivan.duplicatefiles.model;

import com.illoivan.duplicatefiles.repository.search.CustomMatch;
import com.illoivan.duplicatefiles.repository.search.CustomReadBuffer;
import com.illoivan.duplicatefiles.repository.search.IgnoreOptions;
import com.illoivan.duplicatefiles.repository.search.MetadataSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterCustomFile {
	CustomMatch matchBy;
	IgnoreOptions ignoreOptions;
	CustomPathSearch pathSearch;
	CustomReadBuffer customReadBuffer;
	MetadataSearch metadataSearch;
	String checkSumAlgorithm;
}
