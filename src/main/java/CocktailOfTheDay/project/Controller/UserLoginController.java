package CocktailOfTheDay.project.Controller;

import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.Jwt.JwtCreate;
import CocktailOfTheDay.project.model.LoginResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

@RestController
public class UserLoginController {

    @PostMapping("/api/getUserInfo")
    public String getUserInfo (HttpServletRequest _rq){
        if(_rq.getAttribute("jwtStateFlag").toString().equals("-1")){
            System.out.println("토큰에러");
        }else{
            System.out.println("결과값 flag : "+_rq.getAttribute("jwtStateFlag").toString());
            System.out.println("결과값 userId : "+_rq.getAttribute("userId").toString());
        }
        return _rq.getAttribute("userId").toString();
    }


    @PostMapping("/api/login/")
    public LoginResponse UserLogin(@RequestBody HashMap<String,Object> _UserLogin){
        LoginResponse loginResponse = new LoginResponse();
        String ret; // 반환코드

        String userId = null;
        String userPw = null;

        try {
            userId = _UserLogin.get("userID").toString().trim();
            userPw = _UserLogin.get("userPW").toString().trim();

            // 아이디,비밀번호가 비어있거나 NULL인경우 고의적으로 예외처리 발생시켜 핸들러로 이동시킴
            if(userId == null || userId.equals("")){
                throw new Exception("아이디가 비어있음");
            }else if(userPw == null || userPw.equals("")){
                throw new Exception("비밀번호가 비어있음");
            }else{
                userPw = encPw(userPw);
            }

        }catch(Exception e){
            System.out.println("회원정보 입력데이터가 올바르지 않습니다.");
            ret = "99";
            loginResponse.setResultMsg("USER_INPUT_ERROR");
            loginResponse.setReturnCode(ret);
            loginResponse.setJwtToken("");
            return loginResponse;
        }

        // 2. db처리

        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;

        String retUserId = null;

        try{

            DBconn = new DBConn();
            conn = DBconn.connect();

            String sql = "select id from user_information where id = ? and pw = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,userId);
            pstmt.setString(2,userPw);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                retUserId = rs.getString("id");
            }
            System.out.println("db 실행 끝");

        }catch (Exception e){
            System.out.println("DB처리에서 오류가 발생되었습니다.");
            ret = "99";
            loginResponse.setResultMsg("DB_PROCESS_ERROR");
            loginResponse.setReturnCode(ret);
            loginResponse.setJwtToken("");
            return loginResponse;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }

        if(retUserId == null || retUserId.equals("")){
            // 실행은 되었지만 정보가 없음 = 로그인 실패
            System.out.println("로그인실패");
            ret = "0";
            loginResponse.setResultMsg("LOGIN_PROCESS_FAIL");
            loginResponse.setReturnCode(ret);
            loginResponse.setJwtToken("");
        }else{
            // 로그인정보가 있음 = 로그인성공
            System.out.println("로그인성공 : "+retUserId);

            JwtCreate jwt = new JwtCreate();
            String cookieBurn = jwt.createJwt(retUserId);

            if(cookieBurn == null||cookieBurn.equals("")){
                ret = "99";
                loginResponse.setResultMsg("TOKEN_CREATE_FAIL");
                loginResponse.setReturnCode(ret);
                loginResponse.setJwtToken("");
            }else{
                ret = "1";
                loginResponse.setResultMsg("TOKEN_CREATE_PASS");
                loginResponse.setReturnCode(ret);
                loginResponse.setJwtToken(cookieBurn);
            }
        }
        return loginResponse;
    }

    public String encPw(String _pw){

        // 나중에 회원별로 SALT 처리 해줄것

        String ret = "";
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((_pw).getBytes());
            byte[] pwHash = md.digest();

            StringBuffer sb = new StringBuffer();
            for(byte b:pwHash){
                sb.append(String.format("%02x",b));
            }
            ret = sb.toString();
        }catch(Exception e){
            ret = null;
        }
        return ret;
    }
}
