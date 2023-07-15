package com.illoivan.duplicatefiles.controller.view.commons;

import java.util.List;

import com.illoivan.duplicatefiles.model.CustomTableColumn;
import com.illoivan.duplicatefiles.viewmodel.CustomFileViewModel;

public class CustomColumn {

	
	public static List<CustomTableColumn<CustomFileViewModel,Object>> getCheckSumColumns(){
		return List.of(
			new CustomTableColumn<>("FileName", "fileName"),
			new CustomTableColumn<>("Checksum", "checksum"),
			new CustomTableColumn<>("Size", "size"),
			new CustomTableColumn<>("Folder", "folder"),
			new CustomTableColumn<>("CreationDate", "creationDate"),
			new CustomTableColumn<>("LastModifiedDate", "lastModifiedDate")
		);
	}
	public static List<CustomTableColumn<CustomFileViewModel,Object>> getErrorColumns(){
		return List.of(
			new CustomTableColumn<>("FileName", "fileName"),
			new CustomTableColumn<>("Error", "error"),
			new CustomTableColumn<>("Folder", "folder")
		);
	}
	
}
