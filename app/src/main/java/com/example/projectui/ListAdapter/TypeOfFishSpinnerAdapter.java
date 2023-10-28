package com.example.projectui.ListAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectui.R;
import com.example.projectui.dto.TypeOfFish;

import java.util.List;
import java.util.Objects;

public class TypeOfFishSpinnerAdapter extends ArrayAdapter<TypeOfFish> {

    Context context;
    List<TypeOfFish> typeOfFishList;

    public TypeOfFishSpinnerAdapter(Context context, List<TypeOfFish> typeOfFishList) {
        super(context, R.layout.createfishringmade_fragment, typeOfFishList);
        this.context = context;
        this.typeOfFishList = typeOfFishList;
    }

    // Override these methods and instead return our custom view (with image and text)
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // Function to return our custom View (View with an image and text)
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.typeoffishspinneradapter, parent, false);

        // Image and TextViews
        TextView fishName = row.findViewById(R.id.text);
        ImageView ivFishPicture = row.findViewById(R.id.img);
        TypeOfFish currentTypeOfFish = typeOfFishList.get(position);

        // Get flag image from drawables folder
        byte[] pic = currentTypeOfFish.getTypeOfFishPictureBase64();

        if (Objects.nonNull(pic)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            ivFishPicture.setImageBitmap(bitmap);
        }

        //Set state abbreviation and state flag
        fishName.setText(currentTypeOfFish.getTypeOfFishName());

        return row;
    }
}
