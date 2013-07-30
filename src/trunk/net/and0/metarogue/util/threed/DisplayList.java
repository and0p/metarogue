package net.and0.metarogue.util.threed;

// Class to hold display list position and worldmorton, possibly

public class DisplayList {

    Vector3d position;
    int displayListNumber;

    public DisplayList(Vector3d vector3d) {
        this.position = vector3d;
    }

    public DisplayList(int x, int y, int z, int displayListNumber) {
        position = new Vector3d(x,y,z);
        this.displayListNumber = displayListNumber;
    }

}