/**
 * Developed By: Jaycel Estrellado - C20372876
 */

package com.example.timage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.timage.CalendarTask;
import com.example.timage.Model.ToDoModel;
import com.example.timage.R;
import com.example.timage.TimageManager;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private CalendarTask activity;
    private TimageManager db;

    public ToDoAdapter(TimageManager db, CalendarTask activity) {
        this.db = db;
        this.activity = activity;

    } // End of adapt

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        db.open();

        final ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask() + " --- Due: " + item.getDate());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //Log.i("Check This", String.valueOf(item.getId()));
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });

    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public int getItemCount(){
        return todoList.size();
    }

    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public Context getContext() {return activity;}

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

//    public void editItem(int position){
//
//        ToDoModel item = todoList.get(position);
//        Bundle bundle = new Bundle();
//        bundle.putInt("id", item.getId());
//        bundle.putString("task", item.getTask());
//        AddNewTask fragment = new AddNewTask();
//        fragment.setArgument(bundle);
//        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }

}
