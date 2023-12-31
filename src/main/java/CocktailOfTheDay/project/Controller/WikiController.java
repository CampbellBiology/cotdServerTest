package CocktailOfTheDay.project.Controller;

import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.model.HistoryResponse;
import CocktailOfTheDay.project.model.RecipeResponse;
import CocktailOfTheDay.project.model.WikiLikeResponse;
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

    //유저 wikiKeep 가져오기
    @RequestMapping("/api/getWikiKeep")
    public ArrayList<String> getWikiKeep(@RequestBody HashMap<String, Object> _userInfo) throws SQLException {

        String user_id = _userInfo.get("user_id").toString();
        System.out.println("WikiKeepResponse user_id : " + user_id);

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
            String sql = "select wiki_name from wiki_like where user_id = '" + user_id + "';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            //재료 다 담기
            while (rs.next()) {

                String wiki_name = "";

                wiki_name = rs.getString(1);

                //ArrayList에 add하기
                resultList.add(wiki_name);

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
    @RequestMapping("/api/wikiPlus")
    public int wikiPlus(@RequestBody HashMap<String, Object> _data) throws SQLException {

        String user_id = _data.get("user_id").toString();
        System.out.println("wikiPlus user_id : " + user_id);

        String wiki_name = _data.get("wiki_name").toString();
        System.out.println("wikiPlus wiki_name : " + wiki_name);

        int wiki_like = Integer.parseInt(_data.get("wiki_like").toString());
        System.out.println("wikiMinus wiki_like : " + wiki_like);


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
            String sql = "update wiki set wiki_like ='" + (wiki_like +1) +"' where wiki_name ='"
                    + wiki_name + "';";

            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();


            //sql문 세팅
            sql = "insert into wiki_like (user_id, wiki_name) values (?,?);";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user_id);
            pstmt.setString(2, wiki_name);

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


    @RequestMapping("/api/wikiMinus")
    public int wikiMinus(@RequestBody HashMap<String, Object> _data) throws SQLException {

        String user_id = _data.get("user_id").toString();
        System.out.println("wikiMinus user_id : " + user_id);

        String wiki_name = _data.get("wiki_name").toString();
        System.out.println("wikiMinus wiki_name : " + wiki_name);

        int wiki_like = Integer.parseInt(_data.get("wiki_like").toString());
        System.out.println("wikiMinus wiki_like : " + wiki_like);


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
            String sql = "update wiki set wiki_like ='" + (wiki_like -1) +"' where wiki_name ='"
                    + wiki_name + "';";

            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();


            //sql문 세팅
            sql = "delete from wiki_like where wiki_name = '" + wiki_name + "' and user_id = '" + user_id + "';";


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
