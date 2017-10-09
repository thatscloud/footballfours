package com.footballfours.entity;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.footballfours.entity.constant.ParameterParameterCode;

@Entity
@Table( name = "parameter" )
public class Parameter
{
    private UUID myParameterId;
    private ParameterParameterCode myParameterCode;
    private String myParameterText;

    @Id
    @GeneratedValue
    @Column( name = "id_parameter" )
    public UUID getParameterId()
    {
        return myParameterId;
    }

    public void setParameterId( final UUID parameterId )
    {
        myParameterId = parameterId;
    }

    @Column( name = "cd_parameter" )
    @Enumerated( EnumType.STRING )
    public ParameterParameterCode getParameterCode()
    {
        return myParameterCode;
    }

    public void setParameterCode( final ParameterParameterCode parameterCode )
    {
        myParameterCode = parameterCode;
    }

    @Column( name = "tx_parameter" )
    public String getParameterText()
    {
        return myParameterText;
    }

    public void setParameterText( final String parameterText )
    {
        myParameterText = parameterText;
    }

    @Override
    public int hashCode()
    {
        return myParameterId == null ? 0 : myParameterId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof Parameter &&
               Objects.equals( myParameterId,
                              ( (Parameter)obj ).myParameterId );
    }
}
