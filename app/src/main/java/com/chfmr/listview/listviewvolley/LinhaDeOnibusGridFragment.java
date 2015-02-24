package com.chfmr.listview.listviewvolley;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosfm on 23/02/15.
 */
public class LinhaDeOnibusGridFragment extends InternetFragment implements
        Response.Listener<JSONObject>, Response.ErrorListener {

    List<LinhaDeOnibus> mLinhaDeOnibus;
    GridView mGridView;
    TextView mTextMensagem;
    ProgressBar mProgressBar;
    ArrayAdapter<LinhaDeOnibus> mAdapter;

    boolean mIsRunning;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View layout = inflater.inflate(R.layout.fragment_linhas_grid, null);
        mProgressBar = (ProgressBar)layout.findViewById(R.id.progressBar);
        mTextMensagem = (TextView)layout.findViewById(android.R.id.empty);
        mGridView = (GridView)layout.findViewById(R.id.gridview);
        mGridView.setEmptyView(mTextMensagem);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mLinhaDeOnibus == null) {
            mLinhaDeOnibus = new ArrayList<LinhaDeOnibus>();
        }
        mAdapter = new LinhaDeOnibusGridAdapter(getActivity(), mLinhaDeOnibus);
        mGridView.setAdapter(mAdapter);

        if (!mIsRunning) {
            if (AppHttp.hasConect(getActivity())) {
                iniciarDownload();
                // iniciarDownload();
            } else {
                mTextMensagem.setText("Sem conexão");
            }
        } else {
            exibirProgress(true);
        }
    }

    public void iniciarDownload() {
        mIsRunning = true;
        exibirProgress(true);
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();

        JsonObjectRequest request =
                new JsonObjectRequest(
                        Request.Method.GET,    // Requisição via HTTP_GET
                        LinhaDeOnibus.LINHAS_ONIBUS_URL_JSON,  // url da requisição
                        null,  // JSONObject a ser enviado via POST
                        this,  // Response.Listener
                        this); // Response.ErrorListener
        queue.add(request);
    }

    private void exibirProgress(boolean exibir) {
        if (exibir) {
            mTextMensagem.setText("Baixando informações dos livros...");
        }
        mTextMensagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        mIsRunning = false;
        exibirProgress(false);
        mTextMensagem.setText("Falha ao obter livros");
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        mIsRunning = false;
        exibirProgress(false);
        try {
            List<LinhaDeOnibus> linhas = LinhaDeOnibus.readJsonLineBus(jsonObject);
            mLinhaDeOnibus.clear();
            mLinhaDeOnibus.addAll(linhas);
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
