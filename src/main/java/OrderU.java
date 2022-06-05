public class OrderU {

    private String[] ingredients;

    public OrderU(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public OrderU() {
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ingredients='" + ingredients + '\'' +
                '}';
    }


}
