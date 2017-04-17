package com.uvigo.learnfordown.learnfordown;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Juani on 15/04/2017.
 */

public class AzureUser {
    private MobileServiceClient mClient;

    /**
     * Mobile Service Table used to access data
     */
    private MobileServiceTable<Usuarios> nivelTable;
    AppCompatActivity app;


    public AzureUser(AppCompatActivity app) {
        this.app=app;

        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://learnfordown.azurewebsites.net", app);

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            // Get the Mobile Service Table instance to use

            nivelTable = mClient.getTable(Usuarios.class);

            initLocalStore().get();

            // Offline Sync
            //nivelTable = mClient.getSyncTable("ToDoItem", ToDoItem.class);


            // Load the items from the Mobile Service


            //refreshItemsFromTable();

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }
    }

    //Offline Sync
    /**
     * Mobile Service Table used to access and Sync data
     */
    //private MobileServiceSyncTable<ToDoItem> nivelTable;

    /**
     * Adapter to sync the items list with the view
     */




    /**
     * Mark an item as completed in the Mobile Service Table
     *
     * @param item
     *            The item to mark
     */
    public void checkItemInTable(Usuarios item) throws ExecutionException, InterruptedException {
        nivelTable.update(item).get();
    }


    public String addItem(String nombre,int edad) {
        if (mClient == null) {
            return null;
        }

        // Create a new item
        final Usuarios item = new Usuarios(nombre,edad);


        // Insert the new item
     //   AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
      //      @Override
       //     protected Void doInBackground(Void... params) {
                try {
                    final Usuarios entity = addItemInTable(item);
                    return entity.getmId();

                } catch (final Exception e) {
                    e.printStackTrace();
                    createAndShowDialogFromTask(e, "Error");
                    return null;
                }
       //         return null;
          //  }
        //};

       // runAsyncTask(task);


    }

    /**
     * Add an item to the Mobile Service Table
     *
     * @param item
     *            The item to Add
     */
    public Usuarios addItemInTable(Usuarios item) throws ExecutionException, InterruptedException {
        Usuarios entity = nivelTable.insert(item).get();
        return entity;
    }

    /**
     * Refresh the list with the items in the Table
     */
    public String refreshItemsFromTable(String nombre) {

        // Get the items that weren't marked as completed and add them in the
        // adapter
        final String nombreFinal= new String(nombre);
        //AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
         //   @Override
          //  protected Void doInBackground(Void... params) {

                try {

                    final List<Usuarios> results = refreshItemsFromMobileServiceTable(nombreFinal);
                    for(Usuarios a : results){
                        System.out.print(a.toString());
                        return results.get(0).getmId();
                    }
                    return results.get(0).getmId();
                    //Offline Sync
                    //final List<ToDoItem> results = refreshItemsFromMobileServiceTableSyncTable();


                } catch (final Exception e){
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            //}
     //   };

       // runAsyncTask(task);
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     */

    private List<Usuarios> refreshItemsFromMobileServiceTable(String nombre) throws ExecutionException, InterruptedException {
        return nivelTable.where().field("nombre").
                eq(nombre).execute().get();
    }

    //Offline Sync
    /**
     * Refresh the list with the items in the Mobile Service Sync Table
     */
    /*private List<ToDoItem> refreshItemsFromMobileServiceTableSyncTable() throws ExecutionException, InterruptedException {
        //sync the data
        sync().get();
        Query query = QueryOperations.field("complete").
                eq(val(false));
        return nivelTable.read(query).get();
    }*/

    /**
     * Initialize local storage
     * @return
     * @throws MobileServiceLocalStoreException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("mId", ColumnDataType.String);
                    tableDefinition.put("nombre", ColumnDataType.String);
                    tableDefinition.put("edad", ColumnDataType.Integer);

                    localStore.defineTable("Usuarios", tableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }

    //Offline Sync
    /**
     * Sync the current context and the Mobile Service Sync Table
     * @return
     */
    /*
    private AsyncTask<Void, Void, Void> sync() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MobileServiceSyncContext syncContext = mClient.getSyncContext();
                    syncContext.push().get();
                    nivelTable.pull(null).get();
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        return runAsyncTask(task);
    }
    */

    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }


    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
   ///     final AlertDialog.Builder builder = new AlertDialog.Builder(app);

   //     builder.setMessage(message);
  //      builder.setTitle(title);
//        builder.create().show();
    }

    /**
     * Run an ASync task on the corresponding executor
     * @param task
     * @return
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

}
