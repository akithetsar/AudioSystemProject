package subsystemone;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import javax.jms.Message;

@FunctionalInterface
public interface Operation {
    ObjectMessage execute(Message payload, JMSContext context) throws JMSException;
}
