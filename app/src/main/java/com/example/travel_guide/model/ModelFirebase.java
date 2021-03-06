package com.example.travel_guide.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travel_guide.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public boolean isSignedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return (currentUser != null);
    }

    public interface GetAllPostsListener {
        void onComplete(List<UserPost> list);
    }

    public void initFireBaseAuto() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void getAllPosts(Long lastUpdateDate, GetAllPostsListener listener) {
        db.collection(UserPost.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<UserPost> list = new LinkedList<UserPost>();
                    if (task.isSuccessful()) {
                        UserPost userPost;
                        List<String> ids = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult())
                            ids.add(doc.getId());

                        int i = 0;
                        for (QueryDocumentSnapshot doc : task.getResult()) {

                            userPost = UserPost.create(doc.getData()); //convert from json to Post
                            updateId(ids.get(i), userPost);
                            if (userPost != null) {
                                if (userPost.isDeleted.equals("false"))
                                    list.add(userPost);
                            }
                            i++;
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void getAllPosts(GetAllPostsListener listener) {
        db.collection(UserPost.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    List<UserPost> list = new LinkedList<UserPost>();
                    if (task.isSuccessful()) {
                        UserPost userPost;
                        List<String> ids = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult())
                            ids.add(doc.getId());

                        int i = 0;
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            userPost = UserPost.create(doc.getData()); //convert from json to Post
                            updateId(ids.get(i), userPost);
                            if (userPost != null) {
                                if (userPost.isDeleted.equals("false"))
                                    list.add(userPost);
                            }
                            i++;
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void updateId(String id, UserPost userPost) {
        userPost.setId(id);
    }

    public void addUserPost(UserPost userPost, Model.AddPostListener listener) {
        Map<String, Object> json = userPost.toJson();
        db.collection(UserPost.COLLECTION_NAME)
                .add(json)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        updateId(task.getResult().getId(), userPost);
                        listener.onComplete();
                    }
                })
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

    public void addUser(User user, Model.AddUserToFBListener listener) {
        Map<String, Object> json = user.toJson();
        db.collection(User.COLLECTION_NAME)
                .document(user.getId())
                .set(json)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateUserId(user.getId(), user);
                        listener.onComplete("true");
                    }
                })
                .addOnFailureListener(e -> listener.onComplete("failed to add the user"));
    }

    public void updateUser(User user, Model.AddUserListener listener) {
        Map<String, Object> json = user.toJson();
        db.collection(User.COLLECTION_NAME)
                .document(user.getId())
                .set(json)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onComplete();
                    }
                })
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
                            updateUserId(userId, user);
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

    public void createUserWithEmail(String pass, User userFromCode, Model.AddUserToFBListener listener) {
        String email = userFromCode.email;
        String password = pass;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            userFromCode.setId(userId);
                            addUser(userFromCode, listener);
                            listener.onComplete("true");
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            listener.onComplete(task.getException().toString());
                        }
                    }
                });
    }

    public void userSignIn(String email, String password, Model.OnCompleteGeneralListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getUserById(user.getUid(), new Model.GetUserById() {
                                @Override
                                public void onComplete(User usr) {
                                    usr.setId(user.getUid());
                                    listener.onComplete(usr);
                                }
                            });
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            listener.onComplete(null);
                        }
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }

    public void getUserIdFromFB(Model.GetUserId listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = null;
        if (user != null) {
            id = user.getUid();
        }
        listener.onComplete(id);
    }

    public void getConnectedUser(Model.GetConnectedUser listener) {
        FirebaseUser userRB = mAuth.getCurrentUser();
        getUserById(userRB.getUid(), new Model.GetUserById() {
            @Override
            public void onComplete(User user) {
                listener.onComplete(user);
            }
        });
    }

    /**
     * Storage implementation
     */
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void saveImage(Bitmap imageBitmap, String imageName, String savePath, Model.SaveImageListener listener) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("/" + savePath + "/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            listener.onComplete(null);
        }).addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Uri downloadUrl = uri;
                listener.onComplete(downloadUrl.toString());
            });
        });
    }
}
