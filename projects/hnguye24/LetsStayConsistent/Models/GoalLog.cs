using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace LetsStayConsistent.Models
{
    public class GoalLog
    {
        [Display(Name = "Log's id")]
        public int Id { get; set; }

        [Display(Name = "Date logged")]
        public DateTime Date { get; set; }

        [Display(Name = "Was the goal accomplished on this day?")]
        public bool WasCompleted { get; set; }

        public Goal Goal { get; set; }

        [Display(Name = "Goal's id")]
        public int GoalId { get; set; }
    }
}