package com.gitrnd.qaproducer.qa.domain;

import javax.servlet.http.HttpServletRequest;
/**
 * Server Side 테이블 요청 처리를 위한 인자
 * * draw	: 테이블을 새로 그린(데이터를 reload) 횟수
 * * start	: 현재 DB에 요청할 Offset
 * * length : DB에 요청할 데이터 개수
 * * order[0][column] : 정렬에 사용할 컬럼
 * * order[0][dir]	  : 정렬 방향 (asc, desc) 
 * @author HC.Kim
 * @Date 2018. 8. 16. 오후 3:20:28
 * */
public class ServerSideVO {
	private int drawCount;
	private int startIndex;
	private int displayLength;
	private int orderColumn;
	private String orderDirection;
	
	public void setParam(HttpServletRequest request) {
		this.setDrawCount(Integer.parseInt(request.getParameter("draw")));
		this.setStartIndex(Integer.parseInt(request.getParameter("start")));
		this.setDisplayLength(Integer.parseInt(request.getParameter("length")));
		this.setOrderColumn(Integer.parseInt(request.getParameter("order[0][column]")));
		this.setOrderDirection(String.valueOf(request.getParameter("order[0][dir]")));
	}
	
	public int getDrawCount() {
		return drawCount;
	}
	public void setDrawCount(int drawCount) {
		this.drawCount = drawCount;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getDisplayLength() {
		return displayLength;
	}
	public void setDisplayLength(int displayLength) {
		this.displayLength = displayLength;
	}
	public int getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(int orderColumn) {
		this.orderColumn = orderColumn + 1;
	}
	public String getOrderDirection() {
		return orderDirection;
	}
	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}
}
