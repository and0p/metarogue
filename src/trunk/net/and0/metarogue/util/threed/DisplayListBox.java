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

    // This position is for the "0" point of the box at any time. So assuming the order is
    // (x- to x+) 34512 then the 0 point would be 3
    Vector3d zero = new Vector3d(0,0,0);

    // View distance in options
    int viewDistance;
    // Dimensions of box, ((view distance * 2) + 1)
    int boxDim;

    // ArrayList of stuff to update. Yeah.
    ArrayList<DisplayList> toBuild = new ArrayList<DisplayList>();

    // The display lists themselves? No idea.
    DisplayList displayLists[][][];

    // Reference to world we're working with
    World world;

    public DisplayListBox(World world, Vector3d center, int viewDistance) {
        this.world = world;
        this.viewDistance = viewDistance;
        boxDim = viewDistance*2+1;
        this.center = center;
        corner.set(center.getX()-viewDistance, center.getY()-viewDistance, center.getZ()-viewDistance);
        displayLists = new DisplayList[boxDim][boxDim][boxDim];
        for(int x = 0; x < boxDim; x++) {
            for(int y = 0; y < boxDim; y++) {
                for(int z = 0; z < boxDim; z++) {
                    displayLists[x][y][z] = new DisplayList(corner.getX()+x,corner.getY()+y,corner.getZ()+z, NumUtil.unflattenArray3dBox(x,y,z,boxDim)+1);
                    toBuild.add(displayLists[x][y][z]);
                }
            }
        }
        buildMeshes();
    }

    void setCorner() {
        corner.set(center.getX()-viewDistance, center.getY()-viewDistance, center.getZ()-viewDistance);
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

        // Now that those checks are done, we can set the center in the new place...
        center = newCenter;
        // ...and set the corner as well
        setCorner();

        // Start updating on X axis first
        for(int x = zero.getX(); x < zero.getX() + delta.getX(); x++) {

        }

        // Update where the "center" is
        center = newCenter;
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
        buildMeshes();
    }

    void moveDisplayList(DisplayList dl) {
        int x = 0; int y = 0; int z = 0;
        if(delta.getX() > 0) x = boxDim;
        if(delta.getX() < 0) x = boxDim*-1;
        if(delta.getY() > 0) y = boxDim;
        if(delta.getY() < 0) y = boxDim*-1;
        if(delta.getZ() > 0) z = boxDim;
        if(delta.getZ() < 0) z = boxDim*-1;
        dl.position.move(x,y,z);
    }

    public void buildMeshes() {
        for(DisplayList dL : toBuild) {
            DisplayListBuilder.buildCubeDisplayList(dL.displayListNumber, world, dL.position);
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