package io.github.httpcarlsonkellie.engineeringmultitool;

import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by Jaimie on 9/10/2016.
 */
public class SortableDataPoint<T extends Comparable<T>> implements Comparable<SortableDataPoint<T>>  {
    public T x;
    public T y;

    public SortableDataPoint (T strain, T stress){
        x = strain;
        y = stress;
    }

    public T getX(){
        return x;
    }

    public T getY(){
        return y;
    }

    public int compareTo(SortableDataPoint<T> that){
        return this.x.compareTo(that.x);
    }

}
