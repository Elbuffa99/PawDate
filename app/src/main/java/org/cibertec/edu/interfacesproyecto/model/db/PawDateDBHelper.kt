package org.cibertec.edu.interfacesproyecto.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PawDateDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "PawDate.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // TABLA PERFILES
        db.execSQL("""
        CREATE TABLE IF NOT EXISTS PERFILES (
            id_perfil INTEGER PRIMARY KEY AUTOINCREMENT,
            email TEXT NOT NULL,
            telefono TEXT NOT NULL,
            nombre_perro TEXT NOT NULL,
            fecha_nacimiento INTEGER NOT NULL,
            genero TEXT CHECK (genero IN ('Macho', 'Hembra')) NOT NULL,
            busca TEXT,                  -- Lo que busca el perro (definido dinámicamente desde la app)
            relaciones TEXT,             -- Relación elegida en RelacionesActivity
            avatar TEXT                  -- Imagen Base64 del perro
        );
    """)


        // TABLA HABITOS
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS HABITOS (
                id_habito INTEGER PRIMARY KEY AUTOINCREMENT,
                id_perfil INTEGER NOT NULL,
                nivel_energia TEXT,
                frecuencia_paseos TEXT,
                sociabilidad TEXT,
                alimentacion TEXT,
                horarios_descanso TEXT,
                FOREIGN KEY (id_perfil) REFERENCES PERFILES(id_perfil)
                    ON DELETE CASCADE ON UPDATE CASCADE
            );
        """)

        // TABLA PERSONALIDADES
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS PERSONALIDADES (
                id_personalidad INTEGER PRIMARY KEY AUTOINCREMENT,
                id_perfil INTEGER NOT NULL UNIQUE,
                comportamiento TEXT,
                entorno TEXT,
                interaccion_social TEXT,
                FOREIGN KEY (id_perfil) REFERENCES PERFILES(id_perfil)
                    ON DELETE CASCADE ON UPDATE CASCADE
            );
        """)

        // TABLA MATCHES
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS MATCHES (
                id_match INTEGER PRIMARY KEY AUTOINCREMENT,
                id_perfil1 INTEGER NOT NULL,
                id_perfil2 INTEGER NOT NULL,
                estado TEXT CHECK (estado IN ('pendiente', 'aceptado', 'rechazado')) DEFAULT 'pendiente',
                fecha_match INTEGER DEFAULT (strftime('%s','now') * 1000),
                FOREIGN KEY (id_perfil1) REFERENCES PERFILES(id_perfil)
                    ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY (id_perfil2) REFERENCES PERFILES(id_perfil)
                    ON DELETE CASCADE ON UPDATE CASCADE
            );
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS MATCHES")
        db.execSQL("DROP TABLE IF EXISTS PERSONALIDADES")
        db.execSQL("DROP TABLE IF EXISTS HABITOS")
        db.execSQL("DROP TABLE IF EXISTS PERFILES")
        onCreate(db)
    }
}
