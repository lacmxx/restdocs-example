package lachy.training.restdocsexample.web.mapper;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class DateMapper {

    public OffsetDateTime toOffsetDateTime(Timestamp ts){
        if ( ts != null ){
            return OffsetDateTime.of( ts.toLocalDateTime().getYear(),
                    ts.toLocalDateTime().getMonthValue(),
                    ts.toLocalDateTime().getDayOfMonth(),
                    ts.toLocalDateTime().getHour(),
                    ts.toLocalDateTime().getMinute(),
                    ts.toLocalDateTime().getSecond(),
                    ts.toLocalDateTime().getNano(),
                    ZoneOffset.UTC);
        } else {
            return null;
        }
    }

    public Timestamp toTimestamp( OffsetDateTime offsetDateTime ){
        return offsetDateTime != null
                ? Timestamp.valueOf( offsetDateTime.toLocalDateTime() )
                : null;
    }

}
