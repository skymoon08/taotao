package com.mytaotao.web.handlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mytaotao.common.util.CookieUtils;
import com.mytaotao.web.bean.User;
import com.mytaotao.web.service.PropertieService;
import com.mytaotao.web.service.UserService;
import com.mytaotao.web.threadlocal.UserThreadLocal;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    private PropertieService propertieService;

    @Autowired
    private UserService userService;

    /**
     * 前置方法
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        UserThreadLocal.set(null);
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        String loginUrl = this.propertieService.MYTAOTAO_SSO_URL + "/user/login.html";
        if (StringUtils.isEmpty(token)) {
            // 未登录,跳转到登陆页面
            response.sendRedirect(loginUrl);
            return false;
        }
        // 通过SSO的接口服务查询用户信息，如果查询不到，登陆超时
        User user = this.userService.queryUserByToken(token);
        if (null == user) {
            // 登陆超时
            response.sendRedirect(loginUrl);
            return false;
        }
        // 登陆成功
        UserThreadLocal.set(user);// 将User对象绑定到当前线程中
        return true;
    }

    /**
     * 完成方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    /**
     * 后置方法
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
    }

}
