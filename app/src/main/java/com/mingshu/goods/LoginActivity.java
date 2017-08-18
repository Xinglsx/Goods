package com.mingshu.goods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.ApplicationUtil;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.pmp.goods.R;
import com.mingshu.pmp.goods.databinding.ActivityLoginBinding;

import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;

public class LoginActivity extends ScanBaseActivity {

    private ActivityLoginBinding binding;
    private ApiCoreManager apiCoreManager;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        apiCoreManager = new ApiCoreManager(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        binding.txtUserCode.setText(sp.getString(Constant.USERCODE,""));
        if(sp.getBoolean(Constant.REMEMBERPASSWORD_ISCHECK,false)){
            binding.chkRememberPassword.setChecked(true);
            binding.txtPassword.setText(sp.getString(Constant.PASSWORD,""));//不再保存密码
            if(sp.getBoolean(Constant.AUTOLOGIN_ISCHECK,false)){
                binding.chkAutoLogin.setChecked(true);
                Intent intent = this.getIntent();
                if(!intent.getBooleanExtra("isLogout",false)){
                    Login();
                }
            }
        }

        binding.chkAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    binding.chkRememberPassword.setChecked(true);
                }else{
                    sp.edit().putBoolean(Constant.AUTOLOGIN_ISCHECK,false).commit();
                }
            }
        });

        binding.chkRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    binding.chkAutoLogin.setChecked(false);
                    sp.edit().putBoolean(Constant.REMEMBERPASSWORD_ISCHECK,false).commit();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccessScan(String s) {

    }

    public void click(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_load:
                Login();
                break;
            case R.id.txt_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btn_exit:
                finish();
                break;
        }
    }

    private void Login(){
        //加等待条
        PrompUtil.startProgressDialog(this,"登录中，请稍等。。。");
        //获取用户名和密码
        String strCode = binding.txtUserCode.getText().toString();
        final String password = binding.txtPassword.getText().toString();
        if("".equals(strCode)){
            failuerMessage("请输入用户名、邮箱或手机号");
            return;
        }
        if("".equals(password)){
            failuerMessage("请输入密码");
            return;
        }

        //网络访问成功后跳转
        ApiManager.Api api = apiCoreManager.validateUserInfo(strCode,password);
        api.invoke(new NetworkEngine.Success<UserInfo>() {
            @Override
            public void callback(UserInfo data) {
                PrompUtil.stopProgessDialog();
                LoginSuccess(data,password);
            }

        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                failuerMessage(message);
            }
        }, new NetworkEngine.Error() {

            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                failuerMessage(message);
            }
        });
    }

    //登录成功相关处理
    private void LoginSuccess(UserInfo data,String password){
        //保存全局用户信息
        ApplicationUtil.put(this,Constant.USERINFO,data);

        SharedPreferences.Editor editor = sp.edit();
        //自动登录和记住密码处理
        editor.putBoolean(Constant.AUTOLOGIN_ISCHECK,binding.chkAutoLogin.isChecked());
        editor.putBoolean(Constant.REMEMBERPASSWORD_ISCHECK,binding.chkRememberPassword.isChecked());
        editor.putString(Constant.USERCODE,data.getUserid());//自动保存登录用户名
        if( binding.chkRememberPassword.isChecked()){
            editor.putString(Constant.PASSWORD,password);
        }
        editor.commit();

        //弹提示框并跳转主页
        CommonUtil.DisplayToast("登录成功！",this);
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void failuerMessage(String message){
        CommonUtil.ShowMsg(message,this);
    }
}
