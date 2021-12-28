package com.example.travel_guide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.UserPost;

import java.util.List;

public class PostListRvFragment extends Fragment {

    List<UserPost> postList;
    MyAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_list_rv, container, false);
        RecyclerView list = view.findViewById(R.id.post_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        postList = Model.instance.getAllPosts();

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String postId = postList.get(position).getId();
                Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionPostListRvFragmentToPostPage(postId));
            }
        });

        return view;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView postName;
        TextView type;
        TextView location;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            postName = itemView.findViewById(R.id.postname_listrow_tv);
            type = itemView.findViewById(R.id.type_listrow_tv);
            location = itemView.findViewById(R.id.location_listrow_tv);
            imageView = itemView.findViewById(R.id.post_picture_listrow_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v, pos);
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view, listener);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            UserPost post = postList.get(position);

            holder.postName.setText(post.getName());
            holder.type.setText(post.getType());
            holder.location.setText(post.getLocation());
          //  holder.imageView.setImageResource(post.getUserProfile());

            //TODO:: maybe i can save post id here instead of subtract it twice
           //  String post_id = post.getId();
        }

        @Override
        public int getItemCount() {
            if(postList == null)
                return 0;
            return postList.size();
        }
    }

}