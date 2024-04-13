package com.example.mobilen1.controllers;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilen1.models.Post;
import com.example.mobilen1.models.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PostController {

    private Context context;

    public PostController(Context context) {
        this.context = context;
    }

    public void loadPosts(final PostsLoadListener listener) {
        String url = "https://jsonplaceholder.typicode.com/posts";

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Post> posts = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                final Post post = new Post(
                                        jsonObject.getInt("id"),
                                        jsonObject.getInt("userId"),
                                        jsonObject.getString("title"),
                                        jsonObject.getString("body")
                                );
                                loadCommentsForPost(post.getId(), new CommentsLoadListener() {
                                    @Override
                                    public void onCommentsLoaded(List<Comment> comments) {
                                        post.setComments(comments);
                                        posts.add(post);
                                        if (posts.size() == response.length()) {
                                            listener.onPostsLoaded(posts);
                                        }
                                    }

                                    @Override
                                    public void onError(String errorMessage) {
                                        listener.onError("Erro ao carregar os comentários da API");
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Erro ao processar os dados");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Erro ao carregar os dados da API");
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public void loadCommentsForPost(int postId, final CommentsLoadListener listener) {
        String url = "https://jsonplaceholder.typicode.com/posts/" + postId + "/comments";

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Comment> comments = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Comment comment = new Comment(
                                        jsonObject.getInt("id"),
                                        jsonObject.getInt("postId"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("body")
                                );
                                comments.add(comment);
                            }
                            listener.onCommentsLoaded(comments);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Erro ao processar os comentários");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Erro ao carregar os comentários da API");
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public interface PostsLoadListener {
        void onPostsLoaded(List<Post> posts);
        void onError(String errorMessage);
    }

    public interface CommentsLoadListener {
        void onCommentsLoaded(List<Comment> comments);
        void onError(String errorMessage);
    }
}
