

package cn.com.startai.mqttsdk.db.table;

import android.database.Cursor;


import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;

import cn.com.startai.mqttsdk.db.DbException;
import cn.com.startai.mqttsdk.db.DbManager;
import cn.com.startai.mqttsdk.db.IOUtil;
import cn.com.startai.mqttsdk.db.annotation.Table;


public final class TableEntity<T> {

    private final DbManager db;
    private final String name;
    private final String onCreated;
    private ColumnEntity id;
    private Class<T> entityType;
    private Constructor<T> constructor;
    private volatile boolean checkedDatabase;

    /**
     * key: columnName
     */
    private final LinkedHashMap<String, ColumnEntity> columnMap;

    /*package*/ TableEntity(DbManager db, Class<T> entityType) throws Throwable {
        this.db = db;
        this.entityType = entityType;
        this.constructor = entityType.getConstructor();
        this.constructor.setAccessible(true);
        Table table = entityType.getAnnotation(Table.class);
        this.name = table.name();
        this.onCreated = table.onCreated();
        this.columnMap = TableUtils.findColumnMap(entityType);

        for (ColumnEntity column : columnMap.values()) {
            if (column.isId()) {
                this.id = column;
                break;
            }
        }
    }

    public T createEntity() throws Throwable {
        return this.constructor.newInstance();
    }

    public boolean tableIsExist() throws DbException {
        if (this.isCheckedDatabase()) {
            return true;
        }

        Cursor cursor = db.execQuery("SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name='" + name + "'");
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    int count = cursor.getInt(0);
                    if (count > 0) {
                        this.setCheckedDatabase(true);
                        return true;
                    }
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtil.closeQuietly(cursor);
            }
        }

        return false;
    }

    public DbManager getDb() {
        return db;
    }

    public String getName() {
        return name;
    }

    public Class<T> getEntityType() {
        return entityType;
    }

    public String getOnCreated() {
        return onCreated;
    }

    public ColumnEntity getId() {
        return id;
    }

    public LinkedHashMap<String, ColumnEntity> getColumnMap() {
        return columnMap;
    }

    /*package*/ boolean isCheckedDatabase() {
        return checkedDatabase;
    }

    /*package*/ void setCheckedDatabase(boolean checkedDatabase) {
        this.checkedDatabase = checkedDatabase;
    }

    @Override
    public String toString() {
        return name;
    }
}
