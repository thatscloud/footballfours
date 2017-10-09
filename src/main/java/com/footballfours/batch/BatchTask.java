package com.footballfours.batch;

import java.sql.Connection;

import org.h2.jdbcx.JdbcConnectionPool;

import com.footballfours.persist.RunAgainstDatabase;

public abstract class BatchTask implements Runnable
{
    private final String[] myArgs;

    public BatchTask( final String[] args )
    {
        myArgs = args;
    }

    @Override
    public final void run()
    {
        final String sqlUsername = "football";
        final String sqlPassword = "fours";
        final String encryptionPassword = "footballfours";
        final JdbcConnectionPool connectionPool = JdbcConnectionPool.create(
            "jdbc:h2:tcp://localhost:4569/./footballfours;CIPHER=AES",
            sqlUsername,
            encryptionPassword + " " + sqlPassword );
        RunAgainstDatabase.run( connectionPool, connection ->
        {
            try
            {
                runTask( connection, myArgs );
            }
            catch( final RuntimeException e )
            {
                throw e;
            }
            catch( final Exception e )
            {
                throw new RuntimeException( e );
            }
        } );
        connectionPool.dispose();
    }

    protected abstract void runTask( final Connection connection,
                                     final String[] args ) throws Exception;
}
