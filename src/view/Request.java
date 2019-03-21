package view;

import models.ErrorType;
import models.Product;
import models.RequestType;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static models.ErrorType.ERROR_WITHOUT_MESSAGE;
import static models.ErrorType.INVALID_INPUT;
import static models.RequestType.*;

public class Request {
    private Scanner scanner = new Scanner(System.in);
    private static final String BUY_FOR_STORE = "buy for";
    private static final String ADD_STORE = "add";
    private static final String CHANGE_PRICE_COMMAND = "change price";
    private static final String SELL_FROM_STORE = "buy from";
    private static final String SHOW_PRODUCT_OF_STORE = "show";
    private static final String END = "end";
    private ArrayList<Product> products = new ArrayList<Product>();
    private ErrorType error = null;
    private String command;

    private boolean validStoreName() {
        if (getNameOfStore().matches("\\d+\\S+") || getNameOfStore().matches("\\d+")) {
            return false;
        }
        return true;
    }

    public void getNewCommand() {
        this.command = scanner.nextLine();
    }

    public void setError(ErrorType error) {
        this.error = error;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ErrorType getError() {
        return error;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isValid() {
        RequestType type = getType();
        if (type == null)
            return false;
        switch (type) {
            case ADD:
                return checkSyntaxOfAddCommand();
            case BUY:
                return checkSyntaxOfBuyCommand();
            case EXIT:
                return true;
            case SELL:
                return checkSyntaxOfSellCommand();
            case SHOW:
                return showValid();
            case CHANGE_PRICE:
                return checkSyntaxOfChangePrice();
        }
        return true;
    }

    private boolean checkSyntaxOfChangePrice() {
        String str = command.substring(CHANGE_PRICE_COMMAND.length());
        Pattern sell = Pattern.compile("(\\S+) of (\\S+) (\\d+)");
        Matcher matcher = sell.matcher(str);
        if (matcher.find()) {
            Product newP = new Product(matcher.group(1), Integer.parseInt(matcher.group(3)), 0);
            products.add(newP);
        } else {
            error = INVALID_INPUT;
            return false;
        }
        return true;
    }

    private boolean showValid() {
        Pattern show = Pattern.compile("(\\S+)");
        String str = command.substring(SHOW_PRODUCT_OF_STORE.length());
        Matcher matcher = show.matcher(str);
        if (matcher.find()) {
            products = null;
        } else {
            error = INVALID_INPUT;
            return false;
        }
        return true;
    }

    private boolean checkSyntaxOfBuyCommand() {
        Pattern productsPattern = Pattern.compile("(\\S+) (\\d+) (\\d+)");
        String nameOfStore = getNameOfStore();
        if (isStoreNameInvalid(nameOfStore))
            return false;
        String str = command.substring(BUY_FOR_STORE.length() + nameOfStore.length() + 2);
        Matcher matcher = productsPattern.matcher(str);
        if (matcher.find()) {
            Product newP = new Product(matcher.group(1), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(2)));
            products.add(newP);
        } else {
            error = INVALID_INPUT;
            return false;
        }
        return true;
    }

    private boolean checkSyntaxOfSellCommand() {
        Pattern productsPattern = Pattern.compile("(\\S+) (\\d+)");
        String nameOfStore = getNameOfStore();
        if (isStoreNameInvalid(nameOfStore)) return false;
        String str = command.substring(SELL_FROM_STORE.length() + nameOfStore.length() + 2);
        Matcher matcher = productsPattern.matcher(str);
        if (matcher.find()) {
            Product newP = new Product(matcher.group(1), 0, Integer.parseInt(matcher.group(2)));
            products.add(newP);
        } else {
            error = INVALID_INPUT;
            return false;
        }
        return true;
    }

    private boolean isStoreNameInvalid(String nameOfStore) {
        if (nameOfStore == null || nameOfStore.equals("")) {
            error = INVALID_INPUT;
            return true;
        }
        return false;
    }

    private boolean checkSyntaxOfAddCommand() {
        String nameOfStore = getNameOfStore();
        boolean hasProduct = false;
        Pattern productNames = Pattern.compile("(\\S+)\\s*(\\d*)\\s*(\\d*)\\s*");
        if (nameOfStore == null || nameOfStore.equals("")) {
            error = ERROR_WITHOUT_MESSAGE;
            products = null;
            return false;
        }
        String string = command.substring(ADD_STORE.length() + nameOfStore.length() + 1);
        Matcher matcher = productNames.matcher(string);
        while (matcher.find()) {
            hasProduct = true;
            if (matcher.group(2).equals("") || matcher.group(3).equals("")) {
                error = INVALID_INPUT;
                products = null;
                return false;
            } else {
                Product invalidPattern = new Product(matcher.group(1), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(2)));
                if (products.contains(invalidPattern)) {
                    error = INVALID_INPUT;
                    products = null;
                    return false;
                }
                products.add(invalidPattern);
            }
        }
        if (!validStoreName()) {
            error = ERROR_WITHOUT_MESSAGE;
            return false;
        }
        if (!hasProduct) {
            error = ERROR_WITHOUT_MESSAGE;
        }
        return hasProduct;
    }

    public RequestType getType() {
        if (command == null || command.equals("")) {
            return null;
        }
        if (command.substring(0, 3).matches(END)) {
            return EXIT;
        } else if (command.substring(0, 3).equals(ADD_STORE)) {
            return ADD;
        } else if (command.substring(0, 4).equals(SHOW_PRODUCT_OF_STORE)) {
            return SHOW;
        } else if (command.substring(0, 7).equals(BUY_FOR_STORE)) {
            return BUY;
        } else if (command.substring(0, 8).equals(SELL_FROM_STORE)) {
            return SELL;
        } else if (command.substring(0, 12).equals(CHANGE_PRICE_COMMAND)) {
            return CHANGE_PRICE;
        }
        return EXIT;
    }

    public String getNameOfStore() {
        switch (getType()) {
            case ADD:
                if (command.split("\\s").length > 1)
                    return command.split("\\s")[1];
                else
                    error = INVALID_INPUT;
                break;
            case BUY:
                if (command.split("\\s").length > 2)
                    return command.split("\\s")[2];
                else
                    error = INVALID_INPUT;
                break;
            case EXIT:
                return null;
            case SELL:
                if (command.split("\\s").length > 2)
                    return command.split("\\s")[2];
                else
                    error = INVALID_INPUT;
                break;
            case SHOW:
                if (command.split("\\s").length > 1)
                    return command.split("\\s")[1];
                else
                    error = INVALID_INPUT;
                break;
            case CHANGE_PRICE:
                if (command.split("\\s").length > 4)
                    return command.split("\\s")[4];
                else
                    error = INVALID_INPUT;
                break;
        }
        return null;
    }
}
