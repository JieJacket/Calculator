package com.jie.calculator.calculator.clipboard;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.annotations.SerializedName;
import com.jie.calculator.calculator.cache.RepositoryExecutor;

import java.nio.charset.Charset;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created on 2019/2/14.
 *
 * @author Jie.Wu
 */
public class SearchBlacklistManager {

    private RepositoryExecutor<Record> executor;

    private static final long PERIOD = TimeUnit.HOURS.toMillis(12);

    public static class Record implements Parcelable {
        @SerializedName("millis")
        private long recordMillis;
        @SerializedName("text")
        private String text;

        public Record(String text) {
            this.text = text;
            this.recordMillis = System.currentTimeMillis();
        }

        boolean valid(long period) {
            return Math.abs(System.currentTimeMillis() - recordMillis) <= period;
        }

        public String getText() {
            return text;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.recordMillis);
            dest.writeString(this.text);
        }

        protected Record(Parcel in) {
            this.recordMillis = in.readLong();
            this.text = in.readString();
        }

        public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
            @Override
            public Record createFromParcel(Parcel source) {
                return new Record(source);
            }

            @Override
            public Record[] newArray(int size) {
                return new Record[size];
            }
        };
    }

    private SearchBlacklistManager() {
    }

    private static final class LazyHolder {
        private static final SearchBlacklistManager instance = new SearchBlacklistManager();
    }

    public static SearchBlacklistManager getInst() {
        return LazyHolder.instance;
    }

    public void init(@NonNull Context context) {
        executor = new RepositoryExecutor<>("search_black_history", context);
        executor.setMaxSize(Integer.MAX_VALUE);
    }

    public void add2Blacklist(String text) {
        if (!TextUtils.isEmpty(text)) {
            Record record = new Record(Base64.encodeToString(text.getBytes(Charset.defaultCharset()),
                    Base64.NO_WRAP));
            executor.put(record);
        }
    }

    public Observable<Set<String>> getBlacklist(){
        //noinspection unchecked
        return getValidList()
                .map(data -> Base64.decode(data.getText(),Base64.NO_WRAP))
                .map(String::new)
                .toList()
                .map((Function<List<String>, LinkedHashSet>) LinkedHashSet::new)
                .map(data -> (Set<String>)data)
                .toObservable();
    }


    private Observable<Record> getValidList(){
        return executor.getData()
                .flatMap(Observable::fromIterable)
                .filter(data->data.valid(PERIOD));
    }
}
