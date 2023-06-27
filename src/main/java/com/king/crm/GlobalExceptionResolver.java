package com.king.crm;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/14
 */

import com.alibaba.fastjson.JSON;
import com.king.crm.base.ResultInfo;
import com.king.crm.exceptions.AuthException;
import com.king.crm.exceptions.NoLoginException;
import com.king.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常统一处理
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /**
     * ⽅法返回值类型
     *  视图
     *  JSON
     * 如何判断⽅法的返回类型：
     *  如果⽅法级别配置了 @ResponseBody 注解，表示⽅法返回的是JSON；
     *  反之，返回的是视图⻚⾯
     * @param request   请求对象
     * @param response  响应对象
     * @param handler   方法对象
     * @param e         异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        /**
         * 判断异常类型
         * 如果是未登录异常，则先执⾏相关的拦截操作
         */
        if (e instanceof NoLoginException) {
            // 如果捕获的是未登录异常，则重定向到登录⻚⾯
            ModelAndView modelAndView = new ModelAndView("redirect:/index");
            return modelAndView;
        }



        /**
         * 设置默认异常处理（返回视图）
         */
        ModelAndView modelAndView = new ModelAndView("error");
        // 设置异常信息
        modelAndView.addObject("code", 500);
        modelAndView.addObject("msg", "系统异常，请稍后再试...");

        // 判断 HandlerMethod
        if (handler instanceof HandlerMethod) {
            // 类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取⽅法上的 ResponseBody 注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            // 判断 ResponseBody 注解是否存在 (如果不存在，表示返回的是视图;如果存在，表示返回的是JSON)
            if (null == responseBody) {
                /**
                 * 返回方法视图
                 */
                if (e instanceof ParamsException) {
                    ParamsException pe = (ParamsException) e;
                    modelAndView.addObject("code", pe.getCode());
                    modelAndView.addObject("msg", pe.getMsg());
                } else if (e instanceof AuthException) {    // 认证异常
                    AuthException a = (AuthException) e;
                    // 设置异常信息
                    modelAndView.addObject("code", a.getCode());
                    modelAndView.addObject("msg",a.getMsg());
                }
                return modelAndView;
            } else {
                /**
                 * 方法上返回JSON
                 */
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，请重试！");
                // 如果捕获的是⾃定义异常
                if (e instanceof ParamsException) {
                    ParamsException pe = (ParamsException) e;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                } else if (e instanceof AuthException) {    // 认证异常
                    AuthException a = (AuthException) e;
                    // 设置异常信息
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }
                // 设置响应类型和编码格式 （响应JSON格式）
                response.setContentType("application/json;charset=utf-8");
                // 得到输出流
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    // 将对象转换成JSON格式，通过输出流输出
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                return null;
            }
        }
        return modelAndView;
    }
}
