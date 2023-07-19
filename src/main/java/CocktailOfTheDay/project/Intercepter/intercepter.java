package CocktailOfTheDay.project.Intercepter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class intercepter extends HandlerInterceptorAdapter {

    /*

        jwt토큰을 처리작업을 하기위한 첫번째과정
        writer : mintcho95
        date : 2023.07.16

     */

    private final Logger logger = LoggerFactory.getLogger(this.getClass()); // 스프링부트 콘솔로그
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("=====SpringBoot Controller 진입전");
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("=====SpringBoot Controller 진출후");
        super.postHandle(request, response, handler, modelAndView);
    }
}
