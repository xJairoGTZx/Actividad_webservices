package com.example.webservices;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.webservices.api.ApiService;
import com.example.webservices.api.RetrofitClient;
import com.example.webservices.model.Report;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvResult = findViewById(R.id.tv_result);

        // Initialize API service instance
        apiService = RetrofitClient.getApiService();

        // Perform GET request
        fetchReportsFromServer();
        
        // Perform POST request to demonstrate expected results
        createReportOnServer();
    }

    private void fetchReportsFromServer() {
        Call<List<Report>> call = apiService.getAllReports();

        // Enqueue executes request asynchronously on a background thread
        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Report> reports = response.body();
                    Log.d("API_SUCCESS", "Retrieved " + reports.size() + " records.");
                    Toast.makeText(MainActivity.this, 
                        "Successfully loaded " + reports.size() + " items.", 
                        Toast.LENGTH_SHORT).show();
                    
                    if (!reports.isEmpty()) {
                        tvResult.setText("Loaded " + reports.size() + " items.\nFirst item ID: " + reports.get(0).getId());
                    } else {
                        tvResult.setText("Loaded 0 items.");
                    }
                } else {
                    Log.e("API_ERROR", "Server returned HTTP status: " + response.code());
                    Toast.makeText(MainActivity.this, 
                        "Server error code: " + response.code(), 
                        Toast.LENGTH_SHORT).show();
                    tvResult.setText("Server error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                Log.e("NETWORK_FAILURE", "Connection failed: " + t.getMessage());
                Toast.makeText(MainActivity.this, 
                    "Network failure. Please check your internet connection.", 
                    Toast.LENGTH_LONG).show();
                tvResult.setText("Network failure.");
            }
        });
    }

    private void createReportOnServer() {
        Report newReport = new Report("Colima", "High fever, cough", "High");
        Call<Report> call = apiService.createReport(newReport);

        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Report createdReport = response.body();
                    Log.d("API_SUCCESS_POST", "Created report with ID: " + createdReport.getId());
                    Toast.makeText(MainActivity.this, 
                        "Successfully created report with ID: " + createdReport.getId(), 
                        Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_ERROR_POST", "Server returned HTTP status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                Log.e("NETWORK_FAILURE_POST", "POST connection failed: " + t.getMessage());
            }
        });
    }
}