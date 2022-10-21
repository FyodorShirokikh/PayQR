package com.example.payqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PinCode extends AppCompatActivity {
    private EditText etPinCode;
    private final static String FILE_NAME = "profiles.txt";
    private String text_in;
    private String stPinCodeIn;
    private String stPinCode;


    @Override
    public void onBackPressed() {
        Toast.makeText(PinCode.this,"Not Allowed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);
    }

    public void PinCodeBack(View view) {
        Intent BackPinCodeIntent = new Intent(PinCode.this, Profiles.class);
        startActivity(BackPinCodeIntent);
    }

    public void PinCodeOk(View view) {

        FileInputStream fin = null;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            text_in = new String (bytes);
        }
        catch (FileNotFoundException e) {
            //e.printStackTrace();
            FileOutputStream fos = null;
            try {

                //String text_out = "PinCode="+txtPinCode+"|Name="+txtName+"|PersonalAcc="+txtPersonalAcc+"|BankName="+txtBankName+"|BIC="+txtBIC+"|CorrespAcc="+txtCorrespAcc+"|KPP="+txtKPP+"|PayeeINN="+txtPayeeINN;
                //String text_out = "PinCode=5555|Name=ЭВЕРСТОВ СЕМЕН СЕМЕНОВИЧ|PersonalAcc=40817810976000348984|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143502001|PayeeINN=7707083893";
                //String text_out = "PinCode=1111|Name=Татаринов Эдуард Аркадьевич|PersonalAcc=40817810800028162749|BankName=Tinkoff|BIC=044525974|CorrespAcc=30101810145250000974|KPP=773401001|PayeeINN=7710140679";
                //String text_out = "PinCode=1111|Name=Широких Федор Федорович|PersonalAcc=40817810176005410866|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143502001|PayeeINN=7707083893";
                String text_out = "PinCode=1111|Name=ООО СахаСтройСистема|PersonalAcc=40702810576000003190|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143501001|PayeeINN=1435327003";

                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                fos.write(text_out.getBytes());
                Toast.makeText(PinCode.this, "Файл сохранен", Toast.LENGTH_LONG).show();
            }
            catch(IOException ex) {

                Toast.makeText(PinCode.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally{
                try{
                    if(fos!=null)
                        fos.close();
                }
                catch(IOException ex){

                    Toast.makeText(PinCode.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch(IOException ex) {
            Toast.makeText(PinCode.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
                Toast.makeText(PinCode.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        stPinCodeIn = text_in.substring(text_in.indexOf("PinCode=")+8,text_in.indexOf("|"));

        etPinCode = findViewById(R.id.idEntPinCode);
        stPinCode= etPinCode.getText().toString().trim();

        if (stPinCode.equals(stPinCodeIn)) {
            Intent PinCodeOkIntent = new Intent(PinCode.this, BankDetails.class);
            startActivity(PinCodeOkIntent);
        }

    }
}