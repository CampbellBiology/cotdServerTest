package CocktailOfTheDay.project.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientResponse {

    String ingredient_index;
    String ingredient_category;
    String ingredient_name;
    String ingredient_like;
    String img_path;
}
