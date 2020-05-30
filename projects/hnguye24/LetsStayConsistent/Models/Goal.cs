using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace LetsStayConsistent.Models
{
    public class Goal
    {
        [Display(Name = "Id")]
        public int Id { get; set; }

        [Required]
        [Display(Name = "Goal Name")]
        public string Name { get; set; }

        [Display(Name = "Days to complete goal")]
        public int DaysToComplete { get; set; }

        [Required]
        [Display(Name = "Reward after achieving goal")]
        public string Reward { get; set; }

        public List<GoalLog> GoalLogs { get; set; }
    }
}