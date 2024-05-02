package com.example.quetcccd;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quetcccd.Adapter.Khachmoi_Adapter;
import com.example.quetcccd.SQL.KhackMoiDAO;
import com.example.quetcccd.model.KhackMoi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Khachmoi_Activity2 extends AppCompatActivity {
    Button btnThem;
    RecyclerView recy;
    private String selectedItem_Gtinh;
    KhackMoiDAO dao;
    SwipeRefreshLayout refreshLayout;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khachmoi2);
        mapping();
        dao = new KhackMoiDAO(getApplicationContext());
        calendar = Calendar.getInstance();
        loadData();
        refreshLayout.setOnRefreshListener(()->{
            loadData();
            refreshLayout.setRefreshing(false);
        });
        btnThem.setOnClickListener(v-> showdialog());
    }
    private void loadData(){
        List<KhackMoi> list = dao.getAll();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recy.setLayoutManager(layoutManager);
        Khachmoi_Adapter adapter = new Khachmoi_Adapter(list,this);
        recy.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    private void mapping(){
        btnThem = findViewById(R.id.Khachmoi_btnThem);
        recy = findViewById(R.id.Khachmoi_recy);
        refreshLayout = findViewById(R.id.refresh_data_Khachmoi);
    }
    private void showdialog(){
        @SuppressLint("InflateParams") View custom = LayoutInflater.from(this).inflate(R.layout.dialog_them,null);
        Dialog d = new Dialog(this);
        d.setContentView(custom);
        d.setCanceledOnTouchOutside(false);

        Window window = d.getWindow();
        if (window == null) {
            return;
        }
        // set kích thước dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        d.show();
        String[] gioitinh = {"Nam", "Nữ"};
        EditText edt_cccd,edt_name,edt_time;
        Spinner spinner;
        Button btn_huy,btn_them;
        ImageView img_back;
        edt_cccd = d.findViewById(R.id.dialog_edt_cccd);
        edt_name = d.findViewById(R.id.dialog_edt_name);
        spinner = d.findViewById(R.id.dialog_spinner);
        btn_huy = d.findViewById(R.id.dialog_btn_huy);
        btn_them = d.findViewById(R.id.dialog_btn_them);
        img_back = d.findViewById(R.id.icon_back_in_doiMK);
        edt_time = d.findViewById(R.id.dialog_edt_time);
        edt_time.setEnabled(true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem_Gtinh = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(Khachmoi_Activity2.this, "Lựa chọn: " + selectedItem_Gtinh, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có lựa chọn nào được chọn
            }
        });
        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(edt_time);
            }
        });

        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_cccd.getText().toString().length() == 0){
                    Toast.makeText(Khachmoi_Activity2.this, "VUI LÒNG NHẬP SỐ CCCD", Toast.LENGTH_SHORT).show();
                }else if (edt_name.getText().toString().length() == 0){
                    Toast.makeText(Khachmoi_Activity2.this, "VUI LÒNG NHẬP HỌ TÊN", Toast.LENGTH_SHORT).show();

                }else  if (edt_time.getText().toString().length() == 0){
                    Toast.makeText(Khachmoi_Activity2.this, "VUI LÒNG NHẬP THỜI GIAN", Toast.LENGTH_SHORT).show();

                }else {
                    KhackMoi k = new KhackMoi();
                    k.setCCCD(edt_cccd.getText().toString().trim());
                    k.setTen(edt_name.getText().toString().trim());
                    k.setGioiTinh(selectedItem_Gtinh.trim());
                    k.setThoiGian(String.valueOf(calendar.getTime()));
                    if (dao.insert(k)>0){
                        Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
        img_back.setOnClickListener(view -> d.dismiss());
        btn_huy.setOnClickListener(view -> d.dismiss());
    }
    private void showDateTimePicker(EditText edt) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        showTimePicker(edt);
                    }
                },
                year, month, dayOfMonth
        );
        datePickerDialog.show();
    }

    private void showTimePicker(EditText edt) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        // Format và hiển thị thời gian đã chọn
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String formattedDateTime = sdf.format(calendar.getTime());
                        edt.setText(formattedDateTime);
                    }
                },
                hourOfDay, minute, true
        );
        timePickerDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}