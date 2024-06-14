package com.example.quiz.service.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz.constants.OptionType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.Quiz;
import com.example.quiz.entity.Response;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.repository.ResponseDao;
import com.example.quiz.service.ifs.ResponseService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.Fillin;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ResponseServiceImpl implements ResponseService {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private ResponseDao responseDao;

	@Override
	public BasicRes fillin(FillinReq req) {
		// �Ѽ��ˬd
		BasicRes checkReslt = checkParams(req);
		if (checkParams(req) != null) {
			return checkReslt;
		}
		// �ˬd�P�@�ӹq�ܸ��X�O�_�����ƶ�g�P�@�Ӱݨ�
		if(responseDao.existsByQuizIdAndPhone(req.getQuizId(), req.getPhone())) {
			return new BasicRes(ResMessage.DUPLICATED_FILLIN.getCode(),
					ResMessage.DUPLICATED_FILLIN.getMessage());
			
		}
		// �ˬd quiz_id �O�_�s�b��DB��
		// �]������|�h��� req �������׻P�D�ت��ﶵ�O�_�ŦX�A�ҥH�n�� findById
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quiz = op.get();
		// �q quiz �����X questions �r��
		String questionsStr = quiz.getQuestions();
		// �N questions �ন List<question>
		ObjectMapper mapper = new ObjectMapper();
		// fillinStr �n���Ŧr��A���M�w�]�O null
		// �Y fillinStr = null�A������� fillinStr = mapper.writeValueAsString(req.getqIdAnswerMap());
		// �����o�쪺���G��^�� fillinStr �ɡA�|����
		String fillinStr = "";
		try {
			List<Question> quList = mapper.readValue(questionsStr, new TypeReference<>(){});
			// ���C�@�� Question
			for (Question item : quList) {
				for(Entry<Integer, String> map : req.getqIdAnswerMap().entrySet()) {
					//�����D�O�_�O����A�B req ���� qIdAnswerMap ������ qId ������
					//!req.getqIdAnswerMap().containsKey(item.getId()):����ĸ���� map ���䤣�� key ������ value
					// �N��N�O�����D���O�S���@��
					if(item.isNecessary() && !req.getqIdAnswerMap().containsKey(item.getId())) {
						return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(),
								ResMessage.ANSWER_IS_REQUIRED.getMessage());
					}
					// �ˬd���׸�ﶵ�@�P
					// �⵪�צr��(item ���� options)�Τ���(;)���Φ��}�C
					// �z�L item ���� id �� key �Ө��o req.getqIdAnswerMap() ������ value �r��
					// req.getqIdAnswerMap().get(item.getId()): �ϥ� get(key)�Amap �|�ھ� key ���o������ value
					String answerStr = req.getqIdAnswerMap().get(item.getId());
					// �� answerStr(����) ���Φ��}�C
					String[] answerArray = answerStr.split(";");
					// �ư� option type �O ���A�����׫o���h��
					if(item.getType().equalsIgnoreCase(OptionType.SINGLE_CHOICE.getType()) && 
							answerArray.length > 1) {
						return new BasicRes(ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getCode(),
								ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getMessage());
					}
					
					// �D���O��� �B answerArray ������ > 1
					if(item.getType().equalsIgnoreCase(OptionType.SINGLE_CHOICE.getType())&&
							answerArray.length < 1) {
						return new BasicRes(ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getCode(),
								ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getMessage());
					}
					// ��C�ӵ��׸�ﶵ���: �N�O��ﵪ�׸���D���ﶵ�O�_�@�P
					for(String str : answerArray) {
						// ���] item.getOptions() ���ȬO: "A;B;C;D"
						// ���] answerArray =[A , B]
						// for �j�餤�N�O�� A �M B ���O�_�ѳQ�]�t�b�r�� item.getOptions()��
						// �n�ư� option type �O text
						if (!item.getOptions().contains(str)
								&& !item.getType().equalsIgnoreCase(OptionType.TEXT.getType()));
							return new BasicRes(ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getCode(),
								ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getMessage());
					}
				}
			}
			fillinStr = mapper.writeValueAsString(req.getqIdAnswerMap());
		} catch (JsonProcessingException e) {
			return new BasicRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}
		responseDao.save(new Response(req.getQuizId(),req.getName(),req.getPhone(),req.getEmail(),//
				req.getAge(),fillinStr));
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
	}

	private BasicRes checkParams(FillinReq req) {
		if (req.getQuizId() <= 0) {
			return new BasicRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		if (!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_NAME_IS_REQUIRED.getCode(),
					ResMessage.PARAM_NAME_IS_REQUIRED.getMessage());
		}
		if (!StringUtils.hasText(req.getPhone())) {
			return new BasicRes(ResMessage.PARAM_PHONE_IS_REQUIRED.getCode(),
					ResMessage.PARAM_PHONE_IS_REQUIRED.getMessage());
		}
		if (!StringUtils.hasText(req.getEmail())) {
			return new BasicRes(ResMessage.PARAM_EMAIL_IS_REQUIRED.getCode(),
					ResMessage.PARAM_EMAIL_IS_REQUIRED.getMessage());
		}
		if (req.getAge() < 12 || req.getAge() > 99) {
			return new BasicRes(ResMessage.PARAM_AGE_NOT_QUALIFIED.getCode(),
					ResMessage.PARAM_AGE_NOT_QUALIFIED.getMessage());
		}
		return null;
	}

}
