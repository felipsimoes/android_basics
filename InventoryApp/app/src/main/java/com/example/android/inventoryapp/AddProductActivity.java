package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class AddProductActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PRODUCT_LOADER = 0;
    private static final int FILE = 1;
    private static final String TAG = "ProductDetails";
    private EditText editProdName, editProdPrice, editProdQty, editEmail;
    private Button btnAddImage, btnSaveProduct, btnDeleteProduct, btnOrderProduct,
            btnIncreaseQty, btnDecreaseQty;
    private ImageView imageView;
    private boolean productHasChanged = false;
    private Uri currentProductUri;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            productHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Intent intent = getIntent();
        currentProductUri = intent.getData();

        if (currentProductUri != null) {
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }
        initializeButtonsAndEditTexts();

        detailsButtons();

        editProdPrice.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 5, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = editProdPrice.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.toString().indexOf(".") == -1) {
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }
                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });

        btnIncreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(editProdQty.getText().toString());
                qty++;
                editProdQty.setText(String.valueOf(qty));
            }
        });

        btnDecreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(editProdQty.getText().toString());
                qty--;
                editProdQty.setText(String.valueOf(qty));
            }
        });

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrUpdateProduct();
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonImageClick();
            }
        });

        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        btnOrderProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{editEmail.getText().toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Order product");
                intent.putExtra(Intent.EXTRA_TEXT, "Product name: " + editEmail.getText().toString());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private void createOrUpdateProduct() {
        String name, email, price, stock;

        name = editProdName.getText().toString().trim();
        email = editEmail.getText().toString().trim();
        price = editProdPrice.getText().toString().trim();
        stock = editProdQty.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(price)
                || TextUtils.isEmpty(stock)) {
            Toast.makeText(AddProductActivity.this,
                    getString(R.string.editor_insert_product_failed),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageView.getDrawable() == null) {
            Toast.makeText(AddProductActivity.this,
                    getString(R.string.editor_image_required),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap imageBitMap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] imageByteArray = bos.toByteArray();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_STOCK, stock);
        values.put(ProductEntry.COLUMN_SUPPLIER_CONTACT, email);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageByteArray);

        if (currentProductUri == null) {

            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(AddProductActivity.this,
                        getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddProductActivity.this,
                        getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(2000);
                    Intent intent = new Intent(AddProductActivity.this, CatalogActivity.class);
                    startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            int rowsAffected = getContentResolver().update(currentProductUri,
                    values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(AddProductActivity.this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddProductActivity.this, getString(R.string.editor_update_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void detailsButtons() {
        if (currentProductUri == null) {
            btnOrderProduct.setVisibility(View.GONE);
            btnDeleteProduct.setVisibility(View.GONE);
        }
    }

    private void initializeButtonsAndEditTexts() {
        btnAddImage = (Button) findViewById(R.id.add_image);
        btnSaveProduct = (Button) findViewById(R.id.save_product);
        btnDeleteProduct = (Button) findViewById(R.id.detail_btn_delete_prod);
        btnOrderProduct = (Button) findViewById(R.id.detail_btn_order);
        btnIncreaseQty = (Button) findViewById(R.id.increase_count_button);
        btnDecreaseQty = (Button) findViewById(R.id.decrease_count_button);

        editProdName = (EditText) findViewById(R.id.edit_item_name);
        editProdPrice = (EditText) findViewById(R.id.edit_item_price);
        editProdQty = (EditText) findViewById(R.id.edit_stock);
        editEmail = (EditText) findViewById(R.id.edit_sup_contact);

        imageView = (ImageView) findViewById(R.id.image_view);

        editProdName.setOnTouchListener(mTouchListener);
        editProdPrice.setOnTouchListener(mTouchListener);
        editProdQty.setOnTouchListener(mTouchListener);
        editEmail.setOnTouchListener(mTouchListener);
    }

    private void buttonImageClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                    imageView.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                currentProductUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {
            int nameColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int stockColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_STOCK);
            int contactColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_CONTACT);
            int imageColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);

            String productName = data.getString(nameColumnIndex);
            String productPrice = String.valueOf(data.getFloat(priceColumnIndex));
            String productStock = String.valueOf(data.getInt(stockColumnIndex));
            String productSupInfo = data.getString(contactColumnIndex);

            byte[] imageByteArray = data.getBlob(imageColumnIndex);

            Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            imageView.setImageBitmap(bmp);

            editProdName.setText(productName);
            editProdPrice.setText(productPrice);
            editProdQty.setText(productStock);
            editEmail.setText(productSupInfo);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        editProdName.setText("");
        editProdPrice.setText("");
        editProdQty.setText("1");
        editEmail.setText("");
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddProductActivity.this);
        dialogBuilder.setMessage("Are you sure you want to delete?");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton(
                "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteProduct();
                    }
                });
        dialogBuilder.setNegativeButton(
                "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = dialogBuilder.create();
        alert.show();
    }

    private void deleteProduct() {
        if (currentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(currentProductUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        if (!productHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

}
