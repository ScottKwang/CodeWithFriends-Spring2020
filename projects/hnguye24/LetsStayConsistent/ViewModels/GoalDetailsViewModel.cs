using LetsStayConsistent.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LetsStayConsistent.ViewModels
{
    public class GoalDetailsViewModel
    {
        public Goal Goal { get; set; }

        public List<GoalLog> GoalLogs { get; set; }

        public double ProgressPercentage { get; set; }
    }
}