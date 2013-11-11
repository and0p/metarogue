package net.and0.metarogue.controller;

import net.and0.metarogue.model.gameworld.ChunkArray;
import net.and0.metarogue.model.gameworld.World;

import java.sql.*;

/**
 * MetaRogue class
 * User: andrew
 * Date: 10/12/13
 * Time: 12:45 PM
 */
public class DBLoader {

    Connection con = null;
    String databasename = null;

    String getChunkStatement = null;
    String setChunkStatement = null;

    PreparedStatement getChunkPS = null;
    PreparedStatement setChunkPS = null;

    public DBLoader(String databasename) {
        this.databasename = databasename;

        getChunkStatement = "SELECT * FROM ? WHERE Key = ?";
        // setChunkStatement = ???

        try {
            con = DriverManager.getConnection("jdbc:sqlite:/db/" + databasename + ".db");
            getChunkPS = con.prepareStatement(getChunkStatement);
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public void createWorld(World world) {
        String sql = "CREATE TABLE" + world.id + "( id BIGINT UNSIGNED not NULL, "
    }

    public ChunkArray loadChunkArray(String world, int key) {
        return null;
    }
}