package controller;

import models.*;
import view.View;
import view.Request;

import java.util.HashMap;

public class ShoppingCenterController {
    private ShoppingCenter shoppingCenter = ShoppingCenter.getInstance();
    private View view = View.getInstance();

    public void main() {
        boolean isFinish = false;
        do {
            Request request = new Request();
            request.getNewCommand();
            if (request.getType() == RequestType.EXIT) {
                isFinish = true;
            }
            if (!request.isValid()) {
                view.printError(request.getError());
                continue;
            }
            switch (request.getType()) {
                case ADD:
                    add(request);
                    break;
                case CHANGE_PRICE:
                    changePrice(view, request);
                break;
                case SHOW:
                    show(view, request);
                    break;
                case SELL:
                    sell(view, request);
                    break;
                case BUY:
                    buy(view, request);
                    break;
            }
        } while (!isFinish);
    }

    private void buy(View view, Request request) {
        if (checkStoreName(view, request))
            return;
        Product buyP = request.getProducts().get(0);
        shoppingCenter.buy(request.getNameOfStore(), buyP.getName(), buyP.getNumber(), buyP.getPrice());
    }

    private void sell(View view, Request request) {
        if (checkStoreName(view, request))
            return;
        Product sellP = request.getProducts().get(0);
        shoppingCenter.sell(request.getNameOfStore(), sellP.getName(), sellP.getNumber());
    }

    private void add(Request request) {
        convertArrayToHashMap(request);
        shoppingCenter.addStore(request.getNameOfStore(), convertArrayToHashMap(request));
    }

    private void changePrice(View view, Request request) {
        if (checkStoreName(view, request))
            return;
        Product changePriceP = request.getProducts().get(0);
        shoppingCenter.changePrice(request.getNameOfStore(), changePriceP.getName(), changePriceP.getPrice());
    }

    private void show(View view, Request request) {
        if (checkStoreName(view, request))
            return;
        shoppingCenter.show(request.getNameOfStore());
    }

    private boolean checkStoreName(View view, Request request) {
        if (!hasStore(shoppingCenter, request)) {
            request.setError(ErrorType.NOT_SUCH_STORE);
            view.printError(request.getError());
            return true;
        }
        return false;
    }

    private boolean hasStore(ShoppingCenter shoppingCenter, Request request) {
        return shoppingCenter.containStore(request.getNameOfStore());
    }

    private HashMap<String, Product> convertArrayToHashMap(Request request) {
        HashMap<String, Product> products = new HashMap<>();
        for (Product product : request.getProducts()
        ) {
            products.put(product.getName(), product);
        }
        return products;
    }
}
