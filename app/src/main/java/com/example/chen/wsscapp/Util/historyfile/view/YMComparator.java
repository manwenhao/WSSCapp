package com.example.chen.wsscapp.Util.historyfile.view;


import com.example.chen.wsscapp.Util.FileItem;

import java.util.Comparator;


public class YMComparator implements Comparator<FileItem> {

	@Override
	public int compare(FileItem o1, FileItem o2) {
		return o1.getDate().compareTo(o2.getDate());
	}

}
