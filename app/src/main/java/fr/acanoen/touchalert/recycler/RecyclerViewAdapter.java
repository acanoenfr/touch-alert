package fr.acanoen.touchalert.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.acanoen.touchalert.model.Alert;
import fr.acanoen.touchalert.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<Alert> alertList;

    public RecyclerViewAdapter(Context context, List<Alert> alertList) {
        this.context = context;
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.alert_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(alertList.get(i).getName());
        myViewHolder.date.setText(alertList.get(i).getDate());
        myViewHolder.image.setImageResource(alertList.get(i).getImage());
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView date;
        private ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.alertTitle);
            date = (TextView) itemView.findViewById(R.id.alertDate);
            image = (ImageView) itemView.findViewById(R.id.alertImage);
        }
    }

}

package fr.acanoen.touchalert.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.acanoen.touchalert.R;
import fr.acanoen.touchalert.model.Alert;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<Alert> alertList;

    public RecyclerViewAdapter(Context context, List<Alert> alertList) {
        this.context = context;
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.alert_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(alertList.get(i).getName());
        myViewHolder.date.setText(alertList.get(i).getDate());
        myViewHolder.image.setImageResource(alertList.get(i).getImage());
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView date;
        private ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.alertTitle);
            date = (TextView) itemView.findViewById(R.id.alertDate);
            image = (ImageView) itemView.findViewById(R.id.alertImage);
        }
    }

}
