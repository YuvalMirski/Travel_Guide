package com.example.travel_guide.view.PostList;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travel_guide.R;
import com.example.travel_guide.model.Model;
import com.example.travel_guide.model.User;
import com.example.travel_guide.model.UserPost;
import com.squareup.picasso.Picasso;

public class PostListRvFragment extends Fragment {
    PostListRvViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    String categoryName, userId, locationName;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostListRvViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_list_rv, container, false);

        userId = PostListRvFragmentArgs.fromBundle(getArguments()).getUserId();
        categoryName = PostListRvFragmentArgs.fromBundle(getArguments()).getCategoryName();
        locationName = PostListRvFragmentArgs.fromBundle(getArguments()).getLocationName();

        viewModel.demoCtor(categoryName, userId, locationName);

        Model.instance.refreshCategoryPage(categoryName, userId, locationName);
        swipeRefresh = view.findViewById(R.id.post_list_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshCategoryPage(categoryName, userId, locationName));

        RecyclerView list = view.findViewById(R.id.post_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getCategoryPostList().getValue().get(position).getId();
            Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionPostListRvFragmentToPostPage(postId, userId));
        });

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> refresh());
        viewModel.getCategoryPostList().observe(getViewLifecycleOwner(), userPosts -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getPostListLoadingState().getValue() == Model.PostListLoadingState.loading);

        Model.instance.getPostListLoadingState().observe(getViewLifecycleOwner(), postListLoadingState -> {
            if (postListLoadingState == Model.PostListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }
        });
        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView postName, category, location, userName;
        ImageView userAvatar, postImg;
        ImageButton likeImg;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            postName = itemView.findViewById(R.id.postname_listrow_tv);
            category = itemView.findViewById(R.id.category_listrow_tv);
            location = itemView.findViewById(R.id.location_listrow_tv);
            postImg = itemView.findViewById(R.id.post_picture_listrow_imv);
            likeImg = itemView.findViewById(R.id.postRow_save_imb);
            userName = itemView.findViewById(R.id.userName_listrow_tv);
            userAvatar = itemView.findViewById(R.id.userNameAvatar_listRow_imv);

            likeImg.setOnClickListener(v -> likeImgAction(v));

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });
        }

        private void likeImgAction(View v) {
            int position = getAdapterPosition();
            String postId = viewModel.getCategoryPostList().getValue().get(position).getId();

            if (!viewModel.getUserLiveData().getValue().getLstSaved().contains(postId)) //add saved post
            {
                viewModel.getUserLiveData().getValue().getLstSaved().add(postId);
                User u = viewModel.userLiveData.getValue();
                Model.instance.updateUser(u, () -> likeImg.setImageResource(R.drawable.ic_baseline_bookmark_remove_24));
            } else {
                viewModel.getUserLiveData().getValue().getLstSaved().remove(postId);
                User u = viewModel.userLiveData.getValue();
                Model.instance.updateUser(u, () -> likeImg.setImageResource(R.drawable.ic_baseline_saved));
            }
        }

        public void bind(UserPost post) {
            postName.setText(post.getName());
            String categoryUserPost = post.getCategory().substring(0, 1).toUpperCase() + post.getCategory().substring(1);
            category.setText(categoryUserPost);
            location.setText(post.getLocation());
            postImg.setImageResource(R.drawable.avatar);

            if (post.getPostImgUrl() != null) {
                Picasso.get()
                        .load(post.getPostImgUrl())
                        .into(postImg);
            }

            if (viewModel.getUserLiveData().getValue().getLstSaved().contains(post.getId())) {
                likeImg.setImageResource(R.drawable.ic_baseline_bookmark_remove_24);
                Model.instance.deleteSaveFromRoom(post);
            } else {
                likeImg.setImageResource(R.drawable.ic_baseline_saved);
            }

            userName.setText(viewModel.getUserLiveData().getValue().getUserName());
            if (viewModel.getUserLiveData().getValue().getAvatarUrl() != null) {
                Picasso.get()
                        .load(viewModel.getUserLiveData().getValue().getAvatarUrl())
                        .into(userAvatar);
            }

            Model.instance.getUserById(post.getUserId(), user -> {
                userName.setText(user.getUserName());
                if (user.getAvatarUrl() != null) {
                    Picasso.get()
                            .load(user.getAvatarUrl())
                            .into(userAvatar);
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
            UserPost post = viewModel.getCategoryPostList().getValue().get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getCategoryPostList().getValue() == null)
                return 0;
            return viewModel.getCategoryPostList().getValue().size();
        }
    }
}