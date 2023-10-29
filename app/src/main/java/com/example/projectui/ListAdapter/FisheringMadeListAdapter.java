package com.example.projectui.ListAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.projectui.R;
import com.example.projectui.dto.FisheringMade;

import java.util.List;
import java.util.Objects;

public class FisheringMadeListAdapter  extends BaseAdapter {

    Context context;
    List<FisheringMade> fisheringMadeList;
    LayoutInflater inflater;

    public FisheringMadeListAdapter(Context context, List<FisheringMade> fisheringMadeList) {
        this.context = context;
        this.fisheringMadeList = fisheringMadeList;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fisheringMadeList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_customfisheringmadelistview, null);
        TextView txtView = (TextView) view.findViewById(R.id.textviewTOFA);
        ImageView imageViewFishPic = (ImageView) view.findViewById(R.id.imageIconFMLVPic);
        FisheringMade fisheringMade = fisheringMadeList.get(i);

        StringBuilder sb = new StringBuilder();
        sb.append("Fish specie: ")
                .append(fisheringMade.getTypeOfFish().getTypeOfFishName())
                .append(System.lineSeparator())
                .append("Weight KG: ").append(fisheringMade.getWeightKg())
                .append(System.lineSeparator())
                .append("Country registered: ").append(fisheringMade.getCountry().getUrl())
                .append(System.lineSeparator())
                .append("Region: ").append(fisheringMade.getRegion().getUrl())
                .append(System.lineSeparator())
                .append("Date registered: ").append(fisheringMade.getTimeLog().toLocalDate());

        txtView.setText(sb);
        byte[] pic = fisheringMade.getPictureOfFishBase64();

        if (Objects.nonNull(pic)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            imageViewFishPic.setImageBitmap(bitmap);
        }
        return view;
    }
}
