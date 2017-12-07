package com.example.fabricio.controleusuarios.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fabricio.controleusuarios.models.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabricio
 */

public class UsuarioDao extends SQLiteOpenHelper {
    private  static final int VERSAO = 2;
    private static final String TABELA = "usuarios";
    private static final String DATABASE = "usuariocontroladores";

    public UsuarioDao(Context context)
    {
        super(context, DATABASE, null, VERSAO);
    }

    public void onCreate(SQLiteDatabase database)
    {
        String ddl = "CREATE TABLE " + TABELA
                +" (id INTEGER AUTO_INCREMENT PRIMARY KEY,"
                +" nome TEXT NOT NULL,"
                +" telefone TEXT,"
                +" endereco TEXT,"
                +" site TEXT,"
                +" caminhoFoto TEXT);";
        database.execSQL(ddl);
    }

    public void onUpgrade(SQLiteDatabase database, int versaoAntiga, int versaoNova) {
        String sql = "ALTER TABLE " + TABELA + " ADD COLUMN caminhoFoto TEXT; ";
        database.execSQL(sql);
        //camera: adicionamos ADD COLUMN caminhoFoto TEXT
    }

    public void inserir(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("telefone", usuario.getTelefone());
        values.put("endereco", usuario.getEndereco());
        values.put("site", usuario.getSite());
        //camera
        values.put("caminhoFoto", usuario.getCaminhoFoto());

        getWritableDatabase().insert(TABELA, null, values);

    }

    public List<Usuario> listar()
    {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABELA + ";", null);

        while (c.moveToNext())
        {
            Usuario usuario = new Usuario();
            usuario.setId(c.getLong(c.getColumnIndex("id")));
            usuario.setNome(c.getString(c.getColumnIndex("nome")));
            usuario.setTelefone(c.getString(c.getColumnIndex("telefone")));
            usuario.setEndereco(c.getString(c.getColumnIndex("endereco")));
            usuario.setSite(c.getString(c.getColumnIndex("site")));
            //camera
            usuario.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            usuarios.add(usuario);
        }
        c.close();
        return usuarios;
    }

    public void deletar(Usuario usuario)
    {
        String [] args = {usuario.getId().toString()};
        getWritableDatabase().delete(TABELA, "id=?", args);
    }

    public void alterar(Usuario usuario)
    {

        ContentValues values = new  ContentValues();
        values.put("nome", usuario.getNome());
        values.put("telefone", usuario.getTelefone());
        values.put("endereco", usuario.getEndereco());
        values.put("site", usuario.getSite());
        //camera
        values.put("caminhoFoto", usuario.getCaminhoFoto());

        getWritableDatabase().update(TABELA, values, "id=?", new String[] {usuario.getId().toString()});
    }
}
