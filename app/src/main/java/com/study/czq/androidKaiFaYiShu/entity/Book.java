package com.study.czq.androidKaiFaYiShu.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public int bookId;
    public String bookName;
    public String path;
    public Book(){

    }
    public Book(int bookId,String bookName,String path) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.path = path;
    }
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        //相同类的属性变量，out和in读取顺序必须相同
        out.writeInt(bookId);
        out.writeString(bookName);
        out.writeString(path);
    }
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.
            Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
        path = in.readString();
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
