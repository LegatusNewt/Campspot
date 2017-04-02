package com.campspot;

import java.time.*;

/**
*	Name:			Reservation.java
*	Author:			Kenneth C. LaMarca
*	CreateDate:		04/01/2017
*	Description:	Reservation object which stores the start and end dates
*					of a reservation as well as the int id of the relevant campsite object
*/
public class Reservation implements Comparable<Reservation>
{
	private String startDate;
	private String endDate;
	private LocalDate startDateObject = LocalDate.now();
	private LocalDate endDateObject = LocalDate.now();
	private int campsiteId;

	public Reservation()
	{

	}
	public Reservation(int campsite,String start, String end)
    {
        this.campsiteId = campsite;
        this.startDate = start;
        this.endDate = end;
        startDateObject = LocalDate.parse(startDate);
        endDateObject = LocalDate.parse(endDate);
    }

    /**
     * Make sure date objects are matched with the strings
     *  the strings are there for GSON parsing purposes
     *  I would have to write a customized GSON parser for
     *  the LocalDate class to have GSON handle this properly.
     */
    public void setDateObjects()
    {
        if(startDate != null && endDate != null)
        {
            startDateObject = LocalDate.parse(startDate);
            endDateObject = LocalDate.parse(endDate);
        }
    }
	public void setStartDate(String startedDate)
	{
		this.startDate = startedDate;
		startDateObject = LocalDate.parse(startedDate);
	}
	public String getStartDate()
	{
		return startDate;
	}
	public void setEndDate(String endedDate)
	{
		this.endDate = endedDate;
		endDateObject = LocalDate.parse(endedDate);
	}

	public void setStartDateObject(LocalDate s)
    {
        startDateObject = s;
    }
    public LocalDate getStartDateObject()
    {
        return startDateObject;
    }
    public void setEndDateObject(LocalDate e)
    {
        endDateObject = e;
    }
    public LocalDate getEndDateObject()
    {
        return endDateObject;
    }

	public String getEndDate()
	{
		return endDate;
	}
	public int getCampSiteID()
	{
		return campsiteId;
	}
	public void setCampSiteID(int i)
	{
		campsiteId = i;
	}

    @Override
    public int compareTo(Reservation o) {
	    //These reservations are the same
        if(startDateObject.compareTo(o.getStartDateObject()) == 0
                && endDateObject.compareTo(o.getEndDateObject()) ==0)
        {
            return 0;
        }
        //This reservation's start date is after the end date of the compared
        else if(startDateObject.compareTo(o.getEndDateObject()) > 0)
        {
            return 1;
        }
        //This reservation's end date is before the start date of the compared
        else if(endDateObject.compareTo(o.getStartDateObject()) < 0)
        {
            return -1;
        }
        //If one reservation is contained within another they are equivalent
        else if((startDateObject.compareTo(o.getStartDateObject()) > 0 &&
                endDateObject.compareTo(o.getEndDateObject()) < 0) ||
                ((startDateObject.compareTo(o.getStartDateObject()) < 0) &&
                endDateObject.compareTo(o.getEndDateObject()) > 0))
        {
            return 0;
        }
        //They must overlap which makes them the same in this comparison
        else{
            return 0;
        }
    }

    /**
     * equals implementation when comparing two reservations
     * @param o
     * @return
     */
    public boolean equals(Object o)
    {
        if(!(o instanceof Reservation))
            return false;
        //These reservations are the same
        if(startDateObject.compareTo(((Reservation)o).getStartDateObject()) == 0
                && endDateObject.compareTo(((Reservation)o).getEndDateObject()) ==0)
        {
            return true;
        }
        //If one reservation is contained within another they are equivalent
        else if((startDateObject.compareTo(((Reservation)o).getStartDateObject()) > 0 &&
                endDateObject.compareTo(((Reservation)o).getEndDateObject()) < 0) ||
                ((startDateObject.compareTo(((Reservation)o).getStartDateObject()) < 0) &&
                        endDateObject.compareTo(((Reservation)o).getEndDateObject()) > 0))
        {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * ToString for a reservation in the format
     * startdate - endate
     * @return
     */
    public String toString()
    {
        return "\n" + "Start Date : " + startDate + " - " + "End Date : " + endDate;
    }
}