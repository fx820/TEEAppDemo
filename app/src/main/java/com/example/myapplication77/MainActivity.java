package com.example.myapplication77;

import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyInfo;
import android.security.keystore.KeyProperties;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication77.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_NAME = "test001";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    /**
     * 目标：写一个
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("=======================this is app start" );
        super.onCreate(savedInstanceState);
//        KeyInfo keyInfo = null;

        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//            SecretKey key = keyGenerator.generateKey(); // Android Keystore key
//            SecretKeyFactory factory = SecretKeyFactory.getInstance(key.getAlgorithm(), "AndroidKeyStore");
//            keyInfo = (KeyInfo) factory.getKeySpec(key, KeyInfo.class);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");

            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
//模拟器运行：若为true，则报错中断运行
//                            真机运行：true可以正常启动
                    .setUserAuthenticationRequired(false)

                    .setBlockModes(KeyProperties.BLOCK_MODE_ECB)

                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)

                    .build());

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

// Check that private key is inside secure hardware
            PrivateKey key = keyPair.getPrivate();

            KeyFactory factory = KeyFactory.getInstance(key.getAlgorithm(), "AndroidKeyStore");

            KeyInfo keyInfo = factory.getKeySpec(key, KeyInfo.class);
            System.out.println("=======================this is security patten" );
            //真机：一加五实际运行显示为true
            System.out.println("=======================to check if is inside TEE:"+ keyInfo.isInsideSecureHardware() );
            /**
             * 一加五实际运行显示为1，即为SECURITY_LEVEL_TRUSTED_ENVIRONMENT
             *   1          SECURITY_LEVEL_UNKNOWN,
             *  -1          SECURITY_LEVEL_UNKNOWN_SECURE,
             *  0           SECURITY_LEVEL_SOFTWARE,
             *  1           SECURITY_LEVEL_TRUSTED_ENVIRONMENT,
             *  2           SECURITY_LEVEL_STRONGBOX,
             */
            System.out.println("=======================the security level is:"+ keyInfo.getSecurityLevel() );
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
 
        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        KeyInfo finalKeyInfo = keyInfo;
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("this is security pattenn:"+ finalKeyInfo.isInsideSecureHardware() );
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}