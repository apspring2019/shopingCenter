package Models;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCenter {
    private static final ShoppingCenter shop = new ShoppingCenter();
    private ArrayList<Store> stores = new ArrayList<>();

    public static ShoppingCenter getInstance(){
        return shop;
    }

    private ShoppingCenter() {
    }

    public boolean containStore(String name) {
        for (Store store:
                stores) {
            if(store.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void addStore(String storeName , HashMap<String , Product> products ){
        Store store = new Store();
        store.start(storeName , products);
        stores.add(store);
    }

    public void show (String showName ){
        for (Store store:
             stores) {
            if(store.getName().equals(showName)){
                store.show();
                return;
            }
        }
    }

    public void sell(String storeName , String product , int number){
        for (Store store:
                stores) {
            if(store.getName().equals(storeName)){
                store.sell(product , number);
                return;
            }
        }
    }

    public void buy(String storeName , String product , int number , int price ) {
        for (Store store:
                stores) {
            if(store.getName().equals(storeName)){
                Product newProduct = new Product(product , price , number);
                store.buy(newProduct , number);
                return;
            }
        }
    }

    public void changePrice(String storeName ,String  productName ,  int newPrice) {
        for (Store temp:
                stores) {
            if(temp.getName().equals(storeName)){
                temp.changePrice(productName , newPrice);
                return;
            }
        }
    }
}
