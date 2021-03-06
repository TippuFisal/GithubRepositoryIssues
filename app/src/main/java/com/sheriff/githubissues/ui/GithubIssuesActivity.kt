package com.sheriff.githubissues.ui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sheriff.githubissues.R
import com.sheriff.githubissues.adapter.GithubIsssueAdapter
import com.sheriff.githubissues.model.response.GithubIssuesResponse
import com.sheriff.githubissues.viewmodel.GithubIssuesViewModel
import kotlinx.android.synthetic.main.activity_github_issues.*

class GithubIssuesActivity : AppCompatActivity() {

    lateinit var githubIssuesViewModel: GithubIssuesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_issues)
        validateUserInput()
        loadAPIData()
    }

    /**
     * loadAPIData
     */
    private fun loadAPIData() {
        githubIssuesViewModel = ViewModelProvider(this).get(GithubIssuesViewModel::class.java)
        githubIssuesViewModel.getGithubIssuesListObserver()
            .observe(this, Observer<List<GithubIssuesResponse>> {
                when {
                    it != null -> { // check response is not null
                        initAdapter(it)
                    }
                    else -> { // onError
                        Toast.makeText(this, "Error in fetching the data", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    /**
     * validateUserInput
     */
    private fun validateUserInput() {
        btnGet.setOnClickListener {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(btnGet.getWindowToken(), 0)

            val ownerName = editOwnerName.text.toString()
            val repositoryName = editRepositoryName.text.toString()
            when {
                ownerName.isEmpty() -> {
                    Toast.makeText(this, "Enter Owner Name", Toast.LENGTH_SHORT).show()
                }
                repositoryName.isEmpty() -> {
                    Toast.makeText(this, "Enter Repository Name", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    githubIssuesViewModel.makeApiCall(ownerName, repositoryName)
                }
            }
        }

    }

    /**
     * initAdapter
     */
    private fun initAdapter(response: List<GithubIssuesResponse>) {
        rvGithubList.apply {
            val linearLayoutManager = LinearLayoutManager(this@GithubIssuesActivity)
            layoutManager = linearLayoutManager
            val githubIsssueAdapter = GithubIsssueAdapter(response)
            adapter = githubIsssueAdapter
        }
    }
}