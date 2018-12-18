package br.edu.unoesc.webmob.offtrail.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.Date;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.Grupo;
import br.edu.unoesc.webmob.offtrail.model.GrupoTrilheiro;
import br.edu.unoesc.webmob.offtrail.model.Moto;
import br.edu.unoesc.webmob.offtrail.model.Trilheiro;

@EActivity(R.layout.activity_cadastro_trilheiro)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class CadastroTrilheiroActivity extends AppCompatActivity {
    @ViewById
    ImageView imvFoto;
    @ViewById
    EditText edtNome;
    @ViewById
    EditText edtIdade;
    @ViewById
    Spinner spnMotos;
    @ViewById
    Spinner spnGrupos;
    @Bean
    DatabaseHelper dh;

    @AfterViews
    public void inicializar() {
        try {
            // criar o adapter das motos
            ArrayAdapter<Moto> motos
                    = new ArrayAdapter<Moto>(this,
                    android.R.layout.simple_spinner_item,
                    dh.getMotoDao().queryForAll());
            // vincula o adaptar ao spinner
            spnMotos.setAdapter(motos);
            // criar o adaptar do grupo


        } catch (SQLException ex) {

        }
    }

    public void cancelar() {
        finish();
    }

    public void salvar() {
        try {
            Trilheiro t = new Trilheiro();
            t.setNome(edtNome.getText().toString());
            t.setIdade(Integer.parseInt(
                    edtIdade.getText().toString()
            ));
            t.setMoto((Moto) spnMotos.getSelectedItem());
            Bitmap bitmap = ((BitmapDrawable) imvFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            t.setFoto(baos.toByteArray());
            dh.getTrilheiroDao().create(t);
            GrupoTrilheiro gt = new GrupoTrilheiro();
            gt.setTrilheiro(t);
            gt.setGrupo((Grupo) spnGrupos.getSelectedItem());
            gt.setDataCadastro(new Date());
            dh.getGrupoTrilheiroDao().create(gt);
        } catch (SQLException ex) {

        }
    }

    @LongClick(R.id.imvFoto)
    public void capturarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 100);
        }
    }

    @OnActivityResult(100)
    void onResult(int resultCode, Intent data) {
        // se o usuário selecionou a foto
        // tenta mostrar ela
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imvFoto.setImageBitmap(imageBitmap);
        }
    }
}
