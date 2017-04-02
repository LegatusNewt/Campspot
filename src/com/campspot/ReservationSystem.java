package com.campspot;
import com.google.gson.*;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
*	Name:			ReservationSystem.java
*	Author:			Kenneth C. LaMarca
*	CreateDate:		04/01/2017
*	Description:	Main file for the reservation system. Will handle json file input,
*					and System output. Will also handle running tests given the "test"
*					command line argument. See readme for details.
*/
public class ReservationSystem
{    
	private HashMap reservationSchedule;	

    public static void main(String[]args)
    {       
		int state;
		String[] tests = null;
		String filename; 
		Reader reader;
		Gson gson = new Gson();
		Input myInput;
		List<Campsite> results;
        ReservationSystem mySystem = new ReservationSystem();
	
		//There are two possible states, the test state and the run state
		//The run state will be initiaited by the existence of only one argument
		//The test state requires two arguments, the json file and the test / testset being run

        //Check the number of args and set the state based on that
		if(args.length == 1){
			state = 0;
		}
		else if(args.length >= 2){
			state = 1;
			tests = Arrays.copyOfRange(args,1,args.length);
        }
		else{
			state = 2;
		}

		//State 2 is the error state
		if(state != 2)
		{
		    //Read json file into Input.java class
			try
            {
				reader = new FileReader(args[0]);	
				myInput = gson.fromJson(reader,Input.class);
				//System.out.println(myInput.toString());

                if(state == 0) {
                    results = mySystem.reservationSearch(myInput);
                    for(Campsite c : results)
                        System.out.println(c.getName());
                }
                else
                {
                    if(tests != null) {
                        for (String test : tests) {
                            System.out.println(test);
                            mySystem.runTest(test);
                        }
                    }
                }
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("Not enough arguments");
		}
    }

    /**
     * Runs the search for valid campsites for this reservation
     * @param myInput
     * @return
     */
    public List<Campsite> reservationSearch(Input myInput)
    {
        List<Campsite> results = new ArrayList<>(); //this is just to hold me over
        myInput.intializeData();

        for(Campsite c : myInput.campsites)
        {
            if(c.validReservation(myInput.search,myInput.gapRules))
                results.add(c);
        }
        return results;
    }

    /**
     * Testing handler
     * @param test
     */
    public void runTest(String test)
    {
        switch(test)
        {
            case "test1":
                testReservationComparable();
                break;
            case "test2":
                testReservationsSort();
                break;
            case "test3":
                testReservationEquals();
                break;
        }
    }

    /**
     * Test Reservation Comparable with hardcoded data
     */
    public void testReservationComparable()
    {

        Reservation resA = new Reservation(0,"2010-02-01","2010-02-05");
        Reservation resB = new Reservation(0,"2010-02-06","2010-02-08");
        System.out.println("Test Reservation Comparable Expected : -1");
        System.out.println("Result : " + resA.compareTo(resB));
    }

    /**
     * Test sorting a List of reservations
     */
    public void testReservationsSort()
    {
        Reservation resA = new Reservation(0,"2016-06-03","2016-06-04");
        Reservation resB = new Reservation(0,"2016-06-13","2016-06-16");
        Reservation resC = new Reservation(0,"2016-06-07","2016-06-09");

        ArrayList<Reservation> myList = new ArrayList<Reservation>();
        myList.add(resA); myList.add(resB); myList.add(resC);

        Collections.sort(myList);
        System.out.println(myList);
    }

    /**
     * Test Reservation equals
     */
    public void testReservationEquals()
    {
        Reservation resA = new Reservation(0,"2010-02-01","2010-02-05");
        Reservation resB = new Reservation(0,"2010-02-06","2010-02-08");
        System.out.println("Test Reservation Comparable Expected : false");
        System.out.println("Result : " + resA.equals(resB));
    }
}