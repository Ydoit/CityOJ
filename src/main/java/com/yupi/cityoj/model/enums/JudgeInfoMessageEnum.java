package com.yupi.cityoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件上传业务类型枚举
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum JudgeInfoMessageEnum {

    ACCEPTED("判题通过", "Accepted"),
    WRONG_ANSWER("答案错误", "Wrong"),
    COMPOLE_WRONG("编译错误", "CompoleWrong"),
    MEMORY_LIMIT_EXCEEDED("内存超出限制", "MemoryLimitExceeded"),
    TIME_LINMIT_EXCEEDED("时间超出限制", "TimeLinmitExceeded"),
    PRESENTATION_ERROR("输出格式错误", "PresentationError"),
    OUTPUT_LIMIT_EXCEEDED("输出超出限制", "OutputLimitExceeded"),
    WAITTING("判题中", "Waitting"),
    DANGEROUS_OPERATION("危险操作", "DangerousOperation"),
    RUNTIME_ERROR("运行时错误", "RuntimeError"),
    SYSTEM_ERROR("系统错误", "SystemError");

    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
