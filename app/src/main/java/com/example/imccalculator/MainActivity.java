package com.example.imccalculator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //pegar os componentes da minha página principal
    private EditText etNomeMain; //pegar o nome do usuário
    private SeekBar sbAlturaMain, sbPesoMain;
    private Switch swMulherMain; //trocar o tipo de calculo que sera feito
    private TextView tvAlturaMain, tvPesoMain; //valores que ficam logo a frente ao seekBar
    private TextView tvIndiceMain, tvInfoMain; //valores que aprecem abaixo com as informações finais
    private Button btLimparMain, btArmazenarMain;

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

        //pegar os componentes da interface pelos respectivos ID's
        etNomeMain = findViewById(R.id.etNomeMain);
        sbAlturaMain = findViewById(R.id.sbAlturaMain);
        sbPesoMain = findViewById(R.id.sbPesoMain);
        tvAlturaMain = findViewById(R.id.tvAlturaMain);
        tvPesoMain = findViewById(R.id.tvPesoMain);
        tvIndiceMain = findViewById(R.id.tvIndiceMain);
        tvInfoMain = findViewById(R.id.tvInfoMain);
        btLimparMain = findViewById(R.id.btLimparMain);
        btArmazenarMain = findViewById(R.id.btArmazenarMain);
        swMulherMain = findViewById(R.id.swMulherMain);

        //definir metodos quando mudar os valores do seekBar
        sbAlturaMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //chamar o calcularIMC passando: progresso de altura, progresso de peso
                tvAlturaMain.setText(String.format("%.2f", progress/100.0));
                calcularIMC(progress, sbPesoMain.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbPesoMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //chamar o calcularIMC passando: progresso de altura, progresso de peso
                tvPesoMain.setText("" + progress);
                calcularIMC(sbAlturaMain.getProgress(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //metodo para quando o switch for clicado
        swMulherMain.setOnClickListener(v->{
            calcularIMC(sbAlturaMain.getProgress(),sbPesoMain.getProgress());
        });

        btLimparMain.setOnClickListener(v ->{
            resetarValores();
        });

        //metodo para quando alguem clicar no componente nome
        etNomeMain.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etNomeMain.post(() -> etNomeMain.selectAll());
            }
        });

        //para quando o usuário apertar para armazenar as informações
        btArmazenarMain.setOnClickListener(v ->{
            armazenarInfo();
            resetarValores();
        });

        //carregar os dados uma vez salvos
        carregarDados();
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

        //tratar quando quiser ver os dados salvos
        if(id == R.id.opc_consultar_historico){
            //vai para a outra pagina e exibe as informações gravadas
            consultarHistorico();
        }

        return super.onOptionsItemSelected(item);
    }

    //metodo para quando finalizar o aplicativo
    @Override
    protected void onStop(){
        super.onStop();

        //persistir as informações no arquivo .dat
        try {
            FileOutputStream fileOutputStream = openFileOutput("dados.dat", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(Singleton.infoList);
            objectOutputStream.close();

        } catch (Exception e) {
            Log.e("Ocorreu algum erro\n",e.getMessage());
        }
    }

    //função para fechar a aplicação
    private void fecharAplicacao() {
        finishAffinity(); //sai da aplicação mas não finaliza por completo por segurança
    }

    //função que resetar todos os valores
    private void resetarValores(){
        etNomeMain.setText("Seu nome aqui...");
        sbAlturaMain.setProgress(170);
        sbPesoMain.setProgress(70);
        swMulherMain.setChecked(false);
        tvAlturaMain.setText("" + sbAlturaMain.getProgress());
        tvPesoMain.setText("" + sbPesoMain.getProgress());
        calcularIMC(sbAlturaMain.getProgress(), sbPesoMain.getProgress());
    }

    //ir para a outra pagina aonde possui as informações salvas
    private void consultarHistorico(){
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    //função para calcular o IMC
    private void calcularIMC(int altura, int peso){
        double calculo, alturaDecimal = altura/100.0;
        calculo = peso / (alturaDecimal*alturaDecimal);
        tvIndiceMain.setText(String.format("%.2f", calculo));

        //verificar se homem ou mulher
        if(swMulherMain != null && swMulherMain.isChecked()){
            //cálculo para a mulher
            if(calculo < 19.1){
                //abaixo do peso
                tvInfoMain.setText("Abaixo do Peso!!");
            } else if(calculo < 25.8){
                //peso ideal
                tvInfoMain.setText("Peso Ideal!");
            } else if(calculo < 27.3) {
                //marginalmente acima do peso
                tvInfoMain.setText("Um pouco acima!!");
            } else if(calculo < 32.3){
                //acima do peso
                tvInfoMain.setText("Acima do peso!!");
            } else{
                //obesa
                tvInfoMain.setText("Obesa!!");
            }
        }
        else{
            //cálculo para o homem
            if(calculo < 20.7){
                //abaixo do peso
                tvInfoMain.setText("Abaixo do Peso!!");
            } else if(calculo < 26.4){
                //peso ideal
                tvInfoMain.setText("Peso Ideal!");
            } else if(calculo < 27.8) {
                //marginalmente acima do peso
                tvInfoMain.setText("Um pouco acima!!");
            } else if(calculo < 31.1){
                //acima do peso
                tvInfoMain.setText("Acima do peso!!");
            } else{
                //obeso
                tvInfoMain.setText("Obeso!!");
            }
        }
    }

    //função para armazenar as informações
    private void armazenarInfo(){
        IMCModel imcModel = new IMCModel();
        imcModel.setNome(String.valueOf(etNomeMain.getText()));
        imcModel.setCondicao(String.valueOf(tvInfoMain.getText()));
        imcModel.setImcCalculado(Double.parseDouble(String.valueOf(tvIndiceMain.getText())));
        imcModel.setAltura(sbAlturaMain.getProgress());
        imcModel.setPeso(sbPesoMain.getProgress());
        if(swMulherMain.isChecked()) //mulher
            imcModel.setSexo('F');
        else //homem
            imcModel.setSexo('M');
        Singleton.infoList.add(imcModel);
    }

    private void carregarDados(){
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;

        //recuperar os dados do arquivo .dat
        try{
            fileInputStream = openFileInput("dados.dat");
            objectInputStream = new ObjectInputStream(fileInputStream);
            Singleton.infoList = (List<IMCModel>)objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e){
            Log.e("Erro ao carregar os dados\n", e.getMessage());
        }
    }
}