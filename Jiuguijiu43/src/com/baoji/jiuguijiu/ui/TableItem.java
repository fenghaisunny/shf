package com.baoji.jiuguijiu.ui;

import android.graphics.Bitmap;

public class TableItem {
	private String tableId;
	private String tableTitle;
	private String tableText;
	private Bitmap tablePic1;
	private Bitmap tablePic2;
	private String tableUrl;
	private String commentsUrl;
	private String time;
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
	public String getTableUrl() {
		return tableUrl;
	}
	public void setTableUrl(String tableUrl) {
		this.tableUrl = tableUrl;
	}
	public String getCommentsUrl() {
		return commentsUrl;
	}
	public void setCommentsUrl(String commentsUrl) {
		this.commentsUrl = commentsUrl;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "TableItem [tableId=" + tableId + ", tableTitle=" + tableTitle
				+ ", tableText=" + tableText + ", tablePic1=" + tablePic1 + "tabPic2=" + tablePic2
				+ ", tableUrl=" + tableUrl + ", commentsUrl=" + commentsUrl
				+ ", time=" + time + "]";
	}
}
