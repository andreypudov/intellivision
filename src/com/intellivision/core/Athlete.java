/*
 * IntelliJustice Intelligent Referee Assistant System
 *
 * The MIT License
 *
 * Copyright 2011-2013 Andrey Pudov.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.intellivision.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * The athlete entity representation.
 *
 * @author    Andrey Pudov        <mail@andreypudov.com>
 * @version   0.00.00
 * %name      Athlete.java
 * %date      11:30:00 AM, Aug 03, 2013
 */
public class Athlete {

    private static final java.util.logging.Logger LOG
            = java.util.logging.Logger.getLogger(
            com.intellivision.core.Manifest.NAME);

    private final int     id;
    private final String  firstName;
    private final String  secondName;
    private final Date    birthday;
    private final boolean sex;       /* thue is male, false is female */

    /**
     * Constructs new Athletic object.
     *
     * @param id         the database id for the athlete (zero for new entry).
     * @param firstName  the firth name of the athlete.
     * @param secondName the second name of the athlete.
     * @param birthday   the birthday of the athlete.
     * @param sex        the sex of the athlete (true for male).
     */
    public Athlete(final int id, final String firstName,
            final String secondName, final long birthday, final boolean sex) {
        this.id          = id;
        this.firstName   = firstName;
        this.secondName  = secondName;
        this.birthday    = new Date(birthday);
        this.sex         = sex;
    }

    /**
     * Returns the database id for the athlete. If id value is zero, athlete
     * entry don't exists in database.
     *
     * @return the database id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the first name of the athlete.
     *
     * @return the first name of the athlete
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the second name of the athlete.
     *
     * @return the second name of the athlete.
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * Returns the birthday of the athlete.
     *
     * @return the birthday of the athlete.
     */
    public long getBirthday() {
        return birthday.getTime();
    }

    /**
     * Returns the sex of the athlete, where true represents male and false
     * represents female value.
     *
     * @return the sex of the athlete.
     */
    public boolean getSex() {
        return sex;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return    true if this object is the same as the obj argument;
     *            false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Athlete other = (Athlete) obj;
        if (this.id != other.id) {
            return false;
        }
        
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }

        if (!Objects.equals(this.secondName, other.secondName)) {
            return false;
        }

        if (!Objects.equals(this.birthday, other.birthday)) {
            return false;
        }

        if (this.sex != other.sex) {
            return false;
        }

        return true;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.id;

        return hash;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder builder   = new StringBuilder(126);
        DateFormat    formatter = new SimpleDateFormat("dd-MMM-yyyy");

        /* 01 S FistName LastName 01-01-1970 */
        builder.append(id).append(" "
                ).append((sex == true) ? "M" : "F").append(" "
                ).append(firstName).append(" "
                ).append(secondName).append(" "
                ).append(formatter.format(birthday));

        return builder.toString();
    }
}
