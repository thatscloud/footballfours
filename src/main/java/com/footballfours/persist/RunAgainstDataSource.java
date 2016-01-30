package com.footballfours.persist;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.sql.DataSource;

public class RunAgainstDataSource
{
    public static void run( final DataSource dataSource,
                            final Consumer<Connection> consumer )
    {
        final List<Exception> exceptions = new ArrayList<>();
        try( final Connection connection = dataSource.getConnection() )
        {
            final boolean initialAutoCommit =
                connection.getAutoCommit();
            final int initialTransactionIsolation =
                connection.getTransactionIsolation();
            try
            {
                connection.setAutoCommit( false );
                connection.setTransactionIsolation(
                    Connection.TRANSACTION_SERIALIZABLE );
                try
                {
                    consumer.accept( connection );
                    connection.commit();
                }
                catch( final Exception e )
                {
                    exceptions.add( e );
                    try
                    {
                        connection.rollback();
                    }
                    catch( final Exception e1 )
                    {
                        exceptions.add( e1 );
                    }
                }
            }
            finally
            {
                try
                {
                    connection.setAutoCommit( initialAutoCommit );
                }
                catch( final Exception e )
                {
                    exceptions.add( e );
                }
                try
                {
                    connection.setTransactionIsolation( initialTransactionIsolation );
                }
                catch( final Exception e )
                {
                    exceptions.add( e );
                }
            }
        }
        catch( final Exception e )
        {
            final RuntimeException re;
            if( e instanceof RuntimeException )
            {
                re = (RuntimeException)e;
            }
            else
            {
                re = new RuntimeException( e );
            }
            exceptions.forEach( re::addSuppressed );
            throw re;
        }
        if( !exceptions.isEmpty() )
        {
            final Exception firstException = exceptions.remove( 0 );
            final RuntimeException re;
            if( firstException instanceof RuntimeException )
            {
                re = (RuntimeException)firstException;
            }
            else
            {
                re = new RuntimeException( firstException );
            }
            exceptions.forEach( re::addSuppressed );
            throw re;
        }
    }

    public static <S> S evaluate( final DataSource dataSource,
                                  final Function<Connection, S> function )
    {
        S returnValue = null;
        final List<Exception> exceptions = new ArrayList<>();
        try( final Connection connection = dataSource.getConnection() )
        {
            final boolean initialAutoCommit =
                connection.getAutoCommit();
            final int initialTransactionIsolation =
                connection.getTransactionIsolation();
            try
            {
                connection.setAutoCommit( false );
                connection.setTransactionIsolation(
                    Connection.TRANSACTION_SERIALIZABLE );
                try
                {
                    returnValue = function.apply( connection );
                    connection.commit();
                }
                catch( final Exception e )
                {
                    exceptions.add( e );
                    try
                    {
                        connection.rollback();
                    }
                    catch( final Exception e1 )
                    {
                        exceptions.add( e1 );
                    }
                }
            }
            finally
            {
                try
                {
                    connection.setAutoCommit( initialAutoCommit );
                }
                catch( final Exception e )
                {
                    exceptions.add( e );
                }
                try
                {
                    connection.setTransactionIsolation( initialTransactionIsolation );
                }
                catch( final Exception e )
                {
                    exceptions.add( e );
                }
            }
        }
        catch( final Exception e )
        {
            final RuntimeException re;
            if( e instanceof RuntimeException )
            {
                re = (RuntimeException)e;
            }
            else
            {
                re = new RuntimeException( e );
            }
            exceptions.forEach( re::addSuppressed );
            throw re;
        }
        if( !exceptions.isEmpty() )
        {
            final Exception firstException = exceptions.remove( 0 );
            final RuntimeException re;
            if( firstException instanceof RuntimeException )
            {
                re = (RuntimeException)firstException;
            }
            else
            {
                re = new RuntimeException( firstException );
            }
            exceptions.forEach( re::addSuppressed );
            throw re;
        }
        return returnValue;
    }
}
