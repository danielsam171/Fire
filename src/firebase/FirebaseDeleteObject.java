package firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;

public class FirebaseDeleteObject {
    public static void main(String[] args) throws FileNotFoundException {
        Item item1 = new Item();
        // save item objec to firebase.
        new FirebaseDeleteObject().sa(item1);
    }

    private FirebaseDatabase firebaseDatabase;

    /**
         * inicialización de firebase.
*/
    private void initFirebase() throws FileNotFoundException {
        
        try {
            
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://matriz-21fcd-default-rtdb.firebaseio.com/")
                    
                    .setServiceAccount(new FileInputStream(new File("C:\\Users\\HP1000\\Desktop\\Proyectos_Java\\firebase\\matriz-21fcd-firebase-adminsdk-8wmfv-31e7ef4eb4.json ")))

                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("Conexión exitosa....");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
         
    }

    /**
     * Save item object in Firebase.
     * @param item 
     */
    private void sa(Item item) throws FileNotFoundException {
        if (item != null) {
            initFirebase();
            
            /* Get database root reference */
            DatabaseReference databaseReference = firebaseDatabase.getReference("/");
            
            /* Get existing child or will be created new child. */
            DatabaseReference childReference = databaseReference.child("item1");

            /**
             * The Firebase Java client uses daemon threads, meaning it will not prevent a process from exiting.
             * So we'll wait(countDownLatch.await()) until firebase saves record. Then decrement `countDownLatch` value
             * using `countDownLatch.countDown()` and application will continues its execution.
             */
            CountDownLatch countDownLatch = new CountDownLatch(1);
            childReference.setValue(null, new DatabaseReference.CompletionListener() {

                @Override
                public void onComplete(DatabaseError de, DatabaseReference dr) {
                    System.out.println("Registro guardado!");
                    // decrement countDownLatch value and application will be continues its execution.
                    countDownLatch.countDown();
                }
            });
            try {
                //wait for firebase to saves record.
                countDownLatch.await();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    void delete(Item item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}