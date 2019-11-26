package cs2b01.utec.chat_mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {




    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String username = getIntent().getExtras().getString("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTitle("@"+username);
        mRecyclerView = findViewById(R.id.main_recycler_view);

    }

    @Override
    protected void onResume(){
        super.onResume();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getMessages();
    }

    public Activity getActivity(){
        return this;
    }

    public void getMessages(){
        final int userFromId = getIntent().getExtras().getInt("user_from_id");
        final int userToId = getIntent().getExtras().getInt("user_to_id");
        String uri = "http://10.0.2.2:8000/messages/"+userFromId+"/"+userToId;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                uri,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response){
                        mAdapter = new MessageAdapter(response, getActivity(), userFromId);
                        mRecyclerView.setAdapter(mAdapter);

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();

                    }
                }

        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    public void onClickBtnSend(View view){
        final int userFromId = getIntent().getExtras().getInt("user_from_id");
        final int userToId = getIntent().getExtras().getInt("user_to_id");
        EditText txtMessage = findViewById(R.id.txtMessage);
        String msg = txtMessage.getText().toString();
        Toast.makeText( this, userFromId+" to "+userToId, Toast.LENGTH_SHORT).show();

        HashMap<String, String> message = new HashMap<>();
        message.put("content", msg);
        message.put("user_from_id", Integer.toString(userFromId) );
        message.put("user_to_id", Integer.toString(userToId) );



        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8000/sendMessage",
                new JSONObject(message),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //goToMessageActivity(userFromId, userToId);
                        getMessages();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void goToMessageActivity(int user_from_id, int user_to_id){
        String username = getIntent().getExtras().getString("username");
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("user_from_id", user_from_id);
        intent.putExtra("user_to_id", user_to_id);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
