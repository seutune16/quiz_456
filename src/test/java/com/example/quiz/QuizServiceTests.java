package com.example.quiz;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.quiz.constants.OptionType;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateOrUpdateReq;
import com.example.quiz.vo.Question;

@SpringBootTest
public class QuizServiceTests {

	@Autowired
	private QuizService quizService;
	
	@Autowired
	private QuizDao quizDao;
	
	@Test
	public void createTest() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1,"���d�\","�Q����;���ޱ�;�γ�;�����L",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(2,"�����~��","1���\;2���\;3���\;4���\",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(3,"�W½�Ѫ���","��������;���ת���;�׵�����;��X����",//
				OptionType.SINGLE_CHOICE.getType(),true));
		CreateOrUpdateReq req = new CreateOrUpdateReq("���\�Yԣ?","���\�Yԣ?",LocalDate.of(2024,6,1),//
				LocalDate.of(2024,6,1),questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getStatusCode() == 200, "create test false!!");
		// �R�����ո�� TODO
	}
	
	@Test
	public void createNameErrorTest() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1,"���d�\","�Q����;���ޱ�;�γ�;�����L",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(2,"�����~��","1���\;2���\;3���\;4���\",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(3,"�W½�Ѫ���","��������;���ת���;�׵�����;��X����",//
				OptionType.SINGLE_CHOICE.getType(),true));
		CreateOrUpdateReq req = new CreateOrUpdateReq("","���\�Yԣ?",LocalDate.of(2024,6,1),//
				LocalDate.of(2024,6,1),questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("param name error!!"), "create test false!!");
	}
	
	@Test
	public void createStartDateErrorTest() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1,"���d�\","�Q����;���ޱ�;�γ�;�����L",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(2,"�����~��","1���\;2���\;3���\;4���\",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(3,"�W½�Ѫ���","��������;���ת���;�׵�����;��X����",//
				OptionType.SINGLE_CHOICE.getType(),true));
		// ���ѬO 2024/5/30�A�ҥH�}�l�������O���ѩάO���e
		CreateOrUpdateReq req = new CreateOrUpdateReq("���\�Yԣ?","���\�Yԣ?",LocalDate.of(2024,5,30),//
				LocalDate.of(2024,6,1),questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("param start date error!!"), "create test false!!");
	}
	
	@Test
	public void createTest1() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1,"���d�\","�Q����;���ޱ�;�γ�;�����L",//
				OptionType.SINGLE_CHOICE.getType(),true));
		// ���� name error
		CreateOrUpdateReq req = new CreateOrUpdateReq("","���\�Yԣ?",LocalDate.of(2024,6,1),//
				LocalDate.of(2024,6,1),questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("param name error!!"), "create test false!!");
		// ���� start date error :�@// ���]���ѬO 2024/5/30�A�ҥH�}�l�������O���ѩάO���e
		req = new CreateOrUpdateReq("���\�Yԣ?","���\�Yԣ?",LocalDate.of(2024,5,29),//
				LocalDate.of(2024,6,1),questionList, true);
		res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("param start date error!!"), "create test false!!");
		//���� end date error: ������������}�l�����
		req = new CreateOrUpdateReq("���\�Yԣ?","���\�Yԣ?",LocalDate.of(2024,6,31),//
				LocalDate.of(2024,6,1),questionList, true);
		res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("param end date error!!"), "create test false!!");
		// �Ѿl���޿�����P�_������A�̫�~�|�O���զ��\������
		req = new CreateOrUpdateReq("���\�Yԣ?","���\�Yԣ?",LocalDate.of(2024,6,1),//
				LocalDate.of(2024,6,1),questionList, true);
		res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getStatusCode() == 200, "create test false!!");
		System.out.println("========================");
	}


}
