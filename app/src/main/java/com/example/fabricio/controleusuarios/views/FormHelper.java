package com.example.fabricio.controleusuarios.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.fabricio.controleusuarios.R;
import com.example.fabricio.controleusuarios.models.Usuario;

/**
 * Created by Fabricio
 */

public class FormHelper {
    private EditText id;
    private EditText nome;
    private EditText telefone;
    private EditText site;
    private EditText endereco;
    private Usuario usuario;

    //Camera
    private ImageView foto;
    private Button fotoButton;

    public FormHelper(FormActivity activity)
    {

        this.nome = (EditText) activity.findViewById(R.id.nome);
        this.id = (EditText) activity.findViewById(R.id.id);
        this.telefone = (EditText) activity.findViewById(R.id.telefone);
        this.site = (EditText) activity.findViewById(R.id.site);
        this.endereco = (EditText) activity.findViewById(R.id.endereco);
        this.usuario = new Usuario();


        //camera
        foto = (ImageView) activity.findViewById(R.id.formulario_foto);
        fotoButton = (Button) activity.findViewById(R.id.formulario_foto_button);
    }
    //camera
    public Button getFotoButton ()
    {
        return fotoButton;
    }

    //camera
    public void carregaImagem(String localArquivoFoto)
    {
        // Get the dimensions of the View
        int targetW = foto.getWidth();
        int targetH = foto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(localArquivoFoto, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(localArquivoFoto, bmOptions);
        foto.setImageBitmap(bitmap);
        foto.setTag(localArquivoFoto);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public Usuario getUsuario()
    {
        if(!id.getText().toString().equals("")){
            usuario.setId(Long.parseLong(id.getText().toString()));
        }
        usuario.setNome(nome.getText().toString());
        usuario.setEndereco(endereco.getText().toString());
        usuario.setSite(site.getText().toString());
        usuario.setTelefone(telefone.getText().toString());
        //camera
        usuario.setCaminhoFoto((String) foto.getTag());
        return usuario;
    }

    public void setUsuario (Usuario usuario)
    {

        id.setText(usuario.getId().toString());
        nome.setText(usuario.getNome());
        telefone.setText(usuario.getTelefone());
        site.setText(usuario.getSite());
        endereco.setText(usuario.getEndereco());
        this.usuario = usuario;

        //camera
        if (usuario.getCaminhoFoto() != null)
        {
            this.carregaImagem(usuario.getCaminhoFoto());
        }
    }
}
