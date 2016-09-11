package io.github.httpcarlsonkellie.engineeringmultitool;

import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by Jaimie on 9/10/2016.
 */
public class SortableDataPoint<T extends Comparable<T>> implements Comparable<SortableDataPoint<T>>  {
    public T x;
    public T y;

    public SortableDataPoint (T x, T y){
        x = x;
        y = y;
    }

    public T getX(){
        return x;
    }

    public T getY(){
        return y;
    }

    public int compareTo(SortableDataPoint<T> that){
        return this.y.compareTo(that.y);
    }

}
