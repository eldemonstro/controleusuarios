package com.example.fabricio.controleusuarios.views;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fabricio.controleusuarios.R;
import com.example.fabricio.controleusuarios.db.UsuarioDao;
import com.example.fabricio.controleusuarios.models.Usuario;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listaU;
    private List<Usuario> u;
    private static final int TELEFONE_CODE_REQUEST = 10;
    private Usuario bs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaU = (ListView) findViewById(R.id.lista_usuarios);

        Button btAdd = (Button) findViewById(R.id.floating_button);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast
                Toast.makeText(MainActivity.this, "Floating button clicado", Toast.LENGTH_LONG).show();

                //chamar entao o formularioActivity
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);

            }
        });

        listaU.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent edicao = new Intent(MainActivity.this, FormActivity.class);
                edicao.putExtra("usuario", (Serializable) listaU.getItemAtPosition(position));
                startActivity(edicao);
            }
        });

        registerForContextMenu(listaU);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.carregaLista();
    }

    private void carregaLista() {

        UsuarioDao dao = new UsuarioDao(this);
        List<Usuario> usuarios = dao.listar();
        dao.close();
        ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, usuarios);
        this.listaU.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        bs = (Usuario) listaU.getAdapter().getItem(info.position);

        //Ligar
        MenuItem ligar = menu.add("Ligar");

        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                                             @Override
                                             public boolean onMenuItemClick(MenuItem item) {
                                                 String permissaoLigacao = android.Manifest.permission.CALL_PHONE;
                                                 if (ActivityCompat.checkSelfPermission(MainActivity.this, permissaoLigacao) == PackageManager.PERMISSION_GRANTED) {
                                                     fazerLigacao();
                                                 } else {
                                                     ActivityCompat.requestPermissions(MainActivity.this, new String[]{permissaoLigacao}, TELEFONE_CODE_REQUEST);
                                                 }
                                                 return false;
                                             }
                                         }
        );


        //site
        MenuItem site = menu.add("Abrir Página Web");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSite.setData(Uri.parse("http:" + bs.getSite()));
        site.setIntent(intentSite);

        //Mapa
        MenuItem mapa = menu.add("Abrir no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0.0?z=14&q=" + Uri.encode(bs.getEndereco())));
        mapa.setIntent(intentMapa);


        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            //Metodo para deletar
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //para deletar ca xta a acaoa do metodo
                UsuarioDao dao = new UsuarioDao(MainActivity.this);
                dao.deletar(bs);
                dao.close();
                carregaLista();

                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == TELEFONE_CODE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fazerLigacao();
            } else {
                Toast.makeText(this, "Permissao de ligacao nao concedida", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fazerLigacao() {
        Intent intentLigar = new Intent(Intent.ACTION_CALL);
        intentLigar.setData(Uri.parse("tel:" + bs.getTelefone()));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intentLigar);
        //Este erro de startActivity nao tem problema

    }
}
