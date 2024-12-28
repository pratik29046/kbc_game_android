package com.app.mygame.network;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.app.mygame.appConfig.NetworkUtil;
import com.app.mygame.utils.DateDeserializer;
import com.app.mygame.utils.ProgressPopup;
import com.app.mygame.utils.TokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String DEV_BASE_URL = "http://192.168.137.152:1234/";
    private static final String PROD_BASE_URL = "https://prod.api.server/";
    private static final String AES_KEY = "1234567890123456"; // 16-byte AES Key
    private static final String RSA_PUBLIC_KEY = "Your-RSA-Public-Key"; // Replace with your actual key

    private static final boolean isProduction = false; // Toggle this for dev/prod

    public static Retrofit getClient(Context context) {
        String baseUrl = isProduction ? PROD_BASE_URL : DEV_BASE_URL;

        // Instantiate ProgressPopup and show it at the beginning
        ProgressPopup progressPopup = new ProgressPopup(context);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer()) // Register your custom deserializer
                .create();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        // Add Logging Interceptor for debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NotNull String message) {
                Log.d("RetrofitLog", message); // Logs to the Android Logcat
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        // Add encryption/decryption Interceptor
        clientBuilder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws java.io.IOException {
                // Show ProgressPopup when the request starts
                progressPopup.show("Loading, please wait...");

                try {
                    if (!NetworkUtil.isInternetAvailable(context)) {
                        throw new java.io.IOException("No internet connection available");
                    }

                    String token = TokenManager.getAuthToken();
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder()
                            .addHeader("Content-Type", "application/json");

                    // Add token to the Authorization header if it exists
                    if (token != null && !token.isEmpty()) {
                        builder.addHeader("Authorization", "Bearer " + token);
                    }

                    if (isProduction) {
                        builder.addHeader("X-AUTH-KEY", "Your-X-AUTH-KEY");

                        // Encrypt the request body in production
                        if (original.body() != null) {
                            String requestBodyString = bodyToString(original.body());
                            Log.d("RequestBody", "Original: " + requestBodyString); // Log original request body

                            String encryptedBody = encryptAES(requestBodyString);
                            Log.d("RequestBody", "Encrypted: " + encryptedBody); // Log encrypted request body

                            assert encryptedBody != null;
                            builder.method(original.method(), RequestBody.create(encryptedBody, MediaType.parse("application/json")));
                        }
                    }

                    Response response = chain.proceed(builder.build());

                    if (!response.headers("X-Authorization").isEmpty()) {
                        String newToken = response.header("X-Authorization");
                        TokenManager.setAuthToken(newToken);
                        Log.d("TokenRefresh", "New token received and updated in memory: " + newToken);
                    }

                    if (isProduction && response.body() != null) {
                        String encryptedResponseBody = response.body().string();
                        Log.d("ResponseBody", "Encrypted: " + encryptedResponseBody); // Log encrypted response body

                        String decryptedResponse = decryptAES(encryptedResponseBody);
                        Log.d("ResponseBody", "Decrypted: " + decryptedResponse); // Log decrypted response body

                        assert decryptedResponse != null;
                        return response.newBuilder()
                                .body(ResponseBody.create(decryptedResponse, response.body().contentType()))
                                .build();
                    }

                    return response;
                } finally {
                    // Dismiss ProgressPopup once the request is complete
                    progressPopup.dismiss();
                }
            }
        });

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(clientBuilder.build())
                .build();
    }

    private static String encryptAES(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String decryptAES(String encryptedData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bodyToString(final RequestBody body) {
        try {
            Buffer buffer = new Buffer();
            if (body != null) body.writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            return "Error converting body to string";
        }
    }
}
