
package jsf.managedbean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author zihua
 */
@Named(value = "dateRangeFilter")
@ApplicationScoped
public class DateRangeFilter implements Serializable {

    public DateRangeFilter() {
    }
    
    private static final Logger LOG = Logger.getLogger(DateRangeFilter.class.getName());

    public boolean filterByDate(Object value, Object filter, Locale locale) {

        String filterText = (filter == null) ? null : filter.toString().trim();
        
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }
        if (value == null) {
            return false;
        }

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        Date filterDate = (Date)value;
        Date dateFrom;
        Date dateTo;
        
        try {
            String fromPart = filterText.substring(0, filterText.indexOf("-"));
            String toPart = filterText.substring(filterText.indexOf("-") + 1);
            dateFrom = fromPart.isEmpty() ? null : df.parse(fromPart);
            dateTo = toPart.isEmpty() ? null : df.parse(toPart);
        } catch (ParseException pe) {
            LOG.log(Level.SEVERE, "unable to parse date: " + filterText, pe);
            return false;
        }

        return (dateFrom == null || filterDate.after(dateFrom) || filterDate.equals(dateFrom)) && (dateTo == null || filterDate.before(dateTo) || filterDate.equals(dateTo));
    }
     
}
