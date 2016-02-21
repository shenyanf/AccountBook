package com.shanshan.myaccountbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shanshan.myaccountbook.button.AddButton;
import com.shanshan.myaccountbook.database.DBTablesDefinition.AccountsDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;

public class ManageAccount extends AppCompatActivity {
    private ArrayAdapter adapterAccount = null;
    private MyDBHelper myDBHelper = null;
    private ListView accountListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDBHelper = MyDBHelper.newInstance(this);
        adapterAccount = new ArrayAdapter(this, R.layout.spinner_drop_down_layout);
        adapterAccount.setDropDownViewResource(R.layout.spinner_drop_down_layout);

        setContentView(R.layout.activity_manage_account);
        accountListView = (ListView) findViewById(R.id.list_account);
        accountListView.setAdapter(adapterAccount);
        accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
            }
        });
        registerForContextMenu(accountListView);

        AddButton addButton = (AddButton) findViewById(R.id.add_account);
        addButton.setText("添加账户");
        addButton.setTextSize(20);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.list_account) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("账户管理");
            String[] menuItems = getResources().getStringArray(R.array.account_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.account_menu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = "账户管理";

        String accountId = String.valueOf(info.id + 1);

        if (menuItemName.equals("删除账户")) {
            System.out.println("删除账户" + myDBHelper.getAccount(AccountsDefinition.ID + "=?", new String[]{accountId}));
            myDBHelper.deleteAccount(accountId);
            onResume();
        } else {
            Intent intent = new Intent(this, AddAccountActivity.class);
            intent.putExtra(AccountsDefinition.ID, accountId);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterAccount.clear();
        System.out.println("manage account on resume function");
        adapterAccount.addAll(myDBHelper.getAccount(AccountsDefinition.COLUMN_ACCOUNT_STATUS + "=?", new String[]{String.valueOf(AccountsDefinition.ACCOUNT_AVAILABLE)}));
        adapterAccount.notifyDataSetChanged();
    }
}