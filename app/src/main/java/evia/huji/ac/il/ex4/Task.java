package evia.huji.ac.il.ex4;

import java.util.Date;

public class Task {

    private String task;
    private int ID;
    private Date dueDate;

    public Task(String task, int id, Date date) {
        this.task = task;
        this.ID = id;
        this.dueDate = date;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTaskText() {
        return this.task;
    }

    public void setTaskText(String task) {
        this.task = task;
    }

    public int getID() {
        return this.ID;
    }

    @Override
    public String toString() {
        return "#" + this.task;
    }
}
