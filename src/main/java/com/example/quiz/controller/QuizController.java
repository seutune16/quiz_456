package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateOrUpdateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;

@RestController
public class QuizController {
	
	@Autowired
	private QuizService quizService;
	
	@PostMapping(value = "/quiz/create_update")
	public BasicRes create(@RequestBody CreateOrUpdateReq req) {
		return quizService.createOrUpdate(req);
	}
	
	@PostMapping(value = "/quiz/search")
	public SearchRes search(@RequestBody SearchReq req) {
		return quizService.search(req);
		
	}
	
	@PostMapping(value = "/quiz/delete")
	public BasicRes delete(@RequestBody DeleteReq req) {
		return quizService.delete(req);
	
	}	

}
