package org.tc.shiro.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.tc.fastjson.exception.FastJsonException;
import org.tc.mybatis.exception.GunsException;
import org.tc.mybatis.tips.ErrorTip;
import org.tc.mybatis.util.HttpKit;
import org.tc.shiro.core.common.exception.BizExceptionEnum;
import org.tc.shiro.core.log.LogManager;
import org.tc.shiro.core.log.factory.LogTaskFactory;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 全局的的异常拦截器（拦截所有的控制器）
 * 带有@RequestMapping注解的方法上都会拦截）
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午3:19:56
 */
@ControllerAdvice
@Order(-1)
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 拦截业务异常
     */
    @ExceptionHandler(GunsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorTip bizError(GunsException e) {
        HttpKit.getRequest().setAttribute("tip", e.getMessage());
        log.error("业务异常:", e);
        return new ErrorTip(e.getCode(), e.getMessage());
    }

    /**
     * 拦截PO查询异常
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ErrorTip entityNotFound(GunsException e) {
        HttpKit.getRequest().setAttribute("tip", e.getMessage());
        log.error("PO查询异常:", e);
        return new ErrorTip(400, "未查询到相关数据");
    }

    /**
     * 用户未登录异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorTip unAuth(AuthenticationException e) {
        log.error("用户未登陆：", e);
        return new ErrorTip(400, "未查询到相关数据");
    }

    /**
     * 账号被冻结异常
     * (LockedAccountException)
     */
    @ExceptionHandler(DisabledAccountException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorTip accountLocked(DisabledAccountException e) {
        String username = HttpKit.getRequest().getParameter("username");
        LogManager.me().executeLog(LogTaskFactory.loginLog(username, "账号被冻结", HttpKit.getIp()));
        return new ErrorTip(400, "账号被冻结");
    }

    /**
     * 账号密码错误异常
     */
    @ExceptionHandler(CredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorTip credentials(CredentialsException e) {
        String username = HttpKit.getRequest().getParameter("username");
        LogManager.me().executeLog(LogTaskFactory.loginLog(username, "账号密码错误", HttpKit.getIp()));
        return new ErrorTip(400, "账号密码错误");
    }

    /**
     * 账号错误异常
     */
    @ExceptionHandler(UnknownAccountException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorTip unknownAccount(CredentialsException e) {
        String username = HttpKit.getRequest().getParameter("username");
        LogManager.me().executeLog(LogTaskFactory.loginLog(username, "账号密码错误", HttpKit.getIp()));
        return new ErrorTip(400, "账号密码错误");
    }

    /**
     * FastJsonException
     */
    @ExceptionHandler(FastJsonException.class)
    @ResponseBody
    public ErrorTip dateFormateError(GunsException e) {
        HttpKit.getRequest().setAttribute("tip", e.getMessage());
        log.error("前台日期格式错误:", e);
        return new ErrorTip(e.getCode(), e.getMessage());
    }

    /**
     * 处理Kaptcha异常
     *
     * @param exception
     * @return
     */
//    @ExceptionHandler(KaptchaException.class)
//    @ResponseBody
//    public ErrorTip kaptchaExceptionHandler(KaptchaException exception) {
//        String username = HttpKit.getRequest().getParameter("username");
//        LogManager.me().executeLog(LogTaskFactory.loginLog(username, "验证码错误", HttpKit.getIp()));
//        String msg = null;
//        if (exception instanceof KaptchaIncorrectException) {
//            msg = "验证码错误";
//        } else if (exception instanceof KaptchaNotFoundException) {
//            msg = "验证码未找到";
//        } else if (exception instanceof KaptchaTimeoutException) {
//            msg = "验证码过期";
//        } else {
//            msg = "验证码渲染失败";
//        }
//        return new ErrorTip(400, msg);
//    }

    /**
     * 无权访问该资源异常
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorTip credentials(UndeclaredThrowableException e) {
        HttpKit.getRequest().setAttribute("tip", "权限异常");
        log.error("权限异常!", e);
        return new ErrorTip(BizExceptionEnum.NO_PERMITION.getCode(), BizExceptionEnum.NO_PERMITION.getMessage());
    }

//    /**
//     * 重复秒杀异常
//     */
//    @ExceptionHandler(RepeatKillException.class)
//    @ResponseBody
//    public GlobalResult killrepeat(RepeatKillException e) {
//        log.info("重复秒杀异常:", e);
//        return GlobalResult.ok(SeckillExecutionResult.error(SeckillState.REPEAT_KILL));
//    }
//    /**
//     * 秒杀结束异常
//     */
//    @ExceptionHandler(SeckillClosedException.class)
//    @ResponseBody
//    public GlobalResult killover(SeckillClosedException e) {
//        log.info("秒杀结束异常:", e);
//        return GlobalResult.ok(SeckillExecutionResult.error(SeckillState.END));
//    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorTip serverError(RuntimeException e) {
        HttpKit.getRequest().setAttribute("tip", "服务器未知运行时异常");
        log.error("运行时异常:", e);
        return new ErrorTip(BizExceptionEnum.SERVER_ERROR.getCode(), BizExceptionEnum.SERVER_ERROR.getMessage());
    }
}
