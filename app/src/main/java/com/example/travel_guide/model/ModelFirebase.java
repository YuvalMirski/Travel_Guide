package com.example.travel_guide.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public interface GetAllPostsListener {
        void onComplete(List<UserPost> list);
    }

    public void getAllPosts(Long lastUpdateDate, GetAllPostsListener listener) {

        db.collection(UserPost.COLLECTION_NAME)
                //.whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<UserPost> list = new LinkedList<UserPost>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            UserPost userPost = UserPost.create(doc.getData());
                             updateId(lastUpdateDate, doc.getId(), userPost);
                            if (userPost != null) {
                                list.add(userPost);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void updateId(Long lastUpdateDate, String id, UserPost userPost) {
        DocumentReference a = db.collection(UserPost.COLLECTION_NAME).document(id);
        userPost.setId(a.getId());
        a.set(userPost);
    }

    public void addUserPost(UserPost userPost, Model.AddPostListener listener) {
        System.out.println("userPost in add: " + userPost.getId());

        Map<String, Object> json = userPost.toJson();
        db.collection(UserPost.COLLECTION_NAME)
                .add(json)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Long a = new Long(0);
                        updateId(a, task.getResult().getId(), userPost);
                        listener.onComplete();
                    }
                })
                //.addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void updateUserPost(UserPost userPost, Model.AddPostListener listener) {
        Map<String, Object> json = userPost.toJson();
        db.collection(UserPost.COLLECTION_NAME)
                .document(userPost.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void deletePostById(String postId, Model.DeletePostById listener) {
        db.collection(UserPost.COLLECTION_NAME)
                .document(postId)
                .delete()
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void getPostById(String postId, Model.GetPostById listener) {

        db.collection(UserPost.COLLECTION_NAME)
                .document(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        UserPost userPost = null;
                        if (task.isSuccessful() & task.getResult() != null) {
                            userPost = UserPost.create(task.getResult().getData());
                        }
                        listener.onComplete(userPost);
                    }
                });
    }


    //------------------------------------USER------------------------------------//

    public void addUser(User user, Model.AddUserListener listener) {
        Map<String, Object> json = user.toJson();
        db.collection(User.COLLECTION_NAME)
                .add(json)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful() & task.getResult() != null) {
                            updateUserId(task.getResult().getId(), user);
                        }
                        listener.onComplete();
                    }
                })
                //.addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void updateUser(User user, Model.AddUserListener listener) {
        Map<String, Object> json = user.toJson();
        db.collection(User.COLLECTION_NAME)
                .document(user.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());

    }


    public void deleteUserById(String userId, Model.DeleteUserById listener) {
        db.collection(User.COLLECTION_NAME)
                .document(userId)
                .delete()
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }


    public void getUserById(String userId, Model.GetUserById listener) {

        db.collection(User.COLLECTION_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = null;
                        if (task.isSuccessful() & task.getResult() != null) {
                            user = User.create(task.getResult().getData());
                        }
                        listener.onComplete(user);
                    }
                });
    }

    public void updateUserId(String id, User user) {
        DocumentReference documentReference = db.collection(User.COLLECTION_NAME).document(id);
        user.setId(documentReference.getId());
        documentReference.set(user);
    }
}
