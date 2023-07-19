package CocktailOfTheDay.project.Controller;

import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.model.RecipeLikeResponse;
import CocktailOfTheDay.project.model.WikiLikeResponse;
import CocktailOfTheDay.project.model.WikiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@RestController
public class MyPageController {

    //모든 recipe_like
    @RequestMapping("/api/getAllRecipeKeep")
    public ArrayList<RecipeLikeResponse> getAllRecipeKeep() throws SQLException {

        //재료를 넣어서 반환할 ArrayList
        ArrayList<RecipeLikeResponse> resultList = new ArrayList<>();

        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            String sql = "select * from recipe_like;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            //재료 다 담기
            while (rs.next()) {

                //   ArrayList<RecipeResponse> resultList = new ArrayList<>();
                //ArrayList에 담을 클래스 생성
                RecipeLikeResponse RL = new RecipeLikeResponse();

                //클레스에 멤버 세팅
                RL.setRecipe_index(rs.getString(1));
                RL.setUser_id(rs.getString(2));


                //ArrayList에 add하기
                resultList.add(RL);

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

    //모든 wiki_like
    @RequestMapping("/api/getAllWikiKeep")
    public ArrayList<WikiLikeResponse> getAllWikiKeep() throws SQLException {

        //재료를 넣어서 반환할 ArrayList
        ArrayList<WikiLikeResponse> resultList = new ArrayList<>();

        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            String sql = "select * from wiki_like;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            //재료 다 담기
            while (rs.next()) {

                //   ArrayList<RecipeResponse> resultList = new ArrayList<>();
                //ArrayList에 담을 클래스 생성
                WikiLikeResponse WL = new WikiLikeResponse();

                //클레스에 멤버 세팅
                WL.setWiki_name(rs.getString(1));
                WL.setUser_id(rs.getString(2));


                //ArrayList에 add하기
                resultList.add(WL);

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

}
