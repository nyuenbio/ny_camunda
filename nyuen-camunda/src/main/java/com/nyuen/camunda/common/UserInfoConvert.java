package com.nyuen.camunda.common;

import com.nyuen.camunda.domain.vo.UserConvertVo;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/27
 */
public class UserInfoConvert {

    public static Object userConvert(HttpServletRequest request) {
        String loginUserIdStr = request.getHeader("loginUserId");
        String loginUserName = null==request.getHeader("loginUserName")?null: URLDecoder.decode(request.getHeader("loginUserName"));
        Long loginUserId;
        try {
            loginUserId = Long.parseLong(loginUserIdStr);
        }catch (Exception e){
            return new Exception(e);
        }
        return new UserConvertVo(loginUserId,loginUserIdStr,loginUserName);
    }

}
