package com.example.huangshan.tourist.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huangshan.R;
import com.example.huangshan.admin.bean.MyPOI;
import com.example.huangshan.tourist.bean.SpinnerItemWithImg;

import java.util.ArrayList;
import java.util.List;

public class SpinnerItemWithImgAdapter extends BaseAdapter {

    private Context context;
    private List<SpinnerItemWithImg> spinnerItemWithImgs = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public SpinnerItemWithImgAdapter(Context context,List<SpinnerItemWithImg> spinnerItemWithImgs) {
        this.context = context;
        this.spinnerItemWithImgs = spinnerItemWithImgs;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return spinnerItemWithImgs.size();
    }

    @Override
    public SpinnerItemWithImg getItem(int position) {
        return spinnerItemWithImgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_spinner_with_img_item, null);
            new SpinnerWithImgViewHolder(convertView);
        }
        SpinnerWithImgViewHolder viewHolder = (SpinnerWithImgViewHolder) convertView.getTag();
        viewHolder.img.setImageResource(getItem(position).getImg());
        viewHolder.text.setText(getItem(position).getText());
        return convertView;
    }

    public static class SpinnerWithImgViewHolder{
        ImageView img;
        TextView text;
        public SpinnerWithImgViewHolder(View convertView){
            img = (ImageView)convertView.findViewById(R.id.spinner_with_img_img);
            text = (TextView)convertView.findViewById(R.id.spinner_with_img_text);
            convertView.setTag(this);
        }
    }
}
