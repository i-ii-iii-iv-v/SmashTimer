package com.example.toor.smashtimer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder>{

    ArrayList<Task> listItems;
    String username;
    public TaskItemAdapter(ArrayList<Task> listItems, Context context, String username) {
        this.username = username;
        this.listItems = listItems;
        this.context = context;
    }

    private Context context;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskformat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task taskItem = listItems.get(position);

        holder.textStartTime.setText(""+taskItem.getStartHour()+":"+Utility.minuteFormat(Integer.toString(taskItem.getStartMin())));
        holder.textEndTime.setText(""+taskItem.getEndHour()+":"+Utility.minuteFormat(Integer.toString(taskItem.getEndMin())));
        holder.taskName.setText(taskItem.toString());
        if(taskItem.getAlarm() == 0)
        {
            holder.onAlarm.setVisibility(View.GONE);
            holder.offAlarm.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.onAlarm.setVisibility(View.VISIBLE);
            holder.offAlarm.setVisibility(View.GONE);
        }
        final Task cpyTask = taskItem;
        holder.layout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Update_Task_Activity.class);
                intent.putExtra("pTaskName", cpyTask.toString());
                intent.putExtra("pStartHour", Integer.toString(cpyTask.getStartHour()));
                intent.putExtra("pStartMinute", Integer.toString(cpyTask.getStartMin()));
                intent.putExtra("pEndHour", Integer.toString(cpyTask.getEndHour()));
                intent.putExtra("pEndMinute", Integer.toString(cpyTask.getEndMin()));
                intent.putExtra("pDay", Integer.toString(cpyTask.getDay()));
                intent.putExtra("pAlarm", Integer.toString(cpyTask.getAlarm()));
                intent.putExtra("childId", cpyTask.getChildid());
                intent.putExtra("username", username);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textStartTime;
        public TextView textEndTime;
        public TextView taskName;
        public ImageView onAlarm;
        public ImageView offAlarm;
        public RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            textStartTime = itemView.findViewById(R.id.sTime);
            textEndTime = itemView.findViewById(R.id.eTime);
            taskName = itemView.findViewById(R.id.taskName);
            onAlarm = itemView.findViewById(R.id.imageAlarmOn);
            offAlarm = itemView.findViewById(R.id.imageAlarmOff);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
