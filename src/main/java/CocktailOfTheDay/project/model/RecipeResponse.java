package CocktailOfTheDay.project.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeResponse {

    String recipe_index;
    String user_id;
    String cocktail_name;
    String ingredient;
    String amount;
    String method;
    String tip;
    String img_path;
    String recipe_like;
    String view_count;
    String time_stamp;
}
