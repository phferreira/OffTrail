package br.edu.unoesc.webmob.offtrail.ui;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.R;


@EActivity(R.layout.activity_preferencia)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class PreferenciaActivity extends AppCompatActivity {

    @ViewById
    Spinner spnCor;

    @ViewById
    Switch swtInternet;

    //injeção das preferências
    @Pref
    Configuracao_ configuracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencia);
    }

    @AfterViews
    public void inicializar() {
        carregarCorres();
    }

    private void carregarCorres() {
        List<String> cores = new ArrayList<String>();
        cores.add("Vermelho");
        cores.add("Azul");
        cores.add("Verde");
        cores.add("Preto");
        cores.add("Amarelo");

        ArrayAdapter<String> adapterCores = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, cores);
        spnCor.setAdapter(adapterCores);
    }

    public void aplicarConfiguracao(View v) {
        //para escrever parametros
        switch (spnCor.getSelectedItem().toString()) {
            case "Vermelho":
                configuracao.edit().cor().put(Color.RED).apply();
                break;
            case "Azul":
                configuracao.edit().cor().put(Color.BLUE).apply();
                break;
            case "Verde":
                configuracao.edit().cor().put(Color.GREEN).apply();
                break;
            case "Preto":
                configuracao.edit().cor().put(Color.BLACK).apply();
                break;
            case "Amarelo":
                configuracao.edit().cor().put(Color.YELLOW).apply();
                break;
            default:
                break;
        }

        if (swtInternet.isChecked()) {
            configuracao.edit().parametro().put("Sim").apply();
        } else {
            configuracao.edit().parametro().put("Não").apply();
        }

        Toast.makeText(this, "Configuração salva com sucesso", Toast.LENGTH_LONG).show();
    }
}
