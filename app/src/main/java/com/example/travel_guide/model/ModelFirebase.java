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

    //TODO::getAllPosts can use for the saved post in the device
    public void getAllPosts(Long lastUpdateDate, GetAllPostsListener listener) {

        db.collection(UserPost.COLLECTION_NAME).whereEqualTo("isDeleted", "false")
//              .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
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

                            // String postId = doc.getId();
                            // System.out.println("----------"+UserPost.create(doc.getData()));

                            userPost = UserPost.create(doc.getData()); //convert from json to Post
                            //System.out.println(userPost);

                            updateId(ids.get(i), userPost);
                            if (userPost != null) {
                                list.add(userPost);
                            }
                            i++;
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void getCategoryPosts(Long lastUpdateDate, String userId, String categoryName, String location, GetAllPostsListener listener) {
        //DocumentReference a = db.collection(UserPost.COLLECTION_NAME).document();
        String fieldKey, fieldVal;
        CollectionReference categoryReference = db.collection(UserPost.COLLECTION_NAME);
        if (categoryName.equals("userCreatePosts")) {
            fieldKey = "userId";
            fieldVal = userId;
        } else {
            fieldKey = "category";
            fieldVal = categoryName;//.whereEqualTo("location","NYC")
        }
        if (!location.equals("")) {
            Task<QuerySnapshot> q = categoryReference.whereEqualTo(fieldKey, fieldVal).whereEqualTo("location", location).whereEqualTo("isDeleted", "false")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        List<UserPost> list = new LinkedList<UserPost>();

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    UserPost userPost = UserPost.create(document.getData()); //convert from json to Post
                                    updateId(document.getId(), userPost);
                                    list.add(userPost);
                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                            listener.onComplete(list);
                        }
                    });
        } else {// for all user
            Task<QuerySnapshot> q = categoryReference.whereEqualTo(fieldKey, fieldVal).whereEqualTo("isDeleted", "false")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        List<UserPost> list = new LinkedList<UserPost>();

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    UserPost userPost = UserPost.create(document.getData()); //convert from json to Post
                                    updateId(document.getId(), userPost);
                                    list.add(userPost);
                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                            listener.onComplete(list);
                        }
                    });
        }
    }

    public void getUserSavedPost(String userId, List<String> userSavedPostLst, Long lastUpdateDate, GetAllPostsListener listener) {
        // CollectionReference postReference = db.collection(UserPost.COLLECTION_NAME);

        List<UserPost> list = new LinkedList<UserPost>();


//        for (String s : userSavedPostLst) {
//            CollectionReference categoryReference = db.collection(UserPost.COLLECTION_NAME);
//            //Task<QuerySnapshot> q = categoryReference.whereEqualTo("id", s) .whereGreaterThanOrEqualTo("updateDate",new Timestamp(lastUpdateDate,0))
//            Task<QuerySnapshot> q = categoryReference.whereEqualTo("id", s)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    System.out.println("inside +++++++++++++++++++++++++++");
//                                    UserPost userPost = UserPost.create(document.getData()); //convert from json to Post
//                                    updateId(document.getId(), userPost);
//                                    list.add(userPost);
//                                }
//                            }
//                            //listener.onComplete(list);
//                        }
//                    });listener.onComplete(list);
//        }
//        if (userSavedPostLst.size() == 0)
//            listener.onComplete(list);


//toDO:: ----------------------------------------------------------

            for (String s : userSavedPostLst)
            {
                db.collection(UserPost.COLLECTION_NAME)
                        .document(s)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                UserPost userPost = null;
                                if (task.isSuccessful() & task.getResult() != null) {
                                    DocumentSnapshot document = task.getResult();
                                    userPost = UserPost.create(task.getResult().getData());
                                    updateId(document.getId(), userPost);

                                    System.out.println("work to fetch");
                                    System.out.println(userPost.getId());
                                    list.add(userPost);
                                }listener.onComplete(list);
                            }
                        });
            }
            if(userSavedPostLst.size()==0)
                listener.onComplete(list);

    }

    public void updateId(String id, UserPost userPost) {
//        DocumentReference a = db.collection(UserPost.COLLECTION_NAME).document(id);
        // userPost.setId(a.getId());
        userPost.setId(id);
//        a.set(userPost);
    }

    public void addUserPost(UserPost userPost, Model.AddPostListener listener) {
        //TODO:: need to update Room
        System.out.println("userPost in add: " + userPost.getId());

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
                //.addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void updateUserPost(UserPost userPost, Model.AddPostListener listener) {
        //TODO:: need to update Room
        Map<String, Object> json = userPost.toJson();
        db.collection(UserPost.COLLECTION_NAME)
                .document(userPost.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void deletePostById(String postId, Model.DeletePostById listener) {

//        Map<String, Object> json = userPost.toJson();
//        db.collection(UserPost.COLLECTION_NAME)
//                .document(userPost.getId())
//                .set(json)
//                .addOnSuccessListener(unused -> listener.onComplete())
//                .addOnFailureListener(e -> listener.onComplete());

//        db.collection(UserPost.COLLECTION_NAME)
//                .document(postId)
//                .delete()
//                .addOnSuccessListener(unused -> listener.onComplete())
//                .addOnFailureListener(e -> listener.onComplete());
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

    public void createUserWithEmail(User userFromCode, Model.AddUserToFBListener listener) {
        String email = userFromCode.email;
        String password = userFromCode.password;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            userFromCode.setId(userId);
                            addUser(userFromCode, listener);
                            //updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            listener.onComplete(task.getException().toString());
                            // Toast.makeText(MyApplication.getContext().getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();

//                            updateUI(null);
                        }
                    }
                });
    }

    public void isUserIn(Model.OnCompleteGeneralListener listener) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            getUserById(user.getUid(), new Model.GetUserById() {
                @Override
                public void onComplete(User usr) {

                    usr.setId(user.getUid());
                    listener.onComplete(usr);
                }
            });
        }
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

                            //return user;
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            listener.onComplete(null);
                        }
                    }
                });
    }

    public void signOut() {

        mAuth.signOut();
    }

    public void deleteUser(Model.OnCompleteGeneralListener listener) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // connected user can delete itself
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User account deleted.");
                        }
                    }
                });
        listener.onComplete(null);
    }

    public void getUserIdFromFB(Model.GetUserId listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = null;
        if (user != null) {
            id = user.getUid();
        }
        listener.onComplete(id);
    }

    /**
     * Storage implementation
     */
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void saveImage(Bitmap imageBitmap, String imageName, String savePath, Model.SaveImageListener listener) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("/" + savePath + "/" + imageName); //TODO::to catch 2 types of images - user or post

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
