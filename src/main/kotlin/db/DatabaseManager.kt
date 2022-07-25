package db

import com.skyd.db.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

object DatabaseManager {
    private lateinit var instance: AppDatabase
    internal fun getInstance(): AppDatabase {
        if (this::instance.isInitialized) return instance
        else instance = AppDatabase(createDriver())
        return instance
    }

    fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + getDbPath())
        AppDatabase.Schema.create(driver)
        return driver
    }

    val dbName = "AppDatabase.db"
    val directory = File("").absoluteFile.path.let {
        if (it.endsWith("\\")) "${it}Database\\"
        else "$it/Database/"
    }

    private fun getDbPath(): String {
        val directory = File(directory)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val db = File(directory, dbName)
        if (!db.exists()) {
            db.createNewFile()
        }
        return db.path
    }
}

val appDatabase = DatabaseManager.getInstance()
