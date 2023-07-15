package com.illoivan.duplicatefiles.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class CustomFileDuplicate extends ModelBase{
    private CustomFile root;
    private List<CustomFile> duplicate;
}
