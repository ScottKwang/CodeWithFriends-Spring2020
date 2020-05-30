using LetsStayConsistent.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LetsStayConsistent.Utilities
{
    public class GoalUtility
    {
        public static double CalculateProgressPercentage(Goal goal, List<GoalLog> logs)
        {
            double daysCompletedSoFar = 0;

            foreach(var log in logs)
            {
                if (log.WasCompleted)
                {
                    daysCompletedSoFar++;
                }
            }

            return Math.Floor((daysCompletedSoFar / goal.DaysToComplete) * 100);
        }
    }
}