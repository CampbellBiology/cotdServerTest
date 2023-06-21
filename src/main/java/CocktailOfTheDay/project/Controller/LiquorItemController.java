package CocktailOfTheDay.project.Controller;


import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.model.ItemResponse;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
public class LiquorItemController {

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
                    "            on a.ingredient_index = b.ingredient_index where user_id = '" + user_id + "';";


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


}