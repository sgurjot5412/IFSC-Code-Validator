package com.example.ifsccodevalidator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    String ifscCode="";
    private RequestQueue requestQueue;
    Button getDetailsButton;
    EditText ifscCodeEdtTxt;
    TextView detailTxtview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        basicFunctioningOfView();

    }

    private void basicFunctioningOfView() {

        initfunction();
        setClickListener();
        HideKeyboard(getDetailsButton,this);
    }

    private void initfunction() {

        getDetailsButton=findViewById(R.id.ButtonBankDetails);
        ifscCodeEdtTxt=findViewById(R.id.editTextIFSCcode);
        detailTxtview=findViewById(R.id.textViewBankDetails);

    }

    private void setClickListener() {

        getDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifscCode= ifscCodeEdtTxt.getText().toString();
                detailTxtview.setVisibility(View.INVISIBLE);
                ifscCodeEdtTxt.clearFocus();
                if(ifscCode.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter IFSC code", Toast.LENGTH_SHORT).show();
                }
                else {
                    getDetailsFromIfscCode(ifscCode);
                }
            }
        });
    }

    private void getDetailsFromIfscCode(String ifscCode) {

        // clearing our cache of request queue.
        requestQueue.getCache().clear();

        // below is the url from where we will be getting
        // our response in the json format.
        String url = "https://ifsc.razorpay.com/" + ifscCode;

        // below line is use to initialize our request queue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // creating a json object request for our API.
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // this method is used to get
                // the response from the API.
                try {
                   // if the status is successful we are
                        // extracting data from JSON file
                        String state = response.optString("STATE");
                        String bankName = response.optString("BANK");
                        String branch = response.optString("BRANCH");
                        String address = response.optString("ADDRESS");
                        String contact = response.optString("CONTACT");
                        String micrcode = response.optString("MICR");
                        String city = response.optString("CITY");

                        // after extracting this data we are displaying
                        // that data in our text view.
                        detailTxtview.setText("Bank Name : " + bankName + "\nBranch : " + branch + "\nAddress : " + address + "\nMICR Code : " + micrcode + "\nCity : " + city + "\nState : " + state + "\nContact : " + contact);
                        detailTxtview.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    // if we get any error while loading data
                    // we are setting our text as invalid IFSC code.
                    e.printStackTrace();
                    detailTxtview.setText("Invalid IFSC Code");
                    detailTxtview.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // if we get any error while loading json
                // data we are setting our text to invalid IFSC code.
                detailTxtview.setText("Oops! Not Available at the moment \nCheck Your Network Connection or IFSC Code");
                detailTxtview.setVisibility(View.VISIBLE);
            }
        });
        // below line is use for adding object
        // request to our request queue.
        queue.add(objectRequest);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void HideKeyboard(View viewInTheLayoutFile , Context mContext) {
        try {
            viewInTheLayoutFile.setOnTouchListener ( (view , motionEvent) -> {
                try {
                    view = ((Activity) mContext).getCurrentFocus ( );
                    if (view != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService ( Context.INPUT_METHOD_SERVICE );
                        if (inputMethodManager != null) {
                            inputMethodManager.hideSoftInputFromWindow ( view.getWindowToken ( ) , 0 );
                        }
                        view.clearFocus ( );
                    }
                } catch (Exception exception) {
                    exception.printStackTrace ( );
                }
                return false;
            } );
        } catch (Exception exception) {
            exception.printStackTrace ( );
        }
    }

}