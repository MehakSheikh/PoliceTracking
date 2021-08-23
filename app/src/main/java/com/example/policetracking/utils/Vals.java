package com.example.policetracking.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.readystatesoftware.chuck.BuildConfig;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Vals {
    public static final String USER_LOCALE = "user_locale";

    public static final String TOKEN = "token";

    private static final String WSS_URL_LOCAL = "ws://10.0.0.183:8080/ws";
    private static final String WSS_URL_STAGING = "wss://staging.myhealthathand.com/ws";
    private static final String WSS_URL_RELEASE = "wss://app-api.myhealthathand.com/ws";
    private static final String WSS_URL_DEV = "wss://dev.myhealthathand.com/ws";

    private static final String BASE_URL_STAGING = "https://staging.myhealthathand.com/";
    private static final String BASE_URL_RELEASE = "https://app-api.myhealthathand.com/";
    private static final String BASE_URL_DEV = "https://dev.myhealthathand.com/";

    private static final String BASE_URL_LOCAL = "http://10.0.0.183:8000/";

    public static String GET_BASE_URL(Context context) {

        if (TinyDB.dbContext == null)
            TinyDB.dbContext = context;

        TinyDB tinyDB = TinyDB.getInstance();

        if (tinyDB.getBoolean("use_custom_ip_for_wss")) {
            return "http://" + tinyDB.getString("custom_ip_for_wss") + ":8000";

        } else {
                if (BuildConfig.FLAVOR.equals("dev")) {
                    return BASE_URL_DEV;
                } else if (BuildConfig.FLAVOR.equals("staging")) {
                    return BASE_URL_STAGING;
                } else if (BuildConfig.FLAVOR.equals("live")) {
                    return BASE_URL_RELEASE;
                } else if (BuildConfig.FLAVOR.equals("local")) {
                    return BASE_URL_LOCAL;
                } else {
                    return BASE_URL_STAGING;
                }
        }
    }

    public static String GET_WSS_URL(Context context) {

        if (TinyDB.dbContext == null)
            TinyDB.dbContext = context;

        TinyDB tinyDB = TinyDB.getInstance();

        if (tinyDB.getBoolean("use_custom_ip_for_wss")) {
            return "ws://" + tinyDB.getString("custom_ip_for_wss") + ":8080/ws";

        } else {

                if (BuildConfig.FLAVOR.equals("dev")) {
                    return WSS_URL_DEV;
                } else if (BuildConfig.FLAVOR.equals("staging")) {
                    return WSS_URL_STAGING;
                } else if (BuildConfig.FLAVOR.equals("live")) {
                    return WSS_URL_RELEASE;
                } else if (BuildConfig.FLAVOR.equals("local")) {
                    return WSS_URL_LOCAL;
                } else {
                    return WSS_URL_DEV;
                }

        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static String removeDouble(double amount) {
        double cici = amount;
        DecimalFormat format = new DecimalFormat("#,###.##");
        return format.format(cici);
    }

    public static String capWords(String str) {
        try {
            if (str != null) {
                String[] strArray = str.split(" ");
                StringBuilder builder = new StringBuilder();
                for (String s : strArray) {
                    String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                    builder.append(cap + " ");
                }
                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean containsCaseInsensitive(String s, List<String> l) {
        for (String string : l) {
            if (string.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }


  /*  public static void setImage(final Context context, final String imageUri, final int placeholder, final ImageView imageView) {
        try {
            Picasso.with(context)
                    .load(imageUri)
                    .networkPolicy(NetworkPolicy.OFFLINE)
//                    .resize(imageView.getMaxWidth(), 0)
                    .fit()
                    .centerInside()
                    .noFade()
                    .placeholder(context.getResources().getDrawable(placeholder))
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.e("Image loading", "success");
                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(context)
                                    .load(imageUri)
//                                    .resize(imageView.getMaxWidth(), 0)
                                    .fit()
                                    .centerInside()
//                                .error(R.drawable.logo)
                                    .placeholder(context.getResources().getDrawable(placeholder))
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
  /*  public static ErrorDialog serverErrorDialog(Context context, String message) {
        return serverErrorDialog(context, message, null);
    }

    private static ErrorDialog networkErrorDialog;

    public static ErrorDialog serverErrorDialog(Context context, String message, String title,
                                                MaterialDialog.OnDismissListener onDismissListener) {
        Env env = EnvUtil.getInstance();
        if (env != null && title != null && title.equals(env.appNetworkConnetionError)
                && networkErrorDialog != null
                && networkErrorDialog.isShowing()) {
            return networkErrorDialog;
        }
        try {
            String capFirstDesc = title.substring(0, 1).toUpperCase() + title.substring(1);
            ErrorDialog dialog = null;
            if (env != null && title.equals(env.appNetworkConnetionError)) {
                networkErrorDialog = new ErrorDialog(context, R.style.DialogTheme, capFirstDesc);
                dialog = networkErrorDialog;
            } else {
                dialog = new ErrorDialog(context, R.style.DialogTheme, capFirstDesc, message);

            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            if (onDismissListener != null) {
                dialog.setOnDismissListener(onDismissListener);
            }
            dialog.show();
            return dialog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ErrorDialog serverErrorDialog(Context context, String message,
                                                MaterialDialog.OnDismissListener onDismissListener) {
        Env env = EnvUtil.getInstance();
        if (env != null && message != null && message.equals(env.appNetworkConnetionError)
                && networkErrorDialog != null
                && networkErrorDialog.isShowing()) {
            return networkErrorDialog;
        }
        try {
            String capFirst = message.substring(0, 1).toUpperCase() + message.substring(1);
            ErrorDialog dialog = null;
            if (env != null && message.equals(env.appNetworkConnetionError)) {
                networkErrorDialog = new ErrorDialog(context, R.style.DialogTheme, capFirst);
                dialog = networkErrorDialog;
            } else {
                dialog = new ErrorDialog(context, R.style.DialogTheme, capFirst);

            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            if (onDismissListener != null) {
                dialog.setOnDismissListener(onDismissListener);
            }
            dialog.show();
            return dialog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    */

}
