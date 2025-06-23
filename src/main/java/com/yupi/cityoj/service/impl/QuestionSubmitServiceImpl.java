package com.yupi.cityoj.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.cityoj.common.ErrorCode;
import com.yupi.cityoj.exception.BusinessException;
import com.yupi.cityoj.model.dto.questionsubmit.JudgeInfo;
import com.yupi.cityoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.cityoj.model.entity.Question;
import com.yupi.cityoj.model.entity.QuestionSubmit;
import com.yupi.cityoj.model.entity.QuestionSubmit;
import com.yupi.cityoj.model.entity.User;
import com.yupi.cityoj.model.enums.QuestionSubmitLanguageEnum;
import com.yupi.cityoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.cityoj.service.QuestionService;
import com.yupi.cityoj.service.QuestionSubmitService;
import com.yupi.cityoj.service.QuestionSubmitService;
import com.yupi.cityoj.mapper.QuestionSubmitMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author DELL
* @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
* @createDate 2025-06-19 18:29:45
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private QuestionService questionService;

    /**
     * 点赞
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {

        Long questionId = questionSubmitAddRequest.getQuestionId();
        String language = questionSubmitAddRequest.getLanguage();
        String code = questionSubmitAddRequest.getCode();
        //判断编程语言是否合法TO DO
        QuestionSubmitLanguageEnum languageEnumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(languageEnumByValue==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }



        // 判断实体是否存在，根据类别获取实体


        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(code);
        questionSubmit.setLanguage(language);
        //设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITTING.getValue());
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(new JudgeInfo()));
        boolean save=this.save(questionSubmit);
        if(save) {
            return  questionSubmit.getQuestionId();
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入失败");
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionSubmitInner(long userId, long questionId) {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>(questionSubmit);
        QuestionSubmit oldQuestionSubmit = this.getOne(questionSubmitQueryWrapper);
        boolean result;
        // 已点赞
        if (oldQuestionSubmit != null) {
            result = this.remove(questionSubmitQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = questionService.update()
                        .eq("id", questionId)
                        .gt("thumbNum", 0)
                        .setSql("thumbNum = thumbNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(questionSubmit);
            if (result) {
                // 点赞数 + 1
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("thumbNum = thumbNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }


}




