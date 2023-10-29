package com.example.projectui.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ParcelableDTO implements Parcelable {
    private TypeOfFish typeOfFish;
    private boolean isParcelable = true;
    protected ParcelableDTO(Parcel in) {

    }

    public ParcelableDTO(TypeOfFish typeOfFish) {
        this.typeOfFish = typeOfFish;
    }

    public static final Creator<ParcelableDTO> CREATOR = new Creator<ParcelableDTO>() {
        @Override
        public ParcelableDTO createFromParcel(Parcel in) {
            return new ParcelableDTO(in);
        }

        @Override
        public ParcelableDTO[] newArray(int size) {
            return new ParcelableDTO[size];
        }
    };

    public TypeOfFish getTypeOfFish() {
        return typeOfFish;
    }

    public void setTypeOfFish(TypeOfFish typeOfFish) {
        this.typeOfFish = typeOfFish;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
    }

    public boolean isParcelable() {
        return isParcelable;
    }

    public void setParcelable(boolean parcelable) {
        isParcelable = parcelable;
    }
}
