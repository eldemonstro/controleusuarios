package com.example.fabricio.controleusuarios.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fabricio.controleusuarios.R;
import com.example.fabricio.controleusuarios.db.UsuarioDao;
import com.example.fabricio.controleusuarios.models.Usuario;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormActivity extends AppCompatActivity {
    private FormHelper helper;
    public  static  final String USUARIO_SELECIONADO = "usuarioelecionado";

    //camera
    private String localArquivoFoto;
    private static final int REQUEST_IMAGE_CAPTURE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        this.helper = new FormHelper(this);

        //camera
        Button foto = helper.getFotoButton();
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        Intent intent = this.getIntent();
        Usuario usuario = (Usuario) intent.getSerializableExtra("usuario");
        if (usuario != null)
        {
            helper.setUsuario(usuario);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(FormActivity.this, "Erro ao tentar criar o arquivo", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_formulario_ok:
                Usuario usuario = helper.getUsuario();
                UsuarioDao dao = new UsuarioDao(FormActivity.this);
                if(usuario.getId() != null)
                {
                    dao.alterar(usuario);
                }
                else
                {
                    dao.inserir(usuario);
                }
                dao.close();

                finish();

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            helper.carregaImagem(localArquivoFoto);
            Toast.makeText(FormActivity.this, "A captura foi efetuada", Toast.LENGTH_LONG).show();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        localArquivoFoto = image.getAbsolutePath();
        return image;
    }
}
