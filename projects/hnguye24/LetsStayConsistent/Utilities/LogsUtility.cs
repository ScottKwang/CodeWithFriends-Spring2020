using LetsStayConsistent.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace LetsStayConsistent.Utilities
{
    public class LogsUtility
    {
        public static List<SelectListItem> GetGoalDropdown()
        {
            var _context = new ApplicationDbContext();

            List<Goal> goals = _context.Goals.ToList();

            var dropdown = new List<SelectListItem>();

            foreach (var goal in goals)
            {
                dropdown.Add(new SelectListItem
                {
                    Text = goal.Name,
                    Value = goal.Id.ToString()
                });
            }

            return dropdown;
        }
    }
}