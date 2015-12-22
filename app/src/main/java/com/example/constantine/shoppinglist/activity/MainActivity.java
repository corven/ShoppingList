package com.example.constantine.shoppinglist.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.constantine.shoppinglist.R;
import com.example.constantine.shoppinglist.adapter.PurchaseListAdapter;
import com.example.constantine.shoppinglist.database.dao.PurchaseDao;
import com.example.constantine.shoppinglist.model.Purchase;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private static final int EDIT = 0, DELETE = 1, SCREEN_ADD = 0, SCREEN_UPDATE = 1;
    private static final String SAVED_SUM = "saved_sum", PRICE = "price", NAME = "name";
    private static final int ADD = 0;

    TextView txtSumPurchases;
    ArrayList<Purchase> purchases = new ArrayList<>();
    PurchaseDao purchaseDao;

    ListView lvPurchase;
    ArrayAdapter<Purchase> adapter;
    SharedPreferences SPsumPurchases;
    private double sumPurchases;
    SharedPreferences.Editor ed;
    ArrayList<String> selectPositions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSumPurchases = (TextView)findViewById(R.id.txtSumPurchases);
        SPsumPurchases = getPreferences(MODE_PRIVATE);

        String sum = SPsumPurchases.getString(SAVED_SUM, "");

        if (!sum.isEmpty()) {
            sumPurchases = Double.parseDouble(sum);
            txtSumPurchases.setText(String.valueOf(sumPurchases));
        }else {
            sumPurchases = 0;
        }

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD);
            }
        });

        lvPurchase = (ListView)findViewById(R.id.lvPurchase);

        purchaseDao = new PurchaseDao(this);
        purchases = purchaseDao.getAll();
        adapter = new PurchaseListAdapter(this, purchases);
        lvPurchase.setAdapter(adapter);
        lvPurchase.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvPurchase.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    selectPositions.add(String.valueOf(position));
                } else {
                    selectPositions.remove(String.valueOf(position));

                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                //Удаление
                if (item.getItemId() == R.id.item_delete) {
                    ArrayList<Purchase> cloneLessons = (ArrayList<Purchase>) purchases.clone();
                    for (String position : selectPositions) {
                        Purchase book = cloneLessons.get(Integer.parseInt(position));
                        purchaseDao.delete(book);
                        purchases.remove(book);

                        sumPurchases -= book.getPrice();
                        commitSum();
                    }
                }
                mode.finish();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selectPositions.clear();
                mode.finish();
            }
        });

    }
//
//    private void sumDisplay(){
//        txtSumPurchases = (TextView)findViewById(R.id.txtSumPurchases);
//        SPsumPurchases = getPreferences(MODE_PRIVATE);
//
//        String getSum = SPsumPurchases.getString(SAVED_SUM, "");
//
//        if (!getSum.isEmpty()) {
//            sumPurchases = Double.parseDouble(getSum);
//            txtSumPurchases.setText(String.valueOf(sumPurchases));
//        }else {
//            sumPurchases = 0;
//        }
//    }
//
    private void commitSum() {
        ed = SPsumPurchases.edit();
        ed.putString(SAVED_SUM, String.valueOf(sumPurchases));
        ed.commit();
        txtSumPurchases.setText(String.valueOf(sumPurchases));
    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCREEN_ADD & resultCode == RESULT_OK){
            String name = data.getStringExtra(AddActivity.NAME);
            double price = data.getExtras().getDouble(AddActivity.PRICE);
            purchases.add(createPurchase(name, price));
            adapter.notifyDataSetChanged();

            sumPurchases += price;
            commitSum();
            Toast.makeText(getApplicationContext(), name + " был добавлен в список",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private Purchase createPurchase(String name, double price) {
        return new Purchase(purchaseDao.getCount(), name, price);
    }
//
//
//    public boolean onContextItemSelected(MenuItem item){
//        Operations operation = new Operations();
//        switch (item.getItemId()){
//            case EDIT:
//                operation.startActivityUpdate();
//                break;
//
//            case DELETE:
//                operation.delete();
//                break;
//        }
//        return super.onContextItemSelected(item);
//    }

//

//    private class Operations {
//        protected void add(String name, String price) {
//            Purchase purchase = new Purchase(dbHandler.getPuschaseCount(), name, Double.parseDouble(price));
//
//            if (!purchaseExists(purchase)){
//                dbHandler.createPurchase(purchase);
//                purchaseList.add(purchase);
//                purchaseAdapter.notifyDataSetChanged();
//                sumPurchases += purchase.getPrice();
//
////                commitSum();
//
//                Toast.makeText(getApplicationContext(), name + " был добавлен в список",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        protected void delete() {
//            Purchase purchase = purchaseList.get(longClickedItemIndex);
//
//            dbHandler.deletePurchase(purchase);
//            purchaseList.remove(longClickedItemIndex);
//            purchaseAdapter.notifyDataSetChanged();
//
//            sumPurchases -= purchase.getPrice();
//
////            commitSum();
//
//            Toast.makeText(getApplicationContext(), purchase.getName() + " был удален из списка",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        protected void update(String name, String price) {
//            Purchase purchase = new Purchase(dbHandler.getPuschaseCount(), name, Double.parseDouble(price));
//
//            dbHandler.updatePurchase(purchase);
//            purchaseList.set(longClickedItemIndex, purchase);
//            purchaseAdapter.notifyDataSetChanged();
//
//            sumPurchases += purchase.getPrice();
////            commitSum();
//
//            Toast.makeText(getApplicationContext(), "Запись обновлена", Toast.LENGTH_SHORT).show();
//        }
//
//        private void startActivityUpdate(){
//            Purchase purchase = purchaseList.get(longClickedItemIndex);
//            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
//
//            sumPurchases -= purchase.getPrice();
//
//
//            String price = String.valueOf(purchase.getPrice());
//            intent.putExtra(NAME, purchase.getName());
//            intent.putExtra(PRICE, price);
//            startActivityForResult(intent, SCREEN_UPDATE);
//        }
//
//    }

//    private boolean purchaseExists(Purchase purchase){
//        String name = purchase.getName();
//        int bookCount = purchaseList.size();
//
//        for (int i = 0; i < bookCount; i++){
//            if (name.compareToIgnoreCase(purchaseList.get(i).getName()) == 0){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void populateList(){
//        purchaseAdapter = new PurchaseListAdapter();
//        purchaseListView.setAdapter(purchaseAdapter);
//    }

//    public void onClickbtnAddRec(View view) {
//        Intent intent = new Intent(MainActivity.this, AddActivity.class);
//        startActivityForResult(intent, SCREEN_ADD);
//    }


}
