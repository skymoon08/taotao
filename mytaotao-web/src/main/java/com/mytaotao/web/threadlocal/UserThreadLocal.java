package com.mytaotao.web.threadlocal;

import com.mytaotao.web.bean.User;

public class UserThreadLocal {

    private static final ThreadLocal<User> LOCAl=new ThreadLocal<User>();
    
    public static void set(User user){
        LOCAl.set(user);
    }
    
    public static User get(){
      return LOCAl.get();
    }
        
}
