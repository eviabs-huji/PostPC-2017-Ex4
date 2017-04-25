package evia.huji.ac.il.ex4;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task, parent, false);
        }

        convertView.setBackgroundColor((position % 2 == 0) ? Color.BLUE : Color.RED);

        // Get the data item for this position
        Task task = getItem(position);

        // Lookup view for data population
        TextView taskText = (TextView) convertView.findViewById(R.id.task_text);

        // Populate the data into the template view using the data object
        taskText.setText(task.toString());

        // Return the completed view to render on screen
        return convertView;
    }
}