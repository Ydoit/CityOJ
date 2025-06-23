package com.yupi.cityoj.service;

import com.yupi.cityoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.cityoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.cityoj.model.entity.User;

/**
* @author DELL
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2025-06-19 18:29:45
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 题目提交（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doQuestionSubmitInner(long userId, long postId);

}
