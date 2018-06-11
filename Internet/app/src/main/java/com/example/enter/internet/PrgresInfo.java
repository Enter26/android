package com.example.enter.internet;

import android.os.Parcel;
import android.os.Parcelable;


//class to pack info about progress
 class ProgresInfo implements Parcelable {

    int Progres;
    Long Downloaded;

    public ProgresInfo(){
            this.Progres = 0;
            Downloaded=(long) 0;
    }
    protected ProgresInfo(Parcel in){
        Progres = in.readInt();
        if(in.readByte()==0)
            Downloaded=null;
        else Downloaded=in.readLong();
    }

    public static final Creator<ProgresInfo> CREATOR = new Creator<ProgresInfo>() {
        @Override
        public ProgresInfo createFromParcel(Parcel in) {
            return new ProgresInfo(in);
        }

        @Override
        public ProgresInfo[] newArray(int size) {
            return new ProgresInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Progres);
        if(Downloaded == null){
            dest.writeByte((byte)0);
        }else{
            dest.writeByte((byte)1);
            dest.writeLong(Downloaded);
        }
    }
}
