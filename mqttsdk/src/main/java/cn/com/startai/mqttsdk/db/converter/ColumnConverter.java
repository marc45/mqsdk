package cn.com.startai.mqttsdk.db.converter;

import android.database.Cursor;

import cn.com.startai.mqttsdk.db.sqlite.ColumnDbType;


public interface ColumnConverter<T> {

    T getFieldValue(final Cursor cursor, int index);

    Object fieldValue2DbValue(T fieldValue);

    ColumnDbType getColumnDbType();
}
