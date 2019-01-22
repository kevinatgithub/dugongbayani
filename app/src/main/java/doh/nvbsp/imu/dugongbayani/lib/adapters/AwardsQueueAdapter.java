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
import doh.nvbsp.imu.dugongbayani.lib.models.SocketAward;
import doh.nvbsp.imu.dugongbayani.lib.models.WinnerAward;

public class AwardsQueueAdapter extends ArrayAdapter {

    public AwardsQueueAdapter(@NonNull Context context, ArrayList<SocketAward> winnerAwards) {
        super(context, 0, winnerAwards);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SocketAward winnerAward = (SocketAward) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.queue_row, parent,false);
        }

        TextView txt2 = convertView.findViewById(R.id.lbl_agency_name);
        TextView txt1 = convertView.findViewById(R.id.lbl_award);

        if(winnerAward.getAward() != null){
            txt2.setText(winnerAward.getAward());
        }

        if(winnerAward.getAgency() != null){
            txt1.setText(winnerAward.getAgency());
        }

        return convertView;
    }
}
