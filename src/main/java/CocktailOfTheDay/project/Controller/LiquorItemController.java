package CocktailOfTheDay.project.Controller;


import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.model.ItemResponse;
import CocktailOfTheDay.project.model.RecipeResponse;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
public class LiquorItemController {

    //아이템 리스트 만들 때
    @RequestMapping("/api/item")
    public ArrayList<ItemResponse> liquorItem(@RequestBody HashMap<String, Object> _userInfo) throws SQLException {

        String user_id = _userInfo.get("user_id").toString();
        System.out.println("ItemResponse user_id : " + user_id);


        //결과로 반환할 ArrayList
        ArrayList<ItemResponse> result;

        //재료가 있을 때 재료를 넣어서 반환할 ArrayList
        ArrayList<ItemResponse> resultList = new ArrayList<>();

        //디폴트 결과(재료가 없을 때 반환할 ArrayList)
        ArrayList<ItemResponse> resultListD = new ArrayList<>();

        //ArrayList 안에 담을 클래스
        ItemResponse userCabinetD = new ItemResponse();
        //클래스 안에 멤버 세팅
        userCabinetD.setIngredient_name("재료가 없네요");
        userCabinetD.setImg_path("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Empty_set_symbol.svg/123px-Empty_set_symbol.svg.png");
        //클래스를 ArrayList에 추가
        resultListD.add(userCabinetD);
        //결과 확인해보기
        System.out.println("resultListD : " + resultListD.get(0).getIngredient_name());
        //디폴트값 세팅
        result = resultListD;


        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            String sql = "select b.ingredient_name, b.img_path from my_keep a\n" +
                    "            left join ingredient b\n" +
                    "            on a.ingredient_name = b.ingredient_name where user_id = '" + user_id + "';";

            //String sql = "select ingredient_name, img_path from my_keep where user_id = '" + user_id + "';";


            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            //재료 여러 개 담기
            while (rs.next()) {
                //ArrayList에 담을 클래스 생성
                ItemResponse userCabinet = new ItemResponse();

                //클레스에 멤버 세팅
                userCabinet.setIngredient_name(rs.getString(1));
                userCabinet.setImg_path(rs.getString(2));

                //ArrayList에 add하기
                resultList.add(userCabinet);

                //결과로 재료 리스트 반환하기
                //result가 덮어쓰임
                result = resultList;

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

        return result;
    }



    //item 삭제할 때
    @RequestMapping("/api/itemDelete")
    public int itemDelete(@RequestBody HashMap<String, Object> _info) throws SQLException {



        System.out.println("ItemDelete user_id : " + _info.get("user_id"));
        System.out.println("ItemDelete selectedIngName : " + _info.get("ingredient_name").toString());

        String user_id = _info.get("user_id").toString();
        String selectedIngName = _info.get("ingredient_name").toString();



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
            String sql = "delete from my_keep where ingredient_name = '" + selectedIngName + "' and user_id = '" + user_id + "';";


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


    //item 추가할 때
    @RequestMapping("/api/itemAdd")
    public int itemAdd(@RequestBody HashMap<String, Object> _info) throws SQLException {


        System.out.println("ItemAdd user_id : " + _info.get("user_id"));
        System.out.println("ItemAdd selectedIngName : " + _info.get("ingredient_name").toString());

        String user_id = _info.get("user_id").toString();
        String selectedIngName = _info.get("ingredient_name").toString();



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
            String sql = "insert into my_keep (user_id, ingredient_name) values (?,?);";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user_id);
            pstmt.setString(2, selectedIngName);

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


    //모든 재료 가져오기
    @RequestMapping("/api/getAllIngredient")
    public ArrayList<ItemResponse> itemget() throws SQLException {

        //재료를 넣어서 반환할 ArrayList
        ArrayList<ItemResponse> resultList = new ArrayList<>();

        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            String sql = "select ingredient_name, img_path from ingredient;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            //재료 다 담기
            while (rs.next()) {
                //ArrayList에 담을 클래스 생성
                ItemResponse Cabinet = new ItemResponse();

                //클레스에 멤버 세팅
                Cabinet.setIngredient_name(rs.getString(1));
                Cabinet.setImg_path(rs.getString(2));

                //ArrayList에 add하기
                resultList.add(Cabinet);

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



    //모든 레시피 가져오기
    @RequestMapping("/api/getAllRecipe")
    public ArrayList<RecipeResponse> getRecipe() throws SQLException {

        //재료를 넣어서 반환할 ArrayList
        ArrayList<RecipeResponse> resultList = new ArrayList<>();

        //DBConn 초기회
        DBConn DBconn;
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {

            //DB 연결
            DBconn = new DBConn();
            conn = DBconn.connect();


            //sql문 세팅
            String sql = "select * from recipe;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            //재료 다 담기
            while (rs.next()) {

                //   ArrayList<RecipeResponse> resultList = new ArrayList<>();
                //ArrayList에 담을 클래스 생성
                RecipeResponse recipe = new RecipeResponse();

                //클레스에 멤버 세팅
                recipe.setRecipe_index(rs.getString(1));
                recipe.setUser_id(rs.getString(2));
                recipe.setCocktail_name(rs.getString(3));
                recipe.setIngredient(rs.getString(4));
                recipe.setAmount(rs.getString(5));
                recipe.setMethod(rs.getString(6));
                recipe.setTip(rs.getString(7));
                recipe.setImg_path(rs.getString(8));
                recipe.setRecipe_like(rs.getString(9));
                recipe.setView_count(rs.getString(10));
                recipe.setTime_stamp(rs.getString(11));

                //ArrayList에 add하기
                resultList.add(recipe);

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