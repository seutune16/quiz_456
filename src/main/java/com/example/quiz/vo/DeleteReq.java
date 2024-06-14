package com.example.quiz.vo;

import java.util.List;

public class DeleteReq {

	private List<Integer> idList;

	public DeleteReq() {
		super();
	}

	public DeleteReq(List<Integer> idList) {
		this.idList = idList;
	}

	public List<Integer> getIdList() {
		return idList;
	}

}
