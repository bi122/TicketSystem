package pro.hyper_passport.ticketsystem;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mihail on 07.07.2015.
 */
public class ReaderJSON {

    String stringBuilder;

    public ReaderJSON (String stringBuilder){
        this.stringBuilder = stringBuilder;
    }

    //метод используется для проверки ответа сервера при авторизации
    //возвращает true если авторизация успешна
    public boolean checkAuth() throws JSONException {
        boolean result = false;
        JSONObject jsonObject = new JSONObject(stringBuilder);
        int success = jsonObject.getInt("success");
        int code = jsonObject.getInt("code");
        int auth = jsonObject.getInt("auth");
        if (success == 0 && code == 1 && auth == 0){
            result = false;
        }
        else if (success == 1 && code == 0 && auth == 1){
            result = true;
        }
        else {
            //TODO поговорить с Максимом или Владом на счёт этого
        }
        return result;
    }

}
