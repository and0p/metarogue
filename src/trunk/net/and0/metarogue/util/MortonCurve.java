package net.and0.metarogue.util;

public class MortonCurve {

	public MortonCurve() {
		// TODO Auto-generated constructor stub
	}
	
    public static int getMorton(int x, int y)
    {
            int newX = x;
            int newY = y;
            int morton = 0;
           
            for (int i=0; i<16; i++)
            {
                    int mask = 1 << (i);
                    morton += (newY&mask)<<(i+1);
                    morton += (newX&mask)<<i;
            }      
            return morton;
    }


}
