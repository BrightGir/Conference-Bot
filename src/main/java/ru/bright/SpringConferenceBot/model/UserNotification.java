package ru.bright.SpringConferenceBot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name="userNotificationTable")
public class UserNotification {

    @Getter
    @Setter
    private boolean isNotified;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="seminar_id", referencedColumnName = "id")
    @Getter
    @Setter
    private ScienceSeminar seminar;

    @ManyToOne
    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private boolean isActive;

    @Getter
    @Setter
    private int hour;

    //@Override
            //        public boolean equals(Object o) {
        //        if (this == o) return true;
        //        if (o == null || getClass() != o.getClass()) return false;

        //        UserNotification that = (UserNotification) o;

        //        return id == that.id;
        //    }

    //        @Override
            //        public int hashCode() {
        //        return (int) (id ^ (id >>> 32));
        //    }

}
