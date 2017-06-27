package com.example.administrator.fnroad.search.presenter;

import com.example.administrator.fnroad.search.view.ISearchView;

/**
 * Created by Administrator on 2017/6/25 0025.
 */

public class SearchPresenterImpl {
    private ISearchView mSearchView;

    public SearchPresenterImpl(ISearchView searchView){
        this.mSearchView=searchView;
    }
}
