package com.mytaotao.sso.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mytaotao.common.util.CookieUtils;
import com.mytaotao.sso.pojo.User;
import com.mytaotao.sso.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {
    
    public static final String COOKIE_NAME="TT_TOKEN";
    @Autowired
    private UserService userService;

    /**
     * 注册页面
     * 
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }
     
    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value="login",method=RequestMethod.GET)
    public String toLogin(){
        return "login";
    }

    /**
     * 对用户注册数据的校验
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable("param") String param,
            @PathVariable("type") Integer type) {
        try {
            Boolean bool = this.userService.check(param, type);
            if (bool == null) {
                // 400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            // 200 为了兼容前端js逻辑，需要将响应内容取反
            return ResponseEntity.ok(!bool);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 对注册的校验
     * @param user
     * @return
     */
    @RequestMapping(value="doRegister",method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doRegister(User user){
        Map<String, Object> result=new HashMap<String, Object>();
        try {
            Boolean bool=this.userService.doRegister(user);
            if (bool) {
                result.put("status", "200");
            }else {
                result.put("status", "400");
                result.put("data", "该用户名、手机号、邮箱已被注册");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "500");
        }
        return ResponseEntity.ok(result);
    }
    /**
     * 用户登陆
     * @param user
     * @return
     */
    @RequestMapping(value="doLogin",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(User user,HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> result=new HashMap<String, Object>();
        try {
            String token=this.userService.doLogin(user.getUsername(),user.getPassword());
            if (StringUtils.isEmpty(token)) {
                //登陆失败 500
                result.put("status", 500);
                return result;
                }
            //登陆成功，需要将token写入到cookie中
            CookieUtils.setCookie(request, response, COOKIE_NAME, token);
            result.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status",500);
        }
        return result;
    }
    
    /**
     * 根据token查询用户信息
     * 
     * @param token
     * @return
     */
    @RequestMapping(value="{token}",method=RequestMethod.GET)
    public ResponseEntity<User> queryByToken(@PathVariable("token")String token){
        try {
            User user=this.userService.queryByToken(token);
            if (null==user) {
                //404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            //200
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
