package br.edu.unoesc.webmob.offtrail.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.rest.spring.annotations.RestService;

import java.sql.SQLException;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.adapter.TrilheiroAdapter;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.Cidade;
import br.edu.unoesc.webmob.offtrail.model.Usuario;
import br.edu.unoesc.webmob.offtrail.rest.CidadeClient;
import br.edu.unoesc.webmob.offtrail.rest.Endereco;

@EActivity(R.layout.activity_principal)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @ViewById
    ListView lstTrilheiros;
    @Bean
    TrilheiroAdapter trilheiroAdapter;

    //injeção do cliente rest
    @RestService
    CidadeClient cidadeClient;

    ProgressDialog pd;

    @Bean
    DatabaseHelper dh;

    Toolbar toolbar;

    //injeção das preferências
    //@Pref
    Configuracao configuracao;

    @AfterViews
    public void inicializar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itCadastrarTrilheiro = new Intent(PrincipalActivity.this, TrilheiroActivity_.class);
                startActivity(itCadastrarTrilheiro);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //recuperar dados do usuário
        Usuario u = (Usuario) getIntent().getSerializableExtra("usuario");

        Toast.makeText(this, "Seja bem vindo - " + u.getLogin().toUpperCase(), Toast.LENGTH_LONG).show();

        lstTrilheiros.setAdapter(trilheiroAdapter);
    }

    public void atualizarListaTrilheiros() {
        TrilheiroAdapter ta = (TrilheiroAdapter) lstTrilheiros.getAdapter();
        ta.ordenarLista();
        ta.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarListaTrilheiros();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent itSobre = new Intent(this, SobreActivity_.class);
            startActivity(itSobre);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sincronizar) {
            pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setTitle("Aguarde sincronizando ...");
            pd.setIndeterminate(true);
            pd.show();
            consultarCidadePorNome();
        } else if (id == R.id.nav_preferencias) {
            Intent itPreferencias = new Intent(this,PreferenciaActivity_.class);
            startActivity(itPreferencias);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @UiThread
    public void mostrarResultado(String resultado) {
        pd.dismiss();
        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
    }

    @Background(delay = 1000)
    public void consultarCidadePorNome() {
        //pesquisa todas as cidades
        List<Endereco> e = cidadeClient.getEndereco("São");

        //limpa as cidades
        try {
            dh.getCidadeDao().delete(dh.getCidadeDao().queryForAll());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        //cadastra as cidades
        for (Endereco c : e) {
            Cidade cidade = new Cidade();
            cidade.setNome(c.toString());
        }
        if (e != null && e.size() > 0) {
            //mostrarResultado(e.get(0).toString());
            mostrarResultado("Sincronizado: " + e.size() + " cidades");
            pd.dismiss();
        }
    }
}
