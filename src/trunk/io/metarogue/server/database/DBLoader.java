package io.metarogue.server.database;

import io.metarogue.game.Game;
import io.metarogue.game.gameworld.ChunkArray;
import io.metarogue.game.gameworld.World;
import io.metarogue.util.MortonCurve;

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
    String gameName = null;
    Game game;

    String getChunkStatement = null;
    String setChunkStatement = null;

    PreparedStatement getChunkPS = null;
    PreparedStatement setChunkPS = null;

    public DBLoader(Game game) {
        this.game = game;

        getChunkStatement = "SELECT * FROM test WHERE id=? LIMIT 1";
        setChunkStatement = "INSERT OR REPLACE INTO test VALUES(?, ?)";

        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection("jdbc:sqlite:/metarogue/" + game.getName().toLowerCase() + "/worlds/worlds.db");
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
            e.printStackTrace();
        }
    }

    public ChunkArray loadChunkArray(World world, int x, int z) {
        int key = MortonCurve.getMorton(x, z);
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM " + world.id + " WHERE id=" + key +  " LIMIT 1";
            ResultSet rs = stmt.executeQuery(query);
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
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM " + world.id + " WHERE id=" + i +  " LIMIT 1";
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()) return null;
            ByteBuffer bb = ByteBuffer.wrap(rs.getBytes("blocks"));
            //return(new ChunkArray(x, z, world.worldHeight, bb));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveChunkArray(World world, int index) {
        // Grab chunk array by it's morton code index, save that mother. Yeah.
        try {
            Statement stmt = con.createStatement();
            byte[] ba = world.getChunkArray(index).getBytes().array();
            String query = "INSERT OR REPLACE INTO" + world.id + "VALUES(" + index + ", " + ba + ")";
            stmt.execute(query);
            //setChunkPS.setString(1, world.id);
            //setChunkPS.setInt(1, index);
            //setChunkPS.setBytes(2, ba);
            //setChunkPS.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}