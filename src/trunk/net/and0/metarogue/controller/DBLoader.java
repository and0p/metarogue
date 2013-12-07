package net.and0.metarogue.controller;

import net.and0.metarogue.model.gameworld.ChunkArray;
import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.MortonCurve;

import java.nio.ByteBuffer;
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

        getChunkStatement = "SELECT * FROM test WHERE id=? LIMIT 1";
        setChunkStatement = "INSERT OR REPLACE INTO test VALUES(?, ?)";
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
            String sql = "CREATE TABLE IF NOT EXISTS " + "test" + " ( id BIGINT UNSIGNED not NULL, blocks BLOB, PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ChunkArray loadChunkArray(World world, int x, int z) {
        int key = MortonCurve.getMorton(x, z);
        try {
            //getChunkPS.setString(1, world.id);
            getChunkPS.setInt(1, key);
            ResultSet rs = getChunkPS.executeQuery();
            if(!rs.next()) return null;
            ByteBuffer bb = ByteBuffer.wrap(rs.getBytes("blocks"));
            return(new ChunkArray(x, z, world.worldHeight, bb));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChunkArray loadChunkArray(World world, int i) {
        try {
            //getChunkPS.setString(1, world.id);
            getChunkPS.setInt(1, i);
            ResultSet rs = getChunkPS.executeQuery();
            if(!rs.next()) return null;
            ByteBuffer bb = ByteBuffer.wrap(rs.getBytes("blocks"));
            return(new ChunkArray(MortonCurve.getX(i), MortonCurve.getY(i), world.worldHeight, bb));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveChunkArray(World world, int index) {
        // Grab chunk array by it's morton code index, save that mother. Yeah.
        try {
            //setChunkPS.setString(1, world.id);
            setChunkPS.setInt(1, index);
            byte[] ba = world.getChunkArray(index).getBytes().array();
            setChunkPS.setBytes(2, ba);
            setChunkPS.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}