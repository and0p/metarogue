package net.and0.metarogue.util.threed;

import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.NumUtil;
import net.and0.metarogue.util.settings.DisplaySettings;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// 3D array of display lists,
public class DisplayListBox {

    // Center of "box" of display lists around player
    Vector3d center = new Vector3d();
    // Change last calculated
    Vector3d delta = new Vector3d();
    // Actual direction as 1, 0, or -1
    Vector3d direction = new Vector3d();
    // Actual direction for if statements to move, as -1 or 1
    Vector3d ifDir = new Vector3d();


    // Lowest "corner" of the box
    Vector3d corner = new Vector3d();

    // This position is for the "0" point of the box at any time. So assuming the order is
    // (x- to x+) 34512 then the 0 point would be 3
    Vector3d zero = new Vector3d();

    // View distance in options
    int viewDistance;
    // Dimensions of box, ((view distance * 2) + 1)
    int boxDim;

    // ArrayList of stuff to update. Yeah.
    ArrayList<DisplayList> toBuild = new ArrayList<DisplayList>();
    // ArrayList of Futures for CubeMesh objects, waiting to fill the video card as display lists
    ArrayList<DisplayList> toSend = new ArrayList<DisplayList>();
    // ArrayList of stuff to remove from the send list, since I can't count on them all to be finished
    ArrayList<DisplayList> finished = new ArrayList<DisplayList>();

    // The display lists themselves? No idea.
    DisplayList displayLists[][][];

    // Reference to world we're working with
    World world;

    // Executor service
    ExecutorService pool;

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
                    displayLists[x][y][z] = new DisplayList(corner.getX()+x,corner.getY()+y,corner.getZ()+z, NumUtil.unflattenArray3dBox(x,y,z,boxDim)+1, this);
                    toBuild.add(displayLists[x][y][z]);
                }
            }
        }
        pool = Executors.newFixedThreadPool(DisplaySettings.meshCreationThreadCount);
        buildFutures();
        world.chunkChanges = false;
    }

    void setCorner() {
        corner.set(center.getX()-viewDistance, center.getY()-viewDistance, center.getZ()-viewDistance);
    }

    void getDirection() {
        direction.set(0,0,0);
        if(delta.getX() > 0) direction.setX(1);
        if(delta.getX() < 0) direction.setX(-1);
        if(delta.getY() > 0) direction.setY(1);
        if(delta.getY() < 0) direction.setY(-1);
        if(delta.getZ() > 0) direction.setZ(1);
        if(delta.getZ() < 0) direction.setZ(-1);
        ifDir.set(1,1,1);
        if(delta.getX() < 0) ifDir.setX(-1);
        if(delta.getX() < 0) ifDir.setX(-1);
        if(delta.getX() < 0) ifDir.setX(-1);
    }

    public void update(Vector3d newCenter) {
        // Get delta / change since last checked
        delta = Vector3d.getDelta(center, newCenter);

        // Change the direction of the box accordingly
        getDirection();

        // Check if anything is even different, if not then return
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

        // Start updating on X axis first, check direction
        if(delta.getX() > 0) {
            for(int x = zero.getX(); x < zero.getX() + delta.getX(); x++) {
                for(int y = zero.getY(); y < zero.getY() + boxDim; y++) {
                    for(int z = zero.getZ(); z < zero.getZ() + boxDim; z++) {
                        moveDisplayList(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                        toBuild.add(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                    }
                }
            }
        }
        if(delta.getX() < 0) {
            for(int x = zero.getX() - 1; x > zero.getX() + delta.getX() - 1; x--) {
                for(int y = zero.getY(); y < zero.getY() + boxDim; y++) {
                    for(int z = zero.getZ(); z < zero.getZ() + boxDim; z++) {
                        moveDisplayList(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                        toBuild.add(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                    }
                }
            }
        }

        // Then Z
        if(delta.getZ() > 0) {
            for(int z = zero.getZ(); z < zero.getZ() + delta.getZ(); z++) {
                for(int x = zero.getX(); x < zero.getX() + boxDim; x++) {
                    for(int y = zero.getY(); y < zero.getY() + boxDim; y++) {
                        moveDisplayList(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                        toBuild.add(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                    }
                }
            }
        }
        if(delta.getZ() < 0) {
            for(int z = zero.getZ() - 1; z > zero.getZ() + delta.getZ() - 1; z--) {
                for(int x = zero.getX(); x < zero.getX() + boxDim; x++) {
                    for(int y = zero.getY(); y < zero.getY() + boxDim; y++) {
                        moveDisplayList(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                        toBuild.add(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                    }
                }
            }
        }

        // Then Y
        if(delta.getY() > 0) {
            for(int y = zero.getY(); y < zero.getY() + delta.getY(); y++) {
                for(int z = zero.getZ(); z < zero.getZ() + boxDim; z++) {
                    for(int x = zero.getX(); x < zero.getX() + boxDim; x++) {
                        moveDisplayList(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                        toBuild.add(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                    }
                }
            }
        }
        if(delta.getY() < 0) {
            for(int y = zero.getY() - 1; y > zero.getY() + delta.getY() - 1; y--) {
                for(int z = zero.getZ(); z < zero.getZ() + boxDim; z++) {
                    for(int x = zero.getX(); x < zero.getX() + boxDim; x++) {
                        moveDisplayList(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                        toBuild.add(displayLists[getArrayNumber(x)][getArrayNumber(y)][getArrayNumber(z)]);
                    }
                }
            }
        }

        zero.set(getArrayNumber(zero.getX() + delta.getX()), getArrayNumber(zero.getY() + delta.getY()), getArrayNumber(zero.getZ() + delta.getZ()));



        buildFutures();
    }


    public void updateAll() {
        for(int x = 0; x < boxDim; x++) {
            for(int y = 0; y < boxDim; y++) {
                for(int z = 0; z < boxDim; z++) {
                    displayLists[x][y][z] = new DisplayList(corner.getX()+x,corner.getY()+y,corner.getZ()+z, NumUtil.unflattenArray3dBox(x,y,z,boxDim), this);
                    toBuild.add(displayLists[x][y][z]);
                }
            }
        }
        buildFutures();
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

    // Multithreading! I think! The display lists will tell the executor service here to build all the CubeMeshes
    public void buildFutures() {
        for(DisplayList dL : toBuild) {
            //DisplayListBuilder.buildCubeDisplayList(dL.displayListNumber, world, dL.position);
            dL.buildFutureCubeMesh();
            toSend.add(dL);
        }
        toBuild.clear();
    }

    // Send the built CubeMeshes to the video card as a display list
    public void sendMeshes() {
        for(int i = 0; i < toSend.size(); i++) {
            if(toSend.get(i).cubeMeshFuture.isDone()) {
                toSend.get(i).sendListToOpenGL();
                finished.add(toSend.get(i));
            }
        }
        toSend.removeAll(finished);
        finished.clear();
    }

    // "Leapfrog" number. 10 with a dimension size of 8 (base-0) should be 1 for example. -2 should be 5.
    int getArrayNumber(int num) {
        if(num > boxDim-1) return num - boxDim;
        if(num < 0) return num + boxDim;
        return num;
    }

    // Get corresponding display list from real-world coordinates
    Vector3d getDisplayListFromWorldCoordinates(int x, int y, int z) {
        // Get number, negative or positive, representing relationship with lowest (in all dimensions) "corner" of this box
        x -= corner.getX();
        y -= corner.getY();
        z -= corner.getZ();
        // Get what that makes for... oh god that's complicated and silly. Hope this comment helped!
        x = getArrayNumber(x + zero.getX());
        y = getArrayNumber(y + zero.getY());
        z = getArrayNumber(z + zero.getZ());

        return new Vector3d(x,y,z);
    }

    // Check if a Vector3d falls into the space this DisplayListBox takes up (or, the viewable / renderable area)
    boolean checkAgainstBounds(Vector3d v3d) {
        if( v3d.getX() >= corner.getX() && v3d.getX() < corner.getX() + viewDistance*2+1 ||
            v3d.getY() >= corner.getY() && v3d.getY() < corner.getY() + viewDistance*2+1 ||
            v3d.getZ() >= corner.getZ() && v3d.getZ() < corner.getZ() + viewDistance*2+1) { return true; }
        else { return false;}
    }

    // Cleanly end the executor service.
    public void close() {
        pool.shutdownNow();
    }

}