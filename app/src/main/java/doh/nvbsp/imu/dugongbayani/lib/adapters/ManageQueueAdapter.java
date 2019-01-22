package doh.nvbsp.imu.dugongbayani.lib.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import doh.nvbsp.imu.dugongbayani.R;
import doh.nvbsp.imu.dugongbayani.lib.JsonResponse.SocketQueue;
import doh.nvbsp.imu.dugongbayani.lib.models.SocketAward;
import doh.nvbsp.imu.dugongbayani.lib.models.WinnerAward;

public class ManageQueueAdapter extends ArrayAdapter {

    public ManageQueueAdapter(@NonNull Context context, ArrayList<SocketAward> winnerAwards) {
        super(context, 0, winnerAwards);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SocketAward winnerAward = (SocketAward) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.manage_queue_row, parent,false);
        }

        TextView lbl_agency_name = convertView.findViewById(R.id.lbl_agency_name);
        TextView lbl_award = convertView.findViewById(R.id.lbl_award);
        TextView lbl_recipients = convertView.findViewById(R.id.lbl_recipients);
        TextView lbl_seats = convertView.findViewById(R.id.lbl_seats);
        TextView lbl_table = convertView.findViewById(R.id.lbl_table);
        ImageView img_photo = convertView.findViewById(R.id.img_photo);

        lbl_agency_name.setText(winnerAward.getAgency());
        lbl_award.setText(winnerAward.getAward());
        lbl_recipients.setText(winnerAward.getRecipients());
        lbl_seats.setText(winnerAward.getSeats() + " seats alloted");
        lbl_table.setText("Table No. " +winnerAward.getTableAssignment());

        if(winnerAward.getPhoto() != null){

            String base64Image = winnerAward.getPhoto();
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img_photo.setImageBitmap(decodedByte);
        }

        return convertView;
    }
}
