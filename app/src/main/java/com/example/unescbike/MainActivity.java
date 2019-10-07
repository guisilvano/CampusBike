package com.example.unescbike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.*;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

//implementing onclicklistener
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    private Button buttonScan;
    private TextView textViewUsuario, textViewSenha;

    //qr code scanner object
    private IntentIntegrator qrScan;


    /*
     * "LINK" CONTEM AS INFORMAÇÕES DO QRCODE, USUARIO E SENHA.
     *
     * ESTA É A VARIÁVEL QUE DEVERÁ SER LIDA PELO SERVIDOR.
     *
     * Caso seja necessária alguma correção ou modificação na string, modificar o "else" de
     * "if (result != null)" na classe "onActivityResult".
     */
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewUsuario = (TextView) findViewById(R.id.textViewUsuario);
        textViewSenha = (TextView) findViewById(R.id.textViewSenha);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    textViewUsuario.setText(obj.getString("usuario"));
                    textViewSenha.setText(obj.getString("senha"));


                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

            //ATRIBUI O USUARIO E A INFO LIDA NO QR CODE À VARIÁVEL "STRING LINK"
            JSONObject obj = null;
            try {
                obj = new JSONObject(result.getContents());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String usuario = null;
            try {
                usuario = obj.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String senha = null;
            try {
                senha = obj.getString("address");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*
             * TODO: "usuario" e "senha" não são entradas do usuário, mas sim strings estáticas.
             *
             * Mudar os campos "textViewUsuario" e "textViewSenha" para "TextField" assim que o app
             * estiver mais estável.
             */

            link =  data + "$" + usuario + "$" + senha;

        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}