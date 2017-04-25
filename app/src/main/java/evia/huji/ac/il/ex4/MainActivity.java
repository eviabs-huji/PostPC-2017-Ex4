package evia.huji.ac.il.ex4;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<Task> tasks = new ArrayList<Task>();
    ListView listTasks;
    TaskAdapter taskAdapter;
    DatabaseOperations db = new DatabaseOperations(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // List
        listTasks = (ListView)findViewById(R.id.listTasks);
        taskAdapter = new TaskAdapter(this, tasks);
        listTasks.setLongClickable(true);
        listTasks.setAdapter(taskAdapter);

        listTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View view, final int index, long arg3) {
                showDeleteTaskPopUp(view, index);
                return false;
            }

        });


        // Fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewTaskPopUp(view);
            }
        });

        // DB
        Cursor CR = db.getInformation(db);
        if( CR != null && CR.moveToFirst() ){
            CR.moveToFirst();

            do {
                tasks.add(new Task(CR.getString(0), CR.getInt(1), new Date()));
            } while (CR.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showNewTaskPopUp(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Task");
        final View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_new_task, (ViewGroup) findViewById(android.R.id.content), false);
        final View outerView = view;
        final EditText inputText = (EditText) viewInflated.findViewById(R.id.input);
        final DatePicker inputDate = (DatePicker) viewInflated.findViewById(R.id.inputDate);
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String text = inputText.getText().toString();
                Date date = new Date(inputDate.getYear(), inputDate.getMonth(), inputDate.getDayOfMonth());

                if (!text.trim().equals("")) {

                    db.putInformation(db, text);

                    // Create task object and put it "on view".
                    Cursor CR = db.getInformation(db);
                    CR.moveToLast();
                    int task_id = CR.getInt(1);


                    taskAdapter.add(new Task(text, task_id, date));

                } else {
                    Snackbar.make(outerView, "Task should not be empty", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void showDeleteTaskPopUp(View view, int index) {

        final View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_delete_task, (ViewGroup) findViewById(android.R.id.content), false);
        final View outerView = view;
        final TextView taskText = (TextView) viewInflated.findViewById(R.id.taskText);
        taskText.setText(tasks.get(index).toString());
        final int indexToDelete = index;

        boolean isCall = tasks.get(index).toString().matches("#call\\d*");
        Toast.makeText(getApplicationContext(), tasks.get(index).toString() + "", Toast.LENGTH_SHORT).show();

        if (!isCall) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Task?");
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String ass = tasks.get(indexToDelete).getDueDate().toString();

                    db.deleteTask(db, tasks.get(indexToDelete).getID());
                    tasks.remove(indexToDelete);
                    taskAdapter.notifyDataSetChanged();
                    Snackbar.make(outerView, "Task deleted" + ass, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
        else {
            String callNumber =  tasks.get(index).toString().replace("#call", "");
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + callNumber));
            startActivity(intent);
        }

    }
}
