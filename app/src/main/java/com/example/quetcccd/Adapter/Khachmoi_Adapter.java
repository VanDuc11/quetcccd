package com.example.quetcccd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quetcccd.R;
import com.example.quetcccd.model.KhackMoi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Khachmoi_Adapter extends RecyclerView.Adapter<Khachmoi_Adapter.ViewHolder> {

    private Context context;
    private List<KhackMoi> list;
    public Khachmoi_Adapter(  List<KhackMoi> list,Context context) {
        this.context = context;
        this.list = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCCCD,tv_name,tv_gtinh,tvTime;
        public ViewHolder(@NonNull View v) {
            super(v);
            tvCCCD = v.findViewById(R.id.item_khachmoi_cccd);
            tv_name = v.findViewById(R.id.item_khachmoi_hoten);
            tv_gtinh = v.findViewById(R.id.item_khachmoi_gioitinh);
            tvTime = v.findViewById(R.id.item_khachmoi_time);
        }
    }

    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khachmoi,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhackMoi k = list.get(position);
        holder.tvCCCD.setText(k.getCCCD());
        holder.tv_name.setText(k.getTen());
        holder.tv_gtinh.setText(k.getGioiTinh());
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = inputFormat.parse(k.getThoiGian());
            outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
            String formattedDate = outputFormat.format(date);
            holder.tvTime.setText(formattedDate);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
        @Override
    public int getItemCount() {
        return list.size();
    }

}