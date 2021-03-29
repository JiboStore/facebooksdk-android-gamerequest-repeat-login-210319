package com.example.facebooksdktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.GameRequestContent;
//import com.facebook.share.widget.GameRequestDialog;
import com.facebook.gamingservices.GameRequestDialog;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private LoginButton loginButton;
    private Button gameRequestButton;
    private GameRequestDialog requestDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("facebook", "MainActivity.onSuccess: " + loginResult.toString());
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("facebook", "MainActivity.onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("facebook", "MainActivity.onError: " + exception.getMessage());
            }
        });

        requestDialog = new GameRequestDialog(this);
        requestDialog.registerCallback(callbackManager,
                new FacebookCallback<GameRequestDialog.Result>() {
                    public void onSuccess(GameRequestDialog.Result result) {
                        String id = result.getRequestId();
                        Log.e("facebook", "MainActivity.gameRequest.onSuccess: " + id);
                    }
                    public void onCancel() {
                        Log.e("facebook", "MainActivity.gameRequest.onCancel");
                    }
                    public void onError(FacebookException error) {
                        Log.e("facebook", "MainActivity.gameRequest.onError: " + error.getMessage());
                    }
                }
        );

        gameRequestButton = findViewById(R.id.gamerequest);
        gameRequestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameRequestContent content = buildGameRequestContent();
                requestDialog.show(content);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static GameRequestContent buildGameRequestContent() {
        GameRequestContent.Builder gameRequestContentBuilder = new GameRequestContent.Builder();
        gameRequestContentBuilder.setActionType(GameRequestContent.ActionType.TURN);
        gameRequestContentBuilder.setMessage("Please download my app...");
        gameRequestContentBuilder.setTitle("Hello");
        return gameRequestContentBuilder.build();
    }
}