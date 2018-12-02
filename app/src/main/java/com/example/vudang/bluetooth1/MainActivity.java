package com.example.vudang.bluetooth1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity{
    Button btn_on, btn_off, btn_scan;
    ListView danhsach;
    BluetoothAdapter AV;
    private Set<BluetoothDevice> pairedDevice;//cho bạn tạo một liên kết với thiết bị
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_scan =  (Button)findViewById(R.id.btn_scan);
        btn_on  = (Button) findViewById(R.id.btn_on);
        btn_off = (Button) findViewById(R.id.btn_off);
        danhsach = (ListView) findViewById(R.id.listView_blue);
        AV = BluetoothAdapter.getDefaultAdapter();
        // Khởi tạo 2 Arraylist để lưu ID và địa chỉ của thiết bị quét được
        btn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOn, 0);
            }
        });
        btn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AV.disable();
            }
        });





        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevice = AV.getBondedDevices();
                ArrayList list = new ArrayList();
                for(BluetoothDevice bt : pairedDevice)
                    list.add(bt.getName()+ "\n" + bt.getAddress());
                ArrayAdapter adapter= new ArrayAdapter( MainActivity.this, android.R.layout.simple_list_item_1,list );
                danhsach.setAdapter(adapter);
                Toast.makeText(MainActivity.this, "Đang kết nối...", Toast.LENGTH_SHORT).show();

            }
        });
        danhsach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Intent intent = new Intent(MainActivity.this,Control.class);
                //startActivity(intent);

                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);
                Intent i = new Intent(MainActivity.this, Control.class);
                Toast.makeText(MainActivity.this, "Ket noi da thanh cong", Toast.LENGTH_SHORT).show();
                i.putExtra("id",address);
                startActivity(i);

            }
        });

    }
}
