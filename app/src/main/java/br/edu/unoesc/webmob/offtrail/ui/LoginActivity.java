package br.edu.unoesc.webmob.offtrail.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.io.Serializable;
import java.sql.SQLException;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.Usuario;

@EActivity(R.layout.activity_login)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class LoginActivity extends AppCompatActivity {
    @Bean
    DatabaseHelper dh;
    @ViewById
    EditText edtLogin;
    @ViewById
    EditText edtSenha;

    public void entrarLogin(View v) {
        String strLogin = edtLogin.getText().toString();
        String strSenha = edtSenha.getText().toString();


        if (strLogin != null && strSenha != null) {
            Usuario u = dh.validaLogin(strLogin, strSenha);
            if (u != null) {
                //Toast.makeText(this, "Bem vindo " + strLogin.toUpperCase(), Toast.LENGTH_SHORT).show();
                Intent itPrincipal = new Intent(this, PrincipalActivity_.class);
                //passando parâmetro para outra tela (chave,valor)
                itPrincipal.putExtra("usuario", u);
                startActivity(itPrincipal);
                finish();
            } else {
                Toast.makeText(this, "Usuário e senha inválidos", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Preencha os campos", Toast.LENGTH_LONG).show();
        }

    }

    public void sairLogin(View v) {
        finish();
        System.exit(0);
    }
}
