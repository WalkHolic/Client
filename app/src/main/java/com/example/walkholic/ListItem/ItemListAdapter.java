package com.example.walkholic.ListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.walkholic.R;

import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<SearchItem> {

    private static final String TAG = "dlgochan";

    private Context mContext;
    int mResource;

    public ItemListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SearchItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String address = getItem(position).getAddress();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.itemTitle);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.itemAddress);

        tvName.setText(name);
        tvAddress.setText(address);

        return convertView;
    }
}