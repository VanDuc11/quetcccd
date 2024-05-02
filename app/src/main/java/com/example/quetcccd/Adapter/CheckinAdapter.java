package com.example.quetcccd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quetcccd.R;
import com.example.quetcccd.model.CheckIn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CheckinAdapter extends RecyclerView.Adapter<CheckinAdapter.ViewHolder> {
    private Context context;
    private List<CheckIn> list;
    public CheckinAdapter(Context context, List<CheckIn> list){
        this.context = context;
        this.list = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvCCCD,time,lable;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_check_in_name);
            tvCCCD = itemView.findViewById(R.id.item_check_in_CCCD);
            time = itemView.findViewById(R.id.item_check_in_time);
            lable = itemView.findViewById(R.id.item_check_in_lable);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckIn c = list.get(position);
        holder.tvName.setText(c.getName());
        holder.tvCCCD.setText(c.getCCCD());


        if (c.getCheckout() == null){
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            try {
                Date date = inputFormat.parse(c.getCheckin());
                outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
                 String outputDate = outputFormat.format(date);
                holder.time.setText(outputDate);
                holder.lable.setText("Check In");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            try {
                Date date = inputFormat.parse(c.getCheckout());
                outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
                String outputDate = outputFormat.format(date);
                holder.time.setText(outputDate);
                holder.lable.setText("Check Out");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public void filter(String searchText) {
        List<CheckIn> filteredList = new ArrayList<>();
        for (CheckIn item : list) {
            if (item.getCCCD().toLowerCase().contains(searchText)) {
                filteredList.add(item);
            }
        }
        setList(filteredList); // setData là phương thức để cập nhật dữ liệu của RecyclerView
    }
    public void setList(List<CheckIn> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
