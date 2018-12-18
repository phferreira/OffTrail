package br.edu.unoesc.webmob.offtrail.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.adapter.TrilheiroAdapter;
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
    ProgressDialog pd;

    // injeção das preferências
    @Pref
    Configuracao_ configuracao;

    // injeção do cliente Rest
    @RestService
    CidadeClient cidadeClient;

    // após criar as views inicializa o
    // restante dos dados ...
    @AfterViews
    public void inicializar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // recuperar dados do usuario
        Usuario u = (Usuario) getIntent().
                getSerializableExtra("usuario");
        Toast.makeText(this,
                "Seja bem-vindo! - " + u.getLogin(),
                Toast.LENGTH_LONG).show();

        // *** LENDO OS PARÂMETROS
        // mudando a cor de fundo da view principal
        View v = toolbar.getRootView();
        // setando a cor de fundo da activity
        v.setBackgroundColor(configuracao.cor().get());
        Toast.makeText(this,
                configuracao.parametro().get(),
                Toast.LENGTH_SHORT).show();

        // ** ESCREVENDO OS PARÂMETROS
        configuracao.edit().cor().put(Color.BLUE).apply();
    } // inicializar

    @Override
    protected void onResume() {
        super.onResume();
        atualizaListaTrilheiros();
        //TODO: (1,00) Implementar atualização automática da lista de trilheiros ao sair da tela de cadastro do trilheiro.
    }

    public void atualizaListaTrilheiros() {
        //TODO: (1,00) Implementar a ordenação pelo nome do trilheiro de forma descendente.
        lstTrilheiros.setAdapter(trilheiroAdapter);
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
            //TODO: (0,75) Implementar uma tela de sobre o sistema com informações gerais.
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sincronizar) {
            pd =
                    new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setTitle("Aguarde, consultando ...");
            pd.setIndeterminate(true);
            pd.show();
            consultarCidadePorNome();
        } else if (id == R.id.nav_preferencias) {
            //TODO: (0,75) Implementar tela para salvar preferências.
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @UiThread
    public void mostrarResultado(String resultado) {
        pd.dismiss();
        Toast.makeText(this,
                resultado,
                Toast.LENGTH_LONG).show();
    }

    @Background(delay = 2000)
    public void consultarCidadePorNome() {
        //TODO: (1,50) Implementar a busca de todas as cidades que começam com "São" e gravar na tabela cidade.
        // aciona a busca
        List<Endereco> e =
                cidadeClient.getEndereco(
                        "Maravilha");
        if (e != null && e.size() > 0) {
            mostrarResultado(e.get(0).toString());
            pd.dismiss();
        }
    }
}
