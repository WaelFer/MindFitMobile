package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.mindfit.app.adapters.MoodStatsAdapter;
import com.mindfit.app.api.ApiClient;
import com.mindfit.app.models.MoodStat;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsActivity extends AppCompatActivity implements MoodStatsAdapter.OnMoodStatClickListener {
    private PieChart moodChart;
    private RecyclerView rvMoodStats;
    private MoodStatsAdapter adapter;
    private List<MoodStat> moodStats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        initViews();
        setupRecyclerView();
        loadMoodStats();
    }

    private void initViews() {
        moodChart = findViewById(R.id.moodChart);
        rvMoodStats = findViewById(R.id.rvMoodStats);

        // Configure pie chart
        moodChart.getDescription().setEnabled(false);
        moodChart.setDrawHoleEnabled(true);
        moodChart.setHoleRadius(58f);
        moodChart.setTransparentCircleRadius(61f);
        moodChart.setDrawCenterText(true);
        moodChart.setCenterText("Mood\nDistribution");
        moodChart.setRotationEnabled(false);
    }

    private void setupRecyclerView() {
        adapter = new MoodStatsAdapter(moodStats, this);
        rvMoodStats.setLayoutManager(new LinearLayoutManager(this));
        rvMoodStats.setAdapter(adapter);
    }

    private void loadMoodStats() {
        ApiClient.getInstance().getMoodStats().enqueue(new Callback<List<MoodStat>>() {
            @Override
            public void onResponse(Call<List<MoodStat>> call, Response<List<MoodStat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moodStats.clear();
                    moodStats.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    updatePieChart();
                }
            }

            @Override
            public void onFailure(Call<List<MoodStat>> call, Throwable t) {
                Toast.makeText(StatsActivity.this, 
                    "Failed to load stats: " + t.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePieChart() {
        List<PieEntry> entries = new ArrayList<>();
        int[] colors = new int[moodStats.size()];
        
        for (int i = 0; i < moodStats.size(); i++) {
            MoodStat stat = moodStats.get(i);
            entries.add(new PieEntry(stat.getPercentage(), stat.getMoodName()));
            colors[i] = stat.getColor();
        }

        PieDataSet dataSet = new PieDataSet(entries, "Mood Distribution");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        moodChart.setData(data);
        moodChart.invalidate();
    }

    @Override
    public void onMoodStatClick(MoodStat moodStat) {
        Intent intent = new Intent(this, MoodDetailsActivity.class);
        intent.putExtra("mood", moodStat.getMoodName());
        startActivity(intent);
    }
}
