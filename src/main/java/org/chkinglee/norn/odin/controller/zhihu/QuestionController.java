package org.chkinglee.norn.odin.controller.zhihu;

import com.alibaba.fastjson.JSON;
import org.chkinglee.norn.odin.controller.dto.ResultResponse;
import org.chkinglee.norn.odin.mapper.zhihu.QuestionRepository;
import org.chkinglee.norn.odin.model.zhihu.Question;
import org.chkinglee.norn.odin.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.chkinglee.norn.odin.utils.Constant.*;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/7/30
 **/
@RestController
@RequestMapping(value = API_V1 + "/zhihu/question")
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    DocService docService;

    @GetMapping("/{questionTitleKeyword}")
    public ResultResponse<List<Question>> getQuestionByTitleKeyword(@PathVariable("questionTitleKeyword") String keyword) {
        List<Question> questionList = questionRepository.findByTitleContainingAndTitleDetailContaining(keyword, keyword);

        for (Question question : questionList) {
            docService.createOrUpdateDoc("hogwarts", "mail", String.valueOf(question.getQuestionId()), JSON.toJSONString(question));
        }


        return new ResultResponse<>(SUCCESS_CODE, SUCCESS_MSG, questionList);
    }


}
