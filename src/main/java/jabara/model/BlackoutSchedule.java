/**
 * 
 */
package jabara.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jabaraster
 */
public class BlackoutSchedule implements Serializable {
    private static final long serialVersionUID = -389663224693454272L;

    private Date              start;
    private Date              end;
    private final String      group            = "";
}
