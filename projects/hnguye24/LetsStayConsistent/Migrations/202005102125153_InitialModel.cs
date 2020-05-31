namespace LetsStayConsistent.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class InitialModel : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.GoalLogs",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Date = c.DateTime(nullable: false),
                        WasCompleted = c.Boolean(nullable: false),
                        GoalId = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Goals", t => t.GoalId, cascadeDelete: true)
                .Index(t => t.GoalId);
            
            CreateTable(
                "dbo.Goals",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(nullable: false),
                        DaysToComplete = c.Int(nullable: false),
                        Reward = c.String(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.GoalLogs", "GoalId", "dbo.Goals");
            DropIndex("dbo.GoalLogs", new[] { "GoalId" });
            DropTable("dbo.Goals");
            DropTable("dbo.GoalLogs");
        }
    }
}
