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
		
		String str = "A;B;C;D"; //選項
		String ansStr = "A;E"; //回答
		String[] ansArray = ansStr.split(";");//把回答切成陣列
		for(String item : ansArray) { //一一比對
			System.out.println(item + ": " + str.contains(item)); 	
		}
	}
	
	@Test
	public void objectMapperTest() {
		String str = "[{\"id\":1,\"title\":\"健康餐?\",\"options\":\"松阪豬;炸豬排;煎魚;烤雞腿\",\"type\":\"單選\",\"is_necessary\":ture}]";
		String qStr = "{\"id\":2,\"title\":\"丹丹漢堡?\",\"options\":\"1號餐;2號餐;3號餐;4號餐;5號餐\",\"type\":\"單選\",\"is_necessary\":ture}";
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
	
	
		
		



