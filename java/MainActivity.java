package com.example.mobilen1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilen1.adapters.PostAdapter;
import com.example.mobilen1.controllers.PostController;
import com.example.mobilen1.models.Post;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnLoadData;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private PostController postController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoadData = findViewById(R.id.btn_load_data);
        recyclerViewPosts = findViewById(R.id.recycler_view_posts);

        postController = new PostController(this);
        postAdapter = new PostAdapter(new ArrayList<>());

        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPosts.setAdapter(postAdapter);

        btnLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPosts();
            }
        });
    }

    private void loadPosts() {
        Toast.makeText(this, "Carregando posts...", Toast.LENGTH_SHORT).show();

        postController.loadPosts(new PostController.PostsLoadListener() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                postAdapter.setPosts(posts);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
