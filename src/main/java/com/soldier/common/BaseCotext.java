package com.soldier.common;

/**
 * 【工具类：配合 MP 自动插入createUser和UpdateUser】
 *  一次请求是 同一个进程，所以通过ThreadLocal在请求时把id存进当前进程，执型元数据处理是取出
 *
 * @author soldier97
 * @date 2022/7/28 20:23
 */

public class BaseCotext {
   private static ThreadLocal<Long> threadLocal= new ThreadLocal();

    public static void setID(Long id) {
        threadLocal.set(id);
    }
    public static Long getId(){
       return threadLocal.get();
    }
}
