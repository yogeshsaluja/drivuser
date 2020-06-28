package com.thinkincab.app.ui.activity.login;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.thinkincab.app.BuildConfig;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.ui.activity.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmailActivity extends BaseActivity {

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_email;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbar.setNavigationOnClickListener(v -> finish());

        if (BuildConfig.DEBUG) email.setText("User@demo.com");
    }

    @OnClick({R.id.sign_up, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.next:
                String Email = email.getText().toString();
                if (email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(this, PasswordActivity.class);
                i.putExtra("email", Email);
                startActivity(i);
                break;
        }
    }
}
