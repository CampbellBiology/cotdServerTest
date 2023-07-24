package CocktailOfTheDay.project.Controller;

import CocktailOfTheDay.project.DBConn.DBConn;
import CocktailOfTheDay.project.model.RecipeModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Random;

@RestController
public class RecipeUploadController {

    // 1. 파일업로드
    // 2. 파일주소 및 ID 리턴
    // jpg , png 방식으로 처리 할것

    private String fileExtension; // 파일확장자
    private int retCode; // 프로그램 실행결과 플래그 전역변수

    @PostMapping("/upload")
    public int uploadImage(HttpServletRequest request,
                           @RequestParam("file") MultipartFile[] photos,
                           @RequestParam("cocktail_name")String cocktail_name,
                           @RequestParam("recipe_detail")String recipe_detail,
                           @RequestParam("recipe_method")String recipe_method,
                           @RequestParam("recipe_tip")String recipe_tip,
                           @RequestParam("recipe_desc")String recipe_desc) {

        String filePath = "";
        int result;
        String UPLOAD_PATH = "c:/COTD/recipe";
        try {
            if (photos.length == 0) {
                // 글 내용은 있지만 사진이 없는경우
                retCode = 2;
            }else{

                // 글쓴이 정보는 인터셉터에서 가져올것이기 때문에 변수처리
                String writer = "master";
                int reservedIdx = 0;

                // 1. AI값을 얻기위한 글을 먼저 작성
                try {
                    //DB 연결
                    DBConn DBconn;
                    Connection conn = null;
                    PreparedStatement pstmt = null;

                    DBconn = new DBConn();
                    conn = DBconn.connect();

                    String sql = "insert into recipe (user_id,cocktail_name) values (\""+writer+"\","+cocktail_name+");";

                    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    pstmt.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys(); // 쿼리 실행 후 생성된 키 값 반환

                    if (rs.next()) {
                        reservedIdx = rs.getInt(1);
                        System.out.println("autoIncrement: " + reservedIdx);  // 출력
                    }

                }catch(Exception e){
                    System.out.println("1차db에러 : "+e);
                }

                // 2. 예약된 글번호를 사진파일명으로 저장
                for (int i = 0; i < photos.length; i++) {

                    //파일이 여러개 일경우
                    MultipartFile file = photos[i];

                    //fileId = (new Date().getTime()) + "" + (new Random().ints(1000, 9999).findAny().getAsInt()); // 현재 날짜와 랜덤 정수값으로 새로운 파일명 만들기
                    String originName = file.getOriginalFilename();

                    //원본 파일명에서 확장자추출
                    fileExtension = originName.substring(originName.lastIndexOf(".") + 1);

                    // 파일 생성
                    File fileSave = new File(UPLOAD_PATH, reservedIdx + "." + fileExtension);

                    // 폴더가 없을경우 폴더 생성
                    if (!fileSave.exists()) {
                        fileSave.mkdirs();
                    }

                    // 파일경로에 맞게 파일생성
                    file.transferTo(fileSave);

                    filePath = "http://andleeme.iptime.org:60722/img/" + reservedIdx + "." + fileExtension;

                    System.out.println("파일업로드 완료 : " + filePath);

                }

                    // 3. 예약된 글번호를 UPDATE문으로 변경

                    RecipeModel rm = new RecipeModel();
                    rm.setUser_id(""); // 앞에서 먼저 작성을 해놓아서 처리 안해도됨
                    rm.setCocktail_name(""); // 앞에서 먼저 작성을 해놓아서 처리 안해도됨
                    rm.setMethod(recipe_method);
                    rm.setTip(recipe_tip);
                    rm.setIsIBA("0"); // 국제바텐터협회
                    rm.setIsTest("0"); // 조주기능사
                    rm.setFilePath(filePath); // 파일경로저장
                    rm.setRecipe_like("0"); //최초 작성시 0개
                    rm.setView_count("0");
                    rm.setTimestamp("");
                    rm.setDescription(recipe_desc);


                    //DBConn 초기회
                    DBConn DBconn;
                    Connection conn = null;
                    PreparedStatement pstmt = null;

                    try {

                        //DB 연결
                        DBconn = new DBConn();
                        conn = DBconn.connect();

                        String sql = "UPDATE recipe SET method = ?, tip = ? , isIBA =?, isTest=?, img_path=?,"+"" +
                                "recipe_like=?,view_count=?,description=? WHERE recipe_index = ?;";

                        pstmt = conn.prepareStatement(sql);

                        pstmt.setString(1,rm.getMethod());
                        pstmt.setString(2,rm.getTip());
                        pstmt.setString(3,rm.getIsIBA());
                        pstmt.setString(4,rm.getIsTest());
                        pstmt.setString(5,rm.getFilePath());
                        pstmt.setString(6,rm.getRecipe_like());
                        pstmt.setString(7,rm.getView_count());
                        pstmt.setString(8,rm.getDescription());
                        pstmt.setInt(9,reservedIdx);

                        pstmt.executeUpdate();

                        System.out.println("작업 끝!");
                        retCode = 1;

                        System.out.println("출력 : " + recipe_detail);

                        JSONParser jsonParser = new JSONParser();
                        JSONObject jsonObj = (JSONObject) jsonParser.parse(recipe_detail);
                        JSONArray memberArray = (JSONArray) jsonObj.get("ingredient_body");

                        try {
                            System.out.println("오브젝트 갯수 : " + memberArray.size());

                            String ingridientQuery = "insert into recipe_detail (recipe_index,ingredient_name,ingredient_amount,ingredient_type) "+
                                    "values (?,?,?,?);";

                            // INSERT 시 AUTOINCREMENT 탐색 - https://stackoverflow.com/questions/7162989/sqlexception-generated-keys-not-requested-mysql
                            pstmt = null; // 기존에있는 연결옵션 지움
                            pstmt = conn.prepareStatement(ingridientQuery);


                            for (int i = 0; i < memberArray.size(); i++) {
                                JSONObject jo = (JSONObject) memberArray.get(i);
                                //System.out.println("재료이름 : " + jo.get("ingredientName")+" 용량 : " + jo.get("ingredientAmount")+" 단위 : " + jo.get("ingredientType"));
                                pstmt.setInt(1,reservedIdx);
                                pstmt.setString(2,(String) jo.get("ingredientName"));
                                pstmt.setString(3,(String) jo.get("ingredientAmount"));
                                pstmt.setString(4,(String) jo.get("ingredientType"));

                                pstmt.executeUpdate();
                                System.out.println("재료배열 업데이트 끝");
                            }

                        } catch (Exception e) {
                            retCode = 99;
                            System.out.println("JSON 재료배열 파싱 오류 : " + e);
                            //return retCode;

                        }

                        System.out.println("디비 업데이트 끝");

                    } catch (Exception e) {
                        e.printStackTrace();
                        retCode = 99;

                    } finally {
                        try {
                            if (pstmt != null) {
                                pstmt.close();
                            }
                        } catch (Exception e2) {
                            retCode = 99;
                            e2.printStackTrace();
                        }
                        try {
                            if (conn != null) {
                                conn.close();
                            }
                        } catch (Exception e3) {
                            retCode = 99;
                            e3.printStackTrace();
                        }
                    }
                }

            } catch (Exception e) {
                retCode =  99; // 실패
            }
            retCode = 1; // 성공
            return retCode;
    }
}
