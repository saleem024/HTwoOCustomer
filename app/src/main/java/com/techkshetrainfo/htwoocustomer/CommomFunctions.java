package com.techkshetrainfo.htwoocustomer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by savsoft-3 on 2/2/17.
 */

public class CommomFunctions {

    public static boolean isnetworkavailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void show_dialog(final Context context, String title, String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                // Toast.makeText(context, "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();


    }

    public static boolean isValidMobile(String phone2)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone2))
        {
            if(phone2.length() < 6 || phone2.length() > 13)
            {
                check = false;

            }
            else
            {
                check = true;
            }
        }
        else
        {
            check=false;
        }
        return check;
    }

    public static boolean isValidMail(String email2)
    {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email2);
        check = m.matches();


        return check;
    }




    public static void show_loading(final Context context, String title, String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);



        // Showing Alert Message
        alertDialog.show();


    }

}
