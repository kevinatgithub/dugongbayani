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
import doh.nvbsp.imu.dugongbayani.lib.models.WinnerAward;

public class RegistrationAwardAdapter extends ArrayAdapter<WinnerAward> {

    public RegistrationAwardAdapter(@NonNull Context context, ArrayList<WinnerAward> winnerAwards) {
        super(context, 0, winnerAwards);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WinnerAward winnerAward = (WinnerAward) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_title_subtitle, parent,false);
        }

        TextView txt1 = convertView.findViewById(R.id.lbl1);
        TextView txt2 = convertView.findViewById(R.id.lbl2);

        if(winnerAward.getAward() != null){
            txt1.setText(winnerAward.getAward().getName());
        }

        if(winnerAward.getAgency() != null){
            txt2.setText(winnerAward.getRecipients());
        }

        return convertView;
    }
}
