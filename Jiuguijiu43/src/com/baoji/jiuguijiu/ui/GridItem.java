package com.baoji.jiuguijiu.ui;

class GridItem {
	private String title;
	private String imageUrl;
	private String description;

	public GridItem() {
		super();
	}

	public GridItem(String title, String imageId, String time) {
		super();
		this.title = title;
		this.imageUrl = imageId;
		this.description = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}