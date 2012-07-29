/**
 * 
 */
package jabara.web_tools.service;

import jabara.web_tools.entity.BlackoutSchedule;

import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * @author jabara
 * 
 */
@ImplementedBy(BlackoutScheduleServiceImpl.class)
public interface IBlackoutScheduleService {

    /**
     * @param pScheduleText
     * @return
     */
    List<BlackoutSchedule> parse(String pScheduleText);
}
