package com.example.imccalculator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    //pegar os componentes da minha página principal
    private SeekBar sbAlturaMain, sbPesoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //tratar quando o menu for a opção de reset
        if(id == R.id.opc_reset){
            //resetar todos os valores
            resetarValores();
        }

        //tratar quando escolher a opção de fechar
        if(id == R.id.opc_fechar){
            //fechar a aplicação
            fecharAplicacao();
        }

        return super.onOptionsItemSelected(item);
    }

    //função para fechar a aplicação
    private void fecharAplicacao() {

    }

    //função que resetar todos os valores
    private void resetarValores(){

    }
}