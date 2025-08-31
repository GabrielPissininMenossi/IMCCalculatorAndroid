package com.example.imccalculator;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {
    private ListView lvListaInfo;
    private Button btApagarInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listainfo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //pegar os elementos
        btApagarInfo = findViewById(R.id.btApagarInfo);
        lvListaInfo = findViewById(R.id.lvListaInfo);

        if(Singleton.infoList == null){
            Singleton.infoList = new ArrayList<>();
        }

        //definir métodos para os elementos
        lvListaInfo.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                Singleton.infoList
        ));

        btApagarInfo.setOnClickListener(v->{
            //apagar todas as informações da lista singleton
            new AlertDialog.Builder(this)
                    .setTitle("Confirmação")
                    .setMessage("Deseja excluir todas as informações?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        //apagar todas as informações
                        apagarTudo();
                    })
                    .setNegativeButton("Não", (dialog, which) -> {
                        //se clicou em não, não realiza nenhuma ação
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        });

        lvListaInfo.setOnItemLongClickListener((parent, view, posicao, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Excluir item")
                    .setMessage("Deseja excluir este item?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        Singleton.infoList.remove(posicao);
                        ((ArrayAdapter) lvListaInfo.getAdapter()).notifyDataSetChanged();
                        salvarLista();
                    })
                    .setNegativeButton("Não", (dialog, which) -> dialog.dismiss())
                    .show();

            //recreate();
            return true; //algo foi realizado
        });
    }

    private void apagarTudo(){
        Singleton.infoList = null;
        try {
            FileOutputStream fileOutputStream = openFileOutput("dados.dat", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(new ArrayList<>()); // sobrescreve com vazio
            objectOutputStream.close();
        } catch (Exception e) {
            Log.e("Erro ao limpar dados", e.getMessage());
        }

        //recriar a página
        recreate();
    }

    private void salvarLista(){
        try {
            FileOutputStream fileOutputStream = openFileOutput("dados.dat", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(Singleton.infoList);
            objectOutputStream.close();

        } catch (Exception e) {
            Log.e("Ocorreu algum erro\n",e.getMessage());
        }
    }
}
