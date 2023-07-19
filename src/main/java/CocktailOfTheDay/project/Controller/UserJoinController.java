package CocktailOfTheDay.project.Controller;

import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.model.UserModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

@RestController
public class UserJoinController {

    /*

        회원가입 모듈 - mintcho95 - 2023-07-18

     */

    @PostMapping("/api/join")
    public String join(@RequestBody HashMap<String,Object> _userJoinInfo){

        UserModel u = new UserModel(); // 사용자 객체

        u.setId((String)_userJoinInfo.get("user_id"));
        u.setPw(encPw((String)_userJoinInfo.get("user_pw")));
        u.setName((String)_userJoinInfo.get("user_name"));
        u.setEmail((String)_userJoinInfo.get("user_email"));
        u.setBirthday((String)_userJoinInfo.get("user_birthday"));
        u.setGender((String)_userJoinInfo.get("user_gender"));
        u.setPhone((String)_userJoinInfo.get("user_phone"));

        String ret = dbInsert(u);

        System.out.println("처리가 완료 되었습니다.");

        return ret;

    }

    public String encPw(String _pw){
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

    public String dbInsert(UserModel u){

        String retCode = "";

        System.out.println("이름 : "+u.getId());
        System.out.println("비번 : "+u.getPw());
        System.out.println("이름 : "+u.getName());
        System.out.println("이멜 : "+u.getEmail());
        System.out.println("생일 : "+u.getBirthday());
        System.out.println("성별 : "+u.getGender());
        System.out.println("연락 : "+u.getPhone());


        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();

            String sql = "insert into user_information (id,pw,name,email,birthday,gender,phone) "+
                    "values (?,?,?,?,?,?,?);";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,u.getId());
            pstmt.setString(2,u.getPw());
            pstmt.setString(3,u.getName());
            pstmt.setString(4,u.getEmail());
            pstmt.setString(5,u.getBirthday());
            pstmt.setString(6,u.getGender());
            pstmt.setString(7,u.getPhone());

            pstmt.executeUpdate();

            System.out.println("작업 끝!");
            retCode = "1";

        } catch (Exception e) {
            e.printStackTrace();
            retCode = "-1";

        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e2) {
                retCode = "-1";
                e2.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e3) {
                retCode = "-1";
                e3.printStackTrace();
            }
        }

        System.out.println("DB커넥션종료,할일끝!");
        return retCode;

    }
}
