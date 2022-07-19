package com.soldier.service.impl;

import com.soldier.domain.AddressBook;
import com.soldier.dao.AddressBookDao;
import com.soldier.service.IAddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookDao, AddressBook> implements IAddressBookService {

}
