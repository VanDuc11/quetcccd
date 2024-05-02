package com.example.quetcccd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quetcccd.Adapter.CheckinAdapter;
import com.example.quetcccd.SQL.CheckinDAO;
import com.example.quetcccd.SQL.KhackMoiDAO;
import com.example.quetcccd.model.CheckIn;
import com.example.quetcccd.model.KhackMoi;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    Button btn, btn2, btnKhachmoi;
    EditText search;
    RecyclerView recy;
    BarcodeDetector barcodeDetector;
    CheckinDAO dao;
    KhackMoiDAO khackMoiDAO;
    SwipeRefreshLayout refreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mapping();
        dao = new CheckinDAO(this);
        khackMoiDAO = new KhackMoiDAO(this);
        // Khởi tạo và cấu hình BarcodeDetector
        load();
        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });
        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QrCode_Activity2.class));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Tạo Intent để chọn ảnh từ thư viện
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityIfNeeded(intent, 121);
            }
        });
        btnKhachmoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Khachmoi_Activity2.class));
            }
        });


    }

    private void load() {
        List<CheckIn> list = dao.getAll();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy.setLayoutManager(manager);
        CheckinAdapter adapter = new CheckinAdapter(this, list);
        recy.setAdapter(adapter);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                load();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().toLowerCase();
                adapter.filter(searchText); // Phương thức để lọc dữ liệu trong RecyclerView
            }
        });
    }


    private void mapping() {
        btn = findViewById(R.id.button);
        btn2 = findViewById(R.id.taianh);
        search = findViewById(R.id.search_edit_text);
        btnKhachmoi = findViewById(R.id.btn_khachmoi);
        recy = findViewById(R.id.recy);
        refreshLayout = findViewById(R.id.act_main_reslayout);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121 && resultCode == RESULT_OK && data != null) {
            // Nhận Uri của ảnh từ Intent
            Uri imageUri = data.getData();
            try {
                // Đổi Uri của ảnh thành đối tượng Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                // Sử dụng Detector để quét ảnh và lấy dữ liệu QR
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);

                // Xử lý kết quả
                check(barcodes);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void check(SparseArray<Barcode> barcodes) {
        LocalDate today = LocalDate.now();

        // Đặt thời điểm đầu của ngày hôm nay
        LocalDateTime startOfDay = today.atStartOfDay();

        // Đặt thời điểm cuối của ngày hôm nay
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        // Chuyển đổi sang múi giờ GMT+07:00
        ZonedDateTime startOfDayWithZone = startOfDay.atZone(ZoneId.of("GMT+07:00"));
        ZonedDateTime endOfDayWithZone = endOfDay.atZone(ZoneId.of("GMT+07:00"));

        // Định dạng chuỗi theo yêu cầu
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss 'GMT'XXX yyyy");
        for (int index = 0; index < barcodes.size(); index++) {
            Barcode barcode = barcodes.valueAt(index);
            String qrCodeValue = barcode.displayValue;
            Log.d("QRCode", "Value: " + qrCodeValue);
//                    // Xử lý dữ liệu QRCode ở đây
            String[] parts = qrCodeValue.split("\\|");

            // Tạo đối tượng KhachMoi từ dữ liệu phân tích
            String CCCD = parts[0];
            String hoTen = parts[2];


            KhackMoi km_model = khackMoiDAO.getCCCDAndTime(CCCD, startOfDayWithZone.format(formatter) , endOfDayWithZone.format(formatter) );
            if (km_model != null) {
                Log.d("TAG", "Khach mời có data");
            } else {
                Log.d("TAG", "No KhackMoi object found for CCCD and timestamp");
            }


            if (km_model != null) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = null;
                try {
                    date = inputFormat.parse(km_model.getThoiGian());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
                String dateDB = sdf.format(date);

                // ngàyhieenjnj tại
                Date thoiGianHienTai = new Date();
                String dateNow = sdf.format(thoiGianHienTai);
                if (dateDB.equals(dateNow)) {

                    CheckIn ck_model;
                    // Lấy ngày hôm nay

                    ck_model = dao.getCheckInByCCCD(CCCD, startOfDayWithZone.format(formatter) , endOfDayWithZone.format(formatter) );
                    if (ck_model != null) {
                        ck_model.setCheckout(String.valueOf(new Date()));

                        if (dao.update(ck_model) > 0)
                            Log.d("TAG", "ck_model check out pass");
                        else
                            Log.d("TAG", "ck_model check out fail");
                    } else {
                        CheckIn c = new CheckIn();
                        c.setCCCD(CCCD);
                        c.setName(hoTen);
                        c.setCheckin(new Date().toString());
                        // Lưu thông tin check-in vào cơ sở dữ liệu
                        if (dao.insert(c) > 0)
                            Log.d("TAG", "Check-in thành công");
                        else
                            Log.d("TAG", "Check-in không thành công");

                    }


                } else {
                    Log.d("TAG", "Không có lịch hôm nay");

                }
            } else {
                Log.d("TAG", "Không có dữ liệu");

            }
        }
    }
}