package Controllers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Models.User;
import Util.SharedPreferenceHelper;

import static java.util.Collections.singletonList;

public class UserInformationController {
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private Context mContext;
    private User u;
    private final StitchAppClient client;
    private final RemoteMongoClient mongoClient;
    private final RemoteMongoCollection<Document> coll;

    public UserInformationController(Context context) {
        mContext = context;
        mSharedPreferenceHelper = new SharedPreferenceHelper(context);
        client = Stitch.initializeDefaultAppClient("clustertest-sfown");

        mongoClient = client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        coll = mongoClient.getDatabase("jDatabase").getCollection("jCluster");
    }

    public void createUser(String fname, String lname, int weight, int heightFeet, int heightInches, int age, boolean smoker){
        u = new User(fname, lname, weight, heightFeet, heightInches, age, smoker);
        client.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(
                new Continuation<StitchUser, Task<RemoteInsertOneResult>>() {
                    @Override
                    public Task<RemoteInsertOneResult> then(@NonNull Task<StitchUser> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("STITCH", "Login failed!");
                            throw task.getException();
                        }

                        final Document updateDoc = new Document(
                                "owner_id",
                                task.getResult().getId()
                        );
                        updateDoc.put("userFirstName", u.fname);
                        updateDoc.put("userLastName", u.lname);
                        updateDoc.put("userHeightInches", u.heightFeet*12 + u.heightInches);
                        updateDoc.put("userWeight", u.weight);
                        updateDoc.put("userAge", u.age);
                        updateDoc.put("userSmoker", u.smoker);
                        return coll.insertOne(updateDoc);
                    }
                }
        ).continueWithTask(new Continuation<RemoteInsertOneResult, Task<List<Document>>>() {
            @Override
            public Task<List<Document>> then(@NonNull Task<RemoteInsertOneResult> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.e("STITCH", "Update failed!");
                    throw task.getException();
                }
                List<Document> docs = new ArrayList<>();
                return coll
                        .find(new Document("username", u.fname))
                        .limit(100)
                        .into(docs);
            }
        }).addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    Log.d("STITCH", "Found docs: " + task.getResult().toString());
                    return;
                }
                Log.e("STITCH", "Error: " + task.getException().toString());
                task.getException().printStackTrace();
            }
        });
    }
}
