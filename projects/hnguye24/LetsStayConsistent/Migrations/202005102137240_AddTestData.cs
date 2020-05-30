namespace LetsStayConsistent.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class AddTestData : DbMigration
    {
        public override void Up()
        {
            Sql("INSERT INTO Goals (Name, DaysToComplete, Reward) " +
                "VALUES ('Perform pull-ups until failure', 14, 'You can buy a dumbbell set')");

            Sql("INSERT INTO GoalLogs (Date, WasCompleted, GoalId) " +
                "VALUES ('5/10/2020', 1, 1)");
        }
        
        public override void Down()
        {
        }
    }
}
