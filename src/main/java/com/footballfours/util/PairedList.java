package com.footballfours.util;

import static java.util.Objects.requireNonNull;

import java.util.AbstractList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class PairedList<E> extends AbstractList<Pair<E, E>>
{
    private final List<E> myLeftList;
    private final List<E> myRightList;

    public PairedList( final List<E> leftList, final List<E> rightList )
    {
        myLeftList = requireNonNull( leftList );
        myRightList = requireNonNull( rightList );
    }

    @Override
    public Pair<E, E> get( final int index )
    {
        return Pair.of( myLeftList.size() <= index ? null : myLeftList.get( index ),
                        myRightList.size() <= index ? null : myRightList.get( index ) );
    }

    @Override
    public int size()
    {
        return Math.max( myLeftList.size(), myRightList.size() );
    }
}
