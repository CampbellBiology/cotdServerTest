package CocktailOfTheDay.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeModel {

    private String recipe_title; // 칵테일 레시피 이름
    private String user_id; // 글작성자
    private String cocktail_name; // 칵테일이름
    private String method; // 제조방법
    private String tip; // 나만의팁
    private String isIBA; // 국제바텐더협회
    private String isTest; // 조주기능사
    private String filePath; // 파일경로
    private String recipe_like; // 글업로드당시 안햇갈릴려고 : 기본값 0개
    private String view_count; // 조회수
    private String timestamp; // 작성시간
    private String description; // 칵테일 설명

}
