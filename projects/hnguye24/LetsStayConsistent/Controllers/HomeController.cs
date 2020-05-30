using LetsStayConsistent.Models;
using LetsStayConsistent.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace LetsStayConsistent.Controllers
{
    public class HomeController : Controller
    {
        private ApplicationDbContext _context;

        public HomeController()
        {
            _context = new ApplicationDbContext();
        }

        protected override void Dispose(bool disposing)
        {
            _context.Dispose();
        }


        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Test()
        {
            // Query all goals in database
            List<Goal> goals = _context.Goals.ToList();

            // Query all logs in database
            List<GoalLog> goalLogs = _context.GoalLogs.ToList();

            // Query all logs for that specific goal
            foreach (var goal in goals)
            {
                goal.GoalLogs = _context.GoalLogs.Where(log => log.GoalId == goal.Id).ToList();
            }

            // Build view model
            var model = new TestViewModel
            {
                Goals = goals,
                GoalLogs = goalLogs
            };

            return View(model);
        }
    }
}