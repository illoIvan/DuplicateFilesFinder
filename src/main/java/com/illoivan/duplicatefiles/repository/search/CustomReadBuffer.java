package com.illoivan.duplicatefiles.repository.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CustomReadBuffer {
	private int start;
	private int max;
}
