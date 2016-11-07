package com.example.android.inventoryapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductDbHelper;

import java.io.IOException;

public class AddProductActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    ProductValidator validator = new ProductValidator();
    ProductDbHelper db = new ProductDbHelper(this);
    Uri uri;
    TextView messageError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Button btnAddImage = (Button) findViewById(R.id.add_image);
        Button btnSaveProduct = (Button) findViewById(R.id.save_product);
        messageError = (TextView) findViewById(R.id.add_prod_err_msg);

        final EditText editProdName = (EditText) findViewById(R.id.edit_item_name);
        final EditText editProdPrice = (EditText) findViewById(R.id.edit_item_price);
        final EditText editProdQty = (EditText) findViewById(R.id.edit_stock);
        final EditText editEmail = (EditText) findViewById(R.id.edit_sup_contact);

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                if (validateInput(editProdName, editProdPrice, editProdQty, editEmail)) {
                    if (uri == null) {
                        messageError.setText("Image required!");
                    } else {

                        messageError.setText("");
                        String prodName = editProdName.getText().toString();
                        String prodPrice = editProdPrice.getText().toString();
                        String prodQty = editProdQty.getText().toString();
                        String email = editEmail.getText().toString();

                        Product pc = new Product();
                        pc.setName(prodName);
                        pc.setPrice(Float.parseFloat(prodPrice));
                        pc.setStock(Integer.parseInt(prodQty));
                        pc.setImage(uri.toString());
                        pc.setSales(0);
                        pc.setSupplier(email);

                        db.addProduct(pc);

                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
            }

        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent intent;

                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }

                checkWriteToExternalPerms();
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }

        });
    }

    private void checkWriteToExternalPerms() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
            }
        } else {
        }
    }

    private boolean validateInput(EditText name, EditText price, EditText stock, EditText email) {
        if (!validator.isEmpty(name) && !validator.isEmpty(price) &&
                !validator.isEmpty(stock) && !validator.isEmpty(email)) {
            if (!validator.isFloat(price)) {
                messageError.setText("Price is in wrong format");
                return false;
            } else {
                if (!validator.isInteger(stock)) {
                    messageError.setText("Quantity is in wrong format");
                    return false;
                } else {
                    return true;
                }
            }
        }else {
            messageError.setText("Missing field detected.");
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            cursor.close();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = (ImageView) findViewById(R.id.image_view);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
