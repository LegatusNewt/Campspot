package com.campspot;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
*	Name:			Campsite.java
*	Author:			Kenneth C. LaMarca
*	CreateDate:		04/01/2017
*	Description:	Campsite object which contains an id and campsite name
**/
public class Campsite
{
	private int id;
	private String name;
	private ArrayList<Reservation> reservations;

	public Campsite()
	{
		id = -1;
		name = "test";
	}
	public Campsite(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

    /**
     * Given a search object and a set of gap rules, check if a reservation is valid
     * for this campsite
     * @param mysearch
     * @return
     */
	public boolean validReservation(Search mysearch,List<GapRule> rules)
    {
        boolean valid = true;
        Reservation rightRes = null;
        Reservation leftRes = null;
        int tmpIndex = 0;

        //Make a new reservation out of the search
        Reservation newReservation = new Reservation(this.id,mysearch.getStartDate(),mysearch.getEndDate());

        //Check if the new reservation overlaps with any existing ones
        if(!reservations.contains(newReservation)) {
            reservations.add(newReservation);
            Collections.sort(reservations);
            tmpIndex = reservations.indexOf(newReservation);

            //verify neighbors with gaprules
            if(!rules.isEmpty()) {
                if (tmpIndex > 0) {
                    leftRes = reservations.get(tmpIndex - 1);
                    valid = checkRules(leftRes, newReservation, rules);
                }
                if (tmpIndex < reservations.size() - 1 && valid) {
                    rightRes = reservations.get(tmpIndex + 1);
                    valid = checkRules(rightRes, newReservation, rules);
                }
            }
            reservations.remove(newReservation);
        }
        else
        {
            valid = false;
        }
        return valid;
    }

    /**
     * Helper function checkRules, returns true if the reservation is following the gap rules compared
     * to the reservation its being compared to.
     * @param compareRes
     * @param newRes
     * @param rules
     * @return
     */
    public boolean checkRules(Reservation compareRes, Reservation newRes, List<GapRule> rules)
    {
        boolean valid = true;
        long tempGap;
        //Java 8 is fun because there is an enum known as ChronoUnit,
        //days between can be negative so you should take the absolute value
        //Check to see which way the days between should go.
        if(compareRes.compareTo(newRes) > 0)
            tempGap = Math.abs(ChronoUnit.DAYS.between(compareRes.getStartDateObject(),newRes.getEndDateObject()));
        else
            tempGap = Math.abs(ChronoUnit.DAYS.between(newRes.getStartDateObject(),compareRes.getEndDateObject()));

        //Gap size is not days between, its number of days without reservation
        //ex:   2016-06-10 to 2016-06-14 is 4 days between but 3 day gap.
        tempGap -= 1;

        //Loop through gap rules
        for(GapRule g : rules) {
            //If the gap is bigger than the gap rules allow then remove the reservation
            //validity is now false
            if (g.getGapSize() != -1) //make sure the gaprule doesn't have the default value
            { if (tempGap == g.getGapSize()) {
                    valid = false;
                }
            }
        }
        return valid;
    }

	public void setId(int id)
	{
		this.id = id;
	}
	public int getId()
	{
		return this.id;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return this.name;
	}
	public void setReservations(List<Reservation> reserve) { reservations = (ArrayList<Reservation>) reserve; }
    public ArrayList<Reservation> getReservations() { return reservations; }

    /**
     * ToString form of the campsite in the following format
     *  Campsite Name   : #Name
     *  Campsite ID     : #ID
     *  Reservations    :   StartDate - EndDate
     *                      StartDate - EndDate  etc .....
     * @return
     */
    public String toString()
    {
        return "Campsite Name : " + this.getName()
                    + "\n" + "Campsite ID : " + this.getId()
                    + "\n" + "Reservations : " + this.reservations.toString();
    }
}