package com.example.payqr;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class BTPrint extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    TextView lblPrinterName;
    EditText textBox;

    String PinCodeIn, NameIn, PersonalAccIn, BankNameIn, BICIn, CorrespAccIn, KPPIn, PayeeINNIn;

    private final static String FILE_NAME = "profiles.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_t_print);
        // Create object of controls
        Button btnConnect = (Button) findViewById(R.id.btnConnect);
        Button btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        Button btnPrint = (Button) findViewById(R.id.btnPrint);

        textBox = (EditText) findViewById(R.id.txtText);

        lblPrinterName = (TextView) findViewById(R.id.lblPrinterName);


        FileInputStream fin = null;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text_in = new String (bytes);
            PinCodeIn = text_in.substring(text_in.indexOf("PinCode=")+8,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            NameIn = text_in.substring(text_in.indexOf("Name=")+5,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            PersonalAccIn = text_in.substring(text_in.indexOf("PersonalAcc=")+12,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            BankNameIn = text_in.substring(text_in.indexOf("BankName=")+9,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            BICIn = text_in.substring(text_in.indexOf("BIC=")+4,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            CorrespAccIn = text_in.substring(text_in.indexOf("CorrespAcc=")+11,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            KPPIn = text_in.substring(text_in.indexOf("KPP=")+4,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            PayeeINNIn = text_in.substring(text_in.indexOf("PayeeINN=")+9);

        }
        catch (FileNotFoundException e) {
            //e.printStackTrace();
            FileOutputStream fos = null;
            try {

                //String text_out = "PinCode="+txtPinCode+"|Name="+txtName+"|PersonalAcc="+txtPersonalAcc+"|BankName="+txtBankName+"|BIC="+txtBIC+"|CorrespAcc="+txtCorrespAcc+"|KPP="+txtKPP+"|PayeeINN="+txtPayeeINN;
                //String text_out = "PinCode=5555|Name=ЭВЕРСТОВ СЕМЕН СЕМЕНОВИЧ|PersonalAcc=40817810976000348984|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143502001|PayeeINN=7707083893";
                String text_out = "PinCode=1111|Name=Татаринов Эдуард Аркадьевич|PersonalAcc=40817810800028162749|BankName=Tinkoff|BIC=044525974|CorrespAcc=30101810145250000974|KPP=773401001|PayeeINN=7710140679";
                //String text_out = "PinCode=1111|Name=Широких Федор Федорович|PersonalAcc=40817810176005410866|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143502001|PayeeINN=7707083893";
                //String text_out = "PinCode=1111|Name=ООО СахаСтройСистема|PersonalAcc=40702810576000003190|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143501001|PayeeINN=1435327003";

                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                fos.write(text_out.getBytes());
                Toast.makeText(BTPrint.this, "Файл сохранен", Toast.LENGTH_SHORT).show();
            }
            catch(IOException ex) {

                Toast.makeText(BTPrint.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally{
                try{
                    if(fos!=null)
                        fos.close();
                }
                catch(IOException ex){

                    Toast.makeText(BTPrint.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch(IOException ex) {
            Toast.makeText(BTPrint.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
                Toast.makeText(BTPrint.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        String sumTxt = GenQR.dataEdt.getText().toString();

        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy    kk:mm");

        String Cheque = "  " + BankNameIn + "\n" +
                        "ИП " + NameIn + "\n" +
                        "ИНН " + PayeeINNIn + "\n" +
                        formatForDateNow.format(dateNow)+"\n" +
                        "   О Д О Б Р Е Н О\n" +
                        "Тип операции " + GenQR.DestStr + "\n" +
                        "Терминал          10027832\n" +
                        "Расч.счет " + PersonalAccIn + "\n" +
                        "Корр.счет " + CorrespAccIn + "\n" +
                        "БИК " + BICIn + "\n" +
                        "КПП " + KPPIn + "\n" +
                        "\n" +
                        "СУММА " + sumTxt +"РУБ.\n" +
                        "\n";

        textBox.setText(Cheque);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    FindBluetoothDevice();
                    openBluetoothPrinter();

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    disconnectBT();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    printData();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    void FindBluetoothDevice(){

        try{

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                lblPrinterName.setText("No Bluetooth Adapter found");
            }
            if(bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if(pairedDevice.size()>0){
                for(BluetoothDevice pairedDev:pairedDevice){

                    // My Bluetoth printer name is BTP_F09F1A
                    if(pairedDev.getName().equals("BlueTooth Printer")){
                        bluetoothDevice=pairedDev;
                        lblPrinterName.setText("Bluetooth принтер подключен: "+pairedDev.getName());
                        break;
                    }
                }
            }

            lblPrinterName.setText("Bluetooth принтер подключен");
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    void openBluetoothPrinter() throws IOException {
        try{

            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenData();

        }catch (Exception ex){

        }
    }

    void beginListenData(){
        try{

            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte,"UTF-8");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                lblPrinterName.setText(data);
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }

                }
            });

            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    void printData() throws  IOException{
        try{
            String msg = textBox.getText().toString();
            msg+="\n";
            outputStream.write(msg.getBytes("windows-1251"));
            lblPrinterName.setText("Распечатка чека...");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Disconnect Printer //
    void disconnectBT() throws IOException{
        try {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            lblPrinterName.setText("Принтер отключен.");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void BTPrintBack(View view) {
        Intent BackBTPrintIntent = new Intent(BTPrint.this, GenQR.class);
        startActivity(BackBTPrintIntent);
    }
}