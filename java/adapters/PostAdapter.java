package com.example.mobilen1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilen1.R;
import com.example.mobilen1.models.Post;
import com.example.mobilen1.models.Comment; // Importe o modelo de comentário

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtBody;
        private Button btnShowComments;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtBody = itemView.findViewById(R.id.txt_body);
            btnShowComments = itemView.findViewById(R.id.btn_show_comments);
        }

        public void bind(Post post) {
            txtTitle.setText(post.getTitle());
            txtBody.setText(post.getBody());

            btnShowComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Comment> comments = post.getComments();
                    if (comments != null && !comments.isEmpty()) {
                        StringBuilder commentsText = new StringBuilder();
                        for (Comment comment : comments) {
                            commentsText.append("Name: ").append(comment.getName()).append("\n")
                                    .append("Email: ").append(comment.getEmail()).append("\n")
                                    .append("Comment: ").append(comment.getBody()).append("\n\n");
                        }
                        Toast.makeText(itemView.getContext(), commentsText.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(itemView.getContext(), "Não há comentários para este post.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
