package net.and0.metarogue.util.threed;

import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.NumUtil;
import net.and0.metarogue.view.DisplayListBuilder;

import java.util.ArrayList;

// 3D array of display lists,
public class DisplayListBox {

    // Center of "box" of display lists around player
    Vector3d center = new Vector3d(0,0,0);
    // Change last calculated
    Vector3d delta = new Vector3d(0,0,0);
    // Lowest "corner" of the box
    Vector3d corner = new Vector3d(0,0,0);

    // This vector3d is for the "0" point of the box at any time. So assuming the order is
    // (x- to x+) 34512 then the 0 point would be 3
    Vector3d zero = new Vector3d(0,0,0);

    // Dimensions of box, ((view distance * 2) + 1)
    int boxDim;

    // ArrayList of stuff to update. Yeah.
    ArrayList<DisplayList> toBuild = new ArrayList<DisplayList>();

    // The display lists themselves? No idea.
    DisplayList displayLists[][][];

    // Reference to world we're working with
    World world;

    public DisplayListBox(World world, Vector3d center, int viewdistance) {
        boxDim = viewdistance*2+1;
        this.center = center;
        corner.set(center.getX()-viewdistance, center.getY()-viewdistance, center.getZ()-viewdistance);
        displayLists = new DisplayList[boxDim][boxDim][boxDim];
        for(int x = 0; x < boxDim; x++) {
            for(int y = 0; y < boxDim; y++) {
                for(int z = 0; z < boxDim; z++) {
                    displayLists[x][y][z] = new DisplayList(corner.getX()+x,corner.getY()+y,corner.getZ()+z, NumUtil.unflattenArray3dBox(x,y,z,boxDim)+1);
                    toBuild.add(displayLists[x][y][z]);
                }
            }
        }
    }

    public void updateAll() {
        for(int x = 0; x < boxDim; x++) {
            for(int y = 0; y < boxDim; y++) {
                for(int z = 0; z < boxDim; z++) {
                    displayLists[x][y][z] = new DisplayList(corner.getX()+x,corner.getY()+y,corner.getZ()+z, NumUtil.unflattenArray3dBox(x,y,z,boxDim));
                    toBuild.add(displayLists[x][y][z]);
                }
            }
        }
    }

    public void update(World world, Vector3d newCenter) {
        // Get delta / change since last checked
        delta = Vector3d.getDelta(center, newCenter);

        //Check if anything is even different, if not then return
        if(center.getX() == newCenter.getX() && center.getY() == newCenter.getY() && center.getZ() == newCenter.getZ()) return;

        // If any axis of the delta is larger than the dimensions of the box, then everything needs to be updated anyway
        if(Math.abs(delta.getX()) > boxDim || Math.abs(delta.getY()) > boxDim || Math.abs(delta.getZ()) > boxDim) {
            updateAll();
            return;
        }



        // Update where the "center" is
        center = newCenter;
    }

    public void buildAll() {
        for(DisplayList dL : toBuild) {
            DisplayListBuilder.buildCubeDisplayList(dL.displayListNumber, world, dL.vector3d);
        }
    }

    public void buildAll(World world) {
        for(DisplayList dL : toBuild) {
            DisplayListBuilder.buildCubeDisplayList(dL.displayListNumber, world, dL.vector3d);
        }
    }

    // "Leapfrog" number. 10 with a dimension size of 8 (base-0) should be 1 for example. -2 should be 5.
    int getArrayNumber(int num) {
        if(num > boxDim-1) return num - boxDim;
        if(num < 0) return num + boxDim;
        return num;
    }

    // Get actual 3d coordiantes of a part of this array from the center
    Vector3d getActualCoordinates(Vector3d arrayposition) {
        return null;
    }

}