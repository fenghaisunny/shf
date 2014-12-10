package com.thoughtworks.showlistview.view;

import android.graphics.Bitmap;

public class TableItem {
	private String tableId;
	private String tableTitle;
	private String tableText;
	private Bitmap tablePic1;
	private Bitmap tablePic2;
	private String tableImageUrl;
	
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getTableTitle() {
		return tableTitle;
	}
	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}
	public String getTableText() {
		return tableText;
	}
	public void setTableText(String tableText) {
		this.tableText = tableText;
	}


	public String getTableImageUrl() {
		return tableImageUrl;
	}
	public void setTableImageUrl(String tableImageUrl) {
		this.tableImageUrl = tableImageUrl;
	}
	public Bitmap getTablePic1() {
		return tablePic1;
	}
	public void setTablePic1(Bitmap tablePic1) {
		this.tablePic1 = tablePic1;
	}
	public Bitmap getTablePic2() {
		return tablePic2;
	}
	public void setTablePic2(Bitmap tablePic2) {
		this.tablePic2 = tablePic2;
	}
}
