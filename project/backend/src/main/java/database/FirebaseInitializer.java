package database;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FirebaseInitializer {

  private final String token;

  public FirebaseInitializer() throws IOException {
    // Load the service account key JSON file
    FileInputStream serviceAccount = new FileInputStream(""
        + "cs32reciperecommender-firebase-adminsdk-yerc6-3e8bc60286.json");

    // Authenticate a Google credential with the service account
    GoogleCredential googleCred = GoogleCredential.fromStream(serviceAccount);

    // Add the required scopes to the Google credential
    GoogleCredential scoped = googleCred.createScoped(
        Arrays.asList(
            "https://www.googleapis.com/auth/firebase.database",
            "https://www.googleapis.com/auth/userinfo.email"
        )
    );

// Use the Google credential to generate an access token
    scoped.refreshToken();
    this.token = scoped.getAccessToken();

  }

  public String getToken(){
    return this.token;
  }





// See the "Using the access token" section below for information
// on how to use the access token to send authenticated requests to the
// Realtime Database REST API.
}