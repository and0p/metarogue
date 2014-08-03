package io.metarogue.game.gameworld;

import java.util.Hashtable;

/**
 * MetaRogue class
 * User: andrew
 * Date: 7/26/13
 * Time: 1:56 PM
 */
public class WorldTable<K, V> extends Hashtable {
    Class<V> tableClass;

    //ChunkArray()

    public WorldTable(Class<V> tableClass) {
        this.tableClass = tableClass;
    }

    @Override
    public synchronized V get(Object key) {
        if(key==null) return newValue();
        Object v = super.get(key);
        if(v == null){
            return newValue();
        }
        return null;
    }

    private V newValue() {
        try {
            return tableClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException (e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException (e);
        }
    }

}
