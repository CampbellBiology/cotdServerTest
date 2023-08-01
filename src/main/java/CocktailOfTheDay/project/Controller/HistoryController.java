package CocktailOfTheDay.project.Controller;

import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.model.HistoryResponse;
import CocktailOfTheDay.project.model.IngredientResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class HistoryController {

    @RequestMapping("/api/historyAdd")
    public int historyAdd(@RequestBody HashMap<String, Object> _history) throws SQLException {

        String user_id = _history.get("user_id").toString();
        System.out.println("historyAdd user_id : " + user_id);

        String cocktail_name = _history.get("cocktail_name").toString();
        System.out.println("historyAdd cocktail_name : " + cocktail_name);

        String img_path = _history.get("img_path").toString();
        System.out.println("historyAdd img_path : " + img_path);

        String created_at = _history.get("createdAt").toString();
        System.out.println("historyAdd createdAt : " + img_path);


        //결과로 int result 반환
        int result = 0;

        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            //INSERT INTO my_keep (user_id, ingredient_name) VALUES ('1', '토닉워터');
            // String sql = "insert chat (couple_index, email, sender, message, timestamp2) values (?,?,?,?,?);";
            String sql = "insert into history (user_id, cocktail_name, img_path, created_at) values (?,?,?,?);";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user_id);
            pstmt.setString(2, cocktail_name);
            pstmt.setString(3, img_path);
            pstmt.setString(4, created_at);

            pstmt.executeUpdate();

            result = 1;

        } catch (Exception e) {
            e.printStackTrace();
            result = 99;

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

        return result;
    }




    //유저 history 가져오기
    @RequestMapping("/api/getHistory")
    public ArrayList<HistoryResponse> getHistory(@RequestBody HashMap<String, Object> _userInfo) throws SQLException {

        String user_id = _userInfo.get("user_id").toString();
        System.out.println("historyResponse user_id : " + user_id);

        //반환할 ArrayList
        ArrayList<HistoryResponse> resultList = new ArrayList<>();

        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            String sql = "select * from history where user_id = '" + user_id + "' order by created_at desc;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            //재료 다 담기
            while (rs.next()) {
                //ArrayList에 담을 클래스 생성
                HistoryResponse history = new HistoryResponse();

                //클레스에 멤버 세팅
                history.setUser_id(rs.getString(1));
                history.setCocktail_name(rs.getString(2));
                history.setImg_path(rs.getString(3));
                history.setCreatedAt(rs.getString(4));

                //ArrayList에 add하기
                resultList.add(history);

            }

        } catch (Exception e) {
            e.printStackTrace();

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
        return resultList;

    }


    //history 삭제할 때
    @RequestMapping("/api/historyDelete")
    public int historyDelete(@RequestBody HashMap<String, Object> _info) throws SQLException {


        System.out.println("historyDelete user_id : " + _info.get("user_id"));
        System.out.println("historyDelete createdAt : " + _info.get("createdAt").toString());

        String user_id = _info.get("user_id").toString();
        String created_at = _info.get("createdAt").toString();



        //결과로 int result 반환
        int result = 0;

        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            String sql = "delete from history where created_at = '" + created_at + "' and user_id = '" + user_id + "';";


            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

            result = 1;

        } catch (Exception e) {
            e.printStackTrace();
            result = 99;

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

        return result;
    }


}
