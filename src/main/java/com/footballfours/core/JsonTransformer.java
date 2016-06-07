package com.footballfours.core;

import java.util.stream.Stream;

import spark.ResponseTransformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTransformer implements ResponseTransformer
{

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public String render( Object model ) throws JsonProcessingException
    {

        if ( model == null )
        {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        if ( model instanceof Stream )
        {
            ( (Stream)model ).forEach( submodel -> {
                try
                {
                    builder.append( mapper.writeValueAsString( submodel ) );
                }
                catch ( Exception e )
                {
                    throw new RuntimeException( "bah", e );
                }
            } );
            return "{" + builder.toString() + "}";
        }
        return mapper.writeValueAsString( model );
    }

}