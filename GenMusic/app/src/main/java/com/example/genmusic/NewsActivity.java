package com.example.genmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.genmusic.Model.Articles;
import com.example.genmusic.Model.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends Activity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText etQuery;
    Button btnSearch;
    final String API_KEY = "3ca9833c50c549418984c533190e967d";
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearch);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String country = getCountry();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            retrieveJson("",country, API_KEY);
        });
        retrieveJson("",country, API_KEY);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(!etQuery.getText().toString().equals("")) {
                    swipeRefreshLayout.setOnRefreshListener(() -> {
                            retrieveJson(etQuery.getText().toString(), country, API_KEY);
                });

                retrieveJson(etQuery.getText().toString(),country, API_KEY);
                }else{
                    swipeRefreshLayout.setOnRefreshListener(() -> {
                        retrieveJson("",country, API_KEY);
                    });
                    retrieveJson("", country, API_KEY);
                }
            }
        });
    }

    public void retrieveJson(String query, String country, String apiKey){


        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
        if(!etQuery.getText().toString().equals("")){
            call = ApiClient.getInstance().getApi().getSpecificData(query, apiKey);
        }else{
            call = ApiClient.getInstance().getApi().getHeadlines(country, apiKey);

        }
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(response.isSuccessful() && response.body().getArticles() != null){
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(NewsActivity.this,articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(NewsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}
