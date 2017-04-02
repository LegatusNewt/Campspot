package com.campspot;

/**
*	Name:			GapRule.java
*	Author:			Kenneth C. LaMarca
*	CreateDate:		04/01/2017
*	Description:	GapRule object which contains a gap size component.
**/
public class GapRule implements Comparable<GapRule>
{
	private int gapSize;

	public GapRule(int i)
    {
        gapSize = i;
    }
	public GapRule()
	{
		gapSize = -1;
	}
	public void setGapSize(int gr)
	{
		gapSize = gr;
	}
	public int getGapSize()
	{
		return gapSize;
	}

	@Override
	/**
	 * Simply compares gap rules based on the gap size
	 * if o.gapSize is less than this.gapsize a negative is returned
	 */
	public int compareTo(GapRule o) {
		GapRule compared;
		try
		{
			compared = (GapRule) o;
			return Integer.compare(gapSize,compared.getGapSize());
		}
		catch(Exception e)
		{
			System.err.println("GapRule object cannot be compared to object of type" + o.getClass());
		}
		finally
		{
			return 0;
		}
	}

}