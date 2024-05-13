package com.example.exsearch;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private MyExpandableListAdapter expandableListAdapter;
    private List<String> groupList;
    private HashMap<String, List<String>> childList;
    private List<String> originalGroupList;
    private HashMap<String, List<String>> originalChildList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
//        actionBar.setCustomView(R.menu.menu);

        // Get the search view from the custom ActionBar layout
        View customActionBarView = actionBar.getCustomView();
        EditText searchEditText = customActionBarView.findViewById(R.id.search_edit_text);

        // Set a listener for search queries
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Handle the search query
                    String query = searchEditText.getText().toString().trim();
                    performSearch(query);
                    return true;
                }
                return false;
            }
        });

        // Initialize the ExpandableListView and its data
        expandableListView = findViewById(R.id.expandable_list_view);
        prepareData();
        expandableListAdapter = new MyExpandableListAdapter(this, groupList, childList);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void prepareData() {
        // Prepare the original group and child data
        groupList = new ArrayList<>();
        childList = new HashMap<>();

        groupList.add("Group 1");
        groupList.add("Group 2");
        groupList.add("Group 3");

        List<String> child1 = new ArrayList<>();
        child1.add("Child 1 of Group 1");
        child1.add("Child 2 of Group 1");
        child1.add("Child 3 of Group 1");

        List<String> child2 = new ArrayList<>();
        child2.add("Child 1 of Group 2");
        child2.add("Child 2 of Group 2");
        child2.add("Child 3 of Group 2");

        List<String> child3 = new ArrayList<>();
        child3.add("Child 1 of Group 3");
        child3.add("Child 2 of Group 3");
        child3.add("Child 3 of Group 3");

        childList.put(groupList.get(0), child1);
        childList.put(groupList.get(1), child2);
        childList.put(groupList.get(2), child3);

        // Store the original data for filtering
        originalGroupList = new ArrayList<>(groupList);
        originalChildList = new HashMap<>(childList);
    }

//    public void prepareData(){
//        String[] abbreviations = getResources().getStringArray(R.array.abbreviations);
//        String[] acronyms = getResources().getStringArray(R.array.acronyms);
//
//        originalGroupList = new ArrayList<>();
//        originalChildList = new HashMap<>();
//
//        for(int i=0; i<abbreviations.length; i++){
//            originalGroupList.add(abbreviations[i]);
//
//            List<String> child = new ArrayList<>();
//            child.add(acronyms[i]);
//
//            originalChildList.put(originalGroupList.get(i),child);
//        }
//    }

    private void performSearch(String query) {
        groupList.clear();
        childList.clear();

        if (query.isEmpty()) {
            // Restore the original data when the query is empty
            groupList.addAll(originalGroupList);
            childList.putAll(originalChildList);
        } else {
            for (String group : originalGroupList) {
                List<String> originalChildItemList = originalChildList.get(group);
                List<String> filteredChildList = new ArrayList<>();

                for (String child : originalChildItemList) {
                    if (child.toLowerCase().contains(query.toLowerCase())) {
                        filteredChildList.add(child);
                    }
                }

                if (!filteredChildList.isEmpty()) {
                    groupList.add(group);
                    childList.put(group, filteredChildList);
                }
            }
        }

        expandableListAdapter.notifyDataSetChanged();
    }


    // Rest of the code...
}
