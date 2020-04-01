package com.example.huangshan.tourist.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huangshan.R;
import com.example.huangshan.tourist.ui.activity.CustomActivity;
import com.example.huangshan.tourist.ui.activity.DeliciousActivity;
import com.example.huangshan.tourist.ui.activity.FiveJueActivity;
import com.example.huangshan.tourist.ui.activity.FiveShengActivity;
import com.example.huangshan.tourist.ui.activity.LegendActivity;
import com.example.huangshan.tourist.ui.activity.SpecialtyActivity;

import java.util.List;

public class ImpressionAdapter extends RecyclerView.Adapter<ImpressionAdapter.ImpressionViewHolder> {

    public static class ImpressionViewHolder extends RecyclerView.ViewHolder {
        ImageView impressionImg;
        TextView impressionText;

        public ImpressionViewHolder(@NonNull View itemView) {
            super(itemView);
            impressionImg = itemView.findViewById(R.id.impression_img);
            impressionText = itemView.findViewById(R.id.impression_text);
        }
    }

    private Context mContext;
    private int[] imgId;
    private String[] desStr;

    public ImpressionAdapter(Context mContext, int[] imgId, String[] desStr) {
        this.mContext = mContext;
        this.imgId = imgId;
        this.desStr = desStr;
    }

    @NonNull
    @Override
    public ImpressionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_impression_item, parent, false);
        ImpressionViewHolder holder = new ImpressionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImpressionViewHolder holder, int position) {
        holder.impressionImg.setImageResource(imgId[position]);
        holder.impressionText.setText(desStr[position]);

        holder.impressionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(mContext, FiveJueActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(mContext, FiveShengActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(mContext, SpecialtyActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(mContext, CustomActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(mContext, LegendActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(mContext, DeliciousActivity.class);
                        mContext.startActivity(intent);
                        break;
                        default:
                            break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgId.length;
    }
}
