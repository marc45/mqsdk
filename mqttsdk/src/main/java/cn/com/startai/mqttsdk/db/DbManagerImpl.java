

package cn.com.startai.mqttsdk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.db.sqlite.SqlInfo;
import cn.com.startai.mqttsdk.db.sqlite.SqlInfoBuilder;
import cn.com.startai.mqttsdk.db.sqlite.WhereBuilder;
import cn.com.startai.mqttsdk.db.table.ColumnEntity;
import cn.com.startai.mqttsdk.db.table.DbBase;
import cn.com.startai.mqttsdk.db.table.DbModel;
import cn.com.startai.mqttsdk.db.table.TableEntity;

public final class DbManagerImpl extends DbBase {

    //*************************************** create instance ****************************************************

    /**
     * key: dbName
     */
    private final static HashMap<DbManager.DaoConfig, DbManagerImpl> DAO_MAP = new HashMap<DbManager.DaoConfig, DbManagerImpl>();

    private SQLiteDatabase database;
    private DbManager.DaoConfig daoConfig;
    private boolean allowTransaction;

    private DbManagerImpl(Context context, DbManager.DaoConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("daoConfig may not be null");
        }
        this.daoConfig = config;
        this.allowTransaction = config.isAllowTransaction();
        this.database = openOrCreateDatabase(context, config);
        DbManager.DbOpenListener dbOpenListener = config.getDbOpenListener();
        if (dbOpenListener != null) {
            dbOpenListener.onDbOpened(this);
        }
    }

    public synchronized static DbManager getInstance(Context context,DbManager.DaoConfig daoConfig) {

        if (daoConfig == null) {//使用默认配置
            daoConfig = new DbManager.DaoConfig();
        }

        DbManagerImpl dao = DAO_MAP.get(daoConfig);
        if (dao == null) {
            dao = new DbManagerImpl(context,daoConfig);
            DAO_MAP.put(daoConfig, dao);
        } else {
            dao.daoConfig = daoConfig;
        }

        // update the database if needed
        SQLiteDatabase database = dao.database;
        int oldVersion = database.getVersion();
        int newVersion = daoConfig.getDbVersion();
        if (oldVersion != newVersion) {
            if (oldVersion != 0) {
                DbManager.DbUpgradeListener upgradeListener = daoConfig.getDbUpgradeListener();
                if (upgradeListener != null) {
                    upgradeListener.onUpgrade(dao, oldVersion, newVersion);
                } else {
                    try {
                        dao.dropDb();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
            database.setVersion(newVersion);
        }

        return dao;
    }

    @Override
    public SQLiteDatabase getDatabase() {
        return database;
    }

    @Override
    public DaoConfig getDaoConfig() {
        return daoConfig;
    }

    //*********************************************** operations ********************************************************

    @Override
    public void saveOrUpdate(Object entity) throws DbException {
        try {
            beginTransaction();

            if (entity instanceof List) {
                List<?> entities = (List<?>) entity;
                if (entities.isEmpty()) return;
                TableEntity<?> table = this.getTable(entities.get(0).getClass());
                createTableIfNotExist(table);
                for (Object item : entities) {
                    saveOrUpdateWithoutTransaction(table, item);
                }
            } else {
                TableEntity<?> table = this.getTable(entity.getClass());
                createTableIfNotExist(table);
                saveOrUpdateWithoutTransaction(table, entity);
            }

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    @Override
    public void replace(Object entity) throws DbException {
        try {
            beginTransaction();

            if (entity instanceof List) {
                List<?> entities = (List<?>) entity;
                if (entities.isEmpty()) return;
                TableEntity<?> table = this.getTable(entities.get(0).getClass());
                createTableIfNotExist(table);
                for (Object item : entities) {
                    execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(table, item));
                }
            } else {
                TableEntity<?> table = this.getTable(entity.getClass());
                createTableIfNotExist(table);
                execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(table, entity));
            }

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    @Override
    public void save(Object entity) throws DbException {
        try {
            beginTransaction();

            if (entity instanceof List) {
                List<?> entities = (List<?>) entity;
                if (entities.isEmpty()) return;
                TableEntity<?> table = this.getTable(entities.get(0).getClass());
                createTableIfNotExist(table);
                for (Object item : entities) {
                    execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(table, item));
                }
            } else {
                TableEntity<?> table = this.getTable(entity.getClass());
                createTableIfNotExist(table);
                execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(table, entity));
            }

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    @Override
    public boolean saveBindingId(Object entity) throws DbException {
        boolean result = false;
        try {
            beginTransaction();

            if (entity instanceof List) {
                List<?> entities = (List<?>) entity;
                if (entities.isEmpty()) return false;
                TableEntity<?> table = this.getTable(entities.get(0).getClass());
                createTableIfNotExist(table);
                for (Object item : entities) {
                    if (!saveBindingIdWithoutTransaction(table, item)) {
                        throw new DbException("saveBindingId error, transaction will not commit!");
                    }
                }
            } else {
                TableEntity<?> table = this.getTable(entity.getClass());
                createTableIfNotExist(table);
                result = saveBindingIdWithoutTransaction(table, entity);
            }

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
        return result;
    }

    @Override
    public void deleteById(Class<?> entityType, Object idValue) throws DbException {
        TableEntity<?> table = this.getTable(entityType);
        if (!table.tableIsExist()) return;
        try {
            beginTransaction();

            execNonQuery(SqlInfoBuilder.buildDeleteSqlInfoById(table, idValue));

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(Object entity) throws DbException {
        try {
            beginTransaction();

            if (entity instanceof List) {
                List<?> entities = (List<?>) entity;
                if (entities.isEmpty()) return;
                TableEntity<?> table = this.getTable(entities.get(0).getClass());
                if (!table.tableIsExist()) return;
                for (Object item : entities) {
                    execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(table, item));
                }
            } else {
                TableEntity<?> table = this.getTable(entity.getClass());
                if (!table.tableIsExist()) return;
                execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(table, entity));
            }

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    @Override
    public void delete(Class<?> entityType) throws DbException {
        delete(entityType, null);
    }

    @Override
    public int delete(Class<?> entityType, WhereBuilder whereBuilder) throws DbException {
        TableEntity<?> table = this.getTable(entityType);
        if (!table.tableIsExist()) return 0;
        int result = 0;
        try {
            beginTransaction();

            result = executeUpdateDelete(SqlInfoBuilder.buildDeleteSqlInfo(table, whereBuilder));

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
        return result;
    }

    @Override
    public void update(Object entity, String... updateColumnNames) throws DbException {
        try {
            beginTransaction();

            if (entity instanceof List) {
                List<?> entities = (List<?>) entity;
                if (entities.isEmpty()) return;
                TableEntity<?> table = this.getTable(entities.get(0).getClass());
                if (!table.tableIsExist()) return;
                for (Object item : entities) {
                    execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(table, item, updateColumnNames));
                }
            } else {
                TableEntity<?> table = this.getTable(entity.getClass());
                if (!table.tableIsExist()) return;
                execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(table, entity, updateColumnNames));
            }

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    @Override
    public int update(Class<?> entityType, WhereBuilder whereBuilder, KeyValue... nameValuePairs) throws DbException {
        TableEntity<?> table = this.getTable(entityType);
        if (!table.tableIsExist()) return 0;

        int result = 0;
        try {
            beginTransaction();

            result = executeUpdateDelete(SqlInfoBuilder.buildUpdateSqlInfo(table, whereBuilder, nameValuePairs));

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T findById(Class<T> entityType, Object idValue) throws DbException {
        TableEntity<T> table = this.getTable(entityType);
        if (!table.tableIsExist()) return null;

        Selector selector = Selector.from(table).where(table.getId().getName(), "=", idValue);

        String sql = selector.limit(1).toString();
        Cursor cursor = execQuery(sql);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    return CursorUtils.getEntity(table, cursor);
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtil.closeQuietly(cursor);
            }
        }
        return null;
    }

    @Override
    public <T> T findFirst(Class<T> entityType) throws DbException {
        return this.selector(entityType).findFirst();
    }

    @Override
    public <T> List<T> findAll(Class<T> entityType) throws DbException {
        return this.selector(entityType).findAll();
    }

    @Override
    public <T> Selector<T> selector(Class<T> entityType) throws DbException {
        return Selector.from(this.getTable(entityType));
    }

    @Override
    public DbModel findDbModelFirst(SqlInfo sqlInfo) throws DbException {
        Cursor cursor = execQuery(sqlInfo);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    return CursorUtils.getDbModel(cursor);
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtil.closeQuietly(cursor);
            }
        }
        return null;
    }

    @Override
    public List<DbModel> findDbModelAll(SqlInfo sqlInfo) throws DbException {
        List<DbModel> dbModelList = new ArrayList<DbModel>();

        Cursor cursor = execQuery(sqlInfo);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    dbModelList.add(CursorUtils.getDbModel(cursor));
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtil.closeQuietly(cursor);
            }
        }
        return dbModelList;
    }

    //******************************************** config ******************************************************

    private SQLiteDatabase openOrCreateDatabase(Context context, DbManager.DaoConfig config) {
        SQLiteDatabase result = null;

        File dbDir = config.getDbDir();
        if (dbDir != null && (dbDir.exists() || dbDir.mkdirs())) {
            File dbFile = new File(dbDir, config.getDbName());
            result = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } else {
            result = context.openOrCreateDatabase(config.getDbName(), 0, null);
        }
        return result;
    }

    //***************************** private operations with out transaction *****************************
    private void saveOrUpdateWithoutTransaction(TableEntity<?> table, Object entity) throws DbException {
        ColumnEntity id = table.getId();
        if (id.isAutoId()) {
            if (id.getColumnValue(entity) != null) {
                execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(table, entity));
            } else {
                saveBindingIdWithoutTransaction(table, entity);
            }
        } else {
            execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(table, entity));
        }
    }

    private boolean saveBindingIdWithoutTransaction(TableEntity<?> table, Object entity) throws DbException {
        ColumnEntity id = table.getId();
        if (id.isAutoId()) {
            execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(table, entity));
            long idValue = getLastAutoIncrementId(table.getName());
            if (idValue == -1) {
                return false;
            }
            id.setAutoIdValue(entity, idValue);
            return true;
        } else {
            execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(table, entity));
            return true;
        }
    }

    //************************************************ tools ***********************************

    private long getLastAutoIncrementId(String tableName) throws DbException {
        long id = -1;
        Cursor cursor = execQuery("SELECT seq FROM sqlite_sequence WHERE name='" + tableName + "' LIMIT 1");
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    id = cursor.getLong(0);
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtil.closeQuietly(cursor);
            }
        }
        return id;
    }

    @Override
    public void close() throws IOException {
        if (DAO_MAP.containsKey(daoConfig)) {
            DAO_MAP.remove(daoConfig);
            this.database.close();
        }
    }

    ///////////////////////////////////// exec sql /////////////////////////////////////////////////////

    private void beginTransaction() {
        if (allowTransaction) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && database.isWriteAheadLoggingEnabled()) {
                database.beginTransactionNonExclusive();
            } else {
                database.beginTransaction();
            }
        }
    }

    private void setTransactionSuccessful() {
        if (allowTransaction) {
            database.setTransactionSuccessful();
        }
    }

    private void endTransaction() {
        if (allowTransaction) {
            database.endTransaction();
        }
    }


    @Override
    public int executeUpdateDelete(SqlInfo sqlInfo) throws DbException {
        SQLiteStatement statement = null;
        try {
            statement = sqlInfo.buildStatement(database);
            return statement.executeUpdateDelete();
        } catch (Throwable e) {
            throw new DbException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.releaseReference();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public int executeUpdateDelete(String sql) throws DbException {
        SQLiteStatement statement = null;
        try {
            statement = database.compileStatement(sql);
            return statement.executeUpdateDelete();
        } catch (Throwable e) {
            throw new DbException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.releaseReference();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void execNonQuery(SqlInfo sqlInfo) throws DbException {
        SQLiteStatement statement = null;
        try {
            statement = sqlInfo.buildStatement(database);
            statement.execute();
        } catch (Throwable e) {
            throw new DbException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.releaseReference();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void execNonQuery(String sql) throws DbException {
        try {
            database.execSQL(sql);
        } catch (Throwable e) {
            throw new DbException(e);
        }
    }

    @Override
    public Cursor execQuery(SqlInfo sqlInfo) throws DbException {
        try {
            return database.rawQuery(sqlInfo.getSql(), sqlInfo.getBindArgsAsStrArray());
        } catch (Throwable e) {
            throw new DbException(e);
        }
    }

    @Override
    public Cursor execQuery(String sql) throws DbException {
        try {
            return database.rawQuery(sql, null);
        } catch (Throwable e) {
            throw new DbException(e);
        }
    }

}
