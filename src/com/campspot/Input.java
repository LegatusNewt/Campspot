package com.campspot;
import java.util.*;

/**
*	Name:			Input.java
*	Author:			Kenneth C. LaMarca
*	CreateDate:		04/01/2017
*	Description:	A file which will help GSON parse the json file into the
*					java objects we want
**/
public class Input
{
	public Search search;
	
	public List<Campsite> campsites;
	
	public List<GapRule> gapRules;
	
	public List<Reservation> reservations;

    /**
     * Campsites should contain a list of reservations
     */
	public void intializeData()
    {
        ArrayList<Reservation> tempRes;

        //If there are no rules in the json create an empty list of gaprules
        if(gapRules == null)
            gapRules = new ArrayList<GapRule>();

        for(Campsite c : campsites)
        {
            tempRes = new ArrayList<Reservation>();
            for(Reservation r : reservations)
            {
                r.setDateObjects(); //GSON does not trigger setters even on private variables?
                //Add reservation to temp list if the campsite id matches
                if(r.getCampSiteID() == c.getId())
                {
                    tempRes.add(r);
                }
            }
            c.setReservations(tempRes);
            reservations.removeAll(tempRes);
        }
    }
	
	
	/**
	*	Return the entire input object in a string format displaying objects created
	*/
	public String toString()
	{
		String out;
		
		//Search object
		out = "Search : " + "\n" + "Start Date :" + search.getStartDate()
			+ "\n" + "End Date : " + search.getEndDate();
		
		//Campsite objects
		out = out + "\n";
		for(Campsite c : campsites)
		{
			out = out + "\n" + "Camp ID : " + c.getId()
					+ "\n" + "Camp Name : " + c.getName();
		}
		
		//GapRules
		out = out + "\n";
		for(GapRule g : gapRules)
		{
			out = out + "\n" + "Gap Rule : " + g.getGapSize();
		}
		
		//Reservations
		out = out + "\n";
		for(Reservation r : reservations)
		{
			out = out + "\n" + "Start Date : " + r.getStartDate()
				+ "		End Date : " + r.getEndDate()
				+ "		Campsite : " + r.getCampSiteID();
		}

		return out;			
	}
}