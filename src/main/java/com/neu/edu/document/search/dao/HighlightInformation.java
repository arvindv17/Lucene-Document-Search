package com.neu.edu.document.search.dao;

public class HighlightInformation {

	public double getDocIndexScore() {
		return docIndexScore;
	}
	public void setDocIndexScore(double docIndexScore) {
		this.docIndexScore = docIndexScore;
	}
	public String[] getSearchHighlightTextResult() {
		return searchHighlightTextResult;
	}
	public void setSearchHighlightTextResult(String[] searchHighlightTextResult) {
		this.searchHighlightTextResult = searchHighlightTextResult;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getDocHits() {
		return docHits;
	}
	public void setDocHits(int docHits) {
		this.docHits = docHits;
	}
	public int getDocumentCount() {
		return documentCount;
	}
	public void setDocumentCount(int documentCount) {
		this.documentCount = documentCount;
	}
	private double docIndexScore;
	private String[] searchHighlightTextResult;
	private String path;
	private int docHits;
	private int documentCount;

	

}
