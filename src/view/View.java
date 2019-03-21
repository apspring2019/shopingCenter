package view;

import Models.ErrorType;
import Models.Product;

public class View {
    private static final View VIEW = new View();

    private View() {
    }

    public static View getInstance() {
        return VIEW;
    }

    public void printError(ErrorType type) {
        if (type == null)
            return;
        if (type != ErrorType.ERROR_WITHOUT_MESSAGE)
            System.out.println(type.getMessage());
    }

    public void showProduct(Product product) {
        System.out.println(product.toString() + " " + product.getNumber() + " " + product.getPrice());
    }
}
