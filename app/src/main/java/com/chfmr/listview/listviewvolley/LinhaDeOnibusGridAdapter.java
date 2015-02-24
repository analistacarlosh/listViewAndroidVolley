package com.chfmr.listview.listviewvolley;

import android.content.Context;
import android.net.Network;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by carlosfm on 04/02/15.
 */
public class LinhaDeOnibusGridAdapter extends ArrayAdapter<LinhaDeOnibus> {

    private ImageLoader mLoader;

    public LinhaDeOnibusGridAdapter(Context contexto, List<LinhaDeOnibus> linhasDeOnibus){
        super(contexto, 0, linhasDeOnibus);
        mLoader = VolleySingleton.getInstance(contexto).getmImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Context ctx = parent.getContext();

        if(convertView == null){
            Log.d("NGVL", "View Nova => position: " + position);
            convertView = LayoutInflater.from(ctx)
                    .inflate(R.layout.item_linha_grid, null);
        }

        NetworkImageView img = (NetworkImageView)convertView.findViewById(R.id.imgCapa);
        TextView txt = (TextView)convertView.findViewById(R.id.txtNome);

        LinhaDeOnibus linha = getItem(position);
        txt.setText(linha.nome);
        img.setImageUrl(linha.imagem, mLoader);

        return convertView;
    }
}
