package com.example.webservices.api;

import com.example.webservices.model.Report;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // GET: Retrieve all monitoring reports
    @GET("reports")
    Call<List<Report>> getAllReports();

    // GET: Retrieve a specific report by ID
    @GET("reports/{id}")
    Call<Report> getReportById(@Path("id") int reportId);

    // POST: Create a new report entry (Using "posts" instead of "reports" so JSONPlaceholder accepts it)
    @POST("posts")
    Call<Report> createReport(@Body Report report);

    // DELETE: Remove a report by ID
    @DELETE("reports/{id}")
    Call<Void> deleteReport(@Path("id") int reportId);
}