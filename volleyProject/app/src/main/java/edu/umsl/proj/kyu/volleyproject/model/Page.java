package edu.umsl.proj.kyu.volleyproject.model;

public class Page {
	private long pageId;
	private int ns;
	private String title;
	private int index;
	private Thumbnail thumbnail;

	public long getPageId() {
		return pageId;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

	public int getNs() {
		return ns;
	}

	public void setNs(int ns) {
		this.ns = ns;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Thumbnail getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Thumbnail thumbnail) {
		this.thumbnail = thumbnail;
	}
}
