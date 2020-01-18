package com.android.bloga.draggablelayout;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView postListView;
    List<String> postListItems = new ArrayList<String>();
    FrameLayout  commentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        commentFragment = findViewById(R.id.commentFragment);
        postListView = findViewById(R.id.postListView);

        postListItems.add("Post 1");
        postListItems.add("Post 2");
        postListItems.add("Post 3");
        postListItems.add("Post 4");
        postListItems.add("Post 5");
        postListItems.add("Post 6");
        postListItems.add("Post 7");
        postListItems.add("Post 8");
        postListItems.add("Post 9");
        postListItems.add("Post 10");
        postListItems.add("Post 11");
        postListItems.add("Post 12");
        postListItems.add("Post 13");
        postListItems.add("Post 14");
        postListItems.add("Post 15");
        postListItems.add("Post 16");
        postListItems.add("Post 17");
        postListItems.add("Post 18");
        postListItems.add("Post 19");
        postListItems.add("Post 20");

        postListView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, postListItems));

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                findViewById(R.id.root).setBackground(getResources().getDrawable(R.drawable.blur_bg));

                Fragment fr = new CommentsFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.commentFragment, fr);
                fragmentTransaction.commit();


                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(commentFragment, "y", commentFragment.getHeight(), 0);
                objectAnimator.setDuration(300);
                objectAnimator.start();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
