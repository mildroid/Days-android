package com.mildroid.days.utils

import com.mildroid.days.domain.Event
import com.mildroid.days.domain.EventType
import kotlinx.datetime.Month
import java.time.DayOfWeek

object UpcomingEvents {

    private fun events(): List<Event> {
        return listOf(
            Event(
                title = "New Year",
                description = "New Year's Day",
                date = safeLocalDate(1, Month.JANUARY),
                image = "https://images.unsplash.com/photo-1546272192-c19942fa8b26?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=andreas-dress-wtxPbYHxa5I-unsplash.jpg&w=1920",
                type = EventType.PUBLIC

            ), Event(
                title = "Martin Luther King Jr",
                date = safeLocalDate(DayOfWeek.MONDAY, 3, Month.JANUARY),
                image = "https://images.unsplash.com/photo-1597704097219-0f6a59def63d?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=unseen-histories-bTF3gkd2L28-unsplash.jpg&w=1920",
                type = EventType.OFFICIAL

            ), Event(
                title = "Valentine's Day",
                date = safeLocalDate(14, Month.FEBRUARY),
                image = "https://images.unsplash.com/photo-1642524859252-448170eb1b11?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=olga-solodilova-Lp__FzaXvmo-unsplash.jpg&w=1920",
                type = EventType.PUBLIC

            ), Event(
                title = "Mother's Day",
                date = safeLocalDate(DayOfWeek.SUNDAY, 2, Month.MAY),
                image = "https://images.unsplash.com/photo-1524750117611-a3fec8c8791a?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=randy-rooibaatjie-bww_XccH_VU-unsplash.jpg&w=1920",
                type = EventType.NONPUBLIC

            ), Event(
                title = "Memorial Day",
                date = safeLocalDate(DayOfWeek.MONDAY, 4, Month.MAY),
                image = "https://images.unsplash.com/photo-1621898061855-5c3dc8af694b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=chad-madden-YaWCgn_5aHs-unsplash.jpg&w=1920",
                type = EventType.OFFICIAL

            ), Event(
                title = "Father's Day",
                date = safeLocalDate(DayOfWeek.SUNDAY, 3, Month.JUNE),
                image = "https://images.unsplash.com/photo-1566997258389-42d9b103a187?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=bambi-corro-cIFaRLZ4wvk-unsplash.jpg&w=1920",
                type = EventType.NONPUBLIC

            ), Event(
                title = "Independence Day",
                date = safeLocalDate(4, Month.JULY),
                image = "https://images.unsplash.com/photo-1473090826765-d54ac2fdc1eb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=frank-mckenna-JB92NeJSxW4-unsplash.jpg&w=1920",
                type = EventType.OFFICIAL

            )/*, Event(
                title = "Labor Day",
                date = safeLocalDate(DayOfWeek.MONDAY, 1, Month.SEPTEMBER),
                image = "",
                type = EventType.OFFICIAL

            ), Event(
                title = "Veterans Day",
                date = safeLocalDate(11, Month.NOVEMBER),
                image = "",
                type = EventType.OFFICIAL

            )*/, Event(
                    title = "Halloween",
                date = safeLocalDate(31, Month.OCTOBER),
                image = "https://images.unsplash.com/photo-1633380170808-9404cd630e82?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=simom-caban-ZKrEJMhEsr8-unsplash.jpg&w=1920",
                type = EventType.PUBLIC

            ), Event(
                title = "Thanksgiving",
                date = safeLocalDate(DayOfWeek.THURSDAY, 4, Month.NOVEMBER),
                image = "https://images.unsplash.com/photo-1533777857889-4be7c70b33f7?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=pablo-merchan-montes-Orz90t6o0e4-unsplash.jpg&w=1920",
                type = EventType.OFFICIAL

            ), Event(
                title = "Christmas",
                date = safeLocalDate(25, Month.DECEMBER),
                image = "https://images.unsplash.com/photo-1529973625058-a665431328fb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=roberto-nickson-5PQn41LFsQk-unsplash.jpg&w=1920",
                type = EventType.OFFICIAL
            )
        )
    }

    fun byDaysRemaining() = events().sortedBy {
        it.date.daysUntilNow()
    }

}
