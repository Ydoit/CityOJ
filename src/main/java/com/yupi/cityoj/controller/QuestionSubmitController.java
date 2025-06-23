package com.yupi.cityoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.cityoj.common.BaseResponse;
import com.yupi.cityoj.common.ErrorCode;
import com.yupi.cityoj.common.ResultUtils;
import com.yupi.cityoj.exception.BusinessException;
import com.yupi.cityoj.exception.ThrowUtils;
import com.yupi.cityoj.model.dto.question.QuestionQueryRequest;
import com.yupi.cityoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.cityoj.model.entity.Question;
import com.yupi.cityoj.model.entity.QuestionSubmit;
import com.yupi.cityoj.model.entity.User;
import com.yupi.cityoj.model.vo.QuestionVO;
import com.yupi.cityoj.service.QuestionSubmitService;
import com.yupi.cityoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 提交记录的id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionId = questionSubmitAddRequest.getQuestionId();
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                            HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionSubmitService.getQuestionVOPage(questionPage, request));
    }



}
