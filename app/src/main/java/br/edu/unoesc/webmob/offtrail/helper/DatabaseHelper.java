package br.edu.unoesc.webmob.offtrail.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.model.Cidade;
import br.edu.unoesc.webmob.offtrail.model.Grupo;
import br.edu.unoesc.webmob.offtrail.model.GrupoTrilheiro;
import br.edu.unoesc.webmob.offtrail.model.Moto;
import br.edu.unoesc.webmob.offtrail.model.Trilheiro;
import br.edu.unoesc.webmob.offtrail.model.Usuario;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
@EBean
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "offtrail.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<Cidade, Integer> cidadeDao = null;
    private Dao<Usuario, String> usuarioDao = null;
    private Dao<Grupo, Integer> grupoDao = null;
    private Dao<Trilheiro, Integer> trilheiroDao = null;
    private Dao<Moto, Integer> motoDao = null;
    private Dao<GrupoTrilheiro, Integer> grupoTrilheiroDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Cidade.class);
            TableUtils.createTable(connectionSource, Grupo.class);
            TableUtils.createTable(connectionSource, Moto.class);
            TableUtils.createTable(connectionSource, Usuario.class);
            TableUtils.createTable(connectionSource, Trilheiro.class);
            TableUtils.createTable(connectionSource, GrupoTrilheiro.class);


            // inserindo dados na base
            Usuario u = new Usuario();
            u.setLogin("paulo");
            u.setSenha("paulo");
            // insert
            getUsuarioDao().create(u);

            // inserindo cidades
            Cidade c = new Cidade();
            c.setNome("São Miguel do Oeste");
            getCidadeDao().create(c); // insert

            // inserindo grupos
            Grupo g = new Grupo();
            g.setNome("Lobos do Oeste");
            g.setCidade(c);
            getGrupoDao().create(g);
            g = new Grupo();
            g.setNome("Fugitivos do Oeste");
            g.setCidade(c);
            getGrupoDao().create(g);

            // inserindo motos
            Moto m = new Moto();
            m.setModelo("CG");
            m.setMarca("Honda");
            m.setCilindrada("125cc");
            m.setCor("Preta");
            // insert da moto
            m = new Moto();
            m.setModelo("CRF");
            m.setMarca("Honda");
            m.setCilindrada("230cc");
            m.setCor("Vermelha");
            // insert da moto

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }


        Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate!");
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Cidade.class, true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Cidade, Integer> getCidadeDao()
            throws SQLException {
        if (cidadeDao == null) {
            cidadeDao = getDao(Cidade.class);
        }
        return cidadeDao;
    }

    public Dao<Grupo, Integer> getGrupoDao() throws SQLException {
        if (grupoDao == null) {
            grupoDao = getDao(Grupo.class);
        }
        return grupoDao;
    }

    public Dao<Trilheiro, Integer> getTrilheiroDao() throws SQLException {
        if (trilheiroDao == null) {
            trilheiroDao = getDao(Trilheiro.class);
        }
        return trilheiroDao;
    }

    public Dao<GrupoTrilheiro, Integer> getGrupoTrilheiroDao() throws SQLException {
        if (grupoTrilheiroDao == null) {
            grupoTrilheiroDao = getDao(GrupoTrilheiro.class);
        }
        return grupoTrilheiroDao;
    }

    public Dao<Usuario, String> getUsuarioDao() throws SQLException {
        if (usuarioDao == null) {
            usuarioDao = getDao(Usuario.class);
        }
        return usuarioDao;
    }

    public Dao<Moto, Integer> getMotoDao() throws SQLException {
        if (motoDao == null) {
            motoDao = getDao(Moto.class);
        }
        return motoDao;
    }

    public Usuario validaLogin(String login,
                               String senha) {
        List<Usuario> usuarios = null;
        try {
            usuarios = getUsuarioDao().
                    queryBuilder().
                    where().
                    // loginUsuario = login_usuario
                            eq("login", login).
                            and().
                            eq("senha", senha).
                            query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (usuarios != null && usuarios.size() > 0) {
            return usuarios.get(0);
        }

        return null;
    }


    @Override
    public void close() {
        super.close();
        cidadeDao = null;
    }
}