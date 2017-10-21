package com.example.david.equationapp.models;

/**
 * Created by David on 9/25/2017.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManager extends SQLiteOpenHelper{
    private static final String DATABASE_NAME ="equationDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABEL_EQUATIONS = "equations";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String COURSE = "course";
    private static final String EQUATION = "equation";
    private SQLiteDatabase dbWriteable  = this.getWritableDatabase();;

    public DatabaseManager(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public  void onCreate(SQLiteDatabase db){
        //build sql create statment
        String sqlCreate  = "create table " + TABEL_EQUATIONS + " ( " + ID;
        sqlCreate += " integer primary key autoincrement, " + NAME;
        sqlCreate += " text, " + DESCRIPTION + " text, " + COURSE + " text, " + EQUATION + " text )";

        db.execSQL(sqlCreate);
       // dbWriteable  = this.getWritableDatabase();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //drop old table if exists
        db.execSQL("drop table if exists " + TABEL_EQUATIONS);
        //re- create table
        onCreate(db);
    }

    public void insert(MyEquation equ){
        StringBuilder sqlInsert = new StringBuilder("insert into " + TABEL_EQUATIONS);
        sqlInsert.append(" values (null, '" + equ.getName());;
        sqlInsert.append("', '" + equ.getDescription());
        sqlInsert.append("', '" + equ.getCourse());
        sqlInsert.append("', '" + equ.getEquation() + "' )");
        dbWriteable.execSQL(sqlInsert.toString());
    }
    public HashMap<String,MyEquation> selectAll(){
        String sqlQuery = "select * from " + TABEL_EQUATIONS;
        Cursor cursor = dbWriteable.rawQuery(sqlQuery,null);

        HashMap<String,MyEquation> equations = new HashMap<>();
        while(cursor.moveToNext()){
           // MyEquation currentEquation = new MyEquation(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            //equations.put(currentEquation.getName(),currentEquation);
        }
        return equations;
    }
    public void updateById(MyEquation equation){

        StringBuilder sqlUpdate = new StringBuilder("update " + TABEL_EQUATIONS);
        sqlUpdate.append(" set " + NAME + " = '" + equation.getName() + "', ");
        sqlUpdate.append(DESCRIPTION + " = " + "'" + equation.getDescription() + "', ");
        sqlUpdate.append(COURSE + " = " + "'" + equation.getCourse() + "', ");
        sqlUpdate.append(EQUATION + " = " + "'" + equation.getEquation() + "' ");
       // sqlUpdate.append("where " + ID + " = " + equation.getId());

        dbWriteable.execSQL(sqlUpdate.toString());
    }
    public void deleteById(int id){
        StringBuilder sqlUpdate = new StringBuilder("delete from ");
        sqlUpdate.append(TABEL_EQUATIONS + " ");
        sqlUpdate.append("where " + ID + " = " +id);

        dbWriteable.execSQL(sqlUpdate.toString());
    }
    public void onDestroy(){
        dbWriteable.close();
    }
}
