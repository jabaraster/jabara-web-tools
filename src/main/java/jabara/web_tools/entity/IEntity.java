/**
 * 
 */
package jabara.web_tools.entity;

import java.util.Date;

/**
 * @author jabaraster
 */
public interface IEntity {

    /**
     * @return 生成日.
     */
    Date getCreated();

    /**
     * @return ID値. 永続化前の場合はnull.
     */
    Long getId();

    /**
     * @return 最終更新日.
     */
    Date getUpdated();
}
