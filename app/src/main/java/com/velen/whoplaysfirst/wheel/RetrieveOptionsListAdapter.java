package com.velen.whoplaysfirst.wheel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.velen.whoplaysfirst.R;
import com.velen.whoplaysfirst.shared_prefs.SharedPrefsManager;

import java.util.List;

public class RetrieveOptionsListAdapter extends BaseAdapter{

    private List<String> names;
    private Context context;
    private OptionsToDeleteSelectedListener listener;

    public RetrieveOptionsListAdapter(Context context, List<String> names, OptionsToDeleteSelectedListener listener) {
        this.names = names;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.retrieve_options_item, parent, false);
        }

        final String name = names.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView nameTxt = convertView.findViewById(R.id.nameTxt);
        nameTxt.setText(name);

        ImageView deleteIcon = convertView.findViewById(R.id.deleteIcon);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeleteDialog(name);
            }
        });

        return convertView;
    }

    private void openDeleteDialog(final String name) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Are you sure you want to delete the list: " + name);
        alert.setTitle("Delete?");

        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                SharedPrefsManager.getInstance().removeWheelOptions(context, name);
                listener.onSelectedForDelete();
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
}
