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

public class FirebasePushObject {

    public static void main(String[] args) {
        Item item = new Item();
        item.setId(1L);
        item.setName("celu_2");
        item.setPrice(100.156);

        // You can use List<Item> also.
        new FirebasePushObject().saveUsingPush(item);
    }

    private FirebaseDatabase firebaseDatabase;

    /**
     * inicialización de firebase.
     */
    private void initFirebase() {
        try {

            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://matriz-21fcd-default-rtdb.firebaseio.com/") // 
                    // .setDatabaseUrl("https://prueba2-97bb1.firebaseio.com/")
                    .setServiceAccount(new FileInputStream(new File("C:\\Users\\HP1000\\Desktop\\Proyectos_Java\\firebase\\matriz-21fcd-firebase-adminsdk-8wmfv-31e7ef4eb4.json")))
                    // .setServiceAccount(new FileInputStream(new File("/Users/nestor/Documents/pc/NetBeansProjects/firebase/src/firebase/prueba2-97bb1-firebase-adminsdk-74jjt-433b9eb033.json")))

                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("La conexión se realizo exitosamente...");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void saveUsingPush(Item item) {
        if (item != null) {
            initFirebase();

            /* Get database root reference */
            DatabaseReference databaseReference = firebaseDatabase.getReference("/");

            /* Get existing child or will be created new child. */
            DatabaseReference childReference = databaseReference.child("item1");

            /**
             * The Firebase Java client uses daemon threads, meaning it will not
             * prevent a process from exiting. So we'll
             * wait(countDownLatch.await()) until firebase saves record. Then
             * decrement `countDownLatch` value using
             * `countDownLatch.countDown()` and application will continues its
             * execution.
             */
            CountDownLatch countDownLatch = new CountDownLatch(1);

            /**
             * push() Add to a list of data in the database. Every time you push
             * a new node onto a list, your database generates a unique key,
             * like items/unique-item-id/data
             */
            childReference.push().setValue(item, new DatabaseReference.CompletionListener() {

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
}
