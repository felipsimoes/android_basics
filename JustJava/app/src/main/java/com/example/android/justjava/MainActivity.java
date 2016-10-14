package com.example.android.justjava; /**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.name;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    int price = 5;
    boolean hasWhippedCream = false;
    boolean hasChocolate = false;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        int price = calculatePrice();
        Log.v("MainActivity", "The price is " + price);
        CheckBox whippedCreamBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        hasWhippedCream = whippedCreamBox.isChecked();
        Log.v("MainActivity", "Whipped cream is " + hasWhippedCream);
        CheckBox chocolateBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        hasChocolate = chocolateBox.isChecked();
        Log.v("MainActivity", "Chocolate is " + hasChocolate);
        EditText userNameText = (EditText) findViewById(R.id.name_field);
        userName = userNameText.getText().toString();
        Log.v("MainActivity", "Name: " + userName);

        String[] adress = new String[]{"felipe_lima_simoes@hotmail.com"};
        composeEmail(adress, "Just Java order for"+userName, createOrderSummary(price));
//        displayMessage(createOrderSummary(price));
    }

    public void composeEmail(String[] addresses, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @return the price
     */
    private int calculatePrice() {
        int basePrice = 5;
        if(hasWhippedCream){
            basePrice = basePrice + 1;
        }
        if(hasChocolate){
            basePrice = basePrice + 2;
        }
        return quantity * basePrice;
    }

    private String createOrderSummary(int price) {
        String priceMessage = "Name: "+ userName ;
        priceMessage += "\nAdd whipped cream? " + hasWhippedCream;
        priceMessage += "\nAdd chocolate? " + hasChocolate;
        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\nTotal: $" + price;
        priceMessage += "\nThank you!";
        return priceMessage;
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        int quantityValue = quantity;
        if(quantityValue == 100){
            Toast.makeText(getApplicationContext(), "You cannot have more than 100 cups of coffee", Toast.LENGTH_LONG).show();
            return;
        }
        quantity++;
        display(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        int quantityValue = quantity;
        if(quantityValue == 1){
            Toast.makeText(getApplicationContext(), "You cannot have less than 1 cup of coffee", Toast.LENGTH_LONG).show();
            return;
        }
        quantity--;
        display(quantity);
    }

}
