package com.example.payqr;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.Point;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.Display;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Spinner;
        import android.widget.Toast;
        import androidx.appcompat.app.AppCompatActivity;
        import com.google.zxing.WriterException;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.util.ArrayList;

        import androidmads.library.qrgenearator.QRGContents;
        import androidmads.library.qrgenearator.QRGEncoder;


public class GenQR extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView qrCodeIV;
    private Button generateQrBtn;
    static EditText dataEdt;
    Spinner spinner;
    ArrayList spinnerArrayList;
    ArrayAdapter spinnerArrayAdapter;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    static String DestStr;
    String text_in;
    private final static String FILE_NAME = "profiles.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_q_r);

        qrCodeIV = findViewById(R.id.idIVQrcode);
        dataEdt = findViewById(R.id.idEdt);
        generateQrBtn = findViewById(R.id.idBtnGenerateQR);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        spinnerArrayList = new ArrayList();

        spinnerArrayList.add("Оплата");
        spinnerArrayList.add(" ");
        spinnerArrayList.add("Перечисление");
        spinnerArrayList.add("Платёж");
        spinnerArrayList.add("Услуга");


        spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dataEdt.getText().toString())) {

                    Toast.makeText(GenQR.this, "Введите текст для генерации QR кода", Toast.LENGTH_SHORT).show();
                } else {

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

                    Display display = manager.getDefaultDisplay();

                    Point point = new Point();
                    display.getSize(point);

                    int width = point.x;
                    int height = point.y;

                    int dimen = width < height ? width : height;
                    dimen = dimen * 3 / 4;

                    String PureText = dataEdt.getText().toString();
                    PureText = PureText.replaceAll("([.,,])",".");
                    if (PureText.lastIndexOf(".") <= 0) {
                        PureText = PureText.replaceAll("\\D","");
                        PureText = PureText + "00";
                    }
                    else {
                        PureText = PureText.replaceAll("\\D","");
                    }

                    //PureText = "ST00012|Name=Татаринов Эдуард Аркадьевич|PersonalAcc=40817810800028162749|BankName=Tinkoff|BIC=044525974|CorrespAcc=30101810145250000974|KPP=773401001|PayeeINN=7710140679|Purpose="+DestStr+"|Sum=" + PureText;
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
                            String text_out = "PinCode=1111|Name=Татаринов Эдуард Аркадьевич|PersonalAcc=40817810800028162749|BankName=Tinkoff|BIC=044525974|CorrespAcc=30101810145250000974|KPP=773401001|PayeeINN=7710140679";
                            //String text_out = "PinCode=1111|Name=Широких Федор Федорович|PersonalAcc=40817810176005410866|BankName=ЯКУТСКОЕ ОТДЕЛЕНИЕ N8603 ПАО СБЕРБАНК|BIC=049805609|CorrespAcc=30101810400000000609|KPP=143502001|PayeeINN=7707083893";

                            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            fos.write(text_out.getBytes());
                            Toast.makeText(GenQR.this, "Файл сохранен", Toast.LENGTH_SHORT).show();
                        }
                        catch(IOException ex) {

                            Toast.makeText(GenQR.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        finally{
                            try{
                                if(fos!=null)
                                    fos.close();
                            }
                            catch(IOException ex){

                                Toast.makeText(GenQR.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    catch(IOException ex) {
                        Toast.makeText(GenQR.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    finally{
                        try{
                            if(fin!=null)
                                fin.close();
                        }
                        catch(IOException ex){
                            Toast.makeText(GenQR.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    text_in = text_in.substring(text_in.indexOf("|")+1);

                    PureText = "ST00012|" + text_in + "|Purpose="+DestStr+"|Sum=" + PureText;;

                    qrgEncoder = new QRGEncoder(PureText, null, QRGContents.Type.TEXT, dimen);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrCodeIV.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.e("Tag", e.toString());
                    }
                }
            }
        });
    }

    public void GenerateBack(View view) {
        Intent BackGenerateIntent = new Intent(GenQR.this, Profiles.class);
        startActivity(BackGenerateIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DestStr = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void Print_cheque(View view) {
        Intent BTPrintIntent = new Intent(GenQR.this, BTPrint.class);
        startActivity(BTPrintIntent);
    }
}
