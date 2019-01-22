package doh.nvbsp.imu.dugongbayani.lib.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import doh.nvbsp.imu.dugongbayani.R;
import doh.nvbsp.imu.dugongbayani.lib.models.Agency;

public class AgencyAdapter extends ArrayAdapter {

    public AgencyAdapter(@NonNull Context context, ArrayList<Agency> agencies) {
        super(context, 0, agencies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Agency agency = (Agency) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_title_subtitle, parent,false);
        }

        TextView txt1 = convertView.findViewById(R.id.lbl1);
        TextView txt2 = convertView.findViewById(R.id.lbl2);

        // TODO: 16/01/2019 Subtitle Text
//        txt1.setText(agency.getName());
//        String subText = "Table " + agency.getTable() + ", "+ agency.getSeats() + " seats";
//        txt2.setText(subText);

        return convertView;
    }

}
