package com.example.constantine.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class UpdateActivity extends Activity {

    public final static String PRICE = "price", NAME = "name";
    private TextView txtPrice, txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        txtName = (TextView)findViewById(R.id.txtNameUpdate);
        txtPrice = (TextView)findViewById(R.id.txtPriceUpdate);

        String name = getIntent().getExtras().getString(NAME);
        String price = getIntent().getExtras().getString(PRICE);

        txtName.setText(name);
        txtPrice.setText(price);

        final Button updBtn = (Button)findViewById(R.id.btnUpdate);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                updBtn.setEnabled(!txtName.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickbtnUpdate(View view) {
        Intent intent = new Intent();

        String price = String.valueOf(txtPrice.getText());
        String name = String.valueOf(txtName.getText());
        intent.putExtra(NAME, name);
        intent.putExtra(PRICE, price);

        setResult(RESULT_OK, intent);
        finish();
    }
}
