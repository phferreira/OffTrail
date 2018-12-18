package br.edu.unoesc.webmob.offtrail.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.model.Trilheiro;

@EViewGroup(R.layout.lista_trilheiros)
public class TrilheiroItemView extends LinearLayout {

    @ViewById
    TextView txtNome;

    @ViewById
    TextView txtMoto;

    @ViewById
    ImageView imvFoto;

    // variável global
    Trilheiro trilheiro;

    public TrilheiroItemView(Context context) {
        super(context);
    }

    @Click(R.id.imvEditar)
    public void editar() {

        /// criar uma intent para chamar a tela de cadastro/
        // nesta intent passar o objeto Trilheiro
        Toast.makeText(getContext(), "Editar: " +
                        trilheiro.getNome(),
                Toast.LENGTH_SHORT).show();
        //TODO: (2,50) Implementar a edição dos dados do trilheiro.
    }

    @Click(R.id.imvExcluir)
    public void excluir() {
//        Toast.makeText(getContext(), "Excluir: " +
//                        trilheiro.getNome(),
//                Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialogo
                = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Exclusão");
        dialogo.setMessage("Desejar realmente excluir? - "
                + trilheiro.getNome());
        dialogo.setCancelable(false);
        dialogo.setNegativeButton("Não", null);
        dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO: (2,50) Implementar a exclusão do trilheiro e grupo trilheiro.
            }
        });
        dialogo.show();
    }

    public void bind(Trilheiro t) {
        trilheiro = t;
        txtNome.setText(t.getNome());
        txtMoto.setText(t.getMoto().getModelo() + " - " + t.getMoto().getCilindrada());
        imvFoto.setImageBitmap(BitmapFactory.decodeByteArray(t.getFoto(), 0, t.getFoto().length));
    }
}