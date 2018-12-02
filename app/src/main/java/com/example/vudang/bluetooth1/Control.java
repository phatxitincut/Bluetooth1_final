package com.example.vudang.bluetooth1;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Control extends AppCompatActivity {
    BluetoothAdapter myBluetoothAdapter = null;
    BluetoothSocket btSocket;
    private boolean chophepgui = false;
    String address;
    TextView txt;
    ImageButton TB1,TB2,TB3,TB4, On_all, Off_all, btn_speak;
    ArrayList<String> text;
    final static int RESULT_SPEECH = 1;
    private ArrayAdapter<String>V2TArrayAdapter;
    Button hengio1;
    String dulieu = "1";
    private ProgressDialog progress;
    private OutputStream outStream = null;
    private static final String TAG = "HC-05";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // Khởi tạo ArrayAdapter để lưu list thiết bị quét được
    // Khởi tạo ArrayAdapter để lưu text chuyển đổi từ giọng nói

    ///////////Nhận dữ liệu//////////////////////////////////////////
    private InputStream inStream = null;
    int readBufferPosition = 0;
    Handler handler = new Handler();
    byte delimiter = 10;
    boolean stopWorker = false;
    byte[] readBuffer = new byte[1024];

    //////////////Post to Google Sheet/////////////////////////////////////////////////////
    ProgressDialog progressDialog;
    ImageButton Send;
    RequestQueue queue;

    /////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        TB1 = (ImageButton)findViewById(R.id.hinh1);
        TB2= (ImageButton)findViewById(R.id.hinh2);
        TB3 = (ImageButton)findViewById(R.id.hinh3);
        TB4= (ImageButton)findViewById(R.id.hinh4);
        On_all= (ImageButton)findViewById(R.id.On_all);
        Off_all= (ImageButton)findViewById(R.id.Off_all);
        hengio1=(Button)findViewById(R.id.btn_1);
        btn_speak = (ImageButton)findViewById(R.id.speak);
        txt = (TextView) findViewById( R.id.txt );

        //////G_sheet///////////////
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        Send = (ImageButton) findViewById(R.id.btn_send);

        queue = Volley.newRequestQueue(getApplicationContext());

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData("TB1","Đã bật");
            }
        });
        ////////////////////////////////

        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new
                        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, RESULT_SPEECH);

            }
        });


        hengio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Control.this, Timer.class);
                i.putExtra("id1", address);
               // i.putExtra("id1", (Parcelable) outStream)
                startActivity(i);
            }
        });


        On_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dulieu="5";
                beginListenForData();

                try {
                    outStream.write(dulieu.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Off_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dulieu="6";
                beginListenForData();

                try {
                    outStream.write(dulieu.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        TB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dulieu="1";

                beginListenForData();

                try {
                    outStream.write(dulieu.getBytes());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        TB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dulieu="2";
                beginListenForData();

                try {
                    outStream.write(dulieu.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        TB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dulieu="3";
                beginListenForData();

                try {
                    outStream.write(dulieu.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        TB4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dulieu="4";
                beginListenForData();

                try {
                    outStream.write(dulieu.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (chophepgui) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RESULT_SPEECH && resultCode ==RESULT_OK) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                V2TArrayAdapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_item, text);
                txt.setText( text.get( 0 ) );
                for(int i = 0; i< text.size();i++) {
                    String buff = text.get(i);
                    if (buff.contains("bật") || buff.contains("Bật"))
                    {
                        if (buff.contains("đèn") || buff.contains("Đèn")) {
                            dulieu="A";
                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (buff.contains("quạt") || buff.contains("Quạt")) {
                            dulieu="B";
                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (buff.contains("tivi") || buff.contains("Tivi")) {
                            dulieu="C";
                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (buff.contains("lửa") || buff.contains("Lửa")) {
                            dulieu="D";
                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (buff.contains("hết") || buff.contains("Hết")) {
                            dulieu="5";
                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if (buff.contains("tắt") || buff.contains("Tắt"))
                    {
                        if (buff.contains("đèn") || buff.contains("Đèn")) {
                            dulieu="a";

                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (buff.contains("quạt") || buff.contains("Quạt")) {
                            dulieu="b";
                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (buff.contains("tivi") || buff.contains("Tivi")) {
                            dulieu="c";
                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (buff.contains("lửa") || buff.contains("Lửa")) {
                            dulieu="d";
                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (buff.contains("hết") || buff.contains("Hết")) {
                            dulieu="6";
                            beginListenForData();

                            try {
                                outStream.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
            }
        }
    /////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    public void onResume() {
        super.onResume();
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            address = bd.getString("id");
        }
        new ConnectBT().execute();
    }
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
    private class ConnectBT extends AsyncTask<Void, Void, Void>
    {
        private boolean ConnectSuccess = true;

        @Override
        //
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Control.this, "Đang kết nối...", "Xin chờ!!!");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (btSocket == null || !chophepgui)
                {
                    myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetoothAdapter.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();

                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("kết nối lỗi.");
                finish();
            }
            else
            {
                msg("kết nối thành công.");
                chophepgui = true;
            }
            progress.dismiss();
            // mConnectedThread = new ConnectedThread(btSocket);
            //  mConnectedThread.start();
            BTconnect();
        }
    }
    public boolean BTconnect()
    {
        boolean connected = true;

        if(connected)
        {
            try
            {
                outStream = btSocket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return connected;
    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void beginListenForData() {//nhan dữ liệu phản hồi từ bộ điều khiển
        try {
            inStream = btSocket.getInputStream();

        }catch (IOException e) {
        }

        Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()&&!stopWorker) {
                    try {
                        int bytesAvailable = inStream.available();
                        if (bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++)
                            {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes); // để lưu ký tự  nhận về//
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (data.substring(0,3).equals("ON1"))
                                            {
                                                Toast.makeText(getApplicationContext(), "ĐÈN ĐÃ ĐƯỢC MỞ RỒI NHA", Toast.LENGTH_LONG).show();
                                                TB1.setBackgroundResource(R.drawable.tb1on);
                                            }
                                            else if (data.substring(0,4).equals("OFF1"))
                                            {
                                                Toast.makeText(getApplicationContext(), "ĐÈN ĐÃ TẮT RỒI NHA", Toast.LENGTH_LONG).show();
                                                TB1.setBackgroundResource(R.drawable.tb1off);
                                            }
                                            /////////////////
                                            else if (data.substring(0,3).equals("ON2"))
                                            {
                                                TB2.setBackgroundResource(R.drawable.tb2on);
                                            }
                                            else if (data.substring(0,4).equals("OFF2"))
                                            {
                                                TB2.setBackgroundResource(R.drawable.tb2off);
                                            }
                                            ////////////////
                                            else if (data.substring(0,3).equals("ON3"))
                                            {
                                                TB3.setBackgroundResource(R.drawable.tb3on);
                                            }
                                            else if (data.substring(0,4).equals("OFF3"))
                                            {
                                                TB3.setBackgroundResource(R.drawable.tb3off);
                                            }
                                            ///////////////
                                            else if (data.substring(0,3).equals("ON4"))
                                            {
                                                TB4.setBackgroundResource(R.drawable.tb4on);
                                            }
                                            else if (data.substring(0,4).equals("OFF4"))
                                            {
                                                TB4.setBackgroundResource(R.drawable.tb4off);
                                            }
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker=true;
                    }
                }
            }
        });
        workerThread.start();
    }

    ////////////G_sheet////////////
    public void postData(final String name, final String phone) {

        progressDialog.show();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Response: " + response);
                        if (response.length() > 0) {
                            Toast.makeText(getApplicationContext(), "Sucessfull Post", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error while Posting", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.nameField, name);
                params.put(Constants.phoneField, phone);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
    //////////////////////

}
