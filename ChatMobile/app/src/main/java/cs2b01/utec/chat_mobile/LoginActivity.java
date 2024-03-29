package cs2b01.utec.chat_mobile;

import android.app.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.content.Intent;
import org.json.JSONException;
import android.view.View;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public Activity getActivity(){
        return this;
    }

    public void onBtnLoginClicked(View view){
        //Toast.makeText( this, "Logging In", Toast.LENGTH_SHORT).show();
        EditText txtUsername = (EditText)findViewById(R.id.InputUser);
        EditText txtPassword = (EditText)findViewById(R.id.InputPassword);
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        HashMap<String, String> message = new HashMap<>();
        message.put("username", username);
        message.put("password", password);

        //JSONObject jsonMessage = new JSONObject(message);
        //Toast.makeText( this, jsonMessage.toString(), Toast.LENGTH_SHORT).show();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8000/authenticate",
                new JSONObject(message),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showMessage("Authorized!");
                        try {
                            String username = response.getString("username");
                            int user_id = response.getInt("user_id");
                            goToContactsActivity(user_id, username);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMessage("Unauthorized!");
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void goToContactsActivity(int user_id, String username){
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("username", username);
        startActivity(intent);

    }

}
