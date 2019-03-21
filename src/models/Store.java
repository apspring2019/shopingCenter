package models;

import view.View;

import java.util.HashMap;


public class Store {
    private String name;
    private int money = 20;
    private HashMap<String, Product> products;

    public void start(String storeName, HashMap<String, Product> products) {
        this.name = storeName;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Product> getProducts() {
        return products;
    }

    public void changePrice(String productName, int newPrice) {
        if (products.containsKey(productName))
            products.get(productName).setPrice(newPrice);
    }

    public void buy(Product product, int number) {
        if (money >= product.getPrice() * number) {
            if (products.containsValue(product)) {
                products.get(product.toString()).setNumber(products.get(product.toString()).getNumber() + number);
                money -= product.getPrice() * number;
            } else {
                products.put(product.toString(), product);
                money -= product.getPrice() * number;
            }
        } else {
            View.getInstance().printError(ErrorType.NOT_ENOUGH_MONEY);
        }

    }

    public void sell(String productName, int number) {
        Product product = products.get(productName);
        if (!products.containsKey(productName)) {
            View.getInstance().printError(ErrorType.DONT_HAVE_PRODUCT);
            return;
        }
        if (product.getNumber() - number < 0) {
            View.getInstance().printError(ErrorType.DONT_HAVE_PRODUCT);
            return;
        }

        product.setNumber(product.getNumber() - number);
        money += number * product.getPrice();

    }

    public void show() {
        Product[] productList = sortProductByNumber();
        for (Product p : productList
                ) {
            if (p.getNumber() != 0)
                View.getInstance().showProduct(p);
        }
    }

    private Product[] sortProductByNumber() {
        Product[] productList = products.values().toArray(new Product[0]);
        for (int i = 0; i < productList.length; i++) {
            for (int j = 0; j < productList.length - 1 - i; j++) {
                if (productList[j].getNumber() > productList[j + 1].getNumber() || (productList[j].getNumber() == productList[j + 1].getNumber() && productList[j].toString().compareTo(productList[j + 1].toString()) > 0)) {
                    Product temp = productList[j];//swap
                    productList[j] = productList[j + 1];
                    productList[j + 1] = temp;
                }
            }
        }
        return productList;
    }
}

