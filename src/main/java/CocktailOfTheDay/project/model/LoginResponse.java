package CocktailOfTheDay.project.model;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LoginResponse {
    String returnCode;
    String resultMsg;
    String jwtToken;
}
