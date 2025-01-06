package subsystemone;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;

@FunctionalInterface
public interface Operation {
    ObjectMessage execute(Serializable payload, JMSContext context) throws JMSException;
}
