package br.edu.unoesc.webmob.offtrail.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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

@EActivity(R.layout.activity_trilheiro)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class TrilheiroActivity extends AppCompatActivity {

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

    Trilheiro t;

    @AfterViews
    public void inicializar() {
        try {
            ArrayAdapter<Moto> motos = new ArrayAdapter<Moto>(this, android.R.layout.simple_spinner_item, dh.getMotoDao().queryForAll());
            spnMotos.setAdapter(motos);

            ArrayAdapter<Grupo> grupos = new ArrayAdapter<Grupo>(this, android.R.layout.simple_spinner_item, dh.getGrupoDao().queryForAll());
            spnGrupos.setAdapter(grupos);

            t = (Trilheiro) getIntent().getSerializableExtra("trilheiro");
            if (t != null) {
                edtNome.setText(t.getNome());
                edtIdade.setText(t.getIdade().toString());

                for (int i = 0; i < spnMotos.getCount(); i++) {
                    if (spnMotos.getItemAtPosition(i).toString().equals(t.getMoto().toString())) {
                        spnMotos.setSelection(i);
                    }
                }

                for (GrupoTrilheiro g : dh.getGrupoTrilheiroDao().queryForAll()) {
                    if (g.getTrilheiro().getCodigo().equals(t.getCodigo())) {
                        for (int i = 0; i < spnGrupos.getCount(); i++) {
                            if (spnGrupos.getItemAtPosition(i).toString().equals(g.getGrupo().toString())) {
                                spnGrupos.setSelection(i);
                            }
                        }
                    }
                }
                byte[] myImage = t.getFoto();

                Bitmap bmp = BitmapFactory.decodeByteArray(myImage, 0, myImage.length);
                imvFoto.setImageBitmap(bmp);
            }


        } catch (SQLException e) {
            Log.e(Trilheiro.class.getName(), "Erro ao ler os dados de Moto e Grupo");
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trilheiro);
    }

    public void salvarTrilheiro(View v) {
        Trilheiro t = (Trilheiro) getIntent().getSerializableExtra("trilheiro");
        if (t == null) {
            t = new Trilheiro();
            inserirTrilheiro(t);
        } else {
            alterarTrilheiro();
        }

        try {
            GrupoTrilheiro gt = new GrupoTrilheiro();
            gt.setTrilheiro(t);
            gt.setGrupo((Grupo) spnGrupos.getSelectedItem());
            gt.setDataCadastro(new Date());

            dh.getGrupoTrilheiroDao().create(gt);

            Toast.makeText(this, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(this, "Erro ao salvar os dados", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        finish();
    }

    public void inserirTrilheiro(Trilheiro t) {

        t.setNome(edtNome.getText().toString());
        t.setIdade(Integer.parseInt(edtIdade.getText().toString()));
        t.setMoto((Moto) spnMotos.getSelectedItem());

        //para recuperar e setar a imagem
        Bitmap bitmap = ((BitmapDrawable) imvFoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        t.setFoto(baos.toByteArray());

        try {
            dh.getTrilheiroDao().create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void alterarTrilheiro() {
        t.setNome(edtNome.getText().toString());
        t.setIdade(Integer.parseInt(edtIdade.getText().toString()));
        t.setMoto((Moto) spnMotos.getSelectedItem());

        //para recuperar e setar a imagem
        Bitmap bitmap = ((BitmapDrawable) imvFoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        t.setFoto(baos.toByteArray());

        try {
            dh.getTrilheiroDao().update(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelarTrilheiro(View v) {
        edtNome.setText("");
        edtIdade.setText("");
        spnMotos.setSelection(0);
        edtNome.requestFocus();
        finish();
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
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imvFoto.setImageBitmap(imageBitmap);
        }
    }
}
