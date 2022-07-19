package com.soldier.service.impl;

import com.soldier.domain.User;
import com.soldier.dao.UserDao;
import com.soldier.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {

}
