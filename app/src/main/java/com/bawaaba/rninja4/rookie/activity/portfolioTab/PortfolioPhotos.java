//package com.example.rninja4.rookie.activity.portfolioTab;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
///**
// * Created by rninja4 on 7/11/17.
// */
//
//public class PortfolioPhotos  implements Parcelable{
//
//    private String mUrl;
//    private String mTitle;
//
//    public PortfolioPhotos(String url) {
//        mUrl = url;
//        mTitle = title;
//    }
//
//
//
//    protected PortfolioPhotos(Parcel in) {
//
//        mUrl = in.readString();
//        mTitle = in.readString();
//
//    }
//    public static final Creator<PortfolioPhotos> CREATOR = new Creator<PortfolioPhotos>() {
//        @Override
//        public PortfolioPhotos createFromParcel(Parcel in) {
//            return new PortfolioPhotos(in);
//        }
//
//        @Override
//        public PortfolioPhotos[] newArray(int size) {
//            return new PortfolioPhotos[size];
//        }
//    };
//    public String getUrl() {
//        return mUrl;
//    }
//
//    public void setUrl(String url) {
//        mUrl = url;
//    }
//
//    public String getTitle() {
//        return mTitle;
//    }
//
//    public void setTitle(String title) {
//        mTitle = title;
//    }
//    public static PortfolioPhotos [] getSpacePhotos() {
//
//        return new PortfolioPhotos[]{
//                new PortfolioPhotos("http://demo.rookieninja.com/assets/portfolio_img/")};
//
//        };
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int flags) {
//
//
//        parcel.writeString(mUrl);
//        parcel.writeString(mTitle);
//    }
//}
