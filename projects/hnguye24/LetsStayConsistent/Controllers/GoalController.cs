using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using LetsStayConsistent.Models;
using LetsStayConsistent.ViewModels;
using LetsStayConsistent.Utilities;

namespace LetsStayConsistent.Controllers
{
    public class GoalController : Controller
    {
        private ApplicationDbContext _context;

        public GoalController()
        {
            _context = new ApplicationDbContext();
        }

        protected override void Dispose(bool disposing)
        {
            _context.Dispose();
        }

        public ActionResult Index()
        {
            // Query all goals in database
            List<Goal> goals = _context.Goals.ToList();

            // Query all logs for that specific goal
            foreach (var goal in goals)
            {
                goal.GoalLogs = _context.GoalLogs.Where(log => log.GoalId == goal.Id).ToList();
            }

            var model = new GoalIndexViewModel
            {
                Goals = goals
            };

            return View(model);
        }

        public ActionResult Add()
        {
            return View();
        }

        [HttpPost]
        public ActionResult Add(GoalAddViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View("Add", model);
            }

            var goal = new Goal
            {
                Name = model.Goal.Name,
                DaysToComplete = model.Goal.DaysToComplete,
                Reward = model.Goal.Reward
            };

            _context.Goals.Add(goal);
            _context.SaveChanges();

            return RedirectToAction("Index", "Goal");
        }

        public ActionResult Edit(int id)
        {
            Goal goal = _context.Goals.SingleOrDefault(item => item.Id == id);

            if (goal == null)
            {
                return RedirectToAction("Index", "Goal");
            }

            var model = new GoalEditViewModel
            {
                Goal = goal
            };

            return View(model);
        }

        [HttpPost]
        public ActionResult Edit(GoalEditViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View("Edit", model);
            }

            Goal goalInDb = _context.Goals.SingleOrDefault(item => item.Id == model.Goal.Id);

            if (goalInDb == null)
            {
                return RedirectToAction("Index", "Goal");
            }

            goalInDb.Name = model.Goal.Name;
            goalInDb.DaysToComplete = model.Goal.DaysToComplete;
            goalInDb.Reward = model.Goal.Reward;

            _context.SaveChanges();

            return RedirectToAction("Index", "Goal");
        }

        [HttpDelete]
        public ActionResult Delete(int id)
        {
            Goal goalInDb = _context.Goals.SingleOrDefault(item => item.Id == id);

            if (goalInDb == null)
            {
                return RedirectToAction("Index", "Goal");
            }

            _context.Goals.Remove(goalInDb);
            _context.SaveChanges();

            // why does this not execute?
            return RedirectToAction("Index", "Goal");
        }

        public ActionResult Details(int id)
        {
            Goal goalInDb = _context.Goals.SingleOrDefault(g => g.Id == id);

            if (goalInDb == null)
            {
                return RedirectToAction("Index", "Goal");
            }

            List<GoalLog> goalLogs = _context.GoalLogs.Where(log => log.GoalId == goalInDb.Id).ToList();

            double progressPercentage = GoalUtility.CalculateProgressPercentage(goalInDb, goalLogs);

            var model = new GoalDetailsViewModel
            {
                Goal = goalInDb,
                GoalLogs = goalLogs,
                ProgressPercentage = progressPercentage
            };

            return View(model);
        }
    }
}