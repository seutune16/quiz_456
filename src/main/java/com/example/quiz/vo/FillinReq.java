package com.example.quiz.vo;

import java.util.Map;

public class FillinReq {

	private int quizId;

	private String name;

	private String phone;

	private String email;

	private int age;

	// qu_id & answer map: anser 有多個時用分號(;)串接
	private Map<Integer, String> qIdAnswerMap;

	public FillinReq() {
		super();
	}

	public FillinReq(int quizId, String name, String phone, String email, int age, Map<Integer, String> qIdAnswerMap) {
		super();
		this.quizId = quizId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.qIdAnswerMap = qIdAnswerMap;
	}

	public int getQuizId() {
		return quizId;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}

	public Map<Integer, String> getqIdAnswerMap() {
		return qIdAnswerMap;
	}

}
