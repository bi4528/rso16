package si.fri.rso.seznam.lib;

import java.util.List;

public class CocktailDBResponse {
    private List<CocktailDB> drinks;
    public List<CocktailDB> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<CocktailDB> drinks) {
        this.drinks = drinks;
    }
}
