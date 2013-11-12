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
        setChunkStatement = "INSERT OR REPLACE INTO ? VALUES(?, ?)";
        // setChunkStatement = ???

        try {
            con = DriverManager.getConnection("jdbc:sqlite:/db/" + databasename + ".db");
            getChunkPS = con.prepareStatement(getChunkStatement);
            setChunkPS = con.prepareStatement(setChunkStatement);
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public void initWorld(World world) {
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + world.id + " ( id BIGINT UNSIGNED not NULL, blocks BLOB, PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public ChunkArray loadChunkArray(String world, int key) {
        return null;
    }

    public void saveChunkArray(World world, int index) {
        // Grab chunk array by it's morton code index, save that mother. Yeah.
        try {
            setChunkPS.setString(1, world.id);
            //setChunkPS.setInt(2, )
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}