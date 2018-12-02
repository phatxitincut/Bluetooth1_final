package com.example.vudang.bluetooth1;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Timer extends AppCompatActivity {

    ////////////////////////////////////////////////////////
    BluetoothAdapter myBluetoothAdapter1 = null;
    BluetoothSocket btSocket1;
    private boolean chophepgui1 = false;
    String address1;
    ImageButton TB1,TB2,TB3,TB4, On_all, Off_all;
    Button hengio1;
    String dulieu = "1";
    private ProgressDialog progress;
    private OutputStream outStream1 = null;
    private static final String TAG = "HC-05";
    private static final UUID MY_UUID1 = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // Khởi tạo ArrayAdapter để lưu list thiết bị quét được
    // Khởi tạo ArrayAdapter để lưu text chuyển đổi từ giọng nói
    ///////////////////////////////////////////////////////////

    Button btn_gui, btn_xoa;
    TextView txt_time;
    EditText edt_time;
    int dem1, max1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        btn_gui = (Button) findViewById(R.id.btn_gui);
        txt_time = (TextView) findViewById(R.id.txt_time);
        edt_time = (EditText) findViewById(R.id.edt_time);
        btn_xoa = (Button) findViewById(R.id.btn_xoa) ;

        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chuoi1 = edt_time.getText().toString();
                int so1 = Integer.parseInt(chuoi1);

                if ((so1 > 999) || (so1 < 0)) {
                    Toast.makeText(Timer.this, "Nhap so khac", Toast.LENGTH_SHORT).show();
                }
                max1 = so1 * 1000;
                dem1 = 0;
                if ((so1 > 0) && (so1 < 1000)) {
                    final CountDownTimer mycountdowntimer = new CountDownTimer(max1, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            dem1 = dem1 + 1;
                            txt_time.setText(String.valueOf(dem1));
                        }

                        @Override
                        public void onFinish() {

                            dulieu="1";
                            try {
                                outStream1.write(dulieu.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(Timer.this, "Đèn đã bật", Toast.LENGTH_SHORT).show();
                            txt_time.setText(String.valueOf(0));
                        }
                    }.start();
                    btn_xoa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mycountdowntimer.cancel();
                            txt_time.setText(String.valueOf(0));
                            dem1 = 0;
                        }
                    });
                }
            }

        });



        }

    ////////////////////////////
    public void onResume() {
        super.onResume();
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            address1 = bd.getString("id1");
        }
        new ConnectBT1().execute();
    }
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
    public class ConnectBT1 extends AsyncTask<Void, Void, Void>
    {
        private boolean ConnectSuccess = true;

        @Override
        //
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Timer.this, "Đang kết nối...", "Xin chờ!!!");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (btSocket1 == null || !chophepgui1)
                {
                    myBluetoothAdapter1 = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetoothAdapter1.getRemoteDevice(address1);
                    btSocket1 = dispositivo.createInsecureRfcommSocketToServiceRecord(MY_UUID1);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket1.connect();

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
                chophepgui1 = true;
            }
            progress.dismiss();
            // mConnectedThread = new ConnectedThread(btSocket);
            //  mConnectedThread.start();
            BTconnect1();
        }
    }
    public boolean BTconnect1()
    {


        boolean connected = true;

        if(connected)
        {
            try
            {
                outStream1 = btSocket1.getOutputStream(); //gets the output stream of the socket
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
    ////////////////////////////
}
