package com.king.crm.task;

import com.king.crm.service.CustomerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/21
 */

/**
 * 定时任务
 */
@Component
public class JobTask {

    @Resource
    private CustomerService customerService;

    /**
     * 每2秒执行一次
     */
//     @Scheduled(cron = "0/2 * * * * ?")
    public void job() {
        System.out.println("定时任务开始执行 --> " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        // 调用需要被定时执行的方法
        customerService.updateCustomerState();
    }

}
