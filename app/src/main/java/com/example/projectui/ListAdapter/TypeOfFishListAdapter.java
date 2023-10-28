package com.example.projectui.ListAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectui.R;
import com.example.projectui.dto.TypeOfFish;

import java.util.List;
import java.util.Objects;

public class TypeOfFishListAdapter extends BaseAdapter {

    Context context;
    List<TypeOfFish> typeOfFishList;
    LayoutInflater inflater;


    public TypeOfFishListAdapter(Context context, List<TypeOfFish> typeOfFishList) {
        this.context = context;
        this.typeOfFishList = typeOfFishList;
        inflater= LayoutInflater.from(context);
    }


    //Rows will be equal to the count of TypeOfFishList size
    @Override
    public int getCount() {
        return typeOfFishList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_type_of_fish_list_view, null);
        TextView txtView = (TextView) view.findViewById(R.id.textviewTOFA);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageIconTOFA);

        TypeOfFish typeOfFish = typeOfFishList.get(i);
        txtView.setText(typeOfFish.getTypeOfFishName());
        byte[] pic = typeOfFish.getTypeOfFishPictureBase64();

        if (Objects.nonNull(pic)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            imageView.setImageBitmap(bitmap);
        }
        return view;
    }
}
