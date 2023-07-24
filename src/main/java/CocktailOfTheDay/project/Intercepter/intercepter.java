package CocktailOfTheDay.project.Intercepter;

import CocktailOfTheDay.project.Jwt.JwtValidate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

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
        HttpServletRequest returnRequest = addInfomation(request);
        return super.preHandle(returnRequest, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("=====SpringBoot Controller 진출후");
        super.postHandle(request, response, handler, modelAndView);
    }

    public HttpServletRequest addInfomation(HttpServletRequest _request){

        String getJwt = _request.getHeader("jwt");
        System.out.println(" 받은 토큰 : "+getJwt);

        if(getJwt == null) {
            _request.setAttribute("jwtStateFlag","-1"); // 서비스측에서 플래그가 없으면 FAIL
        }else{
            int resultJwt = JwtValidate.validateToken(getJwt);
            System.out.println(" 토큰상태 : "+resultJwt);
            if(resultJwt == 1) { // 정상토큰
                _request.setAttribute("jwtStateFlag", "1"); // 서비스에서 플래그가 1이 될경우 디비작업
                String[] arrJwt = getJwt.split("\\."); // split 를 할때 "." 로 하면 잘리지 가 않고 null 처리
                String getJwtBody = arrJwt[1].toString(); // 1번에 토큰 body가 있다.
                byte[] decoding = Base64.getDecoder().decode(getJwtBody); // base64로 디코딩을 해준다.
                getJwtBody = new String(decoding); // 바이트를 글자로 변환해서 넣는다.
                try {

                    JSONParser jp = new JSONParser();
                    JSONObject obj = (JSONObject) jp.parse(getJwtBody);
                    String str = obj.get("userId").toString(); // 토큰속 userIdx 항목
                    _request.setAttribute("userId", str); // 서비스에서 리퀘스트로 들어온 값을 가져오기 위해 여기서 셋팅을 해줌
                } catch (Exception e) { // JSON파싱오류도 그냥 로그인 실패로 처리
                    _request.setAttribute("jwtStateFlag", "-1");
                    System.out.println("json에러");
                }
            }else{
                _request.setAttribute("jwtStateFlag", "-1");
                System.out.println("유효하지않은토큰");
            }
        }

        return _request;

    }


}
