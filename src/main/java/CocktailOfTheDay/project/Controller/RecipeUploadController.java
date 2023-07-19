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
import java.util.Date;
import java.util.Random;

@RestController
public class RecipeUploadController {

    // 1. 파일업로드
    // 2. 파일주소 및 ID 리턴
    // jpg , png 방식으로 처리 할것

    private String fileId; // 파일명 쓰기위함
    private String fileExtension; // 파일확장자

    private int retCode; // 프로그램 실행결과 플래그 전역변수

    @PostMapping("/upload")
    public int uploadImage(HttpServletRequest request,
                           @RequestParam("file") MultipartFile[] photos,
                           @RequestParam("cocktail_name")String cocktail_name,
                           @RequestParam("recipe_detail")String recipe_detail,
                           @RequestParam("recipe_method")String recipe_method,
                           @RequestParam("recipe_tip")String recipe_tip) {

        int result;
        String UPLOAD_PATH = "c:/uploadSpace";
        try {
            if (photos.length == 0) {
                // 글 내용은 있지만 사진이 없는경우
                retCode = 2;
            } else {

                String filePath = "";

                for (int i = 0; i < photos.length; i++) {

                    //파일이 여러개 일경우
                    MultipartFile file = photos[i];

                    fileId = (new Date().getTime()) + "" + (new Random().ints(1000, 9999).findAny().getAsInt()); // 현재 날짜와 랜덤 정수값으로 새로운 파일명 만들기
                    String originName = file.getOriginalFilename();

                    //원본 파일명에서 확장자추출
                    fileExtension = originName.substring(originName.lastIndexOf(".") + 1);

                    //long fileSize = file.getSize(); // 파일 사이즈

                    // 파일 생성
                    File fileSave = new File(UPLOAD_PATH, fileId + "." + fileExtension);

                    // 폴더가 없을경우 폴더 생성
                    if (!fileSave.exists()) {
                        fileSave.mkdirs();
                    }

                    // 파일경로에 맞게 파일생성
                    file.transferTo(fileSave);

                    filePath = UPLOAD_PATH+"/"+fileId+"."+fileExtension;

                    System.out.println("파일업로드 완료 : "+filePath);

                }

                // 칵테일 재료 배열
                // 올드패션드 예제
                // {"ingredient_body":[{"ingredient_name":"위스키","ingredient_amount":"45","ingredient_type":"ml"},
                //		               {"ingredient_name":"각설탕","ingredient_amount":"1","ingredient_type":"개"},
                //		               {"ingredient_name":"마라스키노 체리","ingredient_amount":"1","ingredient_type":"개"},
                //		               {"ingredient_name":"오렌지필","ingredient_amount":"1","ingredient_type":"개"},
                //		               {"ingredient_name":"앙고스투라 비터","ingredient_amount":"2","ingredient_type":"dash"},
                // ]}
                System.out.println("출력 : " + recipe_detail);

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObj = (JSONObject) jsonParser.parse(recipe_detail);
                JSONArray memberArray = (JSONArray) jsonObj.get("ingredient_body");

                // 1차로 사용자가 레시피 등록후
                // 2차로 레시피디테일 테이블에 IDX 값을 가져올 글 참고 : https://narup.tistory.com/82
                try {
                    System.out.println("오브젝트 갯수 : " + memberArray.size());

                    for (int i = 0; i < memberArray.size(); i++) {
                        JSONObject jo = (JSONObject) memberArray.get(i);
                        System.out.println("재료이름 : " + jo.get("ingredientName")+" 용량 : " + jo.get("ingredientAmount")+" 단위 : " + jo.get("ingredientType"));
                    }

                } catch (Exception e) {
                    retCode = 99;
                    System.out.println("오류 : " + e);
                }

                // 아직 출력만 구현하고 DB쿼리를 안함
                System.out.println("칵테일 제목 : "+cocktail_name);
                System.out.println("제조방법 : "+recipe_method);
                System.out.println("나만의팁 : "+recipe_tip);

                // 여기서부터 글쓰기 구간

                String writerId = "master";// 나중에 스프링부트 인터셉터를 통해서 jwt 처리할 예정

                RecipeModel rm = new RecipeModel();
                rm.setUser_id(writerId);
                rm.setCocktail_name(cocktail_name);
                rm.setMethod(recipe_method);
                rm.setTip(recipe_tip);
                rm.setIsIBA("0"); // 국제바텐터협회
                rm.setIsTest("0"); // 조주기능사
                rm.setFilePath(filePath); // 파일경로저장
                rm.setRecipe_like("0"); //최초 작성시 0개
                rm.setView_count("0");
                rm.setTimestamp("");


                //DBConn 초기회
                DBConn DBconn;
                Connection conn = null;
                PreparedStatement pstmt = null;

                try {
                    //DB 연결
                    DBconn = new DBConn();
                    conn = DBconn.connect();

                    String sql = "insert into recipe (user_id, cocktail_name,method,tip,isIBA,isTest,img_path,recipe_like,view_count) "+
                            "values (?,?,?,?,?,?,?,?,?);";

                    pstmt = conn.prepareStatement(sql);

                    //pstmt.setString(1,"41"); // 나중에 alter로 autoincrement추가 해야할듯
                    pstmt.setString(1,rm.getUser_id());
                    pstmt.setString(2,rm.getCocktail_name());
                    pstmt.setString(3,rm.getMethod());
                    pstmt.setString(4,rm.getTip());
                    pstmt.setString(5,rm.getIsIBA());
                    pstmt.setString(6,rm.getIsTest());
                    pstmt.setString(7,rm.getFilePath());
                    //System.out.println("파일경로 : "+rm.getFilePath());
                    pstmt.setString(8,rm.getRecipe_like());
                    pstmt.setString(9,rm.getView_count());

                    pstmt.executeUpdate();

                    System.out.println("작업 끝!");
                    retCode = 1;

                    // 나중에 https://narup.tistory.com/82 참고해서 글번호 가져오는거 구현해서 레시피디테일 테이블 쿼리 구성할것

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
