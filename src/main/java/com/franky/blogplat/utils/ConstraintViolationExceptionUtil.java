package com.franky.blogplat.utils;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Franky on 2019/5/9.
 */
public class ConstraintViolationExceptionUtil {

    public static String getErrorInfo(ConstraintViolationException e){
        List<String> errorInfo = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for(ConstraintViolation<?> violation : violations){
            errorInfo.add(violation.getMessage());
        }
        return StringUtils.join(errorInfo.toArray(), ";");
    }
}
