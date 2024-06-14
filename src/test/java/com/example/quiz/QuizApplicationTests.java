package com.example.quiz;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.quiz.repository.QuizDao;
import com.example.quiz.vo.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//@SpringBootTest
class QuizApplicationTests {
	
	@Autowired
	private QuizDao quizDao;


	@Test
	void contextLoads() {
//		System.out.println(LocalDate.now());
//		System.out.println(LocalDateTime.now());
//		System.out.println(LocalTime.now());
//		System.out.println("===============================");
		
		String str = "A;B;C;D"; //�ﶵ
		String ansStr = "A;E"; //�^��
		String[] ansArray = ansStr.split(";");//��^�������}�C
		for(String item : ansArray) { //�@�@���
			System.out.println(item + ": " + str.contains(item)); 	
		}
	}
	
	@Test
	public void objectMapperTest() {
		String str = "[{\"id\":1,\"title\":\"���d�\?\",\"options\":\"�Q����;���ޱ�;�γ�;�N���L\",\"type\":\"���\",\"is_necessary\":ture}]";
		String qStr = "{\"id\":2,\"title\":\"�����~��?\",\"options\":\"1���\;2���\;3���\;4���\;5���\\",\"type\":\"���\",\"is_necessary\":ture}";
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			Question q = mapper.readValue(qStr, Question.class);
			System.out.println(q);
//			===================
			List list = mapper.readValue(str, List.class);
			List<Question> list1 = mapper.readValue(str, List.class);
			List<Question> quList = mapper.readValue(str, new TypeReference<>(){});
			System.out.println("===============");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
}
		
			



				


				
	
				
}
	
	
		
		



