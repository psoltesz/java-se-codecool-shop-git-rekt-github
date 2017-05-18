package com.codecool.shop.controller;

import com.codecool.shop.dbconnection.DBConnection;
import com.codecool.shop.model.LineItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Class to handle operations in connection with the Cart (Shopping cart) class.
 */
public class CartController extends DBConnection {

    public void checkCartDB(String userId, Integer productID) {
        String query = "SELECT product_id FROM cart WHERE user_id='" + userId + "' AND product_id = " + productID + ";";

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                updateCartDB(userId, productID);
            } else {
                addToCartDB(userId, productID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTotalPrice(String userID) {
        String query = "SELECT product.defaultprice * cart.quantity AS total FROM cart JOIN product ON cart.product_id = product.id WHERE cart.user_id = '" + userID + "';";
        Double price = (double) 0;

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                price += resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price.toString();
    }

    public ArrayList<LineItem> getCartContentDB(String userID) {
        ArrayList<LineItem> products = new ArrayList<>();
        String query = "SELECT product.name, product.defaultprice, cart.quantity, product.defaultprice * cart.quantity AS total FROM cart JOIN product ON cart.product_id = product.id WHERE cart.user_id = '" + userID + "';";

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String productName = resultSet.getString("name");
                Integer quantity = resultSet.getInt("quantity");
                Integer defaultPrice = resultSet.getInt("defaultprice");
                products.add(new LineItem(productName, defaultPrice, "USD", quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private void updateCartDB(String userId, Integer productID) {
        String query = "UPDATE cart SET quantity = quantity + 1 WHERE user_id='" + userId + "' AND product_id ='" + productID + "';";
        executeQuery(query);
    }

    private void addToCartDB(String userId, Integer prodID) {
        String query = "INSERT INTO cart(user_id, quantity, product_id) VALUES('" + userId + "',1,'" + prodID + "');";
        executeQuery(query);
    }

    public void updateQuantityDB(String userId, String prodName, Integer quantity) {
        Integer prodId = getProductId(prodName);
        String updateQuery = "UPDATE cart SET quantity = " + quantity + " WHERE user_id = '" + userId + "' AND product_id = " + prodId + ";";
        String deleteQuery = "DELETE FROM cart WHERE user_id = '" + userId + "' AND product_id = '" + prodId + "';";

        if (quantity == 0) {
            executeQuery(deleteQuery);
        } else {
            executeQuery(updateQuery);
        }
    }

    public Integer getCartSize(String userID) {
        String query = "SELECT SUM(quantity) AS total_quantity FROM cart WHERE user_id = '" + userID + "';";

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                Integer quantity = resultSet.getInt("total_quantity");
                return quantity;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Integer getProductId(String productName) {
        String query = "SELECT id FROM product WHERE name = '" + productName + "';";

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                Integer productId = resultSet.getInt("id");
                return productId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
