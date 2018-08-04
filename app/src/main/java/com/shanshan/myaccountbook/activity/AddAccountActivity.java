package com.shanshan.myaccountbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shanshan.myaccountbook.util.MyLogger;
import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.database.DBTablesDefinition.AccountsDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.entity.AccountsEntity;

import org.apache.log4j.Logger;

public class AddAccountActivity extends AppCompatActivity {
    private Logger myLogger = MyLogger.getMyLogger(AddAccountActivity.class.getName());
    private MyDBHelper myDBHelper = null;
    private String accountId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*not display title*/
        setContentView(R.layout.activity_add_account);

        setTitle("添加账户");
        /*get the SqliteDatabasehelper*/
        myDBHelper = MyDBHelper.newInstance(this);

        Intent intent = getIntent();
        accountId = intent.getStringExtra(AccountsDefinition.ID);
        if (accountId != null) {
            TextView textView = (TextView) findViewById(R.id.add_account_name);
            AccountsEntity accounts = myDBHelper.getAccount(AccountsDefinition.ID + "=?", new String[]{accountId}).get(0);

            textView.setText(accounts.getName());

        }
    }

    public void addAccount(View view) {
        TextView textView = (TextView) findViewById(R.id.add_account_name);
        String accountName = textView.getText().toString();
        if (accountName.length() == 0) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();    //弹出一个自动消失的提示框
            return;
        }

        if (accountId != null) {
            myDBHelper.updateAccount(accountId, accountName, AccountsDefinition.ACCOUNT_AVAILABLE);
        } else {
            myDBHelper.addAccount(accountName);
        }

        myLogger.debug("add account " + accountName);

        finish();
    }

/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }*/
}
