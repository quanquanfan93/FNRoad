package com.example.administrator.fnroad.search.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.main.model.Project;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity{
    private static final String TAG = "SearchActivity";
    private SearchView svSV;
    private ListView candidateLV;
    private String[] projectNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initAllView();
    }

    private void initAllView(){
//        svSV=(SearchView)findViewById(R.id.sv_search_keyword);
        try {
            svSV = (SearchView) findViewById(R.id.sv_search);
            candidateLV = (ListView) findViewById(R.id.lv_search_candidate);
            projectNames = getIntent().getStringArrayExtra("projectNames");
            int j = 0;
            while (j<projectNames.length&&projectNames[j] != null) {
                j++;
            }
            String[] paths2 = new String[j];
            for (int i = 0; i < j; i++) {
                paths2[i] = projectNames[i];
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, paths2);
            candidateLV.setAdapter(adapter);
            candidateLV.setTextFilterEnabled(true);
            candidateLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String projectName = (String) adapterView.getItemAtPosition(i);
                    svSV.setQuery(projectName, true);
                }
            });
            svSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent intent = new Intent();
                    intent.putExtra("projectName", query);
                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!TextUtils.isEmpty(newText)) {
                        candidateLV.setFilterText(newText);
                    } else {
                        candidateLV.clearTextFilter();
                    }
                    return true;
                }
            });
        }catch (Exception e){
            Log.e(TAG, "initAllView: "+e);
        }
    }

}
