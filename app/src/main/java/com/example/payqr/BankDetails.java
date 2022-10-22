package com.example.payqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BankDetails extends AppCompatActivity {

    private EditText etTextName;
    private EditText etTextPersonalAcc;
    private EditText etTextBankName;
    private EditText etTextBIC;
    private EditText etTextCorrespAcc;
    private EditText etTextKPP;
    private EditText etTextPayeeINN;
    private EditText etTextPinCode;

    SharedPreferences sPref;
    private final static String FILE_NAME = "profiles.txt";

    @Override
    public void onBackPressed() {
        Toast.makeText(BankDetails.this,"Not Allowed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);

        FileInputStream fin = null;

        etTextName = findViewById(R.id.idEditName);
        etTextPersonalAcc = findViewById(R.id.idEditPersonalAcc);
        etTextBankName = findViewById(R.id.idEditBankName);
        etTextBIC = findViewById(R.id.idEditBIC);
        etTextCorrespAcc = findViewById(R.id.idEditCorrespAcc);
        etTextKPP = findViewById(R.id.idEditKPP);
        etTextPayeeINN = findViewById(R.id.idEditPayeeINN);
        etTextPinCode = findViewById(R.id.PinCode);

        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text_in = new String (bytes);
            String PinCodeIn = text_in.substring(text_in.indexOf("PinCode=")+8,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            String NameIn = text_in.substring(text_in.indexOf("Name=")+5,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            String PersonalAccIn = text_in.substring(text_in.indexOf("PersonalAcc=")+12,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            String BankNameIn = text_in.substring(text_in.indexOf("BankName=")+9,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            String BICIn = text_in.substring(text_in.indexOf("BIC=")+4,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            String CorrespAccIn = text_in.substring(text_in.indexOf("CorrespAcc=")+11,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            String KPPIn = text_in.substring(text_in.indexOf("KPP=")+4,text_in.indexOf("|"));
            text_in = text_in.substring(text_in.indexOf("|")+1);
            String PayeeINNIn = text_in.substring(text_in.indexOf("PayeeINN=")+9);
            etTextName.setText(NameIn);
            etTextPersonalAcc.setText(PersonalAccIn);
            etTextBankName.setText(BankNameIn);
            etTextBIC.setText(BICIn);
            etTextCorrespAcc.setText(CorrespAccIn);
            etTextKPP.setText(KPPIn);
            etTextPayeeINN.setText(PayeeINNIn);
            etTextPinCode.setText(PinCodeIn);
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
                Toast.makeText(BankDetails.this, "Файл сохранен", Toast.LENGTH_SHORT).show();
            }
            catch(IOException ex) {

                Toast.makeText(BankDetails.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally{
                try{
                    if(fos!=null)
                        fos.close();
                }
                catch(IOException ex){

                    Toast.makeText(BankDetails.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch(IOException ex) {
            Toast.makeText(BankDetails.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
                Toast.makeText(BankDetails.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void SaveBankDetails(View view) {

        FileOutputStream fos = null;
        try {
            etTextPinCode = findViewById(R.id.PinCode);
            String txtPinCode = etTextPinCode.getText().toString().trim();
            etTextName = findViewById(R.id.idEditName);
            String txtName = etTextName.getText().toString().trim();
            etTextPersonalAcc = findViewById(R.id.idEditPersonalAcc);
            String txtPersonalAcc = etTextPersonalAcc.getText().toString().trim();
            etTextBankName = findViewById(R.id.idEditBankName);
            String txtBankName = etTextBankName.getText().toString().trim();
            etTextBIC = findViewById(R.id.idEditBIC);
            String txtBIC = etTextBIC.getText().toString().trim();
            etTextCorrespAcc = findViewById(R.id.idEditCorrespAcc);
            String txtCorrespAcc = etTextCorrespAcc.getText().toString().trim();
            etTextKPP = findViewById(R.id.idEditKPP);
            String txtKPP = etTextKPP.getText().toString().trim();
            etTextPayeeINN = findViewById(R.id.idEditPayeeINN);
            String txtPayeeINN = etTextPayeeINN.getText().toString().trim();

            //String text_out = "PinCode=1111|Name=ООО СахаСтройСистема|PersonalAcc=40702810576000003190|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143501001|PayeeINN=1435327003";
            String text_out = "PinCode="+txtPinCode+"|Name="+txtName+"|PersonalAcc="+txtPersonalAcc+"|BankName="+txtBankName+"|BIC="+txtBIC+"|CorrespAcc="+txtCorrespAcc+"|KPP="+txtKPP+"|PayeeINN="+txtPayeeINN;
            //String text_out = "PinCode=1111|Name=Татаринов Эдуард Аркадьевич|PersonalAcc=40817810800028162749|BankName=Tinkoff|BIC=044525974|CorrespAcc=30101810145250000974|KPP=773401001|PayeeINN=7710140679";
            //String text_out = "PinCode=1111|Name=Широких Федор Федорович|PersonalAcc=40817810176005410866|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143502001|PayeeINN=7707083893";
            //String text_out = "PinCode=1111|Name=ООО СахаСтройСистема|PersonalAcc=40702810576000003190|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143501001|PayeeINN=1435327003";

            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text_out.getBytes());
            Toast.makeText(BankDetails.this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {

            Toast.makeText(BankDetails.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){

                Toast.makeText(BankDetails.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        Intent SaveBankDetailsIntent = new Intent(BankDetails.this, Profiles.class);
        startActivity(SaveBankDetailsIntent);
    }

    public void BankDetailsBack(View view) {
        Intent BackBankDetailsIntent = new Intent(BankDetails.this, Profiles.class);
        startActivity(BackBankDetailsIntent);
    }

}