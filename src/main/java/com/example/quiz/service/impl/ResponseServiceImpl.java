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
		// 參數檢查
		BasicRes checkReslt = checkParams(req);
		if (checkParams(req) != null) {
			return checkReslt;
		}
		// 檢查同一個電話號碼是否有重複填寫同一個問券
		if(responseDao.existsByQuizIdAndPhone(req.getQuizId(), req.getPhone())) {
			return new BasicRes(ResMessage.DUPLICATED_FILLIN.getCode(),
					ResMessage.DUPLICATED_FILLIN.getMessage());
			
		}
		// 檢查 quiz_id 是否存在於DB中
		// 因為後續會去比對 req 中的答案與題目的選項是否符合，所以要用 findById
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quiz = op.get();
		// 從 quiz 中取出 questions 字串
		String questionsStr = quiz.getQuestions();
		// 將 questions 轉成 List<question>
		ObjectMapper mapper = new ObjectMapper();
		// fillinStr 要給空字串，不然預設是 null
		// 若 fillinStr = null，後續執行 fillinStr = mapper.writeValueAsString(req.getqIdAnswerMap());
		// 把執行得到的結果塞回給 fillinStr 時，會報錯
		String fillinStr = "";
		try {
			List<Question> quList = mapper.readValue(questionsStr, new TypeReference<>(){});
			// 比對每一個 Question
			for (Question item : quList) {
				for(Entry<Integer, String> map : req.getqIdAnswerMap().entrySet()) {
					//比對該題是否是必填，且 req 中的 qIdAnswerMap 對應的 qId 有答案
					//!req.getqIdAnswerMap().containsKey(item.getId()):有驚嘆號表示 map 中找不到 key 對應的 value
					// 意思就是必填題但是沒有作答
					if(item.isNecessary() && !req.getqIdAnswerMap().containsKey(item.getId())) {
						return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(),
								ResMessage.ANSWER_IS_REQUIRED.getMessage());
					}
					// 檢查答案跟選項一致
					// 把答案字串(item 中的 options)用分號(;)切割成陣列
					// 透過 item 中的 id 當成 key 來取得 req.getqIdAnswerMap() 對應的 value 字串
					// req.getqIdAnswerMap().get(item.getId()): 使用 get(key)，map 會根據 key 取得對應的 value
					String answerStr = req.getqIdAnswerMap().get(item.getId());
					// 把 answerStr(答案) 切割成陣列
					String[] answerArray = answerStr.split(";");
					// 排除 option type 是 單選，但答案卻有多個
					if(item.getType().equalsIgnoreCase(OptionType.SINGLE_CHOICE.getType()) && 
							answerArray.length > 1) {
						return new BasicRes(ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getCode(),
								ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getMessage());
					}
					
					// 題型是單選 且 answerArray 的長度 > 1
					if(item.getType().equalsIgnoreCase(OptionType.SINGLE_CHOICE.getType())&&
							answerArray.length < 1) {
						return new BasicRes(ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getCode(),
								ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getMessage());
					}
					// 把每個答案跟選項比對: 就是比對答案跟該題的選項是否一致
					for(String str : answerArray) {
						// 假設 item.getOptions() 的值是: "A;B;C;D"
						// 假設 answerArray =[A , B]
						// for 迴圈中就是把 A 和 B 比對是否由被包含在字串 item.getOptions()中
						// 要排除 option type 是 text
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
