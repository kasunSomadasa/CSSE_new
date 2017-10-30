package com.example.kasun.busysms.taskCalendar.Model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kasun.busysms.R;
import com.example.kasun.busysms.taskCalendar.Helper.DateEx;
import com.example.kasun.busysms.taskCalendar.Helper.ReminderActivator;
import com.example.kasun.busysms.taskCalendar.UpdateTaskActivity;

import java.util.List;

/**
 * Created by Nishan on 10/27/2017.
 * @author Nishan
 * @version 2.3
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{
    private Context context;
    private List<Task> tasks;

    public DataAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Task task = tasks.get(holder.getAdapterPosition());
        holder.txtTaskName.setText(task.getTask_name());
        holder.txtTaskLocation.setText(task.getTask_location());
        holder.txtTaskDate.setText(task.getTask_date().toString());
        holder.txtTaskStartTime.setText(task.getTask_start().toString());
        final int taskId = task.getTask_id();

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                Task task = tasks.get(holder.getAdapterPosition());

                String description = task.getTask_description()+
                        "\nLocation: "+task.getTask_location()+
                        "\nOn: "+ DateEx.getDateString(task.getTask_date());
                if(task.is_all_day_task()){
                    description = description
                            +"\nIn whole day.";
                }else{
                    description = description
                            +"\nFrom: "+DateEx.getTimeString(task.getTask_start())+" to "+DateEx.getTimeString(task.getTask_end())+".";
                }
                if(task.getTask_participants().length() > 0){
                    description = description
                            +"\n"+task.getTask_participants()+" will be with you!";
                }

                alertDialog.setTitle(task.getTask_name())
                        .setCancelable(true)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setMessage(description);
                alertDialog.show();
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_add_remove, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.menuItem_editTask){
                            Intent intent = new Intent(context, UpdateTaskActivity.class);
                            intent.putExtra("oldTaskId", taskId);
                            context.startActivity(intent);
                            notifyItemChanged(holder.getAdapterPosition());
                        }else if(item.getItemId() == R.id.menuItem_removeTask){
                            if(Task.removeTaskFromDB(context, taskId)){
                                ReminderActivator.suspendReminder(context, tasks.get(holder.getAdapterPosition()));
                                tasks.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                Snackbar.make(view, "Task Removed", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                            else{
                                Snackbar.make(view, "Task not removed", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTaskName;
        private TextView txtTaskDate;
        private TextView txtTaskLocation;
        private TextView txtTaskStartTime;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            txtTaskName = (TextView) itemView.findViewById(R.id.txtTaskName);
            txtTaskDate = (TextView) itemView.findViewById(R.id.txtTaskDate);
            txtTaskLocation = (TextView) itemView.findViewById(R.id.txtTaskLocation);
            txtTaskStartTime = (TextView) itemView.findViewById(R.id.txtTaskStartTime);
        }
    }
}
