package CocktailOfTheDay.project.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryResponse {

    String user_id;
    String cocktail_name;
    String img_path;
    String createdAt;
}
