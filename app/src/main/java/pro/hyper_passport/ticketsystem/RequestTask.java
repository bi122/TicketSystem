package pro.hyper_passport.ticketsystem;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihail on 24.06.2015.
 */
public class RequestTask extends AsyncTask<String, String, String> {


    @Override
    protected String doInBackground(String... params) {
        return null;
    }
}
