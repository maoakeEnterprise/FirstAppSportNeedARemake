package com.example.maoak.app_sport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {



    //KEY DU TYPE
    static final String DATABASE_TABLE_TYPE = "type";
    static final String KEY_NOMTYPE = "nom_type";


    //KEY DU TYPE POIDS
    static final String DATABASE_TABLE_TYPEPOIDS = "type_poids";
    static final String KEY_IDTYPE = "_id_type";


    //KEY DU PROFIL
    static final String DATABASE_TABLE_PROFIL = "profil";
    static final String KEY_ROWID = "_id";
    static final String KEY_LOGIN = "login";
    static final String KEY_MDP = "password";
    static final String KEY_NOM = "nom";
    static final String KEY_PRENOM = "prenom";
    static final String KEY_TAILLE = "taille";
    static final String KEY_POIDS = "poids";
    static final String KEY_EMAIL = "email";


    //KEY DES EXERCICES
    static final String DATABASE_TABLE_EXO = "exercice";
    static final String KEY_DESCRIPTIF = "descriptif";
    static final String KEY_CATEG = "id_categorie";
    static final String KEY_TYPE = "id_type";


    //KEY MUSCLE
    static final String DATABASE_TABLE_MUSCLE = "muscle";
    static final String KEY_NOM_MUSCLE = "nom_muscle";


    //KEY MUSCLE PRINC/SEC
    static final String DATABASE_TABLE_MUSCLE_PRINCIPALE_SECONDAIRE = "muscle_principaux_secondaire";
    static final String KEY_IDEXO = "_id_exo";
    static final String KEY_IDMUSCLE = "_id_muscle";
    static final String KEY_ETAT = "etat";


    //KEY INIT PROGRAMMES
    static final String DATABASE_TABLE_PROGRAMME_INIT = "programme_init";
    static final String KEY_NOM_PROG = "_nom_prog";

    //KEY PROGRAMMES
    static final String DATABASE_TABLE_PROGRAMME = "programme";
    static final String KEY_IDPROG = "_id_prog";
    static final String KEY_TEMP_RECUP = "temps_recuperation";


    //KEY SERIES
    static final String DATABASE_TABLE_SERIES = "serie_prog";
    static final String KEY_SERIES = "serie";
    static final String KEY_CHARGE = "charge";
    static final String KEY_REPETITION = "repetition";

    //KEY CATEGORIE
    static final String DATABASE_TABLE_CATEGORIE = "categorie";
    static final String KEY_NOM_CATEGORIE = "nom_categorie";

    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB2";

    //KEY HISTORIQUE
    static final String DATABASE_TABLE_HISTORIQUE = "historique";
    static final String KEY_DATE = "date";


    static final int DATABASE_VERSION = 1;

    //MAIN TABLE

    static final String DATABASE_CREATE_PROFIL =
            "create table profil (_id integer primary key autoincrement, "
                    + "login text not null, password text not null," +
                    "nom text not null, prenom text not null," +
                    "taille text not null, poids text not null,"+
                    "email text not null"+
                    ");";

    static final String DATABASE_CREATE_TYPE ="create table type(" +
            "_id integer primary key autoincrement,"+
            "nom_type text not null"+
            ");";
    static final String DATABASE_CREATE_CATEGORIE ="create table categorie(" +
            "_id integer primary key autoincrement,"+
            "nom_categorie text not null"+
            ");";
    static final String DATABASE_CREATE_EXERCICE =
            "create table exercice (_id integer primary key autoincrement, "
                    + "nom text not null, descriptif text not null," +
                    "id_categorie integer not null, id_type integer not null,"+
                    "foreign key(id_type) references type(_id),"+
                    "foreign key(id_categorie) references categorie(_id)"+
                    ");";

    static final String DATABASE_CREATE_MUSCLE =
            "create table muscle (_id integer primary key autoincrement, "
                    + "nom_muscle text not null"+
                    ");";




    //SUPPORT TABLE
    //POUR PLUSIEURS MUSCLE

    static final String DATABASE_CREATE_TYPE_POIDS ="create table type_poids(" +
            "_id_exo integer not null,"+
            "_id_type integer not null,"+
            "poids integer not null,"+
            "foreign key(_id_exo) references exercice(_id),"+
            "foreign key(_id_type) references type(_id),"+
            "primary key(_id_exo,_id_type)"+
            ");";

    static final String DATABASE_CREATE_MUSCLE_PRINCIPAUX_SEONCDAIRE =
            "create table muscle_principaux_secondaire (_id_exo integer not null, "
                    + "_id_muscle integer not null,"+
                    "etat integer not null,"+
                    "foreign key(_id_exo) references exercice(_id),"+
                    "foreign key(_id_muscle) references muscle(_id),"+
                    "primary key(_id_exo,_id_muscle)"+
                    ");";

    static final String DATABASE_CREATE_PROGRAMME =
            "create table programme (_id_prog integer not null, "
                    + "_id_exo integer not null,"+
                    "temps_recuperation integer not null,"+
                    "foreign key(_id_prog) references programme_init(_id),"+
                    "foreign key(_id_exo) references exercice(_id),"+
                    "primary key(_id_prog,_id_exo)"+
                    ");";
    static final String DATABASE_CREATE_PROGRAMME_INIT =
                    "create table programme_init (_id integer primary key autoincrement," +
                    "_nom_prog test not null" +
                    ");";

    static final String DATABASE_CREATE_SERIE_PROG =
            "create table serie_prog(serie integer not null," +
                    "_id_prog integer not null," +
                    "_id_exo integer not null," +
                    "charge integer not null," +
                    "repetition integer not null," +
                    "foreign key(_id_exo) references exercice(_id),"+
                    "foreign key(_id_prog) references programme_init(_id),"+
                    "primary key(serie,_id_prog,_id_exo)"+
                    ");";

    static final String DATABASE_CREATE_HISTORIQUE =
                    "create table historique(_id integer primary key autoincrement," +
                            "_id_prog integer not null," +
                    "_id_exo integer not null," +
                    "charge integer not null," +
                    "repetition integer not null," +
                    "date text not null,"+
                    "foreign key(_id_prog) references programme_init(_id)," +
                    "foreign key(_id_exo) references exercice(_id)" +
                    ");";


    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public void delete_drop(){
        db.execSQL("DROP TABLE IF EXISTS profil");
        db.execSQL("DROP TABLE IF EXISTS type_poids");
        db.execSQL("DROP TABLE IF EXISTS muscle_principaux_secondaire");
        db.execSQL("DROP TABLE IF EXISTS serie_prog");
        db.execSQL("DROP TABLE IF EXISTS programme");
        db.execSQL("DROP TABLE IF EXISTS programme_init");
        db.execSQL("DROP TABLE IF EXISTS exercice");
        db.execSQL("DROP TABLE IF EXISTS muscle");
        db.execSQL("DROP TABLE IF EXISTS type");
        db.execSQL("DROP TABLE IF EXISTS categorie");

        System.out.println("TABLE SUPPRIME");
    }
    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE_PROFIL);

                db.execSQL(DATABASE_CREATE_TYPE);
                db.execSQL(DATABASE_CREATE_CATEGORIE);
                db.execSQL(DATABASE_CREATE_PROGRAMME_INIT);
                db.execSQL(DATABASE_CREATE_EXERCICE);
                db.execSQL(DATABASE_CREATE_MUSCLE);
                db.execSQL(DATABASE_CREATE_MUSCLE_PRINCIPAUX_SEONCDAIRE);
                db.execSQL(DATABASE_CREATE_TYPE_POIDS);
                db.execSQL(DATABASE_CREATE_PROGRAMME);
                db.execSQL(DATABASE_CREATE_SERIE_PROG);
                db.execSQL(DATABASE_CREATE_HISTORIQUE);
                System.out.println("FAIT2");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("FAIT1");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS profil");
            db.execSQL("DROP TABLE IF EXISTS type_poids");
            db.execSQL("DROP TABLE IF EXISTS muscle_principaux_secondaire");
            db.execSQL("DROP TABLE IF EXISTS historique");
            db.execSQL("DROP TABLE IF EXISTS serie_prog");
            db.execSQL("DROP TABLE IF EXISTS programme");
            db.execSQL("DROP TABLE IF EXISTS exercice");
            db.execSQL("DROP TABLE IF EXISTS muscle");
            db.execSQL("DROP TABLE IF EXISTS categorie");
            db.execSQL("DROP TABLE IF EXISTS type");

            onCreate(db);
        }
    }


    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //FONCTION POUR INSERER DONNER DANS LES TABLES
    //==================================================================================================

    public long insert_donne_profil(String login, String mdp, String nom, String prenom, String taille, String poids, String email)
    {
        ContentValues initialValues = new ContentValues();

        //condition pour pouvoir reconnaitre dans quel table nous allons ajouter les informations .
        initialValues.put(KEY_LOGIN, login);
        initialValues.put(KEY_MDP, mdp);
        initialValues.put(KEY_NOM, nom);
        initialValues.put(KEY_PRENOM, prenom);
        initialValues.put(KEY_TAILLE, taille);
        initialValues.put(KEY_POIDS, poids);
        initialValues.put(KEY_EMAIL, email);

        System.out.println("test");
        return db.insert(DATABASE_TABLE_PROFIL, null, initialValues);
    }


    public long insert_donne_exo(String[] donnee)
    {
        ContentValues initialValues = new ContentValues();

        //Envoi de donne.
        initialValues.put(KEY_NOM, donnee[0]);
        initialValues.put(KEY_DESCRIPTIF, donnee[1]);
        initialValues.put(KEY_CATEG, Integer.parseInt(donnee[2]));
        initialValues.put(KEY_TYPE, Integer.parseInt(donnee[3]));

        return db.insert(DATABASE_TABLE_EXO, null, initialValues);
    }
    public long insert_donne_muscle(String[] donnee)
    {
        ContentValues initialValues = new ContentValues();

        //Envoi de donne.
        initialValues.put(KEY_NOM_MUSCLE, donnee[0]);


        return db.insert(DATABASE_TABLE_MUSCLE, null, initialValues);
    }
    public long insert_donne_principale_secondaire(String[] donnee)
    {
        ContentValues initialValues = new ContentValues();

        //Envoi de donne.
        initialValues.put(KEY_IDEXO, Integer.parseInt(donnee[0]));
        initialValues.put(KEY_IDMUSCLE, Integer.parseInt(donnee[1]));
        initialValues.put(KEY_ETAT, Integer.parseInt(donnee[2]));


        return db.insert(DATABASE_TABLE_MUSCLE_PRINCIPALE_SECONDAIRE, null, initialValues);
    }

    public long insert_donne_type(String[] donnee){
        ContentValues initialValues = new ContentValues();

        //Envoi de donne.
        initialValues.put(KEY_NOMTYPE, donnee[0]);


        return db.insert(DATABASE_TABLE_TYPE, null, initialValues);
    }
    public long insert_donne_categorie(String[] donnee){
        ContentValues initialValues = new ContentValues();

        //Envoi de donne.
        initialValues.put(KEY_NOM_CATEGORIE, donnee[0]);


        return db.insert(DATABASE_TABLE_CATEGORIE, null, initialValues);
    }

    public long insert_donne_programme(String[] donnee){
        ContentValues initialValues = new ContentValues();

        //Envoi de donne.
        initialValues.put(KEY_IDPROG, Integer.parseInt(donnee[0]));
        initialValues.put(KEY_IDEXO, Integer.parseInt(donnee[1]));
        initialValues.put(KEY_TEMP_RECUP, Integer.parseInt(donnee[2]));

        return db.insert(DATABASE_TABLE_PROGRAMME, null, initialValues);
    }
    public long insert_donne_serie(String[] donnee){
        ContentValues initialValues = new ContentValues();

        //Envoi de donne.
        initialValues.put(KEY_SERIES, Integer.parseInt(donnee[0]));
        initialValues.put(KEY_IDPROG, Integer.parseInt(donnee[1]));
        initialValues.put(KEY_IDEXO, Integer.parseInt(donnee[2]));
        initialValues.put(KEY_CHARGE, Integer.parseInt(donnee[3]));
        initialValues.put(KEY_REPETITION, Integer.parseInt(donnee[4]));

        return db.insert(DATABASE_TABLE_SERIES, null, initialValues);
    }

    public long insert_donne_programme_init(String[] donnee){
        ContentValues initialValues = new ContentValues();

        //Envoi de donne.
        initialValues.put(KEY_NOM_PROG, donnee[0]);

        return db.insert(DATABASE_TABLE_PROGRAMME_INIT, null, initialValues);
    }

    public long insert_donne_historique(String[] donnee){
        ContentValues initialValues = new ContentValues();

        //Envoi de donne.
        initialValues.put(KEY_IDPROG, Integer.parseInt(donnee[0]));
        initialValues.put(KEY_IDEXO, Integer.parseInt(donnee[1]));
        initialValues.put(KEY_CHARGE, Integer.parseInt(donnee[2]));
        initialValues.put(KEY_REPETITION, Integer.parseInt(donnee[3]));
        initialValues.put(KEY_DATE, donnee[4]);
        return db.insert(DATABASE_TABLE_HISTORIQUE, null, initialValues);
    }

    //==================================================================================================


    //FONCTION POUR SUPPRIMER DONNEE DANS LES TABLES
    //==================================================================================================

    public boolean delete_donnne_profil(long rowId)
    {
        return db.delete(DATABASE_TABLE_PROFIL, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean delete_donnne_exo(long rowId)
    {
        return db.delete(DATABASE_TABLE_EXO, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean delete_donnne_muscle(long rowId)
    {
        return db.delete(DATABASE_TABLE_MUSCLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public boolean delete_donnne_type(long rowId)
    {
        return db.delete(DATABASE_TABLE_TYPE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public boolean delete_donnne_categorie(long rowId)
    {
        return db.delete(DATABASE_TABLE_CATEGORIE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean delete_donne_muscle_principaux_secondaire(long id_exo,long id_muscle){
        return db.delete(DATABASE_TABLE_MUSCLE_PRINCIPALE_SECONDAIRE, KEY_IDEXO + "=" + id_exo + " and " + KEY_IDMUSCLE + "=" + id_muscle, null) > 0;
    }

    public boolean delete_donne_typepoids(long id_type,long id_exo){
        return db.delete(DATABASE_CREATE_TYPE_POIDS, KEY_IDTYPE + "=" + id_type + " and " + KEY_IDEXO+ "=" + id_exo, null) > 0;
    }
    public boolean delete_donne_programme(long id_prog,long id_exo){
        return db.delete(DATABASE_TABLE_PROGRAMME, KEY_IDPROG + "=" + id_prog + " and " + KEY_IDEXO+ "=" + id_exo, null) > 0;
    }
    public boolean delete_donne_serie(long series, long id_prog,long id_exo){
        return db.delete(DATABASE_TABLE_SERIES, KEY_SERIES + " = " + series + " and " + KEY_IDPROG+ " = " + id_prog + " and " + KEY_IDEXO+ " = " + id_exo, null) > 0;
    }
    public boolean delete_donne_programme_init(long id_prog){
        return db.delete(DATABASE_TABLE_PROGRAMME_INIT, KEY_ROWID + "=" + id_prog, null) > 0;
    }
    //==================================================================================================


    //FONCTION POUR AVOIR TOUTE LES DONNEES DANS LES TABLES
    //==================================================================================================
    public Cursor getAll_donnee_profil()
    {
        return db.query(DATABASE_TABLE_PROFIL, new String[] {
                KEY_ROWID,
                KEY_LOGIN,
                KEY_MDP,
                KEY_NOM,
                KEY_PRENOM,
                KEY_TAILLE,
                KEY_POIDS,
                KEY_EMAIL
        }, null, null, null, null, null);
    }

    public Cursor getAll_donnee_exo()
    {
        return db.query(DATABASE_TABLE_EXO, new String[] {
                        KEY_ROWID,
                        KEY_NOM,
                        KEY_DESCRIPTIF,
                        KEY_CATEG,
                        KEY_TYPE
                },
                null, null, null, null, null);
    }
    public Cursor getAll_donnee_type()
    {
        return db.query(DATABASE_TABLE_TYPE, new String[] {
                        KEY_ROWID,
                        KEY_NOMTYPE
                },
                null, null, null, null, null);
    }
    public Cursor getAll_donnee_categorie()
    {
        return db.query(DATABASE_TABLE_CATEGORIE, new String[] {
                        KEY_ROWID,
                        KEY_NOM_CATEGORIE
                },
                null, null, null, null, null);
    }

    public Cursor getAll_donne_muscle(){
        return db.query(DATABASE_TABLE_MUSCLE, new String[] {
                        KEY_ROWID,
                        KEY_NOM_MUSCLE
                },
                null, null, null, null, null);
    }
    public Cursor getAll_donne_muscle_principaux_secondaire(){
        return db.query(DATABASE_TABLE_MUSCLE_PRINCIPALE_SECONDAIRE, new String[] {
                        KEY_IDEXO,
                        KEY_IDMUSCLE,
                        KEY_ETAT
                },
                null, null, null, null, null);
    }
    public Cursor getAll_donne_typepoids(){
        return db.query(DATABASE_TABLE_TYPEPOIDS, new String[] {
                        KEY_IDTYPE,
                        KEY_IDEXO,
                        KEY_POIDS
                },
                null, null, null, null, null);
    }
    public Cursor getAll_donne_programme(){
        return db.query(DATABASE_TABLE_PROGRAMME, new String[] {
                        KEY_IDPROG,
                        KEY_IDEXO,
                        KEY_TEMP_RECUP
                },
                null, null, null, null, null);
    }
    public Cursor getAll_donne_programme_init(){
        return db.query(DATABASE_TABLE_PROGRAMME_INIT, new String[] {
                        KEY_ROWID,
                        KEY_NOM_PROG
                },
                null, null, null, null, null);
    }
    public Cursor getAll_donne_seri_prog(){
        return db.query(DATABASE_TABLE_SERIES, new String[] {
                        KEY_SERIES,
                        KEY_IDPROG,
                        KEY_IDEXO,
                        KEY_CHARGE,
                        KEY_REPETITION
                },
                null, null, null, null, null);
    }
    public Cursor getAll_donne_historique(){
        return db.query(DATABASE_TABLE_HISTORIQUE, new String[] {
                        KEY_IDPROG,
                        KEY_IDEXO,
                        KEY_CHARGE,
                        KEY_REPETITION,
                        KEY_DATE
                },
                null, null, null, null, null);
    }

    /*
    public Cursor getAll_donne_programme(){}*/


    //==================================================================================================


    //FONCTION POUR CERTAINE DONNNE DANS LES TABLES PAR RAPPORT ID
    //==================================================================================================
    public Cursor get_donne_profil(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_PROFIL, new String[] {
                                KEY_ROWID,
                                KEY_LOGIN,
                                KEY_MDP,
                                KEY_NOM,
                                KEY_PRENOM,
                                KEY_TAILLE,
                                KEY_POIDS,
                                KEY_EMAIL
                        },
                        KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor get_donne_exo(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_EXO, new String[] {
                                KEY_ROWID,
                                KEY_NOM,
                                KEY_DESCRIPTIF,
                                KEY_CATEG,
                                KEY_TYPE
                        },
                        KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor get_donne_serie(long serie, long idprog, long idexo) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_SERIES, new String[] {
                                KEY_SERIES,
                                KEY_IDPROG,
                                KEY_IDEXO,
                                KEY_CHARGE,
                                KEY_REPETITION
                        },
                        KEY_SERIES + " = " + serie + " and " + KEY_IDPROG + " = "  + idprog + " and "+ KEY_IDEXO + " = " + idexo, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor get_donne_prog(long idprog, long idexo) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_PROGRAMME, new String[] {
                                KEY_IDPROG,
                                KEY_IDEXO,
                                KEY_TEMP_RECUP
                        },
                        KEY_IDPROG + " = "  + idprog + " and "+ KEY_IDEXO + " = " + idexo, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor get_init_prog(long idprog) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_PROGRAMME_INIT, new String[] {

                                KEY_NOM_PROG
                        },
                        KEY_ROWID + " = "  + idprog, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //==================================================================================================


    //FONCTION POUR MODIFIER UNE CERTAINE LIGNE DANS LES TABLES
    //==================================================================================================
    public boolean update_donnee_profil(long rowId,String login, String mdp, String nom, String prenom, String taille, String poids, String email)
    {
        ContentValues args = new ContentValues();

        args.put(KEY_LOGIN, login);
        args.put(KEY_MDP, mdp);
        args.put(KEY_NOM, nom);
        args.put(KEY_PRENOM, prenom);
        args.put(KEY_TAILLE, taille);
        args.put(KEY_POIDS, poids);
        args.put(KEY_EMAIL, email);

        return db.update(DATABASE_TABLE_PROFIL, args, KEY_ROWID + "=" + rowId, null) >
                0;
    }

    public boolean update_donnee_exo(long rowId, String donnee[])
    {
        ContentValues args = new ContentValues();

        args.put(KEY_NOM, donnee[0]);
        args.put(KEY_DESCRIPTIF, donnee[1]);
        args.put(KEY_CATEG, Integer.parseInt(donnee[2]));
        args.put(KEY_TYPE, Integer.parseInt(donnee[3]));

        return db.update(DATABASE_TABLE_EXO, args, KEY_ROWID + "=" + rowId, null) >
                0;
    }
    public boolean update_donne_prog(String donnee[], long idprog, long idexo) throws SQLException
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TEMP_RECUP, Integer.parseInt(donnee[0]));

        return db.update(DATABASE_TABLE_PROGRAMME, args, KEY_IDPROG + " = " + idprog + " and " + KEY_IDEXO + " = " + idexo, null) >
                0;
    }

    public boolean update_donne_serie(String donnee[], long serie, long idprog, long idexo) throws SQLException
    {
        ContentValues args = new ContentValues();
        args.put(KEY_CHARGE, Integer.parseInt(donnee[0]));
        args.put(KEY_REPETITION, Integer.parseInt(donnee[1]));

        return db.update(DATABASE_TABLE_SERIES, args, KEY_SERIES + "=" + serie + " and " + KEY_IDPROG + " = " + idprog + " and " + KEY_IDEXO + " = " + idexo, null) >
                0;
    }

    //==================================================================================================


}
