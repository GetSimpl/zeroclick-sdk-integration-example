package com.simpl.android.zeroclicksample;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simpl.android.zeroClickSdk.Simpl;
import com.simpl.android.zeroClickSdk.SimplUser;
import com.simpl.android.zeroClickSdk.SimplUserApprovalListenerV2;
import com.simpl.android.zeroClickSdk.SimplUserApprovalRequest;
import com.simpl.android.zeroClickSdk.SimplZeroClickTokenAuthorization;
import com.simpl.android.zeroClickSdk.SimplZeroClickTokenListener;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZeroClickApprovalActivity extends AppCompatActivity {
    public static final String TAG = ZeroClickApprovalActivity.class.getSimpleName();

    @Bind(R.id.email)
    EditText emailEt;
    @Bind(R.id.phone_number)
    EditText phoneNumberEt;
    @Bind(R.id.is_approved)
    Button isApproved;
    @Bind(R.id.zero_click)
    Button generateTokenButton;
    @Bind(R.id.responseTv)
    TextView responseTv;
    @Bind(R.id.zero_click_token)
    TextView zeroClickTokenTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zero_click_approval);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @OnClick(R.id.zero_click)
    public void generateZeroClickToken(){
        Simpl.getInstance().generateZeroClickToken(new SimplZeroClickTokenListener() {
            @Override
            public void onSuccess(final SimplZeroClickTokenAuthorization simplZeroClickTokenAuthorization) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        zeroClickTokenTextView.setTextColor(ContextCompat.getColor(ZeroClickApprovalActivity.this, android.R.color.holo_green_light));
                        zeroClickTokenTextView.setText(simplZeroClickTokenAuthorization.toString());
                    }
                });
            }

            @Override
            public void onFailure(final Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        zeroClickTokenTextView.setTextColor(ContextCompat.getColor(ZeroClickApprovalActivity.this, android.R.color.holo_red_dark));
                        zeroClickTokenTextView.setText(throwable.getMessage());
                    }
                });
            }
        });
    }

    @OnClick(R.id.is_approved)
    public void isApproved() {
        SimplUserApprovalRequest simplUserApprovalRequest =  Simpl.getInstance().
                isUserApproved(new SimplUser(emailEt.getText().toString(), phoneNumberEt.getText().toString()));

        simplUserApprovalRequest.execute(new SimplUserApprovalListenerV2() {
            @Override
            public void onSuccess(final boolean status, final String buttonText, final boolean showSimplIntroduction) {
                Log.d(TAG, "isUserApproved(): onSuccess(): status: "+status+ " buttonText: "+buttonText+ " showSimplIntroduction: "+showSimplIntroduction);
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        responseTv.setText(String.valueOf(status));
                        Toast.makeText(ZeroClickApprovalActivity.this, "User Approved: "+ status, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }

            @Override
            public void onError(final Throwable throwable) {
                Log.d(TAG, "isUserApproved(): onError(): "+throwable.getMessage());
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(ZeroClickApprovalActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
