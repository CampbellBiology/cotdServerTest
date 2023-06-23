package CocktailOfTheDay.project.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIngredientResponse {
    String detail_index;
    String recipe_index;
    String ingredient_name;
    String ingredient_amount;
    String ingredient_type;
}
