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
*					and System output. Will also handle running tests given an extra test
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
	    boolean verbose = false;

		//There are two possible states, the test state and the run state
		//The run state will be initiaited by the existence of only one argument
		//The test state requires two arguments, the json file and the test / testset being run

        //Check the number of args and set the state based on that
		if(args.length == 1){
			state = 0;
		}
		else if(args.length >= 2){
			state = 1;
			if(args[args.length-1].equals("-v")) {
			    verbose = true;
                tests = Arrays.copyOfRange(args, 1, args.length - 1);
            }
            else
                tests = Arrays.copyOfRange(args, 1, args.length);
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
                            mySystem.runTest(test,verbose);
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


    //______________________________TESTS____________________________\\

    /**
     * Testing handler
     * @param test
     */
    public void runTest(String test,boolean verbose)
    {
        switch(test)
        {
            case "AllTests":
                runReservationTests(verbose);
                runCampsiteTests(verbose);
                break;
            case "ReservationTests":
                runReservationTests(verbose);
                break;
            case "CampsiteTests":
                runCampsiteTests(verbose);
                break;
        }
    }

    /**
     * Run all reservation object tests
     */
    public void runReservationTests(boolean verbose)
    {
        System.out.println("Test Reservation Comparable : " + testReservationComparable(verbose));
        System.out.println("Test Reservation Sort : " + testReservationsSort(verbose));
        System.out.println("Test Reservation Equals : " + testReservationEquals(verbose));
    }

    /**
     * Run all Campsite object tests
     */
    public void runCampsiteTests(boolean verbose)
    {
        System.out.println("Test Campsite Valid Reservation : " + testCampValidReservation(verbose));
    }

    /**
     * Test Campsite validReservation function
     */
    public boolean testCampValidReservation(boolean verbose)
    {
        //Several sets of gap rules
        GapRule zeroRule = new GapRule(0); //why would you want this though
        GapRule campSpotRule = new GapRule(1);
        GapRule twoRule = new GapRule(2);
        GapRule threeRule = new GapRule(3);
        GapRule fourRule = new GapRule(4);
        GapRule bigRule = new GapRule(10);

        List<GapRule>myRules;

        //For the sake of this test we are going to give an already sorted list of reservations
        Reservation resA = new Reservation(0,"2016-06-03","2016-06-04");
        Reservation resB = new Reservation(0,"2016-06-07","2016-06-09");
        Reservation resC = new Reservation(0,"2016-06-13","2016-06-16");
        Reservation resD = new Reservation(0,"2016-07-24","2016-07-31");

        //Make reservation list
        ArrayList<Reservation> myList = new ArrayList<Reservation>();
        myList.add(resA); myList.add(resB); myList.add(resC); myList.add(resD);

        //Make campsite
        Campsite camp = new Campsite(0,"Test Camp");
        camp.setReservations(myList);

        //Make search objects
        Search noGapSearch = new Search("2016-06-05","2016-06-06"); //no gap search
        Search endOfListSearch = new Search("2016-08-11","2016-08-15"); //end of list search
        Search beginListSearch = new Search("2016-05-24","2016-05-30"); //beginning of list search
        Search tenDayGapSearch = new Search("2016-06-27", "2016-06-29"); //ten day gap
        Search campSpotSearch = new Search("2016-06-06","2016-06-06"); //one day gap

        if(verbose)
            System.out.println("Testing No Gap search with GapRules{1,2}");
        if(!camp.validReservation(noGapSearch,new ArrayList<>(Arrays.asList(campSpotRule,twoRule))))  //this is a valid search
            return false;

        if(verbose)
            System.out.println("Testing No Gap search with GapRules{0}");
        if(camp.validReservation(noGapSearch,new ArrayList<>(Arrays.asList(zeroRule))))  //this is an invalid search
            return false;

        if(verbose)
            System.out.println("Testing 10 Day Gap search with GapRules{10}");
        if(camp.validReservation(tenDayGapSearch,new ArrayList<>(Arrays.asList(bigRule))))  //this is an invalid search
            return false;

        if(verbose)
            System.out.println("Testing 10 Day Gap search with GapRules{2,3}");
        if(!camp.validReservation(tenDayGapSearch,new ArrayList<>(Arrays.asList(twoRule,threeRule))))  //this is a valid search
            return false;

        if(verbose)
            System.out.println("Testing end of list search with GapRules{2,3}");
        if(!camp.validReservation(endOfListSearch,new ArrayList<>(Arrays.asList(twoRule,threeRule))))  //this is a valid search
            return false;

        if(verbose)
            System.out.println("Testing end of list search with GapRules{10}");
        if(camp.validReservation(endOfListSearch,new ArrayList<>(Arrays.asList(bigRule))))  //this is an invalid
            return false;

        if(verbose)
            System.out.println("Testing beginning of list search with GapRules{2,3}");
        if(camp.validReservation(beginListSearch,new ArrayList<>(Arrays.asList(twoRule,threeRule))))  //this not a valid search
            return false;

        if(verbose)
            System.out.println("Testing beginning of list search with GapRules{4}");
        if(!camp.validReservation(beginListSearch,new ArrayList<>(Arrays.asList(bigRule))))  //this is valid
            return false;

        if(verbose)
            System.out.println("Testing Campspot one day gap search of list with GapRules{1}");
        if(camp.validReservation(campSpotSearch,new ArrayList<>(Arrays.asList(campSpotRule))))  //this is not valid
            return false;

        if(verbose)
            System.out.println("Testing Campspot one day gap search of list with GapRules{2}");
        if(!camp.validReservation(campSpotSearch,new ArrayList<>(Arrays.asList(twoRule))))  //this is valid
            return false;

        return true;
    }

    /**
     * Test Reservation Comparable with hardcoded data
     */
    public boolean testReservationComparable(boolean verbose)
    {
        Reservation resA = new Reservation(0,"2010-02-01","2010-02-05");
        Reservation resB = new Reservation(0,"2010-02-06","2010-02-08");
        Reservation resC = new Reservation(0,"2010-02-01","2010-02-05");
        Reservation resD = new Reservation(0,"2010-02-09","2010-02-11");
        Reservation resE = new Reservation(0,"2010-02-02","2010-02-04");

        if(verbose)
            System.out.println("Testing comparison where Reservation is before the compared");
        if(!(resA.compareTo(resB) == -1))
            return false;

        if(verbose)
            System.out.println("Testing comparison where Reservation is the same as the compared");
        if(!(resA.compareTo(resC) == 0))
            return false;

        if(verbose)
            System.out.println("Testing comparison where Reservation is after the compared");
        if(!(resD.compareTo(resB)==1))
            return false;

        if(verbose)
            System.out.println("Testing comparison where Reservation contains the compared");
        if(!(resA.compareTo(resE)==0))
            return false;

        if(verbose)
            System.out.println("Testing comparison where Reservation is within the compared");
        if(!(resE.compareTo(resA)==0))
            return false;

        return true;
    }

    /**
     * Test sorting a List of reservations
     */
    public boolean testReservationsSort(boolean verbose)
    {
        Reservation resA = new Reservation(0,"2016-06-03","2016-06-04");
        Reservation resB = new Reservation(0,"2016-06-13","2016-06-16");
        Reservation resC = new Reservation(0,"2016-06-07","2016-06-09");
        Reservation resD = new Reservation(0,"2016-07-24","2016-07-30");

        //Solution
        ArrayList<Reservation> sortedList = new ArrayList<Reservation>();
        sortedList.add(resA); sortedList.add(resC); sortedList.add(resB); sortedList.add(resD);

        ArrayList<Reservation> myList = new ArrayList<Reservation>();
        myList.add(resD); myList.add(resA); myList.add(resB); myList.add(resC);

        Collections.sort(myList);

        //Compare the lists
        for(int i = 0; i < myList.size(); i++)
        {
            //if one item is out of place it failed
            if(verbose)
                System.out.println("Comparing " + myList.get(i) + " to " + sortedList.get(i));
            if(!(myList.get(i).compareTo(sortedList.get(i))==0))
                return false;
        }
        return true;
    }

    /**
     * Test Reservation equals
     */
    public boolean testReservationEquals(boolean verbose)
    {
        Reservation resA = new Reservation(0,"2010-02-01","2010-02-05");
        Reservation resB = new Reservation(0,"2010-02-06","2010-02-08");
        Reservation resC = new Reservation(0,"2010-02-06","2010-02-08");
        Reservation resD = new Reservation(0,"2010-02-02","2010-02-04"); //contianed within resA
        Reservation resE = new Reservation(0,"2010-02-01","2010-02-03"); //match start date with resA
        Reservation resF = new Reservation(0,"2010-01-27","2010-02-05"); //match end date with resA

        if(verbose)
            System.out.println("Testing not matching reservation");
        if(!(resA.equals(resB))==false) //test no match
            return false;
        if(verbose)
            System.out.println("Testing exact matching reservation");
        if(!(resB.equals(resC)==true))  //test exact match
            return false;
        if(verbose)
            System.out.println("Testing where Reservation contains its compared reservation");
        if(!(resA.equals(resD)==true))  //test containing
            return false;
        if(verbose)
            System.out.println("Testing where Reservation is contained within the compared reservation");
        if(!(resD.equals(resA)==true))  //test containing reverse
            return false;
        if(verbose)
            System.out.println("Testing where Reservation start dates match but not end dates");
        if(!(resA.equals(resE)==true))  //test containing reverse
            return false;
        if(verbose)
            System.out.println("Testing where Reservation end dates match but not start dates");
        if(!(resA.equals(resF)==true))  //test containing reverse
            return false;

        return true;
    }
}