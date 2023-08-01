package CocktailOfTheDay.project.Controller;

import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.model.WikiResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class RecipeController {


    //유저 recipeKeep 가져오기
    @RequestMapping("/api/getRecipeKeep")
    public ArrayList<String> getRecipeKeep(@RequestBody HashMap<String, Object> _userInfo) throws SQLException {

        String user_id = _userInfo.get("user_id").toString();
        System.out.println("RecipeKeepResponse user_id : " + user_id);

        //반환할 String
        ArrayList<String> resultList = new ArrayList<>();

        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            String sql = "select recipe_index from recipe_like where user_id = '" + user_id + "';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //재료 다 담기
            while (rs.next()) {

                String recipe_name = "";

                recipe_name = rs.getString(1);

                //ArrayList에 add하기
                resultList.add(recipe_name);
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


    //좋아요 변경
    @RequestMapping("/api/recipePlus")
    public int recipePlus(@RequestBody HashMap<String, Object> _data) throws SQLException {

        String user_id = _data.get("user_id").toString();
        System.out.println("recipePlus user_id : " + user_id);

        String recipe_index = _data.get("recipe_index").toString();
        System.out.println("recipePlus wiki_name : " + recipe_index);

        int recipe_like = Integer.parseInt(_data.get("recipe_like").toString());
        System.out.println("recipePlus recipe_like : " + recipe_like);


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

            //sql문 세팅, like update
            String sql = "update recipe set recipe_like ='" + (recipe_like +1) +"' where recipe_index ='"
                    + recipe_index + "';";

            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();


            //sql문 세팅, recipe_like에 추가
            sql = "insert into recipe_like (user_id, recipe_index) values (?,?);";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user_id);
            pstmt.setString(2, recipe_index);

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


    @RequestMapping("/api/recipeMinus")
    public int recipeMinus(@RequestBody HashMap<String, Object> _data) throws SQLException {

        String user_id = _data.get("user_id").toString();
        System.out.println("recipeMinus user_id : " + user_id);

        String recipe_index = _data.get("recipe_index").toString();
        System.out.println("recipeMinus wiki_name : " + recipe_index);

        int recipe_like = Integer.parseInt(_data.get("recipe_like").toString());
        System.out.println("recipePlus recipe_like : " + recipe_like);


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

            //sql문 세팅, like update
            String sql = "update recipe set recipe_like ='" + (recipe_like -1) +"' where recipe_index ='"
                    + recipe_index + "';";

            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();


            //sql문 세팅
            sql = "delete from recipe_like where recipe_index = '" + recipe_index + "' and user_id = '" + user_id + "';";


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


    //recipe 삭제
    @RequestMapping("/api/recipeRemove")
    public int recipeRemove(@RequestBody HashMap<String, Object> _userInfo) throws SQLException {

        String recipe_index = _userInfo.get("recipe_index").toString();
        System.out.println("recipeRemove recipe_index : " + recipe_index);

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
            String sql = "delete from recipe where recipe_index = '" + recipe_index + "';";

            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

            //sql문 세팅
            sql = "delete from recipe_detail where recipe_index = '" + recipe_index + "';";


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
