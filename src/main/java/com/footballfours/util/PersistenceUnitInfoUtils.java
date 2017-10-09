package com.footballfours.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PersistenceUnitInfoUtils
{
    private PersistenceUnitInfoUtils(){}

    private static final Logger theLogger =
        LoggerFactory.getLogger( PersistenceUnitInfoUtils.class );

    public static PersistenceUnitInfo createPersistenceUnitInfo( final DataSource dataSource )
    {
        return new PersistenceUnitInfo()
        {
            private List<URL> myJarFileUrls;

            {
                try
                {
                    final ClassLoader classLoader =
                        PersistenceUnitInfoUtils.class.getClassLoader();
                    if( classLoader instanceof URLClassLoader )
                    {
                        myJarFileUrls = Arrays.asList( ( (URLClassLoader)classLoader ).getURLs() );
                    }
                    else
                    {
                        myJarFileUrls = Collections.list( classLoader.getResources( "" ) );
                    }
                    theLogger.info( "Hibernate will scan the following urls: " + myJarFileUrls );
                }
                catch( final IOException e )
                {
                    throw new UncheckedIOException( e );
                }
            }

            @Override
            public String getPersistenceUnitName()
            {
                return "com.footballfours";
            }

            @Override
            public String getPersistenceProviderClassName()
            {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType()
            {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource()
            {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource()
            {
                return dataSource;
            }

            @Override
            public List<String> getMappingFileNames()
            {
                return Collections.emptyList();
            }

            @Override
            public List<URL> getJarFileUrls()
            {
                return myJarFileUrls;
            }

            @Override
            public URL getPersistenceUnitRootUrl()
            {
                return null;
            }

            @Override
            public List<String> getManagedClassNames()
            {
                return Collections.emptyList();
            }

            @Override
            public boolean excludeUnlistedClasses()
            {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode()
            {
                return null;
            }

            @Override
            public ValidationMode getValidationMode()
            {
                return null;
            }

            @Override
            public Properties getProperties()
            {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion()
            {
                return null;
            }

            @Override
            public ClassLoader getClassLoader()
            {
                return null;
            }

            @Override
            public void addTransformer( final ClassTransformer transformer )
            {

            }

            @Override
            public ClassLoader getNewTempClassLoader()
            {
                return null;
            }
        };
    }
}
