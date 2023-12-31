package CocktailOfTheDay.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {

    private String id; // 아이디
    private String pw; // 비밀번호
    private String name; // 이름
    private String email; // 이메일
    private String birthday; // 생년월일
    private String gender; // 성별
    private String phone; // 연락처

}