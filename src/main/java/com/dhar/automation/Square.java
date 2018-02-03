package com.dhar.automation;

/**
 * @author Dharmendra.Singh
 */
public class Square implements Comparable {

    public int length;
    public int width;

    @Override
    public int compareTo(Object o) {
        Square sq = (Square) o;

        if(this.length * this.width > (sq.length * sq.width)){
            return 1;
        }
        if(this.length * this.width < (sq.length * sq.width)){
            return -1;
        }
        return 0;
    }
}
