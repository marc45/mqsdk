package cn.com.startai.mqttsdk.db.converter;

import android.database.Cursor;

import cn.com.startai.mqttsdk.db.sqlite.ColumnDbType;


public class ByteArrayColumnConverter implements ColumnConverter<byte[]> {
    @Override
    public byte[] getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getBlob(index);
    }

    @Override
    public Object fieldValue2DbValue(byte[] fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.BLOB;
    }
}
