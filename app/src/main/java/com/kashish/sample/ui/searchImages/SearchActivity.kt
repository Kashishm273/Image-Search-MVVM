package com.kashish.sample.ui.searchImages

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.kashish.sample.R
import com.kashish.sample.data.local.AppDatabase
import com.kashish.sample.utils.Constants
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), RecentSearchAdapter.SearchInterface, View.OnClickListener {

    private lateinit var database: AppDatabase
    private lateinit var recentSearchAdapter: RecentSearchAdapter
    private var recentSearchList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
        setListeners()
        setRecentSearchAdapter()
    }

    private fun setListeners() {
        ivSearch.setOnClickListener(this)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                filterSearch(s)
            }

        })
    }

    private fun filterSearch(s: CharSequence) {
        val temp = ArrayList<String>()
        for (d in recentSearchList) {
            if (d.toLowerCase().contains(s)) {
                temp.add(d)
            }
            recentSearchAdapter.updateList(temp)
        }
    }

    private fun init() {
        database = AppDatabase.getInstance(this)
    }

    override fun onSearchTextClick(searchText: String) {
        val intent = Intent()
        intent.putExtra(Constants.SEARCH_TEXT, searchText)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    private fun setRecentSearchAdapter() {
        recentSearchList = database.userDao().loadSearchText()
        rvRecentSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false)
        recentSearchAdapter = RecentSearchAdapter(this, recentSearchList, this)
        rvRecentSearch.adapter = recentSearchAdapter
        rlRecentSearch.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        if (!etSearch.text.toString().trim().isEmpty())
            onSearchTextClick(etSearch.text.toString().trim())
    }
}
