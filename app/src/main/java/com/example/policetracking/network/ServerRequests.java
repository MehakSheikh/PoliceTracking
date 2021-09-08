package com.example.policetracking.network;

import android.content.Context;

import com.example.policetracking.interfaces.RestAPI;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Vals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerRequests {

    private static RestAPI restAPI;

    public static RestAPI getInstance(final Context context) {
        if (restAPI == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(new ChuckInterceptor(context))
                    .addNetworkInterceptor(chain -> {
                        Request original = chain.request();

                        if (TinyDB.dbContext == null) {
                            TinyDB.dbContext = context.getApplicationContext();
                        }

                        String headerTag = original.header(RestAPI.HEADER_TAG);
                        if (headerTag == null) {
                            headerTag = "";
                        }

//                        String headerDomain = original.header(RestAPI.HEADER_DOMAIN);
//                        if (headerDomain == null) {
//                            headerDomain = "";
//                        }

                        TinyDB tinyDB = TinyDB.getInstance();
                        String locale = tinyDB.getString(Vals.USER_LOCALE, "en");
                        String token = tinyDB.getString(Vals.TOKEN);

                        if (!headerTag.equalsIgnoreCase(RestAPI.HEADER_TAG_PUBLIC)
                                && token != null && !token.equalsIgnoreCase("")) {
//                            Logger.d(ServerRequests.class.getName(), "Token: " + token + "\n" + "Url: " + original.url().toString());
                            Request request = original.newBuilder()
                                    .addHeader("X-Domain-Info", TinyDB.getInstance().getString("domain", ""))
                                    .addHeader("Authorization", "Bearer " + token)
                                    .addHeader("Accept-Language", locale)
                                    .removeHeader(RestAPI.HEADER_TAG)
                                    .method(original.method(), original.body())
                                    .build();


                            Response response = chain.proceed(request);
                            if (response.code() == 401) {
                             //   HAHMainActivity.logoutUser(context, true, null);
                            } else if (response.code() == 500) {
                              /*  Env env = EnvUtil.getEnv(context);
                                if (env != null) {
                                    Vals.serverErrorDialog(context, env.appGenericError);
                                }*/
                            }
                            return response;
                        } else {
                            Request request = original.newBuilder()
                                    .addHeader("X-Domain-Info", TinyDB.getInstance().getString("domain", ""))
                                    .addHeader("Accept-Language", locale)
                                    .removeHeader(RestAPI.HEADER_TAG)
                                    .method(original.method(), original.body())
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            String baseUrl = Vals.GET_BASE_URL(context);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();

            restAPI = retrofit.create(RestAPI.class);
            return restAPI;
        } else {
            return restAPI;
        }
    }
}
