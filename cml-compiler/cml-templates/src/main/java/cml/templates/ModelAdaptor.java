package cml.templates;

import org.jooq.lambda.Seq;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.Optional;

public class ModelAdaptor extends ObjectModelAdaptor
{
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName)
        throws STNoSuchPropertyException
    {
        final Object value = super.getProperty(interp, self, o, property, propertyName);

        if (value instanceof Optional)
        {
            @SuppressWarnings("rawtypes")
            final Optional optionalValue = (Optional) value;

            //noinspection unchecked
            return optionalValue.orElse(null);
        }
        else if (value instanceof Seq)
        {
            @SuppressWarnings("rawtypes")
            final Seq seq = (Seq) value;

            //noinspection unchecked
            return seq.toList();
        }
        else
        {
            return value;
        }
    }
}
