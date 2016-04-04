package com.mytaotao.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaotao.common.service.RedisService;
import com.mytaotao.sso.mapper.UserMapper;
import com.mytaotao.sso.pojo.User;

@Service
public class UserService {

    @Autowired
    public UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer CACHE_TIME = 60 * 30;

    /**
     * 检测数据是否可用，返回：true:可用； false：不可用，null：参数不合法
     * 
     * @param param
     * @param type
     * @return
     */
    public Boolean check(String param, Integer type) {
        if (type < 1 || type > 3) {
            return null;
        }
        User record = new User();
        switch (type) {
        case 1:
            record.setUsername(param);
            break;
        case 2:
            record.setPhone(param);
            break;
        case 3:
            record.setEmail(param);
            ;
            break;
        default:
            break;
        }
        return this.userMapper.selectOne(record) == null;
    }

    /**
     * 注册页面
     * 
     * @param user
     * @return
     */
    public Boolean doRegister(User user) {
        // 密码要加密,MD5加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        // 初始化数据
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        return this.userMapper.insert(user) == 1;
    }

    /**
     * 登陆页面
     * 
     * @param username
     * @param password
     * @return
     * @throws JsonProcessingException
     */
    public String doLogin(String username, String password) throws Exception {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (null == user) {
            // 该用户不存在
            return null;
        }
        // 判断密码是否相同
        if (!StringUtils.equals(DigestUtils.md5Hex(password), user.getPassword())) {
            // 密码错误
            return null;
        }
        // 登陆成功
        String token = DigestUtils.md5Hex(System.currentTimeMillis() + user.getUsername());
        // 将用户数据保存到redis中，时间为30分钟
        // 当做内存数据库来用
        this.redisService.set("TOKEN_" + token, MAPPER.writeValueAsString(user), CACHE_TIME);
        return token;
    }

    /**
     * 根据token查询用户数据
     * 
     * @param token
     * @return
     */
    public User queryByToken(String token) {
        try {
            String key = "TOKEN_" + token;
            String jsonData = this.redisService.get(key);
            if (StringUtils.isEmpty(jsonData)) {
                // 登陆超时
                return null;
            }
            // 用户处于活跃状态，需要重新设置生存时间
            this.redisService.expire(key, CACHE_TIME);
            return MAPPER.readValue(jsonData, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
