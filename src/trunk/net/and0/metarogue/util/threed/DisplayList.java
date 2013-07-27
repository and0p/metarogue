package net.and0.metarogue.util.threed;

// Class to hold display list vector3d and worldmorton, possibly

public class DisplayList {

    Vector3d vector3d;
    int displayListNumber;

    public DisplayList(Vector3d vector3d) {
        this.vector3d = vector3d;
    }

    public DisplayList(int x, int y, int z, int displayListNumber) {
        vector3d = new Vector3d(x,y,z);
        this.displayListNumber = displayListNumber;
    }

}