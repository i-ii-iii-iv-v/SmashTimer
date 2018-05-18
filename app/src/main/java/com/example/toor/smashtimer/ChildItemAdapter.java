package com.example.toor.smashtimer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChildItemAdapter extends RecyclerView.Adapter<ChildItemAdapter.ViewHolder>{
    ArrayList<Child> listItems;

    public ChildItemAdapter(ArrayList<Child> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    private Context context;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.childformat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Child childItem = listItems.get(position);

        holder.childName.setText(childItem.getid());

        holder.layout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TaskList_Activity.class);
                Log.e("child adapter", childItem.getid());
                intent.putExtra("childid", childItem.getid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView childName;
        public RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            childName = itemView.findViewById(R.id.childid);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
