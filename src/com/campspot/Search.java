package com.campspot;
import java.time.*;

/**
*	Name:			Search.java
*	Author:			Kenneth C. LaMarca
*	CreateDate:		04/01/2017
*	Description:	Search object that contains the start date
*					and end date of a search for an open reservation spot				
**/
public class Search
{
	private String startDate;
	private String endDate;
	private LocalDate startDateObject = LocalDate.now();
	private LocalDate endDateObject = LocalDate.now();

	public Search(LocalDate start, LocalDate end)
    {
        startDate = start.toString();
        endDate = end.toString();
        startDateObject = start;
        endDateObject = end;
    }
    public Search(String start, String end)
    {
        startDate = start;
        endDate = end;
        startDateObject = LocalDate.parse(start);
        endDateObject = LocalDate.parse(end);
    }

	public Search()
	{
	}
	public void setStartDate(String start)
	{
		this.startDate = start;
		startDateObject = LocalDate.parse(start);
	}
	public void setEndDate(String end)
	{
		this.endDate = end;
		endDateObject = LocalDate.parse(end);
	}
	public String getStartDate()
	{
		return startDate;
	}
	public String getEndDate()
	{
		return endDate;
	}
}