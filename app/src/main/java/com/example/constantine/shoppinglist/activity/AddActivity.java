package com.example.constantine.shoppinglist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.constantine.shoppinglist.R;
import com.example.constantine.shoppinglist.database.dao.PurchaseDao;
import com.example.constantine.shoppinglist.model.Purchase;

import java.util.List;


public class AddActivity extends Activity {

    public final static String PRICE = "price", NAME = "name";
    private TextView name, price;
    PurchaseDao purchaseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = (TextView)findViewById(R.id.txtNameAdd);
        price = (TextView)findViewById(R.id.txtPriceAdd);

//        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabOk);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (name.getText().toString().equals("")) {
//                    Toast.makeText(getApplicationContext(), "Поле 'Название' не был заполнен",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                purchaseDao.create(createPurchase());
//                Intent intent = new Intent();
//                intent.putExtra(NAME, String.valueOf(name.getText()));
//                if (price.getText().toString().equals("")) {
//                    intent.putExtra(PRICE, 0);
//                } else {
//                    intent.putExtra(PRICE, Double.parseDouble(price.getText().toString()));
//                }
//                setResult(RESULT_OK, intent);
//                Toast.makeText(getApplicationContext(), name.getText().toString() +
//                        " был добавлен в список", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabOk);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Поле 'Название' не был заполнен",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                purchaseDao.create(createPurchase());
                Intent intent = new Intent();
                intent.putExtra(NAME, String.valueOf(name.getText()));
                if (price.getText().toString().equals("")) {
                    intent.putExtra(PRICE, 0);
                } else {
                    intent.putExtra(PRICE, Double.parseDouble(price.getText().toString()));
                }
                setResult(RESULT_OK, intent);
                Toast.makeText(getApplicationContext(), name.getText().toString() +
                        " был добавлен в список", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        purchaseDao = new PurchaseDao(this);
    }

    private Purchase createPurchase() {
        return new Purchase(purchaseDao.getCount(),
                name.getText().toString(),
                Double.parseDouble(price.getText().toString()));
    }

    private boolean purchaseExists(Purchase purchase){
        String name = purchase.getName();
        List<Purchase> books = purchaseDao.getAll();
        int bookCount = books.size();

        for (int i = 0; i < bookCount; i++){
            if (name.compareToIgnoreCase(books.get(i).getName()) == 0){
                return true;
            }
        }
        return false;
    }

}
