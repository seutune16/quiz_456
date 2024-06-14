package com.example.quiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "response")
public class Response {
	
	// 因為 PK 是 AI(Auto Incremental),所以要加上此 @Generatedvalue
	// strategy: 指的是 AI的生成策略
	// GenerationType.IDENTITY: 代表 PK 數字由資料庫來自動增加
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	private int id ;
	
	@Column(name = "quiz_id")
	private int quizId ;
	
	@Column(name = "name")
	private String name ;
	
	@Column(name = "phone")
	private String phone ;
	
	@Column(name = "email")
	private String email ;
	
	@Column(name = "age")
	private int age ;
	
	@Column(name = "fillin")
	private String fillin ;

	public Response() {
		super();
	}

	public Response(int quizId, String name, String phone, String email, int age, String fillin) {
		super();
		this.quizId = quizId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.fillin = fillin;
	}

	public int getId() {
		return id;
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

	public String getFillin() {
		return fillin;
	}
	
	
	
	
	
	
	
	

}
