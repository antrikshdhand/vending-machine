package VendingMachine.pages;

import VendingMachine.SceneManager;
import VendingMachine.pages.Page;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import com.opencsv.*;

public class PayCard extends Page {
    
    private SceneManager sceneManager;

    public PayCard(SceneManager sceneManager) {
        
        this.sceneManager = sceneManager;

        GridPane grid = new GridPane();
        
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        grid.setAlignment(Pos.CENTER); 
        this.scene = new Scene(grid, WIDTH, HEIGHT);

        Text scenetitle = new Text("Card Payment");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label cardNumberLabel = new Label("Card No:");
        grid.add(cardNumberLabel, 0, 1);

        TextField cardNumberTextField = new TextField();
        grid.add(cardNumberTextField, 1, 1);

        Label cvvLabel = new Label("CVV:");
        grid.add(cvvLabel, 0, 2);

        PasswordField cvvBox = new PasswordField();
        grid.add(cvvBox, 1, 2);

        Button payButton = new Button("Pay");

        cvvBox.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                payButton.fire();
        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hbBtn, 1, 4);
        payButton.setOnAction(e -> {
            this.sceneManager.getDatabase().openConn();

            // Set variables
            String username = sceneManager.getSession().getUserName();
            String cardNumber = cardNumberTextField.getText();
            String cvv = cvvBox.getText();

            boolean checkedCardNumber = checkCardNumber(cardNumber);
            boolean checkedCVV = checkCVV(cvv);

            // Write to transactions.csv if valid
            if (checkedCardNumber == false) {
                Alert invalidCardNumberAlert = new Alert(AlertType.ERROR);
                invalidCardNumberAlert.setTitle("Invalid card number.");
                invalidCardNumberAlert.setHeaderText(String.format("The card number inputted is invalid.", cardNumber));
                invalidCardNumberAlert.setContentText("Please try again.");
                invalidCardNumberAlert.showAndWait();
                return;
            }
            else if (checkedCVV == false) {
                Alert incorrectCVVAlert = new Alert(AlertType.ERROR);
                incorrectCVVAlert.setTitle("Incorrect CVV");
                incorrectCVVAlert.setHeaderText("The CVV inputted was invalid.");
                incorrectCVVAlert.setContentText("Please try again.");
                incorrectCVVAlert.showAndWait();
                return;
            }
            else if (checkedCardNumber == true && checkedCVV == true) {
                writeTransaction(username, cardNumber, cvv, 420.69);
                Alert paymentSuccessfulAlert = new Alert(AlertType.ERROR);
                paymentSuccessfulAlert.setTitle("Success!");
                paymentSuccessfulAlert.setHeaderText("Your payment was a success.");
                paymentSuccessfulAlert.setContentText("Have a great day!");
                paymentSuccessfulAlert.showAndWait();
            }

            // Successful payment
            System.out.println("Payment successful!");
            
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            sceneManager.switchScenes(sceneManager.getCheckoutPageScene());
        });

        // Add buttons to Hbox
        hbBtn.getChildren().add(backButton);
        hbBtn.getChildren().add(payButton);

    }

    /**
     * Writes transaction details to transactions.csv file
     * (found in resources directory)
     * @param username
     * @param cardNumber
     * @param CVV
     * @param amount
     */
    public void writeTransaction(String username, String cardNumber, String cvv, double amount) {

        File file = new File("transactions.csv");
        try {
            // Create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file, true);
    
            // Create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
    
            // Add header to transactions.csv if empty
            if (file.length() == 0) {
                String[] header = {"USERNAME", "CARD_NUMBER", "CVV", "TRANSACTION_AMOUNT"};
                writer.writeNext(header);
            }
    
            // Add data to transactions.csv
            String[] data = {
                username, 
                cardNumber, 
                cvv,
                Double.toString(amount)
            };
            
            writer.writeNext(data);
    
            // Closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Checks if card number is valid.
     * @param cardNumber
     */
    public static boolean checkCardNumber(String cardNumber) {

        long number;

        // Checks if long
        try {
            number = Long.parseLong(cardNumber);
        } catch (Exception e) {
            return false;
        }

        // Checks if 16 digits long
        int numDigits = String.valueOf(number).length();
        if (numDigits != 16)
            return false;

        // If all conditions are met
        return true;
    }


    /**
     * Checks if CVV is valid.
     * @param CVV
     */
    public static boolean checkCVV(String CVV) {

        int number;

        // Checks if integer
        try {
            number = Integer.parseInt(CVV);
        } catch (Exception e) {
            return false;
        }

        // Checks if 16 digits long
        int numDigits = String.valueOf(number).length();
        if (numDigits != 3)
            return false;

        // If all conditions are met
        return true;
    }

    /**
     * Returns the PayCard scene.
     */
    public Scene getScene() {
        return this.scene;
    }

}