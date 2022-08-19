package com.soldier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.soldier.common.BaseCotext;
import com.soldier.common.CustomException;
import com.soldier.common.Result;
import com.soldier.domain.AddressBook;
import com.soldier.domain.OrderDetail;
import com.soldier.domain.Orders;
import com.soldier.dao.OrdersDao;
import com.soldier.domain.ShoppingCart;
import com.soldier.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements IOrdersService {
    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IAddressBookService addressBookService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderDetailService orderDetailService;

    /*   * ************************************
                    //提交订单
                    function  addOrderApi(data){
    *         return $axios({
                               'url': '/order/submit',
    *                 'method': 'post',
    *                 data
                      })
    *     }
    * ************************************
   async goToPaySuccess(){
   const params = {
               remark:this.note,
               payMethod:1,
               addressBookId:this.address.id
   }
   const res = await addOrderApi(params)
       if(res.code === 1){
           window.requestAnimationFrame(()=>{
                   window.location.replace('/front/page/pay-success.html')
           })
       }else{
           this.$notify({ type:'warning', message:res.msg});
       }
   },
 ***********
   */
    @Transactional
    @Override
    public Result<String> addOrder(Orders order) {
        //根据用户ID获得用户购物车详请
        Long userId = BaseCotext.getId();

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lambdaQueryWrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，无法下单");
        }

        Long addressBookId = order.getAddressBookId();
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getId, addressBookId).eq(AddressBook::getUserId, userId);
        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);

        if (addressBook == null) {
            throw new CustomException("地址不存在，无法下单");
        }

        //计算订单金额的
        BigDecimal amount = new BigDecimal(0);
        log.info("BigDecimal amount初始值：" + amount);
        //生成一个订单号id
        long id = IdWorker.getId();

        List<Long> shoppingCartIds = new ArrayList<>();

        //将购物车详情组装成订单详情

        List<OrderDetail> orderDetails = shoppingCarts.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(item.getName());
            orderDetail.setOrderId(id);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.add(item.getAmount().multiply(BigDecimal.valueOf(item.getNumber())));
        //将购物车id保存在集合里，用作后面清空购物车
            shoppingCartIds.add(item.getId());
            return orderDetail;
        }).collect(Collectors.toList());

        //将订单保存
        order.setNumber(String.valueOf(id));
        order.setUserId(userId);
        order.setStatus(1); //订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
        order.setCheckoutTime(LocalDateTime.now()); //这里是简化了，把提交的时候时间算成支付时间
        order.setOrderTime(LocalDateTime.now());
        order.setPayMethod(1);
        order.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        order.setUserName(userService.getById(userId).getName());
        order.setConsignee(addressBook.getConsignee());
        order.setAmount(amount);

        this.save(order);

        //将订单详情关联订单号保存
        orderDetailService.saveBatch(orderDetails);
        //删除购物车信息
        shoppingCartService.removeByIds(shoppingCartIds);
        return new Result<>(1, "提交成功", null);
    }
}
