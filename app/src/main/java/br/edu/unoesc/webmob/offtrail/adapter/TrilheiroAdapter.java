package br.edu.unoesc.webmob.offtrail.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.Trilheiro;
import br.edu.unoesc.webmob.offtrail.ui.TrilheiroItemView;
import br.edu.unoesc.webmob.offtrail.ui.TrilheiroItemView_;

@EBean
public class TrilheiroAdapter
        extends BaseAdapter {
    List<Trilheiro> trilheiros;

    @Bean
    DatabaseHelper dh;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        try {
            trilheiros = dh.getTrilheiroDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return trilheiros.size();
    }

    @Override
    public Trilheiro getItem(int position) {
        return trilheiros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrilheiroItemView trilheiroItemView;
        if (convertView == null) {
            trilheiroItemView = TrilheiroItemView_.build(context);
        } else {
            trilheiroItemView = (TrilheiroItemView) convertView;
        }

        trilheiroItemView.bind(getItem(position));

        return trilheiroItemView;
    }

    public void ordenarLista() {
        initAdapter();
        Collections.sort(trilheiros, new Comparator<Trilheiro>() {
            @Override
            public int compare(Trilheiro t1, Trilheiro t2) {
                return t2.getNome().compareTo(t1.getNome());
            }
        });
    }
}