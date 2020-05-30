using LetsStayConsistent.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LetsStayConsistent.ViewModels
{
    public class TestViewModel
    {
        public List<Goal> Goals { get; set; }

        public List<GoalLog> GoalLogs { get; set; }
    }
}