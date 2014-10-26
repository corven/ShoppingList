package com.example.constantine.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static final int EDIT = 0, DELETE = 1, SCREEN_ADD = 0, SCREEN_UPDATE = 1;
    private static final String SAVED_SUM = "saved_sum", PRICE = "price", NAME = "name";

    TextView txtSumPurchases;
    List<Purchase> purchaseList = new ArrayList<Purchase>();
    DBHandler dbHandler;
    ArrayAdapter<Purchase> purchaseAdapter;
    ListView purchaseListView;
    int longClickedItemIndex;
    SharedPreferences SPsumPurchases;
    private double sumPurchases;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sumDisplay();


        dbHandler = new DBHandler(getApplicationContext());
        purchaseListView = (ListView)findViewById(R.id.listView);

        longClickedView();

        if (dbHandler.getPuschaseCount() != 0){
            purchaseList.addAll(dbHandler.getAllPurchases());
        }

        populateList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    private void longClickedView(){
        registerForContextMenu(purchaseListView);
        purchaseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });
    }

    private void sumDisplay(){
        txtSumPurchases = (TextView)findViewById(R.id.txtSumPurchases);
        SPsumPurchases = getPreferences(MODE_PRIVATE);

        String getSum = SPsumPurchases.getString(SAVED_SUM, "");

        if (!getSum.isEmpty()) {
            sumPurchases = Double.parseDouble(getSum);
            txtSumPurchases.setText(String.valueOf(sumPurchases));
        }else {
            sumPurchases = 0;
        }
    }

    private void commitSum() {
        ed = SPsumPurchases.edit();
        ed.putString(SAVED_SUM, String.valueOf(sumPurchases));
        ed.commit();
        txtSumPurchases.setText(String.valueOf(sumPurchases));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Operations operation = new Operations();

        if (requestCode == SCREEN_ADD & resultCode == RESULT_OK){
            String name = data.getStringExtra(AddActivity.NAME);
            String price = data.getStringExtra(AddActivity.PRICE);

            operation.add(name, price);
        }
        if (requestCode == SCREEN_UPDATE & resultCode == RESULT_OK){
            String name = data.getStringExtra(AddActivity.NAME);
            String price = data.getStringExtra(AddActivity.PRICE);

            operation.update(name, price);
        }
        if (requestCode == SCREEN_UPDATE & resultCode == RESULT_CANCELED){
            Purchase purchase = purchaseList.get(longClickedItemIndex);
            sumPurchases += purchase.getPrice();
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderIcon(R.drawable.pencil1);
        menu.setHeaderTitle("Опции");
        menu.add(Menu.NONE, EDIT, Menu.NONE, "Изменить");
        menu.add(Menu.NONE, DELETE, Menu.NONE, "Удалить");
    }

    public boolean onContextItemSelected(MenuItem item){
        Operations operation = new Operations();
        switch (item.getItemId()){
            case EDIT:
                operation.startActivityUpdate();
                break;

            case DELETE:
                operation.delete();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private class PurchaseListAdapter extends ArrayAdapter<Purchase>{
        public PurchaseListAdapter() {
            super(MainActivity.this, R.layout.list_view, purchaseList);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null){
                view = getLayoutInflater().inflate(R.layout.list_view, parent, false);
            }

            Purchase currentPurchase = purchaseList.get(position);

            TextView name = (TextView)view.findViewById(R.id.txtSumPurchases);
            name.setText(currentPurchase.getName());
            TextView price = (TextView)view.findViewById(R.id.price);
            price.setText(String.valueOf(currentPurchase.getPrice()));

            return view;
        }
    }

    private class Operations {
        protected void add(String name, String price) {
            Purchase purchase = new Purchase(dbHandler.getPuschaseCount(), name, Double.parseDouble(price));

            if (!purchaseExists(purchase)){
                dbHandler.createPurchase(purchase);
                purchaseList.add(purchase);
                purchaseAdapter.notifyDataSetChanged();
                sumPurchases += purchase.getPrice();

                commitSum();

                Toast.makeText(getApplicationContext(), name + " был добавлен в список",
                        Toast.LENGTH_SHORT).show();
            }
        }

        protected void delete() {
            Purchase purchase = purchaseList.get(longClickedItemIndex);

            dbHandler.deletePurchase(purchase);
            purchaseList.remove(longClickedItemIndex);
            purchaseAdapter.notifyDataSetChanged();

            sumPurchases -= purchase.getPrice();

            commitSum();

            Toast.makeText(getApplicationContext(), purchase.getName() + " был удален из списка",
                    Toast.LENGTH_SHORT).show();
        }

        protected void update(String name, String price) {
            Purchase purchase = new Purchase(dbHandler.getPuschaseCount(), name, Double.parseDouble(price));

            dbHandler.updatePurchase(purchase);
            purchaseList.set(longClickedItemIndex, purchase);
            purchaseAdapter.notifyDataSetChanged();

            sumPurchases += purchase.getPrice();
            commitSum();

            Toast.makeText(getApplicationContext(), "Запись обновлена", Toast.LENGTH_SHORT).show();
        }

        private void startActivityUpdate(){
            Purchase purchase = purchaseList.get(longClickedItemIndex);
            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);

            sumPurchases -= purchase.getPrice();


            String price = String.valueOf(purchase.getPrice());
            intent.putExtra(NAME, purchase.getName());
            intent.putExtra(PRICE, price);
            startActivityForResult(intent, SCREEN_UPDATE);
        }

    }

    private boolean purchaseExists(Purchase purchase){
        String name = purchase.getName();
        int bookCount = purchaseList.size();

        for (int i = 0; i < bookCount; i++){
            if (name.compareToIgnoreCase(purchaseList.get(i).getName()) == 0){
                return true;
            }
        }
        return false;
    }

    private void populateList(){
        purchaseAdapter = new PurchaseListAdapter();
        purchaseListView.setAdapter(purchaseAdapter);
    }

    public void onClickbtnAddRec(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivityForResult(intent, SCREEN_ADD);
    }


}
