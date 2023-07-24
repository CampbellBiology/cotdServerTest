package CocktailOfTheDay.project.Controller;

import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.model.HistoryResponse;
import CocktailOfTheDay.project.model.RecipeResponse;
import CocktailOfTheDay.project.model.WikiResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class WikiController {

    //모든 레시피 가져오기
    @RequestMapping("/api/getAllWiki")
    public ArrayList<WikiResponse> getWiki() throws SQLException {

        //재료를 넣어서 반환할 ArrayList
        ArrayList<WikiResponse> resultList = new ArrayList<>();

        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            String sql = "select * from wiki;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            //재료 다 담기
            while (rs.next()) {

                //   ArrayList<RecipeResponse> resultList = new ArrayList<>();
                //ArrayList에 담을 클래스 생성
                WikiResponse wiki = new WikiResponse();

                //클레스에 멤버 세팅
                wiki.setWiki_index(rs.getString(1));
                wiki.setWiki_category(rs.getString(2));
                wiki.setWiki_name(rs.getString(3));
                wiki.setWiki_like(rs.getString(4));
                wiki.setWiki_img_path(rs.getString(5));
                wiki.setWiki_description(rs.getString(6));

                //ArrayList에 add하기
                resultList.add(wiki);

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
